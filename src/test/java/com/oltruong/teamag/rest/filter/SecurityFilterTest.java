package com.oltruong.teamag.rest.filter;

import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class SecurityFilterTest extends AbstractFilterTest {


    @Test
    public void testFilter_allowed() throws IOException {
        securityFilter.filter(mockContainerRequestContext);
    }

    @Test
    public void testFilter_notAllowed() throws IOException {
        member.setPassword("NOK");
        filter_notAllowed();
    }

    protected void filter_notAllowed() throws IOException {
        try {
            securityFilter.filter(mockContainerRequestContext);
            fail("an exception should have been raised");
        } catch (WebApplicationException exception) {
            assertThat(exception.getResponse().getStatusInfo()).isEqualTo(Response.Status.UNAUTHORIZED);
        }
    }

    @Override
    protected SecurityFilter getSecurityFilter() {
        return new SecurityFilter();
    }
}