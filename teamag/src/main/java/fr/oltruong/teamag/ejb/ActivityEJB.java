package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;

import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.exception.ExistingDataException;

@Stateless
public class ActivityEJB
    extends AbstractEJB
{

    @SuppressWarnings( "unchecked" )
    public List<BusinessCase> findBC()
    {
        Query query = entityManager.createNamedQuery( "findAllBC" );
        return query.getResultList();
    }

    public BusinessCase createBC( BusinessCase bc )
        throws ExistingDataException
    {
        BusinessCase existingBC = entityManager.find( BusinessCase.class, bc.getNumber() );

        if ( existingBC != null )
        {
            throw new ExistingDataException();
        }
        else
        {
            entityManager.persist( bc );
        }
        return bc;
    }

    @SuppressWarnings( "unchecked" )
    public List<Activity> findActivities()
    {
        Query query = entityManager.createNamedQuery( "findAllActivities" );
        return query.getResultList();
    }

    public Activity createActivity( Activity activity )
        throws ExistingDataException
    {
        Query query = entityManager.createNamedQuery( "findActivity" );
        query.setParameter( "fname", activity.getName() );
        query.setParameter( "fbc", activity.getBc() );
        @SuppressWarnings( "unchecked" )
        List<Activity> activityList = query.getResultList();

        if ( CollectionUtils.isNotEmpty( activityList ) )
        {
            throw new ExistingDataException();
        }
        else
        {
            entityManager.persist( activity );
        }
        return activity;
    }

}
