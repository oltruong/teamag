package com.oltruong.teamag.rest;

import com.oltruong.teamag.exception.DateOverlapException;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.service.AbsenceService;
import com.oltruong.teamag.service.AbstractService;
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
public class AbsenceEndPoint extends AbstractEndPoint<Absence> {


    @Inject
    private AbsenceService absenceService;


    @GET
    @Path("/all")
    public Response getAllAbsences() {
        return ok(AbsenceWebBeanTransformer.transformList(absenceService.findAll()));
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
            Absence absence = AbsenceWebBeanTransformer.transformWebBean(absenceWebBean);
            absence = absenceService.addAbsence(absence, memberId);
            response = created(absence.getId());
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
        Absence absence = absenceService.find(absenceId);
        Response response;
        if (absence == null) {
            response = notFound();
        } else if (absence.getMember().getId().equals(memberId)) {
            response = delete(absenceService::remove, absenceId);
        } else {
            response = forbidden();
        }
        return response;
    }


    @Override
    AbstractService getService() {
        return absenceService;
    }
}
