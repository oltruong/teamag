package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.interfaces.SecurityChecked;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.service.AbsenceService;
import fr.oltruong.teamag.service.WorkLoadService;
import fr.oltruong.teamag.transformer.AbsenceWebBeanTransformer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("absences")
@Stateless
@SecurityChecked
public class AbsenceEndPoint extends AbstractEndPoint {

    @EJB
    private AbsenceService absenceService;

    @EJB
    private WorkLoadService workLoadService;


    @GET
    @Path("/{memberId}")
    public Response getAbsences(@PathParam("memberId") Long memberId) {
        return buildResponseOK(AbsenceWebBeanTransformer.transformList(absenceService.findAbsencesByMemberId(memberId)));
    }

    @DELETE
    @Path("/{memberId}/{absenceId}")
    public Response deleteAbsence(@PathParam("memberId") Long memberId, @PathParam("absenceId") Long absenceId) {
        Absence absence = absenceService.find(absenceId);

        if (absence == null) {
            return buildResponseNotFound();
        }
        if (!absence.getMember().getId().equals(memberId)) {
            return buildResponseForbidden();
        }
        workLoadService.removeAbsence(absenceId);
        absenceService.deleteAbsence(absenceId);

        return buildResponseNoContent();
    }


}
