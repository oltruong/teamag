package fr.oltruong.teamag.service;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.utils.CalendarUtils;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Olivier Truong
 */
public class TaskService extends AbstractService {


    @Inject
    private WorkService workService;

    @SuppressWarnings("unchecked")
    public List<Task> findAllTasks() {
        return createNamedQuery("findAllTasks").getResultList();
    }

    public List<Task> findAllNonAdminTasks() {
        List<Task> taskList = findAllTasks();

        List<Task> nonAdminTaskList = Lists.newArrayListWithCapacity(taskList.size());

        taskList.forEach(task -> {
            if (task.isNonAdmin()) {
                nonAdminTaskList.add(task);
            }
        });

        return nonAdminTaskList;
    }


    public List<Task> findTaskWithActivity() {
        return getNamedQueryList("findAllTasksWithActivity");
    }

    public Task findTask(Long taskId) {
        return find(Task.class, taskId);
    }

    public void deleteTask(Long taskId) {
        Task task = findTask(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task with id " + taskId + " not found");
        }
        remove(task);
    }

    public void updateTask(Task taskToUpdate) {
        if (isLoop(taskToUpdate)) {
            throw new IllegalArgumentException();
        }
        merge(taskToUpdate);
    }

    public void createTask(Task task) throws ExistingDataException {
        Query query = createNamedQuery("findTaskByName");
        query.setParameter("fname", task.getName());
        query.setParameter("fproject", task.getProject());


        @SuppressWarnings("unchecked")
        List<Task> allTaskList = query.getResultList();

        if (CollectionUtils.isNotEmpty(allTaskList)) {
            throw new ExistingDataException();
        } else {
            persist(task);
        }
    }

    public void createTask(DateTime month, Member member, Task task) throws ExistingDataException {
        Query query = createNamedQuery("findTaskByName");
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
                merge(myTask);
                taskDB = myTask;
            }
        } else {
            getLogger().debug("new task creation");

            // Reset task ID
            task.setId(null);
            task.addMember(member);
            persist(task);
            taskDB = task;

        }

        flush();

        getLogger().debug("Creation of WORK objects");
        List<DateTime> workingDayList = CalendarUtils.getWorkingDays(month);

        for (DateTime day : workingDayList) {
            workService.createWork(member, month, taskDB, day);
        }

    }

    public List<Task> findTasksForMember(Member member) {
        List<Task> allTaskList = findAllTasks();

        List<Task> taskList = Lists.newArrayList();

        for (Task task : allTaskList) {

            if (task.getMembers() != null && !task.getMembers().isEmpty()) {

                if (task.getMembers().contains(member)) {
                    taskList.add(task);
                }
            }
        }

        if (CollectionUtils.isEmpty(taskList)) {
            addAbsenceTask(member, taskList);
        }


        return taskList;
    }

    private void addAbsenceTask(Member member, List<Task> taskList) {
        Task absenceTask = findTask(1L);
        absenceTask.addMember(member);
        updateTask(absenceTask);
        taskList.add(absenceTask);
    }

    public void removeTask(Task task, Member member, DateTime month) {
        Query query = createNamedQuery("deleteWorksByMemberTaskMonth");
        query.setParameter("fmemberId", member.getId());
        query.setParameter("ftaskId", task.getId());
        query.setParameter("fmonth", month);

        int rowsNumberDeleted = query.executeUpdate();

        getLogger().debug("Works deleted : " + rowsNumberDeleted);

        // Delete of task for user

        Task taskDb = find(Task.class, task.getId());

        Member memberDb = find(Member.class, member.getId());

        taskDb.getMembers().remove(memberDb);

        if (taskDb.getMembers().isEmpty() && taskHasNoWorks(taskDb)) {
            getLogger().info("Task has no more Members on it. It will be deleted");
            remove(taskDb);
        } else {
            getLogger().debug("Task updated");
            persist(taskDb);
        }
    }

    private boolean taskHasNoWorks(Task taskDb) {

        Query query = createNamedQuery("countWorksTask");
        query.setParameter("fTaskId", taskDb.getId());
        int total = ((Number) query.getSingleResult()).intValue();
        return total == 0;
    }


    private boolean isLoop(Task taskToUpdate) {
        boolean result = (taskToUpdate != null && taskToUpdate.getTask() != null && taskToUpdate.getId().equals(taskToUpdate.getTask().getId()));
        if (!result && taskToUpdate.getTask() != null) {
            List<Long> idList = Lists.newArrayList();
            idList.add(taskToUpdate.getId());
            Task task = taskToUpdate;

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
