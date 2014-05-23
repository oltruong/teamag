package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.WorkRealized;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class WorkRealizedService extends AbstractService {

    public List<WorkRealized> getAllWorkRealized() {
        return getNamedQueryList("findAllWorkRealized");
    }

    public List<WorkRealized> getWorkRealizedbyMember(Long memberId) {
        Query query = createNamedQuery("findAllWorkRealizedByMember");
        query.setParameter("fMemberId", memberId);
        return query.getResultList();
    }

    public void createOrUpdate(List<WorkRealized> workRealizedList) {
        if (workRealizedList != null) {
            workRealizedList.forEach(workRealized -> {

                if (workRealized.getRealized() == 0) {
                    if (workRealized.getId() != null) {
                        remove(WorkRealized.class, workRealized.getId());
                    }
                } else if (workRealized.getId() != null) {
                    merge(workRealized);
                } else {
                    persist(workRealized);
                }
            });
        }

    }
}
