package fr.oltruong.teamag.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.Work;
import fr.oltruong.teamag.utils.CalendarUtils;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Stateless
public class WorkService extends AbstractService {


    public Map<Task, List<Work>> findWorksNotNullByMonth(Member member, DateTime month) {
        return transformWorkList(findWorkByMemberMonth(member.getId(), month, "Work.FIND_BY_MEMBER_MONTH_NOT_NULL"));
    }

    public List<Work> findWorksNotNullByWeek(Long memberId, int weekNumber) {
        List<Work> workList = findWorkListByMemberMonth(memberId, DateTime.now().withWeekOfWeekyear(weekNumber).withDayOfMonth(1));

        workList.removeIf(work -> work.getDay().getWeekOfWeekyear() != weekNumber);

        List<Task> tasksNotEmpty = Lists.newArrayList();
        workList.forEach(work -> {
            if (work.getTotal().doubleValue() != 0 && !tasksNotEmpty.contains(work.getTask())) {
                tasksNotEmpty.add(work.getTask());
            }
        });
        workList.removeIf(work -> !tasksNotEmpty.contains(work.getTask()));
        return workList;
    }

    public Map<Task, List<Work>> findOrCreateWorks(Member member, DateTime month, List<Task> taskList, List<AbsenceDay> absenceDayList) {

        List<Work> listWorks = findWorkListByMemberMonth(member.getId(), month);

        if (CollectionUtils.isEmpty(listWorks)) {
            listWorks = createWorks(member, month, taskList, absenceDayList);
        } else {
            //Check if all work are here
            Multimap<Task, Work> taskWorkMap = buildTaskWorkMap(listWorks);
            List<DateTime> workingDays = CalendarUtils.getWorkingDays(month);

            //FIXME when Task has a real equals, clean this
            List<Task> missingTaskList = Lists.newArrayListWithCapacity(taskList.size());
            for (Task task : taskList) {
                boolean contains = false;
                for (Task taskKey : taskWorkMap.keySet()) {
                    if (taskKey.equals(task)) {
                        contains = true;
                    }
                }
                if (!contains) {
                    logger.error("Task [" + task.getProject() + "-" + task.getName() + "] has no work. It will be created");
                    missingTaskList.add(task);
                }
            }

            if (!missingTaskList.isEmpty()) {
                List<Work> listMissingWorks = createWorks(member, month, missingTaskList, absenceDayList);
                listWorks.addAll(listMissingWorks);
                taskWorkMap = buildTaskWorkMap(listWorks);
            }

            //Check all days are present
            for (DateTime day : workingDays) {

                for (Task task : taskWorkMap.keySet()) {

                    boolean found = false;
                    Collection<Work> workList = taskWorkMap.get(task);
                    for (Work work : workList) {
                        if (work.getDay().equals(day)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        logger.warn("CREATING WORK FOR TASK [" + task.getId() + "] for Day [" + day + "]");
                        Work workCreated = createWork(member, month, task, day);
                        taskWorkMap.get(task).add(workCreated);
                        listWorks.add(workCreated);
                    }
                }
            }
        }
        List<Work> workListNoDuplicates = removeDuplicates(listWorks);
        return transformWorkList(workListNoDuplicates);

    }

    private List<Work> removeDuplicates(List<Work> listWorks) {
        List<String> workReferenceList = Lists.newArrayListWithCapacity(listWorks.size());
        List<Work> noDuplicateList = Lists.newArrayListWithCapacity(listWorks.size());
        for (Work work : listWorks) {
            String workKey = buildWorkKey(work);
            if (workReferenceList.contains(workKey)) {

                logger.error("ERROR DUPLICATE IN WORK TASK[" + work.getTask().getId().toString() + "] DAY[" + work.getDay() + "] MEMBER ID[" + work.getMember().getId().toString() + "]");
                logger.error("Removing WORK" + work.getId());

                remove(work);
            } else {
                workReferenceList.add(workKey);
                noDuplicateList.add(work);
            }
        }

        if (noDuplicateList.size() != listWorks.size()) {
            logger.error(listWorks.size() - noDuplicateList.size() + " duplicates");
        }
        return noDuplicateList;
    }

    private Multimap<Task, Work> buildTaskWorkMap(List<Work> listWorks) {
        Multimap<Task, Work> taskWorkMap = ArrayListMultimap.create();
        listWorks.forEach(work -> {
            if (taskWorkMap.containsKey(work.getTask())) {
                taskWorkMap.get(work.getTask()).add(work);
            } else {
                taskWorkMap.put(work.getTask(), work);
            }
        });
        return taskWorkMap;
    }

    private String buildWorkKey(Work work) {
        return work.getMember().getId().toString() + work.getTask().getId().toString() + work.getDay().toString();
    }

    private List<Work> createWorks(Member member, DateTime month, List<Task> taskList, List<AbsenceDay> absenceDayList) {

        List<DateTime> workingDays = CalendarUtils.getWorkingDays(month);

        List<Work> workList = Lists.newArrayListWithExpectedSize(taskList.size() * workingDays.size());
        for (Task task : taskList) {
            for (DateTime day : workingDays) {
                Double total = 0d;
                //Absence Task
                if (Long.valueOf(1L).equals(task.getId())) {
                    AbsenceDay absenceDay = findAbsenceDay(absenceDayList, day);
                    if (absenceDay != null) {
                        total = Double.valueOf(absenceDay.getValue().toString());
                    }
                }
                Work work = createWork(member, month, task, day, total);
                workList.add(work);
            }
        }

        return workList;
    }


    private AbsenceDay findAbsenceDay(List<AbsenceDay> absenceDayList, DateTime day) {
        if (absenceDayList != null) {
            for (AbsenceDay absenceDay : absenceDayList) {
                if (absenceDay.getDay().isEqual(day)) {
                    return absenceDay;
                }
            }
        }

        return null;
    }

    private List<Work> findWorkListByMemberMonth(Long memberId, DateTime month) {
        return findWorkByMemberMonth(memberId, month, "Work.FIND_BY_MEMBER_MONTH");
    }

    private List<Work> findWorkByMemberMonth(Long memberId, DateTime month, String queryString) {
        TypedQuery<Work> query = createNamedQuery(queryString, Work.class);
        query.setParameter("fmemberId", memberId);
        query.setParameter("fmonth", month);

        return query.getResultList();
    }

    public Map<DateTime, Double> findWorkDays(Member member, DateTime month) {
        TypedQuery<Object[]> query = createNamedQuery("Work.FIND_WORKDAYS_BY_MEMBER_MONTH", Object[].class);
        query.setParameter("fmemberId", member.getId());
        query.setParameter("fmonth", month);
        List<Object[]> objects = query.getResultList();

        Map<DateTime, Double> map = Maps.newHashMapWithExpectedSize(objects.size());
        objects.forEach(object -> map.put((DateTime) object[0], (Double) object[1]));
        return map;
    }

    public int getSumWorks(Member member, DateTime month) {
        Query query = createNamedQuery("Work.SUM_BY_MONTH_MEMBER");
        query.setParameter("fmemberId", member.getId());
        query.setParameter("fmonth", month);
        Number sumOfPrice = (Number) query.getSingleResult();
        return sumOfPrice.intValue();
    }

    private Map<Task, List<Work>> transformWorkList(List<Work> listWorks) {

        Map<Task, List<Work>> worksByTask = Maps.newHashMap();
        if (listWorks != null) {
            for (Work work : listWorks) {
                if (!worksByTask.containsKey(work.getTask())) {
                    worksByTask.put(work.getTask(), new ArrayList<>());
                }
                worksByTask.get(work.getTask()).add(work);
                work.getTask().addTotal(work.getTotal());
            }

        }

        return worksByTask;
    }

    public Work createWork(Member member, DateTime month, Task task, DateTime day) {
        return createWork(member, month, task, day, 0d);
    }

    private Work createWork(Member member, DateTime month, Task task, DateTime day, Double value) {
        Work work = new Work();
        work.setDay(day);
        work.setMember(member);
        work.setMonth(month);
        work.setTask(task);
        work.setTotal(value);

        persist(work);
        return work;
    }

    public void updateWorks(List<Work> workList) {
        for (Work work : workList) {
            work.setTotal(work.getTotalEdit());
            merge(work);
        }
    }


    public List<Work> getWorksMonth(DateTime month) {
        TypedQuery<Work> query = createNamedQuery("Work.FIND_BY_MONTH", Work.class);
        query.setParameter("fmonth", month);

        return query.getResultList();
    }

    public List<Work> findWorkByTask(Long taskId) {
        TypedQuery<Work> query = createNamedQuery("Work.FIND_BY_TASK_MEMBER", Work.class);
        query.setParameter("fTaskId", taskId);

        return query.getResultList();

    }

    private Work findAbsenceWork(AbsenceDay absenceDay) {
        Work absenceWork = null;

        TypedQuery<Work> query = createNamedQuery("Work.FIND_ABSENCE_BY_MEMBER", Work.class);
        query.setParameter("fmemberId", absenceDay.getMember().getId());
        query.setParameter("fday", absenceDay.getDay());


        List<Work> workAbsenceList = query.getResultList();
        if (workAbsenceList != null && !workAbsenceList.isEmpty()) {
            absenceWork = workAbsenceList.get(0);

        }
        return absenceWork;
    }

    public void updateWorkAbsence(AbsenceDay absenceDay) {
        updateWorkAbsence(absenceDay, Double.parseDouble(absenceDay.getValue().toString()));
    }

    public void removeWorkAbsence(AbsenceDay absenceDay) {
        updateWorkAbsence(absenceDay, 0d);
    }

    private void updateWorkAbsence(AbsenceDay absenceDay, double total) {
        if (isPresentOrFutureMonth(absenceDay)) {
            Work absenceWork = findAbsenceWork(absenceDay);
            if (absenceWork != null) {

                absenceWork.setTotal(total);
                merge(absenceWork);
            }
        }
    }

    private boolean isPresentOrFutureMonth(AbsenceDay absenceDay) {
        return absenceDay.getDay().withDayOfMonth(2).withTimeAtStartOfDay().isAfter(DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay());
    }


}
