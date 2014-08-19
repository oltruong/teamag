package fr.oltruong.teamag.transformer;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.utils.CalendarUtils;
import fr.oltruong.teamag.utils.MessageManager;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

import java.util.List;

/**
 * @author Olivier Truong
 */
public final class ScheduleEventTransformer {

    private ScheduleEventTransformer() {
    }

    public static List<ScheduleEvent> convertAbsenceList(List<Absence> absenceList, MessageManager messageManager) {
        List<ScheduleEvent> eventList = Lists.newArrayListWithExpectedSize(absenceList.size());
        for (Absence absence : absenceList) {
            eventList.addAll(convertAbsence(absence, "member-" + absence.getMember().getId(), messageManager));
        }


        return eventList;
    }

    public static List<ScheduleEvent> getDaysOff(MessageManager messageManager) {
        List<DateTime> daysOffList = CalendarUtils.getListDaysOff();

        List<ScheduleEvent> daysOffEvents = Lists.newArrayListWithExpectedSize(daysOffList.size());

        String dayOffMessage = messageManager.getMessage("dayOff");
        for (DateTime dayOff : daysOffList) {
            DefaultScheduleEvent event = new DefaultScheduleEvent();
            event.setTitle(dayOffMessage);
            event.setStartDate(dayOff.toDate());
            event.setEndDate(dayOff.toDate());
            event.setAllDay(true);
            event.setStyleClass("dayOff");
            daysOffEvents.add(event);
        }
        return daysOffEvents;
    }

    private static List<ScheduleEvent> convertAbsence(Absence absence, String className, MessageManager messageManager) {

        List<ScheduleEvent> eventList = Lists.newArrayList();

        //Same day
        if (absence.getBeginDate().withTimeAtStartOfDay().isEqual(absence.getEndDate().withTimeAtStartOfDay())) {
            eventList.add(buildBeginDayEvent(absence, className, messageManager));
            //Several days
        } else {

            MutableDateTime beginMutableTime = absence.getBeginDate().withTimeAtStartOfDay().toMutableDateTime();
            MutableDateTime endMutableTime = absence.getEndDate().withTimeAtStartOfDay().toMutableDateTime();


            if (Absence.AFTERNOON_ONLY.equals(absence.getBeginType())) {
                eventList.add(buildBeginDayEvent(absence, className, messageManager));
                beginMutableTime.addDays(1);
            }

            if (Absence.MORNING_ONLY.equals(absence.getEndType())) {
                eventList.add(buildEndMorningDayEvent(absence, className, messageManager));
                endMutableTime.addDays(-1);
            }

            if (!beginMutableTime.isAfter(endMutableTime)) {
                DefaultScheduleEvent event = new DefaultScheduleEvent();
                event.setTitle(messageManager.getMessage("absencePerson", absence.getMember().getName()));
                event.setStartDate(beginMutableTime.toDate());
                event.setEndDate(endMutableTime.toDate());
                event.setAllDay(true);
                event.setStyleClass(className);
                eventList.add(event);
            }

        }


        return eventList;

    }

    private static DefaultScheduleEvent buildBeginDayEvent(Absence absence, String className, MessageManager messageManager) {
        DefaultScheduleEvent event = new DefaultScheduleEvent();

        if (Absence.MORNING_ONLY.equals(absence.getBeginType())) {
            event.setTitle(messageManager.getMessage("absenceMorningPerson", absence.getMember().getName()));
            event.setStartDate(absence.getBeginDate().withTimeAtStartOfDay().toDate());
            event.setEndDate(absence.getBeginDate().withTimeAtStartOfDay().plusHours(12).toDate());
        } else if (Absence.AFTERNOON_ONLY.equals(absence.getBeginType()) || Absence.AFTERNOON_ONLY.equals(absence.getEndType())) {
            event.setTitle(messageManager.getMessage("absenceAfternoonPerson", absence.getMember().getName()));
            event.setStartDate(absence.getBeginDate().withTimeAtStartOfDay().plusHours(13).toDate());
            event.setEndDate(absence.getBeginDate().withTimeAtStartOfDay().plusHours(23).toDate());

        } else {
            event.setTitle(messageManager.getMessage("absencePerson", absence.getMember().getName()));
            event.setStartDate(absence.getBeginDate().withTimeAtStartOfDay().toDate());
            event.setEndDate(absence.getBeginDate().withTimeAtStartOfDay().toDate());
            event.setAllDay(true);
        }
        event.setStyleClass(className);
        return event;
    }


    private static DefaultScheduleEvent buildEndMorningDayEvent(Absence absence, String className, MessageManager messageManager) {
        DefaultScheduleEvent event = new DefaultScheduleEvent();

        event.setTitle(messageManager.getMessage("absenceMorningPerson", absence.getMember().getName()));
        event.setStartDate(absence.getEndDate().withTimeAtStartOfDay().toDate());
        event.setEndDate(absence.getEndDate().withTimeAtStartOfDay().plusHours(12).toDate());
        event.setStyleClass(className);
        return event;
    }

}
