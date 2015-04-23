package com.oltruong.teamag.backingbean;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oltruong.teamag.interfaces.UserLogin;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.model.Work;
import com.oltruong.teamag.service.*;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.webbean.ColumnDayBean;
import com.oltruong.teamag.webbean.RealizedFormWebBean;
import com.oltruong.teamag.webbean.TaskWeekBean;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SessionScoped
@ManagedBean
public class WorkController extends Controller {

    @Inject
    private Logger logger;
    @Inject
    @UserLogin
    private Instance<Member> memberInstance;
    @Inject
    private Task newTask;
    private Map<Task, List<Work>> works;
    @Inject
    private RealizedFormWebBean realizedBean;
    @Inject
    private WorkService workService;

    @Inject
    private WeekCommentService weekCommentService;
    @Inject
    private TaskService taskService;


    @Inject
    private AbsenceDayService absenceDayService;

    @Inject
    private EmailService mailService;

    private WeekComment weekComment;

    private List<Task> taskList;
    private static final String VIEWNAME = "realized";

    public String init() {

        return initInformation(DateTime.now());
    }

    public String initInformation(DateTime dateTime) {


        DateTime firstDayOfMonth = dateTime.withDayOfMonth(1);
        realizedBean.setCurrentMonth(firstDayOfMonth);
        taskList = taskService.findTasksForMember(getMember());
        works = workService.findOrCreateWorks(getMember(), firstDayOfMonth, taskList, absenceDayService.findByMemberAndMonth(getMember().getId(), firstDayOfMonth.getMonthOfYear()));


        DateTime firstIncompleteDay = findFirstIncompleteDay(firstDayOfMonth);

        if (firstIncompleteDay != null) {
            realizedBean.setDayCursor(firstIncompleteDay);
        } else {
            realizedBean.setDayCursor(dateTime);
        }
        initTaskWeek();
        return VIEWNAME;
    }

    private DateTime findFirstIncompleteDay(DateTime firstDayOfMonth) {

        DateTime result = null;

        Map<DateTime, Double> map = workService.findWorkDays(getMember(), firstDayOfMonth);


        if (map != null && !map.isEmpty()) {

            for (DateTime day : map.keySet()) {
                if (Math.abs(map.get(day).doubleValue() - 1d) > 0.01) {
                    if (result == null || day.isBefore(result)) {
                        result = day;
                    }
                }
            }

        }

        return result;

    }

    public String doCreateActivity() {

        logger.info("Adding a new activity");

        if (StringUtils.isBlank(newTask.getName())) {
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessageManager().getMessage("impossibleAdd"), getMessage("nameTask"));
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            try {
                taskService.createTask(realizedBean.getCurrentMonth(), getMember(), newTask);

                works = workService.findOrCreateWorks(getMember(), DateTime.now().withDayOfMonth(1), taskService.findTasksForMember(getMember()), absenceDayService.findByMemberAndMonth(getMember().getId(), DateTime.now().getMonthOfYear()));
                initTaskWeek();

                FacesMessage msg = null;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("createdTask"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            } catch (EntityExistsException e) {
                FacesMessage msg = null;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("existingTask"), getMessage("noChange"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

        return VIEWNAME;
    }

    public String deleteTask() {
        logger.info("Deleting task " + realizedBean.getSelectedTaskWeek().getTask().getName());

        taskService.remove(realizedBean.getSelectedTaskWeek().getTask(), getMember(), realizedBean.getCurrentMonth());

        initInformation(realizedBean.getDayCursor());
        return VIEWNAME;
    }

    public String previousWeek() {
        logger.debug("Click Previous week");
        realizedBean.decrementWeek();
        initTaskWeek();
        return VIEWNAME;
    }

    public String nextWeek() {
        realizedBean.incrementWeek();
        initTaskWeek();
        return VIEWNAME;
    }

    public String update() {

        List<Work> changedWorks = findChangedWorks(realizedBean.getTaskWeeks());
        workService.mergeList(changedWorks);

        updateComment();


        if (!changedWorks.isEmpty()) {
            sendNotification();
            initTaskWeek();
        }

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("updated"), "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        return VIEWNAME;
    }

    private void updateComment() {
        if (Strings.isNullOrEmpty(weekComment.getComment())) {
            if (weekComment.getId() != null) {
                weekCommentService.remove(weekComment);
            }
        } else {
            if (weekComment.getId() != null) {
                weekCommentService.merge(weekComment);
            } else {
                weekCommentService.persist(weekComment);
            }

            MailBean email = buildEmailComment(weekComment);
            mailService.sendEmailAdministrator(email);

        }
    }


    private MailBean buildEmailComment(WeekComment weekComment) {
        MailBean email = new MailBean();
        email.setContent("Semaine " + weekComment.getWeekYear() + "\n" + weekComment.getComment());
        email.setSubject(getMember().getName() + " Commentaire de Realise");
        return email;
    }

    public List<String> completeProject(String query) {
        List<Task> tasks = taskService.findAllNonAdminTasks();

        List<String> results = Lists.newArrayListWithExpectedSize(tasks.size());
        if (!StringUtils.isBlank(query) && query.length() > 1) {

            for (Task task : tasks) {
                if (StringUtils.containsIgnoreCase(task.getProject(), query) && !results.contains(task.getProject())) {
                    results.add(task.getProject());
                }

            }
        }
        return results;
    }

    public List<String> completeName(String query) {
        List<Task> tasks = taskService.findAllNonAdminTasks();

        tasks.removeIf(t -> taskList.contains(t));

        List<String> results = Lists.newArrayListWithExpectedSize(tasks.size());
        if (!StringUtils.isBlank(query) && query.length() > 1) {
            for (Task task : tasks) {
                // Do not propose task that the member already has
                if (StringUtils.containsIgnoreCase(task.getName(), query) && !results.contains(task.getName())) {
                    results.add(task.getName());
                }

            }
        }
        return results;
    }

    private void sendNotification() {
        int total = workService.getSumWorks(getMember(), realizedBean.getCurrentMonth());

        int nbWorkingDays = CalendarUtils.getWorkingDays(realizedBean.getCurrentMonth()).size();
        if (total == nbWorkingDays) {

            MailBean email = buildEmail();
            mailService.sendEmailAdministrator(email);
        }
    }

    private MailBean buildEmail() {
        MailBean email = new MailBean();
        email.setContent("Realise complet");
        email.setSubject("Realise de " + getMember().getName());
        return email;
    }


    private void initTaskWeek() {
        weekComment = weekCommentService.findWeekComment(memberInstance.get().getId(), realizedBean.getWeekNumber(), realizedBean.getYear());

        if (weekComment == null) {
            weekComment = new WeekComment(memberInstance.get(), realizedBean.getWeekNumber(), realizedBean.getCurrentMonth().getMonthOfYear(), realizedBean.getYear());
        }

        if (works != null) {
            Integer weekNumber = realizedBean.getWeekNumber();

            Map<String, ColumnDayBean> mapColumns = Maps.newHashMapWithExpectedSize(5);

            List<TaskWeekBean> taskWeekList = Lists.newArrayListWithExpectedSize(works.keySet().size());
            for (Task task : works.keySet()) {
                TaskWeekBean taskWeek = new TaskWeekBean();
                taskWeek.setTask(task);
                for (Work work : works.get(task)) {

                    if (work.getDay().getWeekOfWeekyear() == weekNumber) {

                        ColumnDayBean columnDay = new ColumnDayBean();
                        columnDay.setDay(work.getDay());
                        taskWeek.addWork(columnDay.getDayNumber(), work);

                        if (mapColumns.get(work.getDayStr()) == null) {
                            columnDay.addTotal(work.getTotal());
                            mapColumns.put(work.getDayStr(), columnDay);
                        } else {
                            mapColumns.get(work.getDayStr()).addTotal(work.getTotal());
                        }

                    }
                }
                taskWeekList.add(taskWeek);

            }

            realizedBean.getColumnsDay().clear();
            for (ColumnDayBean col : mapColumns.values()) {
                realizedBean.addColumnDay(col);

            }
            Collections.sort(realizedBean.getColumnsDay());
            realizedBean.setTaskWeeks(taskWeekList);
            Collections.sort(realizedBean.getTaskWeeks());

        } else {
            logger.debug("No taskMonth found");
        }

    }

    private List<Work> findChangedWorks(List<TaskWeekBean> taskWeeks) {
        List<Work> worksChanged = Lists.newArrayList();
        for (TaskWeekBean taskWeek : taskWeeks) {
            for (Work work : taskWeek.getWorks()) {
                if (work.hasChanged()) {
                    worksChanged.add(work);
                }
            }
        }

        return worksChanged;
    }

    public RealizedFormWebBean getRealizedBean() {
        return realizedBean;
    }

    public void setRealizedBean(RealizedFormWebBean realizedBean) {
        this.realizedBean = realizedBean;
    }

    public Member getMember() {
        return memberInstance.get();
    }

    public Task getNewTask() {
        return newTask;
    }

    public void setNewTask(Task newActivity) {
        newTask = newActivity;
    }

    public WeekComment getWeekComment() {
        return weekComment;
    }

    public void setWeekComment(WeekComment weekComment) {
        this.weekComment = weekComment;
    }
}
