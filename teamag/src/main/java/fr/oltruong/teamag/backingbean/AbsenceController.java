package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.fr.oltruong.teamag.transformer.AbsenceTransformer;
import fr.oltruong.teamag.qualifier.UserLogin;
import fr.oltruong.teamag.webbean.AbsenceWebBean;

import javax.enterprise.inject.Instance;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.List;

@SessionScoped
@ManagedBean
public class AbsenceController {

    @Inject
    @UserLogin
    private Instance<Member> member;

    @Inject
    private AbsenceWebBean absence;

    private List<AbsenceWebBean> absencesList;

    @Inject
    private AbsenceEJB absenceEJB;

    // @Inject
    // private EmailEJB mailEJB;

    public String init() {

        absencesList = AbsenceTransformer.transformList(absenceEJB.findAbsencesByMember(getMember()));

        return "absence";

    }

    public void addAbsence() {
        Absence newAbsence = AbsenceTransformer.transformWebBean(absence);
        newAbsence.setMember(getMember());
        absenceEJB.addAbsence(newAbsence);
        absencesList = AbsenceTransformer.transformList(absenceEJB.findAbsencesByMember(getMember()));

        absence = new AbsenceWebBean();

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

    private Member getMember() {
        return member.get();
    }


}
