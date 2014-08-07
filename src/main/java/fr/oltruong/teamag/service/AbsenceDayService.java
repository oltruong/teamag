package fr.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import fr.oltruong.teamag.model.AbsenceDay;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class AbsenceDayService extends AbstractService {


    public List<AbsenceDay> findAbsenceDayList(Long memberId, Integer month) {

        Preconditions.checkArgument(memberId != null);
        Preconditions.checkArgument(month != null);

        Query query = createNamedQuery("findAbsenceDayByMemberAndMonth");

        query.setParameter("fMemberId", memberId);
        query.setParameter("fMonth", month);

        return query.getResultList();
    }

}
