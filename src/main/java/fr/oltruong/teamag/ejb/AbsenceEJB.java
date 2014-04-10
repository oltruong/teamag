package fr.oltruong.teamag.ejb;

import com.google.common.base.Preconditions;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AbsenceEJB extends AbstractEJB {


    public List<Absence> findAllAbsences() {
        return createNamedQuery("findAllAbsences").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Absence> findAbsencesByMember(Member member) {

        Preconditions.checkArgument(member != null);
        Query query = createNamedQuery("findAbsencesByMember");
        query.setParameter("fmemberId", member.getId());

        return (List<Absence>) query.getResultList();
    }


    @SuppressWarnings("unchecked")
    public List<Absence> findAbsencesByMemberId(Long memberId) {

        Preconditions.checkArgument(memberId != null);
        Query query = createNamedQuery("findAbsencesByMember");
        query.setParameter("fmemberId", memberId);

        return (List<Absence>) query.getResultList();
    }


    public void addAbsence(Absence absence) {
        persist(absence);
    }

    public void deleteAbsence(Long absenceId) {
        Absence absenceDB = find(Absence.class, absenceId);
        remove(absenceDB);
    }

}
