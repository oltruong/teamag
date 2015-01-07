package fr.oltruong.teamag.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.WeekComment;
import fr.oltruong.teamag.model.Work;
import fr.oltruong.teamag.utils.CalendarUtils;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Stateless
public class WorkService extends AbstractService {


    @Inject
    private AbsenceDayService absenceDayService;


    public Map<Task, List<Work>> findWorksNotNull(Member member, DateTime month) {

        Map<Task, List<Work>> worksByTask = transformWorkList(findWorkList(member, month));
        List<Task> emptyWorkTasks = Lists.newArrayListWithCapacity(worksByTask.size());

        for (Task task : worksByTask.keySet()) {
            List<Work> workList = worksByTask.get(task);

            boolean empty = true;
            for (Work work : workList) {
                empty &= !Double.valueOf(0d).equals(work.getTotal());
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

    public Map<Task, List<Work>> findOrCreateWorks(Member member, DateTime month, List<Task> taskList) {

        List<Work> listWorks = findWorkList(member, month);

        if (CollectionUtils.isEmpty(listWorks)) {

            getLogger().debug("Creating new works for member " + member.getName());
            listWorks = createWorks(member, month, taskList);
        } else {
            //Check if all work are here
            Multimap<Task, Work> multimap = ArrayListMultimap.create();
            listWorks.forEach(work -> {
                if (multimap.containsKey(work.getTask())) {
                    multimap.get(work.getTask()).add(work);
                } else {
                    multimap.put(work.getTask(), work);
                }
            });

            List<DateTime> workingDays = CalendarUtils.getWorkingDays(month);

            //Check all days are present
            for (DateTime day : workingDays) {

                for (Task task : multimap.keySet()) {

                    boolean found = false;
                    Collection<Work> workList = multimap.get(task);
                    for (Work work : workList) {
                        if (work.getDay().equals(day)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        getLogger().warn("CREATING WORK FOR TASK [" + task.getId() + "] for Day [" + day + "]");
                        Work workCreated = createWork(member, month, task, day);
                        multimap.get(task).add(workCreated);
                        listWorks.add(workCreated);
                    }
                }


            }


        }


        List<String> workReferenceList = Lists.newArrayListWithCapacity(listWorks.size());

        int count = 0;
        for (Work work : listWorks) {
            String workKey = work.getMember().getId().toString() + work.getTask().getId().toString() + work.getDay().toString();
            if (workReferenceList.contains(workKey)) {
                count++;
                getLogger().error("ERROR DUPLICATE IN WORK TASK[" + work.getTask().getId().toString() + "] DAY[" + work.getDay() + "] MEMBER ID[" + work.getMember().getId().toString() + "]");
                getLogger().error("Removing WORK" + work.getId());
                remove(work);
            } else {
                workReferenceList.add(workKey);
            }
        }
        if (count != 0) {
            getLogger().error(count + " duplicates");

        } else {
            getLogger().debug("no duplicate");
        }


        return transformWorkList(listWorks);

    }

    @SuppressWarnings("unchecked")
    private List<Work> findWorkList(Member member, DateTime month) {

        Query query = createNamedQuery("findWorksByMemberMonth");
        query.setParameter("fmemberId", member.getId());
        query.setParameter("fmonth", month);

        return (List<Work>) query.getResultList();


    }

    public Map<DateTime, Double> findWorkDays(Member member, DateTime month) {
        Query query = createNamedQuery("findWorkDaysByMemberMonth");
        query.setParameter("fmemberId", member.getId());
        query.setParameter("fmonth", month);

        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();


        Map<DateTime, Double> map = Maps.newHashMapWithExpectedSize(objects.size());

        objects.forEach(object -> map.put((DateTime) object[0], (Double) object[1]));

        return map;

    }

    public int getSumWorks(Member member, DateTime month) {
        Query query = createNamedQuery("countWorksMemberMonth");
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


    private List<Work> createWorks(Member member, DateTime month, List<Task> taskList) {

        List<Work> workList = null;


        List<AbsenceDay> absenceDayList = absenceDayService.findAbsenceDayList(member.getId(), month.getMonthOfYear());


        List<DateTime> workingDays = CalendarUtils.getWorkingDays(month);

        workList = Lists.newArrayListWithExpectedSize(taskList.size() * workingDays.size());
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

    @SuppressWarnings("unchecked")
    public List<Work> getWorksMonth(DateTime month) {
        Query query = createNamedQuery("findWorksMonth");
        query.setParameter("fmonth", month);

        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Work> findWorkByTask(Long taskId) {
        Query query = createNamedQuery("findWorksByTask");
        query.setParameter("fTaskId", taskId);

        return query.getResultList();

    }

    public WeekComment findWeekComment(Long memberId, int weekYear, int year) {

        WeekComment result = null;
        Query query = createNamedQuery("findWeekComment");
        query.setParameter("fmemberId", memberId);
        query.setParameter("fweekYear", weekYear);
        query.setParameter("fyear", year);

        @SuppressWarnings("unchecked")
        List<WeekComment> weekCommentList = query.getResultList();
        if (!weekCommentList.isEmpty()) {
            result = weekCommentList.get(0);
        }

        return result;
    }


    public WeekComment createWeekComment(WeekComment weekComment) {
        persist(weekComment);
        return weekComment;
    }

    public void updateWeekComment(WeekComment weekComment) {
        merge(weekComment);
    }

    public void removeWeekComment(WeekComment weekComment) {
        WeekComment weekCommentDb = find(WeekComment.class, weekComment.getId());
        remove(weekCommentDb);
    }


    public Multimap<Task, Work> findWorksNotNull(Long memberId, int weekNumber) {
        Query query = createNamedQuery("findWorksByMemberMonth");
        query.setParameter("fmemberId", memberId);
        query.setParameter("fmonth", DateTime.now().withWeekOfWeekyear(weekNumber));

        List<Work> workList = query.getResultList();

        Multimap<Task, Work> multimap = ArrayListMultimap.create();
        for (Work work : workList) {
            multimap.put(work.getTask(), work);
        }
        return multimap;
    }

    public List<Work> findWorksList(Long memberId, int weekNumber) {
        Query query = createNamedQuery("findWorksByMemberMonth");
        query.setParameter("fmemberId", memberId);
        query.setParameter("fmonth", DateTime.now().withWeekOfWeekyear(weekNumber).withDayOfMonth(1));


        List<Work> workList = query.getResultList();

        workList.removeIf(work -> work.getDay().getWeekOfWeekyear() != weekNumber);

        List<Task> tasksNotEmpty = Lists.newArrayList();
        for (Work work : workList) {
            if (work.getTotal().doubleValue() != 0 && !tasksNotEmpty.contains(work.getTask())) {
                tasksNotEmpty.add(work.getTask());
            }
        }


        workList.removeIf(work -> !tasksNotEmpty.contains(work.getTask()));


        return workList;
    }


    private Work findAbsenceWork(AbsenceDay absenceDay) {
        Work absenceWork = null;

        Query query = createNamedQuery("findAbsenceWorkByMemberDay");
        query.setParameter("fmemberId", absenceDay.getMember().getId());
        query.setParameter("fday", absenceDay.getDay());


        List<Work> workAbsenceList = query.getResultList();
        if (workAbsenceList != null && !workAbsenceList.isEmpty()) {
            absenceWork = workAbsenceList.get(0);

        }
        return absenceWork;
    }

    public void updateWorkAbsence(AbsenceDay absenceDay) {
        if (absenceDay.getDay().getMonthOfYear() >= DateTime.now().getMonthOfYear()) {

            Work absenceWork = findAbsenceWork(absenceDay);
            if (absenceWork != null) {
                absenceWork.setTotal(Double.parseDouble(absenceDay.getValue().toString()));
                merge(absenceWork);
            }

        }
    }

    public void removeWorkAbsence(AbsenceDay absenceDay) {
        if (absenceDay.getDay().getMonthOfYear() >= DateTime.now().getMonthOfYear()) {
            Work absenceWork = findAbsenceWork(absenceDay);
            if (absenceWork != null) {

                absenceWork.setTotal(0d);
                merge(absenceWork);
            }
        }
    }


}
