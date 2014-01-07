package fr.oltruong.teamag.entity;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Olivier Truong
 */
public class EntityFactory {
    private EntityFactory() {

    }

    public static Member createMember() {
        Member member = new Member();

        member.setName("Carot" + DateTime.now().getMillis());
        member.setPassword(Hashing.sha256().hashString("toto", Charsets.UTF_8).toString());
        member.setCompany("my company");
        member.setEmail("dummy@email.com");
        member.setEstimatedworkDays(0f);
        member.setComment("Comment");
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

    public static List<Absence> createAbsenceList(int number) {
        List<Absence> absenceList = Lists.newArrayListWithExpectedSize(number);
        for (int i = 0; i < number; i++) {
            absenceList.add(createAbsence());
        }
        return absenceList;
    }

    public static List<Activity> createActivityList(int number) {
        List<Activity> activityList = Lists.newArrayListWithExpectedSize(number);
        for (int i = 0; i < number; i++) {
            activityList.add(createActivity());
        }
        return activityList;
    }


    public static List<BusinessCase> createBCList(int number) {
        List<BusinessCase> bcList = Lists.newArrayListWithExpectedSize(number);
        for (int i = 0; i < number; i++) {
            bcList.add(createBusinessCase());
        }
        return bcList;
    }


    public static Activity createActivity() {
        Activity activity = new Activity();
        activity.setName("MyActivity" + DateTime.now().getMillis());
        activity.setBc(createBusinessCase());
        return activity;
    }


    public static BusinessCase createBusinessCase() {
        BusinessCase businessCase = new BusinessCase();
        businessCase.setName("MyBC" + DateTime.now().getMillis());
        businessCase.setAmount(Float.valueOf(DateTime.now().getMillis()));
        businessCase.setNumber(Integer.valueOf(DateTime.now().getMillisOfSecond()));
        return businessCase;
    }


}
