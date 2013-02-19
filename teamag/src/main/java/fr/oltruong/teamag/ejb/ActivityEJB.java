package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.exception.ExistingDataException;

@Stateless
public class ActivityEJB
{

    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    @SuppressWarnings( "unchecked" )
    public List<BusinessCase> findBC()
    {
        Query query = em.createNamedQuery( "findAllBC" );
        return query.getResultList();
    }

    public BusinessCase createBC( BusinessCase bc )
        throws ExistingDataException
    {
        BusinessCase existingBC = em.find( BusinessCase.class, bc.getNumber() );

        if ( existingBC != null )
        {
            throw new ExistingDataException();
        }
        else
        {
            em.persist( bc );
        }
        return bc;
    }

    @SuppressWarnings( "unchecked" )
    public List<Activity> findActivities()
    {
        Query query = em.createNamedQuery( "findAllActivities" );
        return query.getResultList();
    }

    public Activity createActivity( Activity activity )
        throws ExistingDataException
    {
        Query query = em.createNamedQuery( "findActivity" );
        query.setParameter( "fname", activity.getName() );
        query.setParameter( "fbc", activity.getBc() );
        @SuppressWarnings( "unchecked" )
        List<Activity> activities = query.getResultList();

        if ( activities != null && !activities.isEmpty() )
        {
            throw new ExistingDataException();
        }
        else
        {
            em.persist( activity );
        }
        return activity;
    }

}
