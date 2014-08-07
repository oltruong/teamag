package fr.oltruong.teamag.model.builder;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.Activity;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Parameter;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.WeekComment;
import fr.oltruong.teamag.model.Work;
import fr.oltruong.teamag.model.WorkLoad;
import fr.oltruong.teamag.model.WorkRealized;
import fr.oltruong.teamag.model.enumeration.MemberType;
import fr.oltruong.teamag.model.enumeration.ParameterName;
import fr.oltruong.teamag.utils.TestUtils;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author Olivier Truong
 */
public class EntityFactory {

    private static final int MINIMUM = 3;

    private static final int MAXIMUM = 10;

    private EntityFactory() {

    }

    public static Long createRandomLong() {
        return Long.valueOf(new Random().nextLong());
    }

    public static Integer createRandomInteger() {
        return Integer.valueOf(new Random().nextInt());
    }

    public static Member createMember() {
        Member member = new Member();

        member.setName("Carot" + DateTime.now().getMillis());
        member.setPassword(Hashing.sha256().hashString("toto", Charsets.UTF_8).toString());
        member.setCompany("my company");
        member.setEmail("dummy@email.com");
        member.setEstimatedWorkDays(0d);
        member.setMemberType(MemberType.BASIC);
        return member;
    }

    public static Absence createAbsence() {

        Absence absence = new Absence();
        absence.setBeginDate(DateTime.now());
        absence.setBeginType(Absence.AFTERNOON_ONLY);
        absence.setEndDate(DateTime.now().plusDays(5));
        absence.setEndType(Absence.MORNING_ONLY);
        absence.setMember(createMember());
        return absence;
    }


    public static Absence createAbsence(DateTime beginDate, Integer beginType, DateTime endDate, Integer endType) {
        Absence absence = new Absence();
        absence.setBeginDate(beginDate.withTimeAtStartOfDay());
        absence.setBeginType(beginType);
        absence.setEndDate(endDate.withTimeAtStartOfDay());
        absence.setEndType(endType);
        absence.setMember(createMember());
        return absence;
    }


    public static AbsenceDay createAbsenceDay() {

        AbsenceDay absenceDay = new AbsenceDay(createAbsence());

        DateTime now = DateTime.now();
        absenceDay.setDay(now);
        absenceDay.setMember(createMember());
        return absenceDay;
    }


    public static Activity createActivity() {
        Activity activity = new Activity();
        activity.setId(null);
        activity.setName("MyActivity" + DateTime.now().getMillis());
        activity.setBc(createBusinessCase());
        activity.setComment("MyComment" + LocalDate.now().toString());
        activity.setDelegated(Boolean.FALSE);
        activity.setAmount(Double.valueOf(LocalDate.now().getDayOfMonth()));
        return activity;
    }


    public static BusinessCase createBusinessCase() {
        DateTime now = DateTime.now();
        BusinessCase businessCase = new BusinessCase();
        businessCase.setName("MyBC" + now.getMillis());
        businessCase.setAmount(Double.valueOf(now.getMillis()));
        businessCase.setIdentifier("" + now.getMillisOfSecond());
        businessCase.setComment("Comment" + now.toString());
        businessCase.setId(null);
        return businessCase;
    }

    public static WeekComment createWeekComment() {
        DateTime now = DateTime.now();
        WeekComment weekComment = new WeekComment(createMember(), now.getWeekOfWeekyear(), now.getYear());
        weekComment.setComment("Comment" + now.toString());
        return weekComment;
    }

    public static Parameter createParameter() {
        Parameter parameter = new Parameter();
        parameter.setName(ParameterName.ADMINISTRATOR_EMAIL);
        parameter.setValue("toto");
        return parameter;
    }


    public static WorkLoad createWorkLoad() {
        WorkLoad workLoad = new WorkLoad(createBusinessCase(), createMember());
        workLoad.setEstimated(Double.valueOf(Double.valueOf(12d)));
        workLoad.setRealized(Double.valueOf(10d));
        return workLoad;
    }


    public static Task createTask() {
        Task task = new Task();

        task.setName("createTask" + Instant.now().getEpochSecond());
        task.setProject("my project");

        task.addMember(createMember());

        return task;

    }


    public static Work createWork() {
        Work work = new Work();

        TestUtils.setPrivateAttribute(work, LoggerFactory.getLogger(Work.class.getName()), "logger");
        work.setMember(createMember());
        work.setDay(DateTime.now());
        work.setTask(createTask());
        work.setMonth(DateTime.now().withDayOfMonth(1));

        work.setTotal(Double.valueOf(0.5d));
        return work;
    }

    public static WorkRealized createWorkRealized() {
        LocalDate now = LocalDate.now();


        Task task = createTask();

        WorkRealized workRealized = new WorkRealized();
        workRealized.setId(null);
        workRealized.setMemberId(11l);
        workRealized.setMonth(now.getMonth().getValue());
        workRealized.setYear(now.getYear());
        workRealized.setRealized(2.5d);
        workRealized.setTaskId(12l);
        return workRealized;
    }

    public static <E> List<E> createList(Supplier<E> supplier) {
        int randomListSize = Math.abs(new Random().nextInt(MAXIMUM - MINIMUM)) + MINIMUM;
        return createList(supplier, randomListSize);
    }

    public static <E> List<E> createList(Supplier<E> supplier, int listSize) {


        List<E> objectList = Lists.newArrayListWithExpectedSize(listSize);
        for (int i = 0; i < listSize; i++) {
            objectList.add(supplier.get());
        }
        return objectList;
    }


}
