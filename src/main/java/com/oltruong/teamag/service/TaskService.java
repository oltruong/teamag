package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Olivier Truong
 */
public class TaskService extends AbstractService {


    @Inject
    private WorkService workService;

    public List<Task> findAllTasks() {
        return createNamedQuery("Task.FIND_ALL", Task.class).getResultList();
    }

    public List<Task> findAllNonAdminTasks() {
        return createNamedQuery("Task.FIND_NONTYPE", Task.class).setParameter("memberType", MemberType.ADMINISTRATOR).getResultList();
    }

    public List<Task> findTaskWithActivity() {
        return createNamedQuery("Task.FIND_ALL_WITH_ACTIVITY", Task.class).getResultList();
    }

    public Task findTask(Long taskId) {
        return find(Task.class, taskId);
    }

    public void deleteTask(Long taskId) {
        Task task = findTask(taskId);
        if (task == null) {
            throw new EntityNotFoundException("Task with id " + taskId + " not found");
        }
        remove(task);
    }

    public void updateTask(Task taskToUpdate) {
        if (isLoop(taskToUpdate)) {
            throw new IllegalArgumentException();
        }
        merge(taskToUpdate);
    }

    public void createTask(Task task) {
        List<Task> allTaskList = createNamedQuery("Task.FIND_BY_NAME", Task.class).setParameter("fname", task.getName()).setParameter("fproject", task.getProject()).getResultList();
        if (CollectionUtils.isNotEmpty(allTaskList)) {
            throw new EntityExistsException();
        } else {
            persist(task);
        }
    }

    @Transactional
    public void createTask(DateTime month, Member member, Task task) {
        TypedQuery<Task> query = createNamedQuery("Task.FIND_BY_NAME", Task.class);
        query.setParameter("fname", task.getName());
        query.setParameter("fproject", task.getProject());

        Task taskDB = null;

        List<Task> allTaskList = query.getResultList();

        if (CollectionUtils.isNotEmpty(allTaskList)) {
            Task myTask = allTaskList.get(0);
            if (myTask.getMembers().contains(member)) {
                throw new EntityExistsException();
            } else {
                myTask.addMember(member);
                merge(myTask);
                taskDB = myTask;
            }
        } else {
            // Reset task ID
            task.setId(null);
            task.addMember(member);
            persist(task);
            taskDB = task;

        }

        flush();

        List<DateTime> workingDayList = CalendarUtils.getWorkingDays(month);

        for (DateTime day : workingDayList) {
            workService.createWork(member, month, taskDB, day);
        }

    }

    public List<Task> findTasksForMember(Member member) {
        List<Task> taskList = entityManager.createNamedQuery("Task.FIND_MEMBER", Task.class).setParameter("memberId", member.getId()).getResultList();
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
        deleteWorks(task, member, month);

        Task taskDb = find(Task.class, task.getId());
        Member memberDb = find(Member.class, member.getId());
        taskDb.getMembers().remove(memberDb);

        if (taskDb.getMembers().isEmpty() && taskHasNoWorks(taskDb)) {
            logger.info("Task has no more Members on it. It will be deleted");
            remove(taskDb);
        } else {
            persist(taskDb);
        }
    }

    private void deleteWorks(Task task, Member member, DateTime month) {
        Query query = createNamedQuery("Work.DELETE_BY_MEMBERTaskMonth");
        query.setParameter("fmemberId", member.getId());
        query.setParameter("ftaskId", task.getId());
        query.setParameter("fmonth", month);

        query.executeUpdate();
    }

    private boolean taskHasNoWorks(Task taskDb) {

        Query query = createNamedQuery("Work.COUNT_BY_TASK");
        query.setParameter("fTaskId", taskDb.getId());
        int total = ((Number) query.getSingleResult()).intValue();
        return total == 0;
    }


    private boolean isLoop(Task taskToUpdate) {
        boolean result = taskToUpdate != null && taskToUpdate.getTask() != null && taskToUpdate.getId().equals(taskToUpdate.getTask().getId());
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
