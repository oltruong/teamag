package fr.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.Member;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AbsenceService extends AbstractService {


    public List<Absence> findAllAbsences() {
        return getNamedQueryList("findAllAbsences");
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
        remove(Absence.class, absenceId);

    }

    public Absence find(Long absenceId) {
        return find(Absence.class, absenceId);
    }

}
