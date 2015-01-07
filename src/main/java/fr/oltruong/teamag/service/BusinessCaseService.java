package fr.oltruong.teamag.service;

import com.google.common.base.Strings;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.BusinessCase;

import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 * @author oltruong
 */
public class BusinessCaseService extends AbstractService {

    @Inject
    private WorkLoadService workLoadService;

    @SuppressWarnings("unchecked")
    public List<BusinessCase> findAll() {
        Query query = createNamedQuery("findAllBC");
        return query.getResultList();
    }

    public BusinessCase create(BusinessCase businessCase) throws ExistingDataException {

        if (!Strings.isNullOrEmpty(businessCase.getIdentifier())) {
            Query query = createNamedQuery("findBCByNumber");
            query.setParameter("fidentifier", businessCase.getIdentifier());
            if (!query.getResultList().isEmpty()) {
                throw new ExistingDataException();
            }
        }
        persist(businessCase);
        workLoadService.createFromBusinessCase(businessCase);

        return businessCase;
    }


    public void delete(Long businessCaseId) {
        BusinessCase businessCase = find(BusinessCase.class, businessCaseId);
        remove(businessCase);
    }

    public void update(BusinessCase bcUpdated) {
        merge(bcUpdated);
    }

    public BusinessCase find(Long businessCaseId) {
        return find(BusinessCase.class, businessCaseId);
    }


}
