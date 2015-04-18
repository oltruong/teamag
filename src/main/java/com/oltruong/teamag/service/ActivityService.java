package com.oltruong.teamag.service;

import com.oltruong.teamag.model.Activity;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
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

    public Activity createActivity(Activity activity) {
        Query query = createNamedQuery("findActivity");
        query.setParameter("fname", activity.getName());
        query.setParameter("fbc", activity.getBc());
        @SuppressWarnings("unchecked")
        List<Activity> activityList = query.getResultList();

        if (CollectionUtils.isNotEmpty(activityList)) {
            throw new EntityExistsException();
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
