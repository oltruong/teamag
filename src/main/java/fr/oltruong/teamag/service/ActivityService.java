package fr.oltruong.teamag.service;

import com.google.common.base.Strings;
import fr.oltruong.teamag.model.Activity;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.WorkLoad;
import fr.oltruong.teamag.exception.ExistingDataException;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ActivityService extends AbstractService {

    @SuppressWarnings("unchecked")
    public List<BusinessCase> findBC() {
        Query query = createNamedQuery("findAllBC");
        return query.getResultList();
    }

    public BusinessCase createBC(BusinessCase bc) throws ExistingDataException {

        if (!Strings.isNullOrEmpty(bc.getIdentifier())) {
            Query query = createNamedQuery("findBCByNumber");
            query.setParameter("fidentifier", bc.getIdentifier());
            if (!query.getResultList().isEmpty()) {
                throw new ExistingDataException();
            }
        }
        persist(bc);


        //Create WorkLoad
        List<Member> memberList = MemberService.getMemberList();
        if (memberList != null) {
            for (Member member : memberList) {
                WorkLoad workLoad = new WorkLoad(bc, member);
                persist(workLoad);
            }
        }


        return bc;
    }


    public void deleteBC(Long businessCaseId) {
        BusinessCase businessCase = find(BusinessCase.class, businessCaseId);
        remove(businessCase);
    }


    public void deleteActivity(Long activityId) {
        Activity activity = find(Activity.class, activityId);
        remove(activity);
    }

    @SuppressWarnings("unchecked")
    public List<Activity> findActivities() {
        Query query = createNamedQuery("findAllActivities");
        return query.getResultList();
    }

    public Activity createActivity(Activity activity) throws ExistingDataException {
        Query query = createNamedQuery("findActivity");
        query.setParameter("fname", activity.getName());
        query.setParameter("fbc", activity.getBc());
        @SuppressWarnings("unchecked")
        List<Activity> activityList = query.getResultList();

        if (CollectionUtils.isNotEmpty(activityList)) {
            throw new ExistingDataException();
        } else {
            persist(activity);
        }
        return activity;
    }

    public void updateBC(BusinessCase bcUpdated) {
        merge(bcUpdated);
    }


    public void updateActivity(Activity activityToUpdate) {
        merge(activityToUpdate);
    }

    public BusinessCase findBC(Long businessCaseId) {
        return find(BusinessCase.class, businessCaseId);
    }

    public Activity findActivity(Long activityId) {
        return find(Activity.class, activityId);
    }
}
