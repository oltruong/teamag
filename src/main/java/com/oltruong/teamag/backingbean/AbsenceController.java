package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.exception.DateOverlapException;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.interfaces.UserLogin;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.service.AbsenceService;
import com.oltruong.teamag.transformer.AbsenceWebBeanTransformer;
import com.oltruong.teamag.utils.MessageManager;
import com.oltruong.teamag.webbean.AbsenceWebBean;

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

    private static final String VIEWNAME = "absence";


    public String init() {

        refreshList();
        return VIEWNAME;

    }

    public void addAbsence() {
        try {
            Absence newAbsence = AbsenceWebBeanTransformer.transformWebBean(absence);
            absenceService.addAbsence(newAbsence, getMember().getId());
            refreshList();

            absence = new AbsenceWebBean();
            getMessageManager().displayMessage(MessageManager.INFORMATION, "absenceAdded");

        } catch (InconsistentDateException e) {
            getMessageManager().displayMessageWithDescription(MessageManager.ERROR, "impossibleAdd", "inconsistentDates");
        } catch (DateOverlapException e) {
            getMessageManager().displayMessageWithDescription(MessageManager.ERROR, "impossibleAdd", "overlappingDates");
        }
    }


    public void deleteAbsence() {

        Absence absenceToDelete = absenceService.find(getSelectedAbsence().getId());
        absenceService.deleteAbsence(absenceToDelete);
        refreshList();

    }


    private void refreshList() {
        absencesList = AbsenceWebBeanTransformer.transformList(absenceService.findAbsencesByMember(getMember().getId()));
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
