package fr.oltruong.teamag.backingbean;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.ejb.EmailEJB;
import fr.oltruong.teamag.ejb.MailBean;
import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.WeekComment;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.qualifier.UserLogin;
import fr.oltruong.teamag.utils.CalendarUtils;
import fr.oltruong.teamag.webbean.ColumnDayBean;
import fr.oltruong.teamag.webbean.RealizedFormWebBean;
import fr.oltruong.teamag.webbean.TaskWeekBean;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
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
    private WorkEJB workEJB;
    @Inject
    private EmailEJB mailEJB;

    private WeekComment weekComment;


    private static final String VIEWNAME = "realized";

    public String init() {
        DateTime now = DateTime.now();
        // this.realizedBean = new RealizedFormWebBean();
        realizedBean.setDayCursor(now);

        DateTime firstDayOfMonth = now.withDayOfMonth(1);
        realizedBean.setCurrentMonth(firstDayOfMonth);
        works = workEJB.findWorks(getMember(), firstDayOfMonth);

        initTaskWeek();
        return VIEWNAME;
    }

    public String doCreateActivity() {

        logger.info("Adding a new activity");

        if (StringUtils.isBlank(newTask.getName())) {
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("impossibleAdd"), getMessage("nameTask"));
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            try {
                workEJB.createTask(realizedBean.getCurrentMonth(), getMember(), newTask);

                works = workEJB.findWorks(getMember(), DateTime.now().withDayOfMonth(1));
                initTaskWeek();

                FacesMessage msg = null;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("createdTask"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            } catch (ExistingDataException e) {
                FacesMessage msg = null;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("existingTask"), getMessage("noChange"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

        return VIEWNAME;
    }

    public String deleteTask() {
        logger.info("Deleting task " + realizedBean.getSelectedTaskWeek().getTask().getName());

        workEJB.removeTask(realizedBean.getSelectedTaskWeek().getTask(), getMember(), realizedBean.getCurrentMonth());
        init();
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
        workEJB.updateWorks(changedWorks);

        updateComment();


        if (changedWorks.isEmpty()) {
            //    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("noChangesDetected"), "");

        } else {
            //      msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("updated"), "");
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
                workEJB.removeWeekComment(weekComment);
            }
        } else {
            if (weekComment.getId() != null) {
                workEJB.updateWeekComment(weekComment);
            } else {
                workEJB.createWeekComment(weekComment);
            }

        }
    }

    public List<String> completeProject(String query) {
        List<Task> tasks = workEJB.findAllTasks();

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
        List<Task> tasks = workEJB.findAllTasks();

        List<String> results = Lists.newArrayListWithExpectedSize(tasks.size());
        if (!StringUtils.isBlank(query) && query.length() > 1) {
            for (Task task : tasks) {
                // Do not propose task that the member already has
                if (!task.getMembers().contains(getMember()) && StringUtils.containsIgnoreCase(task.getName(), query) && !results.contains(task.getName())) {
                    results.add(task.getName());
                }

            }
        }
        return results;
    }

    private void sendNotification() {
        int total = workEJB.getSumWorks(getMember(), realizedBean.getCurrentMonth());

        int nbWorkingDays = CalendarUtils.getWorkingDays(realizedBean.getCurrentMonth()).size();
        if (total == nbWorkingDays) {

            MailBean email = buildEmail();
            mailEJB.sendEmailAdministrator(email);
        }
    }

    private MailBean buildEmail() {
        MailBean email = new MailBean();
        email.setContent("Realise complet");
        email.setSubject("Realise de " + getMember().getName());
        return email;
    }


    private void initTaskWeek() {
        weekComment = workEJB.findWeekComment(memberInstance.get(), realizedBean.getWeekNumber(), realizedBean.getYear());

        if (weekComment == null) {
            weekComment = new WeekComment(memberInstance.get(), realizedBean.getWeekNumber(), realizedBean.getYear());
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
