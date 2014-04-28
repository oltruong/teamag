package fr.oltruong.teamag.rest.filter;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.interfaces.AdminChecked;
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
@Provider
@AdminChecked
public class AdminFilter extends SecurityFilter {

    protected boolean isAllowed(String authorization, String idMember){
        return super.isAllowed(authorization,idMember) && isAdmin(idMember);
    }

    private boolean isAdmin(String idMember) {
        return MemberEJB.getMemberMap().get(Long.valueOf(idMember)).isAdministrator();
    }
}
