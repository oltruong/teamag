package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.interfaces.SecurityChecked;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.service.AbsenceService;
import fr.oltruong.teamag.transformer.AbsenceWebBeanTransformer;
import fr.oltruong.teamag.utils.CalendarUtils;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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


    @Inject
    private Logger logger;

    @Inject
    private AbsenceService absenceService;


    @GET
    @Path("/all")
    public Response getAllAbsences() {
        return buildResponseOK(AbsenceWebBeanTransformer.transformList(absenceService.findAllAbsences()));
    }

    @GET
    @Path("/daysoff")
    public Response getDaysOff() {
        return buildResponseOK(AbsenceWebBeanTransformer.transformListfromDays(CalendarUtils.getListDaysOff()));
    }

    @GET
    public Response getAbsences(@HeaderParam("userid") Long memberId) {
        return buildResponseOK(AbsenceWebBeanTransformer.transformList(absenceService.findAbsencesByMemberId(memberId)));
    }

    @POST
    public Response createAbsence(@HeaderParam("userid") Long memberId, AbsenceWebBean absenceWebBean) {
        Response response = null;
        try {
            absenceService.addAbsence(AbsenceWebBeanTransformer.transformWebBean(absenceWebBean), memberId);
            response = buildResponseCreated();
        } catch (DateOverlapException e) {
            logger.warn("Creating absence with DateOverLap", e);
            response = buildResponseForbidden();
        } catch (InconsistentDateException e) {
            logger.warn("Creating absence with InconsistentDate", e);
            response = buildResponseBadRequest();
        }
        return response;
    }

    @DELETE
    @Path("/{absenceId}")
    public Response deleteAbsence(@HeaderParam("userid") Long memberId, @PathParam("absenceId") Long absenceId) {
        Absence absence = absenceService.find(absenceId);
        Response response = null;
        if (absence == null) {
            response = buildResponseNotFound();
        } else if (!absence.getMember().getId().equals(memberId)) {
            response = buildResponseForbidden();
        } else {
            absenceService.deleteAbsence(absence);
            response = buildResponseNoContent();
        }
        return response;
    }


}
