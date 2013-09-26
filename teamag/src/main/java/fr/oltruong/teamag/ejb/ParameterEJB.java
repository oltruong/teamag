/**
 * 
 */
package fr.oltruong.teamag.ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;

import fr.oltruong.teamag.entity.Parameter;
import fr.oltruong.teamag.entity.ParameterName;

/**
 * @author Olivier Truong
 */
@Singleton
public class ParameterEJB
    extends AbstractEJB
{

    private final Logger logger = Logger.getLogger( getClass().getName() );

    private Map<ParameterName, Parameter> parameterMap;

    @PostConstruct
    private void initValue()
    {
        loadParameters();
    }

    public void saveAndReloadParameters( Parameter smtpHostParameter, Parameter administratorEmailParameter )
    {
        setSmtpHostParameter( smtpHostParameter );
        setAdministratorEmailParameter( administratorEmailParameter );
        saveParameters();
        loadParameters();
    }

    private void saveParameters()
    {
        for ( Parameter parameter : this.parameterMap.values() )
        {
            this.entityManager.merge( parameter );
        }
    }

    private void loadParameters()
    {
        Query query = this.entityManager.createNamedQuery( "findParameters" );
        @SuppressWarnings( "unchecked" )
        List<Parameter> parameterList = query.getResultList();
        if ( CollectionUtils.isNotEmpty( parameterList ) )
        {
            this.parameterMap = new HashMap<ParameterName, Parameter>( parameterList.size() );
            for ( Parameter parameter : parameterList )
            {
                this.parameterMap.put( parameter.getName(), parameter );
            }
        }
        else
        {

            initAndPersistParameterMap();
        }
    }

    private void initAndPersistParameterMap()
    {

        this.logger.info( "Creating parameters Map" );

        this.parameterMap = new HashMap<ParameterName, Parameter>( 2 );
        Parameter smtpHostParameter = new Parameter( ParameterName.SMTP_HOST );
        Parameter administratorEmailParameter = new Parameter( ParameterName.ADMINISTRATOR_EMAIL );

        this.entityManager.persist( smtpHostParameter );
        this.entityManager.persist( administratorEmailParameter );

        this.parameterMap.put( ParameterName.SMTP_HOST, smtpHostParameter );
        this.parameterMap.put( ParameterName.ADMINISTRATOR_EMAIL, administratorEmailParameter );

    }

    @Lock( LockType.READ )
    public String getSmtpHost()
    {
        return getSmtpHostParameter().getValue();
    }

    @Lock( LockType.READ )
    public String getAdministratorEmail()
    {
        return getAdministratorEmailParameter().getValue();
    }

    @Lock( LockType.READ )
    public Parameter getSmtpHostParameter()
    {
        return this.parameterMap.get( ParameterName.SMTP_HOST );
    }

    @Lock( LockType.READ )
    public Parameter getAdministratorEmailParameter()
    {
        return this.parameterMap.get( ParameterName.ADMINISTRATOR_EMAIL );
    }

    private void setSmtpHostParameter( Parameter smtpHostParameter )
    {
        this.parameterMap.put( ParameterName.SMTP_HOST, smtpHostParameter );

    }

    private void setAdministratorEmailParameter( Parameter administratorEmailParameter )
    {
        this.parameterMap.put( ParameterName.ADMINISTRATOR_EMAIL, administratorEmailParameter );

    }

}
