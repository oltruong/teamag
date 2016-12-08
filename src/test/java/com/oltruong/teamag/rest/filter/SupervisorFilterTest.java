package com.oltruong.teamag.rest.filter;

import com.oltruong.teamag.model.enumeration.MemberType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class SupervisorFilterTest extends SecurityFilterTest {


    @Before
    public void prepare() {
        super.setup();
        member.setMemberType(MemberType.SUPERVISOR);
    }


    @Test
    public void filterNotSupervisor() throws IOException {
        member.setMemberType(MemberType.BASIC);
        filter_notAllowed();
    }

    @Override
    protected SecurityFilter getSecurityFilter() {
        return new SupervisorFilter();
    }

}