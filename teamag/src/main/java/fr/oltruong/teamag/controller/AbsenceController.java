package fr.oltruong.teamag.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.ejb.MailEJB;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;

@SessionScoped
@ManagedBean
public class AbsenceController
{

    @ManagedProperty( value = "#{loginBean.member}" )
    private Member member;

    private Absence absence = new Absence();

    private List<Absence> absencesList;

    @EJB
    private AbsenceEJB absenceEJB;

    @EJB
    private MailEJB mailEJB;

    public String init()
    {
        absence.setMember( member );
        absencesList = absenceEJB.findAbsences( member );

        return "absence.xhtml";

    }

    public void addAbsence()
    {
        absenceEJB.addAbsence( absence );
        absencesList = absenceEJB.findAbsences( member );
        absence.setId( null );

    }

    public Absence getAbsence()
    {
        return absence;
    }

    public void setAbsence( Absence absence )
    {
        this.absence = absence;
    }

    public List<Absence> getAbsencesList()
    {
        return absencesList;
    }

    public void setAbsencesList( List<Absence> absencesList )
    {
        this.absencesList = absencesList;
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember( Member member )
    {
        this.member = member;
    }

}
