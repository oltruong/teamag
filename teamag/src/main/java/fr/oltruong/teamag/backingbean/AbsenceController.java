package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.qualifier.UserLogin;
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
    private AbsenceEJB absenceEJB;

    private static final String VIEWNAME = "absence";

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
            absenceEJB.addAbsence(newAbsence);
            refreshList();

            absence = new AbsenceWebBean();
            getMessageManager().displayMessage(MessageManager.INFORMATION, "absenceAdded");

        } catch (InconsistentDateException e) {
            getMessageManager().displayMessage(MessageManager.ERROR, "impossibleAdd", "inconsistentDates");
        } catch (DateOverlapException e) {
            getMessageManager().displayMessage(MessageManager.ERROR, "impossibleAdd", "overlappingDates");
        }
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
        absenceEJB.deleteAbsence(getSelectedAbsence().getId());
        refreshList();

    }

    private void refreshList() {
        absencesList = AbsenceWebBeanTransformer.transformList(absenceEJB.findAbsencesByMember(getMember()));
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
