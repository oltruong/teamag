package com.oltruong.teamag.service;

import com.google.common.base.Strings;
import com.oltruong.teamag.model.BusinessCase;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


public class BusinessCaseService extends AbstractService<BusinessCase> {


    @Inject
    private WorkLoadService workLoadService;

    @Inject
    private ActivityService activityService;

    @Override
    public List<BusinessCase> findAll() {
        return getTypedQueryList("findAllBC");
    }

    @Override
    Class<BusinessCase> entityProvider() {
        return BusinessCase.class;
    }

    public BusinessCase persist(BusinessCase businessCase) {

        if (!Strings.isNullOrEmpty(businessCase.getIdentifier())) {
            TypedQuery<BusinessCase> query = createTypedQuery("findBCByNumber");
            query.setParameter("fidentifier", businessCase.getIdentifier());
            if (!query.getResultList().isEmpty()) {
                throw new EntityExistsException();
            }
        }
        super.persist(businessCase);
        workLoadService.createFromBusinessCase(businessCase);

        return businessCase;
    }

    @Override
    @Transactional
    public void remove(Long businessCaseId) {
        workLoadService.removeBusinessCase(businessCaseId);
        activityService.removeBusinessCase(businessCaseId);

        super.remove(businessCaseId);
    }

}
