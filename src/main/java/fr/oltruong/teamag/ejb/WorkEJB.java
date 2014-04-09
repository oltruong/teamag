package fr.oltruong.teamag.ejb;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.WeekComment;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.utils.CalendarUtils;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class WorkEJB extends AbstractEJB {

    public Map<Task, List<Work>> findWorksNotNull(Member member, DateTime month) {

        Map<Task, List<Work>> worksByTask = transformWorkList(findWorkList(member, month));


        List<Task> emptyWorkTasks = Lists.newArrayListWithCapacity(worksByTask.size());

        for (Task task : worksByTask.keySet()) {
            List<Work> workList = worksByTask.get(task);

            boolean empty = true;
            for (Work work : workList) {
                empty &= !Float.valueOf(0f).equals(work.getTotal());
            }
            if (empty) {
                emptyWorkTasks.add(task);
            }
        }

        for (Task task : emptyWorkTasks) {
            worksByTask.remove(task);
        }

        return worksByTask;

    }

    public Map<Task, List<Work>> findOrCreateWorks(Member member, DateTime month) {

        List<Work> listWorks = findWorkList(member, month);

        if (CollectionUtils.isEmpty(listWorks)) {

            getLogger().debug("Creating new works for member " + member.getName());
            listWorks = createWorks(member, month);
        }


        return transformWorkList(listWorks);
    }


    private List<Work> findWorkList(Member member, DateTime month) {

        Map<Task, List<Work>> worksByTask = Maps.newHashMap();

        Query query = getEntityManager().createNamedQuery("findWorksByMemberMonth");
        query.setParameter("fmemberId", member.getId());
        query.setParameter("fmonth", month);

        return (List<Work>) query.getResultList();


    }


    @SuppressWarnings("unchecked")
    public List<Task> findAllTasks() {
        return getEntityManager().createNamedQuery("findAllTasks").getResultList();
    }

    public List<Task> findAllNonAdminTasks() {
        List<Task> taskList = findAllTasks();

        List<Task> nonAdminTaskList = Lists.newArrayListWithCapacity(taskList.size());

        for (Task task : taskList) {
            if (isTaskNonAdmin(task)) {
                nonAdminTaskList.add(task);
            }

        }
        return nonAdminTaskList;
    }

    private boolean isTaskNonAdmin(Task task) {
        boolean verdict = false;
        if (task.getMembers() != null && !task.getMembers().isEmpty()) {
            for (Member member : task.getMembers()) {
                verdict |= !member.isAdministrator();
            }
        } else {
            verdict = true;
        }
        return verdict;
    }


    public List<Task> findTasksByProject(String project) {

        Query query = getEntityManager().createNamedQuery("findTaskByProject");
        query.setParameter("fproject", project);
        return query.getResultList();
    }

    public int getSumWorks(Member member, DateTime month) {
        Query query = getEntityManager().createNamedQuery("countWorksMemberMonth");
        query.setParameter("fmemberId", member.getId());
        query.setParameter("fmonth", month);
        Number sumOfPrice = (Number) query.getSingleResult();
        getLogger().debug("total " + sumOfPrice);
        return sumOfPrice.intValue();
    }

    private Map<Task, List<Work>> transformWorkList(List<Work> listWorks) {

        Map<Task, List<Work>> worksByTask = Maps.newHashMap();
        if (listWorks != null) {
            for (Work work : listWorks) {
                if (!worksByTask.containsKey(work.getTask())) {
                    worksByTask.put(work.getTask(), new ArrayList<Work>());
                }
                worksByTask.get(work.getTask()).add(work);
                work.getTask().addTotal(work.getTotal());
            }

        }

        return worksByTask;
    }

    private List<Work> createWorks(Member member, DateTime month) {

        List<Work> workList = null;

        List<Task> taskList = findMemberTasks(member);
        if (CollectionUtils.isNotEmpty(taskList)) {

            List<DateTime> workingDays = CalendarUtils.getWorkingDays(month);

            workList = Lists.newArrayListWithExpectedSize(taskList.size() * workingDays.size());
            for (Task task : taskList) {
                for (DateTime day : workingDays) {
                    Work work = new Work();
                    work.setDay(day);
                    work.setMember(member);
                    work.setMonth(month);
                    work.setTask(task);

                    getEntityManager().persist(work);

                    workList.add(work);
                }
            }

        } else {
            getLogger().debug("No activity");
        }
        return workList;

    }

    public void removeTask(Task task, Member member, DateTime month) {
        Query query = getEntityManager().createNamedQuery("deleteWorksByMemberTaskMonth");
        query.setParameter("fmemberId", member.getId());
        query.setParameter("ftaskId", task.getId());
        query.setParameter("fmonth", month);

        int rowsNumberDeleted = query.executeUpdate();

        getLogger().debug("Works deleted : " + rowsNumberDeleted);

        // Delete of task for user

        Task taskDb = getEntityManager().find(Task.class, task.getId());

        Member memberDb = getEntityManager().find(Member.class, member.getId());

        taskDb.getMembers().remove(memberDb);

        if (taskDb.getMembers().isEmpty() && taskHasNoWorks(taskDb)) {
            getLogger().info("Task has no more Members on it. It will be deleted");
            getEntityManager().remove(taskDb);
        } else {
            getLogger().debug("Task updated");
            getEntityManager().persist(taskDb);
        }
    }

    private boolean taskHasNoWorks(Task taskDb) {

        Query query = getEntityManager().createNamedQuery("countWorksTask");
        query.setParameter("fTaskId", taskDb.getId());
        int total = ((Number) query.getSingleResult()).intValue();
        return total == 0;
    }

    public List<Task> findMemberTasks(Member member) {
        Query query = getEntityManager().createNamedQuery("findAllTasks");

        @SuppressWarnings("unchecked")
        List<Task> allTaskList = query.getResultList();

        List<Task> taskList = Lists.newArrayList();

        for (Task task : allTaskList) {
            getLogger().debug("tache " + task.getId());

            if (task.getMembers() != null && !task.getMembers().isEmpty()) {

                getLogger().debug("Task Name" + task.getMembers().get(0).getName());
                getLogger().debug("Task Id" + task.getMembers().get(0).getId());
                getLogger().debug("Member id" + member.getId());

                if (task.getMembers().contains(member)) {
                    getLogger().debug("Task has member " + member.getName());
                    taskList.add(task);
                }
            }
        }

        return taskList;
    }

    public void createTask(DateTime month, Member member, Task task) throws ExistingDataException {
        Query query = getEntityManager().createNamedQuery("findTaskByName");
        query.setParameter("fname", task.getName());
        query.setParameter("fproject", task.getProject());

        Task taskDB = null;
        @SuppressWarnings("unchecked")
        List<Task> allTaskList = query.getResultList();

        if (CollectionUtils.isNotEmpty(allTaskList)) {
            getLogger().debug("Existing task");
            Task myTask = allTaskList.get(0);
            if (myTask.getMembers().contains(member)) {
                getLogger().debug("Already affected to member");
                throw new ExistingDataException();
            } else {
                getLogger().debug("Affecting to member " + member.getId());
                myTask.addMember(member);
                getEntityManager().merge(myTask);
                taskDB = myTask;
            }
        } else

        {
            getLogger().debug("new task creation");

            // Reset task ID
            task.setId(null);
            task.addMember(member);
            getEntityManager().persist(task);
            taskDB = task;

        }

        getEntityManager().flush();

        getLogger().debug("Creation of WORK objects");
        List<DateTime> workingDayList = CalendarUtils.getWorkingDays(month);

        for (DateTime day : workingDayList) {
            Work work = new Work();
            work.setDay(day);
            work.setMember(member);
            work.setMonth(month);
            work.setTask(taskDB);

            getEntityManager().persist(work);
        }

    }

    public void updateWorks(List<Work> workList) {
        for (Work work : workList) {
            work.setTotal(work.getTotalEdit());
            getEntityManager().merge(work);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Work> getWorksMonth(DateTime month) {
        Query query = getEntityManager().createNamedQuery("findWorksMonth");
        query.setParameter("fmonth", month);

        return query.getResultList();
    }


    public List<Work> findWorkByTask(Long taskId) {
        Query query = getEntityManager().createNamedQuery("findWorksByTask");
        query.setParameter("fTaskId", taskId);
        return query.getResultList();

    }

    public WeekComment findWeekComment(Member member, int weekYear, int year) {

        WeekComment result = null;
        Query query = getEntityManager().createNamedQuery("findWeekComment");
        query.setParameter("fmember", member);
        query.setParameter("fweekYear", weekYear);
        query.setParameter("fyear", year);

        List<WeekComment> weekCommentList = query.getResultList();
        if (!weekCommentList.isEmpty()) {
            result = weekCommentList.get(0);
        }

        return result;
    }

    public WeekComment createWeekComment(WeekComment weekComment) {
        getEntityManager().persist(weekComment);
        return weekComment;
    }

    public void updateWeekComment(WeekComment weekComment) {
        getEntityManager().merge(weekComment);
    }

    public void removeWeekComment(WeekComment weekComment) {
        WeekComment weekCommentDb = getEntityManager().find(WeekComment.class, weekComment.getId());
        getEntityManager().remove(weekCommentDb);
    }

    public void createTask(Task task) throws ExistingDataException {
        Query query = getEntityManager().createNamedQuery("findTaskByName");
        query.setParameter("fname", task.getName());
        query.setParameter("fproject", task.getProject());

        Task taskDB = null;
        @SuppressWarnings("unchecked")
        List<Task> allTaskList = query.getResultList();

        if (CollectionUtils.isNotEmpty(allTaskList)) {
            throw new ExistingDataException();
        } else {
            getEntityManager().persist(task);
        }
    }

    public Task findTask(Long taskId) {
        return getEntityManager().find(Task.class, taskId);
    }

    public void deleteTask(Long taskId) {
        getEntityManager().remove(findTask(taskId));
    }

    public void updateTask(Task taskToUpdate) {
        if (isLoop(taskToUpdate)) {
            throw new IllegalArgumentException();
        }
        getEntityManager().merge(taskToUpdate);
    }

    private boolean isLoop(Task taskToUpdate) {
        boolean result = false;
        result = (taskToUpdate != null && taskToUpdate.getTask() != null && taskToUpdate.getId().equals(taskToUpdate.getTask().getId()));
        if (!result && taskToUpdate.getTask() != null) {
            List<Long> idList = Lists.newArrayList();
            idList.add(taskToUpdate.getId());

            Task task = taskToUpdate;
            boolean finished = false;
            while (!result && task.getTask() != null) {
                task = task.getTask();
                if (idList.contains(task.getId())) {
                    result = true;
                }
                idList.add(task.getId());
            }

        }

        return result;
    }
}
