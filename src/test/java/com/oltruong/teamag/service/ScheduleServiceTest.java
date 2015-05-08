package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.TestUtils;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
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
    public void testSendReminder() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Member member = EntityFactory.createMember();
        member.setId(randomLong);
        when(mockMemberService.findActiveMembers()).thenReturn(Lists.newArrayList(member));
        when(mockAbsenceDayService.findByMemberAndMonth(anyLong(), anyInt())).thenReturn(Lists.newArrayListWithExpectedSize(0));


        scheduleService.sendReminder();

        verify(mockAbsenceDayService).findByMemberAndMonth(eq(randomLong), eq(LocalDate.now().getMonthOfYear()));
        verify(mockEmailService).sendEmailCopyBlindAdministrator(any());
    }

}