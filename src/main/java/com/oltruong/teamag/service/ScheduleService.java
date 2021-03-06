package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.utils.CalendarUtils;
import org.joda.time.DateTime;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;


@Singleton
public class ScheduleService {

    @Inject
    MemberService memberService;

    @Inject
    EmailService emailService;

    @Inject
    AbsenceDayService absenceDayService;

    @Inject
    TaskService taskService;

    @Inject
    WorkService workService;

    @Schedule(second = "0", minute = "0", hour = "2", dayOfMonth = "1")
    public void generateWorkList() {
        memberService.findActiveMembers().forEach(this::generateWork);
    }

    private void generateWork(Member member) {
        DateTime dateTimeMonth = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay();

        List<AbsenceDay> absenceDayList = absenceDayService.findByMemberAndMonth(member.getId(), dateTimeMonth.getMonthOfYear());
        List<Task> taskList = taskService.findTasksForMember(member);
        workService.findOrCreateWorks(member, dateTimeMonth, taskList, absenceDayList);
    }


    @Schedule(second = "0", minute = "0", hour = "13")
    public void sendReminderAfternoon() {
        final DateTime now = DateTime.now();
        if (CalendarUtils.isWorkingDay(now)) {
            memberService.findActiveMembers().forEach(this::sendReminder);
        }
    }

    private void sendReminder(Member member) {
        final DateTime now = DateTime.now().withTimeAtStartOfDay();
        List<AbsenceDay> absenceDayList = absenceDayService.findByMemberAndMonth(member.getId(), now.getMonthOfYear());

        if (!sendReminderMonth(member, now, absenceDayList)) {
            sendReminderWeek(member, now, absenceDayList);
        }
    }

    private boolean sendReminderWeek(Member member, DateTime now, List<AbsenceDay> absenceDayList) {
        return sendReminderUnit(member, now, absenceDayList, CalendarUtils::isLastWorkingDayOfWeek, this::buildEmailWeek);
    }

    private boolean sendReminderMonth(Member member, DateTime now, List<AbsenceDay> absenceDayList) {
        return sendReminderUnit(member, now, absenceDayList, CalendarUtils::isLastWorkingDayOfMonth, this::buildEmailMonth);
    }

    private boolean sendReminderUnit(Member member, DateTime now, List<AbsenceDay> absenceDayList, BiPredicate<DateTime, List<DateTime>> tester, Function<Member, MailBean> emailSupplier) {

        if (isLastWorkingDay(now, absenceDayList, tester)) {
            MailBean emailBean = emailSupplier.apply(member);
            emailService.sendEmailCopyBlindAdministrator(emailBean);
            return true;
        }
        return false;
    }


    private boolean isLastWorkingDay(DateTime now, List<AbsenceDay> absenceDayList, BiPredicate<DateTime, List<DateTime>> tester) {

        List<DateTime> absenceDays = Lists.newArrayListWithExpectedSize(absenceDayList.size());
        absenceDayList.forEach(absence -> absenceDays.add(absence.getDay()));

        List<DateTime> workingDays = CalendarUtils.getWorkingDays(now);

        workingDays.removeIf(absenceDays::contains);

        return tester.test(now, workingDays);
    }

    private MailBean buildEmailWeek(Member member) {

        MailBean mail = new MailBean();
        mail.setSubject("Rappel : realise semaine");
        mail.setContent("Bonjour " + member.getName() + ".\nComme c'est le dernier jour complet travaille de la semaine, n'hesite pas a remplir ton realise si ca n'est pas deja fait.\nMerci d'avance et bon week-end !\n\n----------------\nMessage genere automatiquement");
        mail.addRecipient(member.getEmail());
        return mail;
    }

    private MailBean buildEmailMonth(Member member) {
        MailBean mail = new MailBean();
        mail.setSubject("Rappel : realise du mois");
        mail.setContent("Bonjour " + member.getName() + ".\nComme c'est le dernier jour complet travaille du mois, merci de remplir ton realise si ca n'est pas deja fait.\nMerci d'avance !\n\n----------------\nMessage genere automatiquement");
        mail.addRecipient(member.getEmail());
        return mail;
    }
}
