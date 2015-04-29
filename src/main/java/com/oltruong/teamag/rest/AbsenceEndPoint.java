package com.oltruong.teamag.rest;

import com.oltruong.teamag.exception.DateOverlapException;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.service.AbsenceService;
import com.oltruong.teamag.transformer.AbsenceWebBeanTransformer;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.webbean.AbsenceWebBean;

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
    private AbsenceService absenceService;


    @GET
    @Path("/all")
    public Response getAllAbsences() {
        return ok(AbsenceWebBeanTransformer.transformList(absenceService.findAllAbsences()));
    }

    @GET
    @Path("/daysoff")
    public Response getDaysOff() {
        return ok(AbsenceWebBeanTransformer.transformListfromDays(CalendarUtils.getListDaysOff()));
    }

    @GET
    public Response getAbsences(@HeaderParam("userid") Long memberId) {
        return ok(AbsenceWebBeanTransformer.transformList(absenceService.findAbsencesByMember(memberId)));
    }

    @POST
    public Response createAbsence(@HeaderParam("userid") Long memberId, AbsenceWebBean absenceWebBean) {
        Response response;
        try {
            absenceService.addAbsence(AbsenceWebBeanTransformer.transformWebBean(absenceWebBean), memberId);
            response = created();
        } catch (DateOverlapException e) {
            LOGGER.warn("Creating absence with DateOverLap", e);
            response = forbidden();
        } catch (InconsistentDateException e) {
            LOGGER.warn("Creating absence with InconsistentDate", e);
            response = badRequest();
        }
        return response;
    }

    @DELETE
    @Path("/{absenceId}")
    public Response deleteAbsence(@HeaderParam("userid") Long memberId, @PathParam("absenceId") Long absenceId) {
        return delete(() -> absenceService.find(absenceId), (absence) -> ((Absence) absence).getMember().getId().equals(memberId), (absence) -> absenceService.remove((Absence) absence));
    }


}
