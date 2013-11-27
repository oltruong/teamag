package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.qualifier.UserLogin;
import fr.oltruong.teamag.transformer.AbsenceTransformer;
import fr.oltruong.teamag.validation.AbsenceWebBeanValidator;
import fr.oltruong.teamag.webbean.AbsenceWebBean;

import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
    private AbsenceEJB absenceEJB;

    // @Inject
    // private EmailEJB mailEJB;

    public String init() {

        refreshList();
        return "absence";

    }

    public void addAbsence() {
        FacesMessage msg = null;

        try {
            AbsenceWebBeanValidator.validate(absence, absencesList);
            Absence newAbsence = AbsenceTransformer.transformWebBean(absence);
            newAbsence.setMember(getMember());
            absenceEJB.addAbsence(newAbsence);
            refreshList();

            absence = new AbsenceWebBean();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("absenceAdded"), "");
        } catch (InconsistentDateException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("errorAddingAbsence"), getMessage("inconsistentDates"));
        } catch (DateOverlapException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("errorAddingAbsence"), getMessage("overlappingDates"));
        }


        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void deleteAbsence() {
        absenceEJB.deleteAbsence(getSelectedAbsence().getId());
        refreshList();

    }

    private void refreshList() {
        absencesList = AbsenceTransformer.transformList(absenceEJB.findAbsencesByMember(getMember()));
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
