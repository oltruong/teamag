package fr.oltruong.teamag.ejb;

import com.google.common.base.Strings;
import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.WorkLoad;
import fr.oltruong.teamag.exception.ExistingDataException;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ActivityEJB extends AbstractEJB {

    @SuppressWarnings("unchecked")
    public List<BusinessCase> findBC() {
        Query query = getEntityManager().createNamedQuery("findAllBC");
        return query.getResultList();
    }

    public BusinessCase createBC(BusinessCase bc) throws ExistingDataException {

        if (!Strings.isNullOrEmpty(bc.getIdentifier())) {
            Query query = getEntityManager().createNamedQuery("findBCByNumber");
            query.setParameter("fidentifier", bc.getIdentifier());
            if (!query.getResultList().isEmpty()) {
                throw new ExistingDataException();
            }
        }
        getEntityManager().persist(bc);


        //Create WorkLoad
        List<Member> memberList = MemberEJB.getMemberList();
        if (memberList != null) {
            for (Member member : memberList) {
                WorkLoad workLoad = new WorkLoad(bc, member);
                persist(workLoad);
            }
        }


        return bc;
    }


    public void deleteBC(Long businessCaseId) {
        BusinessCase businessCase = getEntityManager().find(BusinessCase.class, businessCaseId);
        getEntityManager().remove(businessCase);
    }


    public void deleteActivity(Long activityId) {
        Activity activity = getEntityManager().find(Activity.class, activityId);
        getEntityManager().remove(activity);
    }

    @SuppressWarnings("unchecked")
    public List<Activity> findActivities() {
        Query query = getEntityManager().createNamedQuery("findAllActivities");
        return query.getResultList();
    }

    public Activity createActivity(Activity activity) throws ExistingDataException {
        Query query = getEntityManager().createNamedQuery("findActivity");
        query.setParameter("fname", activity.getName());
        query.setParameter("fbc", activity.getBc());
        @SuppressWarnings("unchecked")
        List<Activity> activityList = query.getResultList();

        if (CollectionUtils.isNotEmpty(activityList)) {
            throw new ExistingDataException();
        } else {
            getEntityManager().persist(activity);
        }
        return activity;
    }

    public void updateBC(BusinessCase bcUpdated) {
        getEntityManager().merge(bcUpdated);
    }


    public void updateActivity(Activity activityToUpdate) {
        getEntityManager().merge(activityToUpdate);
    }

    public BusinessCase findBC(Long businessCaseId) {
        return getEntityManager().find(BusinessCase.class, businessCaseId);
    }

    public Activity findActivity(Long activityId) {
        return getEntityManager().find(Activity.class, activityId);
    }
}
