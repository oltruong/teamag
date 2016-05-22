package com.oltruong.teamag.service;

import com.oltruong.teamag.model.Activity;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ActivityService extends AbstractService<Activity> {


    @Override
    Class<Activity> entityProvider() {
        return Activity.class;
    }

    @Override
    public List<Activity> findAll() {
        return getTypedQueryList("Activity.FIND_ALL");
    }

    @Override
    public Activity persist(Activity activity) {
        TypedQuery<Activity> query = createTypedQuery("Activity.FIND_BY_NAME_BC");
        query.setParameter("fname", activity.getName());
        query.setParameter("fbc", activity.getBusinessCase());
        List<Activity> activityList = query.getResultList();

        if (CollectionUtils.isNotEmpty(activityList)) {
            throw new EntityExistsException();
        } else {
            super.persist(activity);
        }
        return activity;
    }


    public void removeBusinessCase(Long businessCaseId) {
        Query removeQuery = createNamedQuery("Activity.REMOVE_BC");
        removeQuery.setParameter("fBusinessCaseId", businessCaseId);
        removeQuery.executeUpdate();
    }
}
