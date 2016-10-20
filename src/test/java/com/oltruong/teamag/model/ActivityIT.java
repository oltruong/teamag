package com.oltruong.teamag.model;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityIT extends AbstractEntityIT {


    @Test
    public void createAndFind() {
        Activity activity = createActivity();

        assertThat(activity.getId()).isNotNull();

        Activity activityDB = entityManager.find(Activity.class, activity.getId());
        assertThat(activity).isEqualToComparingFieldByField(activityDB).isEqualTo(activityDB);
    }

    @Test
    public void namedQueryFindAllActivities() {

        List<Activity> activityList = createActivities();


        Query query = entityManager.createNamedQuery("Activity.FIND_ALL");
        List<Activity> activityListResult = query.getResultList();

        assertThat(activityListResult).containsExactlyElementsOf(activityList);
    }


    private Activity generateAndPersistActivity() {
        Activity activity = EntityFactory.createActivity();
        assertThat(activity.getId()).isNull();
        createWithoutCommit(activity.getBusinessCase());
        createWithoutCommit(activity);
        return activity;
    }

    private Activity createActivity() {
        Activity activity = generateAndPersistActivity();
        transaction.commit();
        return activity;
    }

    private List<Activity> createActivities() {
        List<Activity> activityList = Lists.newArrayList();
        IntStream.range(1, 3).forEach((i) -> activityList.add(generateAndPersistActivity()));
        transaction.commit();
        return activityList;
    }


    @Test
    public void namedQueryFindActivity() {

        List<Activity> activityList = createActivities();
        Activity firstActivity = activityList.get(0);

        Query query = entityManager.createNamedQuery("Activity.FIND_BY_NAME_BC");

        query.setParameter("fname", firstActivity.getName());
        query.setParameter("fbc", firstActivity.getBusinessCase());


        List<Activity> activityListResult = query.getResultList();

        assertThat(activityListResult).containsExactly(firstActivity);
    }



}