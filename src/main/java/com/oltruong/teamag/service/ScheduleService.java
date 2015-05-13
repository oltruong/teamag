package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.utils.CalendarUtils;
import org.joda.time.DateTime;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * @author Olivier Truong
 */
@Singleton
public class ScheduleService {

    @Inject
    MemberService memberService;

    @Inject
    EmailService emailService;

    @Inject
    AbsenceDayService absenceDayService;


    @Schedule(second = "0", minute = "0", hour = "15")
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

        workingDays.removeIf(day -> absenceDays.contains(day));

        return tester.test(now, workingDays);
    }

    private MailBean buildEmailWeek(Member member) {

        MailBean mail = new MailBean();
        mail.setSubject("Rappel : réalisé semaine");
        mail.setContent("Bonjour " + member.getName() + ".\nComme c'est le dernier jour complet travaillé de la semaine, n'hésite pas à remplir ton réalisé si ça n'est pas déjà fait.\nMerci d'avance et bon week-end !\n\n----------------\nMessage généré automatiquement");
        mail.addRecipient(member.getEmail());
        return mail;
    }

    private MailBean buildEmailMonth(Member member) {
        MailBean mail = new MailBean();
        mail.setSubject("Rappel : réalisé du mois");
        mail.setContent("Bonjour " + member.getName() + ".\nComme c'est le dernier jour complet travaillé du mois, merci de remplir ton réalisé si ça n'est pas déjà fait.\nMerci d'avance !\n\n----------------\nMessage généré automatiquement");
        mail.addRecipient(member.getEmail());
        return mail;
    }
}
