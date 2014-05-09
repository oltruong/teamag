package fr.oltruong.teamag.rest.filter;

import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.interfaces.AdminChecked;

import javax.ws.rs.ext.Provider;

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
        return MemberService.getMemberMap().get(Long.valueOf(idMember)).isAdministrator();
    }
}
