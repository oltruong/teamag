/**
 * 
 */
package fr.oltruong.teamag.ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import fr.oltruong.teamag.entity.Parameter;
import fr.oltruong.teamag.entity.ParameterName;

/**
 * @author Olivier Truong
 */
@Singleton
public class ParameterEJB extends AbstractEJB {

    @Inject
    private Logger logger;

    private Map<ParameterName, Parameter> parameterMap;

    @PostConstruct
    private void initValue() {
        loadParameters();
    }

    public void saveAndReloadParameters(Parameter smtpHostParameter, Parameter administratorEmailParameter) {
        setSmtpHostParameter(smtpHostParameter);
        setAdministratorEmailParameter(administratorEmailParameter);
        saveParameters();
        loadParameters();
    }

    private void saveParameters() {
        for (Parameter parameter : parameterMap.values()) {
            this.getEntityManager().merge(parameter);
        }
    }

    private void loadParameters() {
        Query query = this.getEntityManager().createNamedQuery("findParameters");
        @SuppressWarnings("unchecked")
        List<Parameter> parameterList = query.getResultList();
        if (CollectionUtils.isNotEmpty(parameterList)) {
            parameterMap = new HashMap<ParameterName, Parameter>(parameterList.size());
            for (Parameter parameter : parameterList) {
                parameterMap.put(parameter.getName(), parameter);
            }
        } else {

            initAndPersistParameterMap();
        }
    }

    private void initAndPersistParameterMap() {

        logger.info("Creating parameters Map");

        parameterMap = new HashMap<ParameterName, Parameter>(2);
        Parameter smtpHostParameter = new Parameter(ParameterName.SMTP_HOST);
        Parameter administratorEmailParameter = new Parameter(ParameterName.ADMINISTRATOR_EMAIL);

        this.getEntityManager().persist(smtpHostParameter);
        this.getEntityManager().persist(administratorEmailParameter);

        parameterMap.put(ParameterName.SMTP_HOST, smtpHostParameter);
        parameterMap.put(ParameterName.ADMINISTRATOR_EMAIL, administratorEmailParameter);

    }

    @Lock(LockType.READ)
    public String getSmtpHost() {
        return getSmtpHostParameter().getValue();
    }

    @Lock(LockType.READ)
    public String getAdministratorEmail() {
        return getAdministratorEmailParameter().getValue();
    }

    @Lock(LockType.READ)
    public Parameter getSmtpHostParameter() {
        return parameterMap.get(ParameterName.SMTP_HOST);
    }

    @Lock(LockType.READ)
    public Parameter getAdministratorEmailParameter() {
        return parameterMap.get(ParameterName.ADMINISTRATOR_EMAIL);
    }

    private void setSmtpHostParameter(Parameter smtpHostParameter) {
        parameterMap.put(ParameterName.SMTP_HOST, smtpHostParameter);

    }

    private void setAdministratorEmailParameter(Parameter administratorEmailParameter) {
        parameterMap.put(ParameterName.ADMINISTRATOR_EMAIL, administratorEmailParameter);

    }

}
