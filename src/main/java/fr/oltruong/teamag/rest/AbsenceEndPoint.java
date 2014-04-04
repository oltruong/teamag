package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.ejb.ActivityEJB;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.interfaces.SecurityChecked;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("absence")
@Stateless
//@SecurityChecked
public class AbsenceEndPoint extends AbstractEndPoint {

    @EJB
    AbsenceEJB absenceEJB;


    @GET
    @Path("/{id}")
    public Response getAbsences(@PathParam("id") Long memberId) {
        return buildResponseOK(absenceEJB.findAbsencesByMemberId(memberId));
    }


}
