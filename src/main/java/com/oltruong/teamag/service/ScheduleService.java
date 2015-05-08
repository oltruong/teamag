package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.utils.CalendarUtils;
import java.time.LocalDate;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

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
    public void sendReminder() {
        final LocalDate now = LocalDate.now();
        if (CalendarUtils.isWorkingDay(now)) {
            List<Member> activeMembers = memberService.findActiveMembers();
            activeMembers.forEach(this::sendReminder);
        }
    }

    private void sendReminder(Member member) {
        final LocalDate now = LocalDate.now().withTimeAtStartOfDay();
        List<AbsenceDay> absenceDayList = absenceDayService.findByMemberAndMonth(member.getId(), now.getMonthOfYear());

        if (isLastWorkingWeekDay(now, absenceDayList)) {
            MailBean emailBean = buildEmail(member);
            emailService.sendEmailCopyBlindAdministrator(emailBean);
        }
    }

    private boolean isLastWorkingWeekDay(LocalDate now, List<AbsenceDay> absenceDayList) {

        List<LocalDate> absenceDays = Lists.newArrayListWithExpectedSize(absenceDayList.size());
        absenceDayList.forEach(absence -> absenceDays.add(absence.getDay()));

        List<LocalDate> workingDays = CalendarUtils.getWorkingDays(now);

        workingDays.removeIf(day -> absenceDays.contains(day));

        return CalendarUtils.isLastWorkingDayOfWeek(now, workingDays);
    }

    private MailBean buildEmail(Member member) {

        MailBean mail = new MailBean();
        mail.setSubject("Rappel : réalisé semaine");
        mail.setContent("Bonjour " + member.getName() + ".\nComme c'est le dernier jour complet travaillé de la semaine, n'hésite pas à remplir ton réalisé si ça n'est pas déjà fait.\nMerci d'avance et bon week-end !\n\n----------------\nMessage généré automatiquement");
        mail.addRecipient(member.getEmail());
        return mail;
    }
}
