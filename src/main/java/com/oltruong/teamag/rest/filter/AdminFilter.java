package com.oltruong.teamag.rest.filter;

import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.service.MemberService;

import javax.ws.rs.ext.Provider;


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
