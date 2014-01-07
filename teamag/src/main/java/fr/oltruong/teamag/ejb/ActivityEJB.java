package fr.oltruong.teamag.ejb;

import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.BusinessCase;
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
        BusinessCase existingBC = getEntityManager().find(BusinessCase.class, bc.getNumber());

        if (existingBC != null) {
            throw new ExistingDataException();
        } else {
            getEntityManager().persist(bc);
        }
        return bc;
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
}
