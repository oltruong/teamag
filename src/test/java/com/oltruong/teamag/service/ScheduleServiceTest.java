package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.CalendarUtilsTest;
import com.oltruong.teamag.utils.TestUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest({DateTime.class})
public class ScheduleServiceTest {


    @Mock
    private EmailService mockEmailService;

    @Mock
    private AbsenceDayService mockAbsenceDayService;

    @Mock
    private MemberService mockMemberService;


    private ScheduleService scheduleService;

    private Long randomLong;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleService = new ScheduleService();
        randomLong = EntityFactory.createRandomLong();
        TestUtils.setPrivateAttribute(scheduleService, mockEmailService, "emailService");
        TestUtils.setPrivateAttribute(scheduleService, mockMemberService, "memberService");
        TestUtils.setPrivateAttribute(scheduleService, mockAbsenceDayService, "absenceDayService");
    }

    @Test
    public void testSendReminder_week_warning() throws Exception {
        testSendReminder_warning(CalendarUtilsTest.createDay(7, 5, 2015), "semaine", "mois");
    }

    @Test
    public void testSendReminder_month_warning() throws Exception {
        testSendReminder_warning(CalendarUtilsTest.createDay(29, 5, 2015), "mois", "semaine");
    }


    private void testSendReminder_warning(DateTime date, String expected, String notExpected) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        testSendReminder(date);

        ArgumentCaptor<MailBean> emailCaptor = ArgumentCaptor.forClass(MailBean.class);

        verify(mockAbsenceDayService).findByMemberAndMonth(eq(randomLong), eq(date.getMonthOfYear()));
        verify(mockEmailService).sendEmailCopyBlindAdministrator(emailCaptor.capture());

        MailBean mailBean = emailCaptor.getValue();

        assertThat(mailBean.getContent()).contains(expected).doesNotContain(notExpected);
    }

    @Test
    public void testSendReminder_nowarning() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        DateTime date = DateTime.now().withTimeAtStartOfDay().withYear(2015).withMonthOfYear(5).withDayOfMonth(6);

        testSendReminder(date);
        verify(mockAbsenceDayService).findByMemberAndMonth(eq(randomLong), eq(date.getMonthOfYear()));
        verify(mockEmailService, never()).sendEmailCopyBlindAdministrator(any());
    }

    @Test
    public void testSendReminder_nowarningDayOff() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        DateTime date = DateTime.now().withTimeAtStartOfDay().withYear(2015).withMonthOfYear(5).withDayOfMonth(8);

        testSendReminder(date);
        verify(mockAbsenceDayService, never()).findByMemberAndMonth(eq(randomLong), eq(date.getMonthOfYear()));
        verify(mockEmailService, never()).sendEmailCopyBlindAdministrator(any());
    }


    private void testSendReminder(DateTime date) {
        mockStatic(DateTime.class);
        when(DateTime.now()).thenReturn(date);

        Member member = EntityFactory.createMember();
        member.setId(randomLong);
        when(mockMemberService.findActiveMembers()).thenReturn(Lists.newArrayList(member));
        when(mockAbsenceDayService.findByMemberAndMonth(anyLong(), anyInt())).thenReturn(Lists.newArrayListWithExpectedSize(0));


        scheduleService.sendReminderAfternoon();

    }

}