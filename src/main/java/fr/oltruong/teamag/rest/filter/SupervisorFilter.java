package fr.oltruong.teamag.rest.filter;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.interfaces.AdminChecked;
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
        return MemberEJB.getMemberMap().get(Long.valueOf(idMember)).isSupervisor();
    }
}
