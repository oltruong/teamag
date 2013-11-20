package fr.oltruong.teamag.ejb;

import com.google.common.base.Preconditions;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AbsenceEJB extends AbstractEJB {

    @SuppressWarnings("unchecked")
    public List<Absence> findAbsencesByMember(Member member) {

        Preconditions.checkArgument(member != null);
        Query query = getEntityManager().createNamedQuery("findAbsencesByMember");
        query.setParameter("fmemberId", member.getId());

        return (List<Absence>) query.getResultList();
    }

    public void addAbsence(Absence absence) {
        getEntityManager().persist(absence);
    }

}
