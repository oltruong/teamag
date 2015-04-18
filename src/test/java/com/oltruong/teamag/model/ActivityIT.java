package com.oltruong.teamag.model;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityIT extends AbstractEntityIT {


    @Test
    public void testCreateAndFind() {
        Activity activity = createActivity();

        assertThat(activity.getId()).isNotNull();


        Activity activityDB = entityManager.find(Activity.class, activity.getId());

        assertThat(activity).isEqualToComparingFieldByField(activityDB).isEqualTo(activityDB);
        assertThat(activity).isEqualToComparingFieldByField(activityDB).isEqualTo(activityDB);



    }

    private Activity createActivity() {
        Activity activity = EntityFactory.createActivity();

        assertThat(activity.getId()).isNull();

        createWithoutCommit(activity.getBc());
        createWithCommit(activity);
        return activity;
    }


    @Test
    public void test_NamedQuery_FindAllActivities() {

        List<Activity> activityList = createActivities();

        Query query = entityManager.createNamedQuery("findAllActivities");

        List<Activity> activityListResult = query.getResultList();

        assertThat(activityListResult).containsExactlyElementsOf(activityList);
    }

    private List<Activity> createActivities() {
        Activity activity = createActivity();
        transaction.begin();
        Activity secondActivity = createActivity();

        List<Activity> activityList = Lists.newArrayList();
        activityList.add(activity);
        activityList.add(secondActivity);
        return activityList;
    }


    @Test
    public void test_NamedQuery_FindActivity() {

        List<Activity> activityList = createActivities();
        Activity firstActivity = activityList.get(0);

        Query query = entityManager.createNamedQuery("findActivity");

        query.setParameter("fname", firstActivity.getName());
        query.setParameter("fbc", firstActivity.getBc());


        List<Activity> activityListResult = query.getResultList();

        assertThat(activityListResult).containsExactly(firstActivity);
    }

}