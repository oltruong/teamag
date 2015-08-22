/**
 *
 */
package com.oltruong.teamag.service;

import com.google.common.collect.Maps;
import com.oltruong.teamag.model.Parameter;
import com.oltruong.teamag.model.enumeration.ParameterName;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Olivier Truong
 */
@Singleton
public class ParameterService extends AbstractService<Parameter> {

    public static final String DEFAULT_PORT = "25";
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

    @Lock(LockType.WRITE)
    private void saveParameters() {
        parameterMap.values().forEach(this::merge);
    }

    private void loadParameters() {
        List<Parameter> parameterList = getTypedQueryList("Parameter.FIND_ALL");
        if (CollectionUtils.isNotEmpty(parameterList)) {
            parameterMap = Maps.newHashMapWithExpectedSize(parameterList.size());
            for (Parameter parameter : parameterList) {
                parameterMap.put(parameter.getName(), parameter);
            }
        } else {

            initAndPersistParameterMap();
        }
    }

    private void initAndPersistParameterMap() {

        logger.info("Creating parameters Map");

        parameterMap = new HashMap<>(2);
        Parameter smtpHostParameter = new Parameter(ParameterName.SMTP_HOST);
        Parameter administratorEmailParameter = new Parameter(ParameterName.ADMINISTRATOR_EMAIL);

        persist(smtpHostParameter);
        persist(administratorEmailParameter);

        parameterMap.put(ParameterName.SMTP_HOST, smtpHostParameter);
        parameterMap.put(ParameterName.ADMINISTRATOR_EMAIL, administratorEmailParameter);

    }


    public String getSmtpPort() {
        return DEFAULT_PORT;
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

    @Override
    Class<Parameter> entityProvider() {
        return Parameter.class;
    }

    @Override
    public List<Parameter> findAll() {
        throw new UnsupportedOperationException();
    }
}
