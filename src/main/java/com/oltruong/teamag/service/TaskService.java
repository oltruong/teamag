package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.utils.CalendarUtils;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


public class TaskService extends AbstractService<Task> {


    @Inject
    private WorkService workService;


    @Override
    public List<Task> findAll() {
        return createTypedQuery("Task.FIND_ALL", entityProvider()).getResultList();
    }

    public List<Task> findAllNonAdminTasks() {
        return createTypedQuery("Task.FIND_NONTYPE", entityProvider()).setParameter("memberType", MemberType.ADMINISTRATOR).getResultList();
    }

    public List<Task> findTaskWithActivity() {
        return createTypedQuery("Task.FIND_ALL_WITH_ACTIVITY", entityProvider()).getResultList();
    }

    @Override
    public void merge(Task taskToUpdate) {
        if (isLoop(taskToUpdate)) {
            throw new IllegalArgumentException();
        }
        super.merge(taskToUpdate);
    }

    public Task persist(Task task) {
        List<Task> allTaskList = createTypedQuery("Task.FIND_BY_NAME", entityProvider()).setParameter("fname", task.getName()).setParameter("fproject", task.getProject()).getResultList();

        if (CollectionUtils.isNotEmpty(allTaskList)) {
            throw new EntityExistsException();
        } else {
            super.persist(task);
        }
        return task;
    }

    @Transactional
    public Task persist(DateTime month, Member member, Task task) {
        TypedQuery<Task> query = createTypedQuery("Task.FIND_BY_NAME", entityProvider());
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
            super.persist(task);
            taskDB = task;

        }

        flush();

        List<DateTime> workingDayList = CalendarUtils.getWorkingDays(month);

        for (DateTime day : workingDayList) {
            workService.createWork(member, month, taskDB, day);
        }
        return taskDB;

    }

    public List<Task> findTasksForMember(Member member) {
        List<Task> taskList = entityManager.createNamedQuery("Task.FIND_MEMBER", entityProvider()).setParameter("memberId", member.getId()).getResultList();
        if (CollectionUtils.isEmpty(taskList)) {
            addAbsenceTask(member, taskList);
        }
        return taskList;
    }

    private void addAbsenceTask(Member member, List<Task> taskList) {
        Task absenceTask = find(1L);
        absenceTask.addMember(member);
        merge(absenceTask);
        taskList.add(absenceTask);
    }

    @Transactional
    public void remove(Long taskId, Long memberId, DateTime month) {
        deleteWorks(taskId, memberId, month);

        Task taskDb = find(taskId);
        Member memberDb = findOtherEntity(Member.class, memberId);
        taskDb.getMembers().remove(memberDb);

        if (taskDb.getMembers().isEmpty() && taskHasNoWorks(taskDb)) {
            logger.info("Task has no more Members on it. It will be deleted");
            remove(taskDb);
        } else {
            super.persist(taskDb);
        }
    }

    private void deleteWorks(Long taskId, Long memberId, DateTime month) {
        Query query = createNamedQuery("Work.DELETE_BY_MEMBERTaskMonth");
        query.setParameter("fmemberId", memberId);
        query.setParameter("ftaskId", taskId);
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

    public Task getOrCreateAbsenceTask() {
        TypedQuery<Task> query = createTypedQuery("Task.FIND_BY_NAME", entityProvider());
        query.setParameter("fname", "Absence");
        query.setParameter("fproject", "");

        Task task;
        List<Task> taskList = query.getResultList();

        if (!CollectionUtils.isEmpty(taskList)) {
            task = taskList.get(0);
        } else {
            logger.info("Absence task is not found. Will be created");
            Task newTask = new Task();
            newTask.setName("Absence");
            newTask.setProject("");
            super.persist(newTask);
            task = newTask;
        }
        return task;
    }


    @Override
    Class<Task> entityProvider() {
        return Task.class;
    }
}
