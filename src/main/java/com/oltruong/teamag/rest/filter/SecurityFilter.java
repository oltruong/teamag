package com.oltruong.teamag.rest.filter;

import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;


@SecurityChecked
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_PROPERTY = "Authorization";

    private static final String USER_PROPERTY = "userid";

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String authorization = containerRequestContext.getHeaders().getFirst(AUTHORIZATION_PROPERTY);
        String idMember = containerRequestContext.getHeaders().getFirst(USER_PROPERTY);


        if (!isAllowed(authorization, idMember)) {
            LOGGER.warn("Unauthorized Access to member of id[" + idMember + "]" + containerRequestContext.getRequest().getMethod());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }


    }

    protected boolean isAllowed(String authorization, String idMember) {
        return isLoggedIn(authorization, idMember);
    }

    protected boolean isLoggedIn(String authorization, String idMember) {

        boolean result = false;

        if (idMember != null && authorization != null) {
            Member member = MemberService.getMemberMap().get(Long.valueOf(idMember));

            result = member != null && member.getPassword().equals(authorization);
        }
        return result;


    }
}
