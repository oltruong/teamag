package fr.oltruong.teamag.backingbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.qualifier.UserLogin;

@SessionScoped
@ManagedBean
public class AbsenceController {

    @Inject
    @UserLogin
    private Member member;

    @Inject
    private Absence absence;

    private List<Absence> absencesList;

    @Inject
    private AbsenceEJB absenceEJB;

    // @Inject
    // private EmailEJB mailEJB;

    public String init() {
        absence.setMember(member);
        absencesList = absenceEJB.findAbsences(member);

        return "absence.xhtml";

    }

    public void addAbsence() {
        absenceEJB.addAbsence(absence);
        absencesList = absenceEJB.findAbsences(member);
        absence.setId(null);

    }

    public Absence getAbsence() {
        return absence;
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
    }

    public List<Absence> getAbsencesList() {
        return absencesList;
    }

    public void setAbsencesList(List<Absence> absencesList) {
        this.absencesList = absencesList;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
