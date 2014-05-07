package fr.oltruong.teamag.rest.filter;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.interfaces.SecurityChecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Olivier Truong
 */
@SecurityChecked
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private final String AUTHORIZATION_PROPERTY = "Authorization";

    private final String USER_PROPERTY = "userid";

    private Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String authorization = containerRequestContext.getHeaders().getFirst(AUTHORIZATION_PROPERTY);
        String idMember = containerRequestContext.getHeaders().getFirst(USER_PROPERTY);


        if (!isAllowed(authorization,idMember)) {
            logger.warn("Unauthorized Access to member of id[" + idMember+"]"+ containerRequestContext.getRequest().getMethod());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }


    }

    protected boolean isAllowed(String authorization, String idMember){
        return isLoggedIn(authorization,idMember);
    }

    protected boolean isLoggedIn(String authorization, String idMember) {

        boolean result = false;

        if (idMember != null && authorization != null) {
            Member member = MemberEJB.getMemberMap().get(Long.valueOf(idMember));

            result = member != null && member.getPassword().equals(authorization);
        }
        return result;


    }
}
