package fr.oltruong.teamag.service;

import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.Activity;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ActivityService extends AbstractService {

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


    public void updateActivity(Activity activityToUpdate) {
        merge(activityToUpdate);
    }


    public Activity findActivity(Long activityId) {
        return find(Activity.class, activityId);
    }
}
