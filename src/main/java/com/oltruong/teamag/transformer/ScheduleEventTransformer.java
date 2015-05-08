package com.oltruong.teamag.transformer;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.utils.MessageManager;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
        List<LocalDate> daysOffList = CalendarUtils.getListDaysOff();

        List<ScheduleEvent> daysOffEvents = Lists.newArrayListWithExpectedSize(daysOffList.size());

        String dayOffMessage = messageManager.getMessage("dayOff");
        for (LocalDate dayOff : daysOffList) {
            DefaultScheduleEvent event = new DefaultScheduleEvent();
            event.setTitle(dayOffMessage);
            event.setStartDate(Date.from(dayOff.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            event.setEndDate(Date.from(dayOff.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            event.setAllDay(true);
            event.setStyleClass("dayOff");
            daysOffEvents.add(event);
        }
        return daysOffEvents;
    }

    private static List<ScheduleEvent> convertAbsence(Absence absence, String className, MessageManager messageManager) {

        List<ScheduleEvent> eventList = Lists.newArrayList();

        //Same day
        if (absence.getBeginDate().isEqual(absence.getEndDate())) {
            eventList.add(buildBeginDayEvent(absence, className, messageManager));
            //Several days
        } else {

            LocalDate beginMutableTime = absence.getBeginDate();
            LocalDate endMutableTime = absence.getEndDate();


            if (Absence.AFTERNOON_ONLY.equals(absence.getBeginType())) {
                eventList.add(buildBeginDayEvent(absence, className, messageManager));
                beginMutableTime = beginMutableTime.plusDays(1);
            }

            if (Absence.MORNING_ONLY.equals(absence.getEndType())) {
                eventList.add(buildEndMorningDayEvent(absence, className, messageManager));
                endMutableTime = endMutableTime.plusDays(-1);
            }

            if (!beginMutableTime.isAfter(endMutableTime)) {
                DefaultScheduleEvent event = new DefaultScheduleEvent();
                event.setTitle(messageManager.getMessage("absencePerson", absence.getMember().getName()));
                event.setStartDate(Date.from(beginMutableTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                event.setEndDate(Date.from(endMutableTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
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
            event.setStartDate(Date.from(absence.getBeginDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            event.setEndDate(absence.getBeginDate().plusHours(12).toDate());
        } else if (Absence.AFTERNOON_ONLY.equals(absence.getBeginType()) || Absence.AFTERNOON_ONLY.equals(absence.getEndType())) {
            event.setTitle(messageManager.getMessage("absenceAfternoonPerson", absence.getMember().getName()));
            event.setStartDate(absence.getBeginDate().plusHours(13).toDate());
            event.setEndDate(absence.getBeginDate().plusHours(23).toDate());

        } else {
            event.setTitle(messageManager.getMessage("absencePerson", absence.getMember().getName()));
            event.setStartDate(Date.from(absence.getBeginDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            event.setEndDate(Date.from(absence.getBeginDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            event.setAllDay(true);
        }
        event.setStyleClass(className);
        return event;
    }


    private static DefaultScheduleEvent buildEndMorningDayEvent(Absence absence, String className, MessageManager messageManager) {
        DefaultScheduleEvent event = new DefaultScheduleEvent();

        event.setTitle(messageManager.getMessage("absenceMorningPerson", absence.getMember().getName()));
        event.setStartDate(Date.from(absence.getEndDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        event.setEndDate(absence.getEndDate().plusHours(12).toDate());
        event.setStyleClass(className);
        return event;
    }

}
