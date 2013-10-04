package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;

@Stateless
public class AbsenceEJB extends AbstractEJB {

    @SuppressWarnings("unchecked")
    public List<Absence> findAbsences(Member member) {
        if (member != null) {

            Query query = getEntityManager().createNamedQuery("findAbsencesByMember");
            query.setParameter("fmemberName", member.getName());

            return (List<Absence>) query.getResultList();
        } else {
            getLogger().warn("Member is null");
            return null;
        }
    }

    public void addAbsence(Absence absence) {

        getEntityManager().persist(absence);

    }

}
