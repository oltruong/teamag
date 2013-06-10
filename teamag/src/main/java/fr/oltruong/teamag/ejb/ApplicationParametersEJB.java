/**
 * 
 */
package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;

import fr.oltruong.teamag.entity.ApplicationParameters;

/**
 * @author Olivier Truong
 */
@Singleton
public class ApplicationParametersEJB
    extends AbstractEJB
{

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
        entityManager.merge( parameters );
        loadParameters();
    }

    public ApplicationParameters getParameters()
    {
        return parameters;
    }

    private void loadParameters()
    {
        Query query = entityManager.createNamedQuery( "findParameters" );
        @SuppressWarnings( "unchecked" )
        List<ApplicationParameters> listParam = query.getResultList();
        if ( CollectionUtils.isNotEmpty( listParam ) )
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
