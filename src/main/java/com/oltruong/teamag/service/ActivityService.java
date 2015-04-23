package com.oltruong.teamag.service;

import com.oltruong.teamag.model.Activity;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ActivityService extends AbstractService<Activity> {


    public List<Activity> findActivities() {
        return getTypedQueryList("findAllActivities");
    }

    @Override
    Class<Activity> entityProvider() {
        return Activity.class;
    }

    @Override
    public Activity persist(Activity activity) {
        TypedQuery<Activity> query = createTypedQuery("findActivity");
        query.setParameter("fname", activity.getName());
        query.setParameter("fbc", activity.getBc());
        List<Activity> activityList = query.getResultList();

        if (CollectionUtils.isNotEmpty(activityList)) {
            throw new EntityExistsException();
        } else {
            super.persist(activity);
        }
        return activity;
    }


}
