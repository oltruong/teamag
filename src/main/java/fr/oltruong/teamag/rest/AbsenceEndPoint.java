package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.interfaces.SecurityChecked;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.service.AbsenceService;
import fr.oltruong.teamag.service.EmailService;
import fr.oltruong.teamag.service.MailBean;
import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.service.WorkLoadService;
import fr.oltruong.teamag.transformer.AbsenceWebBeanTransformer;
import fr.oltruong.teamag.validation.AbsenceWebBeanValidator;
import fr.oltruong.teamag.webbean.AbsenceWebBean;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("absences")
@Stateless
@SecurityChecked
public class AbsenceEndPoint extends AbstractEndPoint {

    private final String DATE_FORMAT = "EEEE dd MMMM";

    @Inject
    private AbsenceService absenceService;

    @Inject
    private WorkLoadService workLoadService;

    @Inject
    private EmailService emailService;


    @GET
    @Path("/{memberId}")
    public Response getAbsences(@PathParam("memberId") Long memberId) {
        return buildResponseOK(AbsenceWebBeanTransformer.transformList(absenceService.findAbsencesByMemberId(memberId)));
    }

    @POST
    @Path("/{memberId}")
    public Response createAbsence(@PathParam("memberId") Long memberId, AbsenceWebBean absenceWebBean) {
        Response response = null;
        try {
            addAbsence(absenceWebBean, memberId);
            response = buildResponseCreated();
        } catch (DateOverlapException e) {
            response = buildResponseNotAcceptable();
        } catch (InconsistentDateException e) {
            response = buildResponseNotAcceptable();
        }
        return response;
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

    public void addAbsence(AbsenceWebBean absence, Long memberId) throws DateOverlapException, InconsistentDateException {


        List<AbsenceWebBean> absencesList = AbsenceWebBeanTransformer.transformList(absenceService.findAbsencesByMemberId(memberId));
        format(absence);
        AbsenceWebBeanValidator.validate(absence, absencesList);
        Absence newAbsence = AbsenceWebBeanTransformer.transformWebBean(absence);
        newAbsence.setMember(MemberService.getMemberMap().get(memberId));
        absenceService.addAbsence(newAbsence);
        workLoadService.registerAbsence(newAbsence);


        emailService.sendEmailAdministrator(buildEmailAdd(newAbsence, memberId));

    }

    private void format(AbsenceWebBean absence) {
        if (absence.getEndDateTime() == null) {
            absence.setEndDateTime(absence.getBeginDateTime());
            absence.setEndType(absence.getBeginType());
        }

        if (absence.getBeginDateTime() == null) {
            absence.setBeginDateTime(absence.getEndDateTime());
            absence.setBeginType(absence.getEndType());
        }
    }

    private MailBean buildEmailAdd(Absence absence, Long memberId) {

        MailBean mailBean = new MailBean();
        mailBean.setSubject(MemberService.getMemberMap().get(memberId).getName() + ": ajout d'absence " + buildEmailContent(absence));

        mailBean.setContent(buildEmailContent(absence));
        return mailBean;
    }

    private String buildEmailContent(Absence absence) {
        return "du " + absence.getBeginDate().toString(DATE_FORMAT) + decrypt(absence.getBeginType()) + " au " + absence.getEndDate().toString(DATE_FORMAT) + decrypt(absence.getBeginType());
    }

    private String decrypt(Integer type) {
        String result = null;
        if (Absence.AFTERNOON_ONLY.equals(type)) {
            result = " apres-midi";
        } else if (Absence.MORNING_ONLY.equals(type)) {
            result = " matin";
        } else {
            result = "";
        }


        return result;
    }


}
