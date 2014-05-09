package fr.oltruong.teamag.rest.filter;

import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.interfaces.SupervisorChecked;

import javax.ws.rs.ext.Provider;

/**
 * @author Olivier Truong
 */
@Provider
@SupervisorChecked
public class SupervisorFilter extends SecurityFilter {

    protected boolean isAllowed(String authorization, String idMember){
        return super.isAllowed(authorization,idMember) && isSupervisor(idMember);
    }

    private boolean isSupervisor(String idMember) {
        return MemberService.getMemberMap().get(Long.valueOf(idMember)).isSupervisor();
    }
}
