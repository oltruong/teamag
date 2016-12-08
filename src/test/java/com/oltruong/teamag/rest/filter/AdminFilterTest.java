package com.oltruong.teamag.rest.filter;

import com.oltruong.teamag.model.enumeration.MemberType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class AdminFilterTest extends SecurityFilterTest {


    @Before
    public void prepare() {
        super.setup();
        member.setMemberType(MemberType.ADMINISTRATOR);
    }


    @Test
    public void filterNotAdmin() throws IOException {
        member.setMemberType(MemberType.BASIC);
        filter_notAllowed();
    }

    @Override
    protected SecurityFilter getSecurityFilter() {
        return new AdminFilter();
    }

}
