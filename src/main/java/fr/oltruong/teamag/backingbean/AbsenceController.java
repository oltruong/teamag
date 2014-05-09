package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.service.AbsenceService;
import fr.oltruong.teamag.service.EmailService;
import fr.oltruong.teamag.service.MailBean;
import fr.oltruong.teamag.service.WorkLoadService;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.interfaces.UserLogin;
import fr.oltruong.teamag.transformer.AbsenceWebBeanTransformer;
import fr.oltruong.teamag.utils.MessageManager;
import fr.oltruong.teamag.validation.AbsenceWebBeanValidator;
import fr.oltruong.teamag.webbean.AbsenceWebBean;

import javax.enterprise.inject.Instance;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.List;

@SessionScoped
@ManagedBean
public class AbsenceController extends Controller {

    @Inject
    @UserLogin
    private Instance<Member> member;

    @Inject
    private AbsenceWebBean absence;

    @Inject
    private AbsenceWebBean selectedAbsence;

    private List<AbsenceWebBean> absencesList;

    @Inject
    private AbsenceService absenceService;

    @Inject
    private WorkLoadService workLoadService;


    @Inject
    private EmailService emailService;


    private static final String VIEWNAME = "absence";

    private final String DATE_FORMAT = "EEEE dd MMMM";

    public String init() {

        refreshList();
        return VIEWNAME;

    }

    public void addAbsence() {
        try {
            format(absence);
            AbsenceWebBeanValidator.validate(absence, absencesList);
            Absence newAbsence = AbsenceWebBeanTransformer.transformWebBean(absence);
            newAbsence.setMember(getMember());
            absenceService.addAbsence(newAbsence);
            workLoadService.registerAbsence(newAbsence);
            refreshList();

            absence = new AbsenceWebBean();
            getMessageManager().displayMessage(MessageManager.INFORMATION, "absenceAdded");

            emailService.sendEmailAdministrator(buildEmailAdd(newAbsence));

        } catch (InconsistentDateException e) {
            getMessageManager().displayMessageWithDescription(MessageManager.ERROR, "impossibleAdd", "inconsistentDates");
        } catch (DateOverlapException e) {
            getMessageManager().displayMessageWithDescription(MessageManager.ERROR, "impossibleAdd", "overlappingDates");
        }
    }

    private MailBean buildEmailAdd(Absence absence) {

        MailBean mailBean = new MailBean();
        mailBean.setSubject(member.get().getName() + ": ajout d'absence " + buildEmailContent(absence));

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


    /**
     * Enable the possibility to fill only beginDate or endDate
     *
     * @param absence
     */
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

    public void deleteAbsence() {
        workLoadService.removeAbsence(getSelectedAbsence().getId());
        absenceService.deleteAbsence(getSelectedAbsence().getId());

        emailService.sendEmailAdministrator(buildEmailDelete(getSelectedAbsence()));

        refreshList();


    }

    private MailBean buildEmailDelete(AbsenceWebBean selectedAbsence) {
        MailBean mailBean = new MailBean();
        Absence newAbsence = AbsenceWebBeanTransformer.transformWebBean(selectedAbsence);
        mailBean.setSubject(member.get().getName() + ": suppression d'absence " + buildEmailContent(newAbsence));
        mailBean.setContent(buildEmailContent(newAbsence));
        return mailBean;
    }

    private void refreshList() {
        absencesList = AbsenceWebBeanTransformer.transformList(absenceService.findAbsencesByMember(getMember()));
    }

    public AbsenceWebBean getAbsence() {
        return absence;
    }

    public void setAbsence(AbsenceWebBean absence) {
        this.absence = absence;
    }

    public List<AbsenceWebBean> getAbsencesList() {
        return absencesList;
    }

    public void setAbsencesList(List<AbsenceWebBean> absencesList) {
        this.absencesList = absencesList;
    }

    public AbsenceWebBean getSelectedAbsence() {
        return selectedAbsence;
    }

    public void setSelectedAbsence(AbsenceWebBean selectedAbsence) {
        this.selectedAbsence = selectedAbsence;
    }

    private Member getMember() {
        return member.get();
    }


}
