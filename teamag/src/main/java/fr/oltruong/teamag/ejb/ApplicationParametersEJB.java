/**
 * 
 */
package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.ApplicationParameters;

/**
 * @author Olivier Truong
 */
@Singleton
public class ApplicationParametersEJB
{

    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    private ApplicationParameters parameters;

    @PostConstruct
    private void initValue()
    {
        loadParameters();
    }

    public void setParameters( ApplicationParameters updatedParameters )
    {
        this.parameters = updatedParameters;
    }

    public void saveParameters()
    {
        em.merge( parameters );
        loadParameters();
    }

    public ApplicationParameters getParameters()
    {
        return parameters;
    }

    private void loadParameters()
    {
        Query query = em.createNamedQuery( "findParameters" );
        @SuppressWarnings( "unchecked" )
        List<ApplicationParameters> listParam = query.getResultList();
        if ( listParam != null && !listParam.isEmpty() )
        {
            parameters = listParam.get( 0 );

        }
        else
        {
            System.out.println( "Warning creation objet parameters" );
            parameters = new ApplicationParameters();
        }
    }

}
