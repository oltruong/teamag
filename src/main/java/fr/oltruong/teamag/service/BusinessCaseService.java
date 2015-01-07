package fr.oltruong.teamag.service;

import com.google.common.base.Strings;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.WorkLoad;

import javax.persistence.Query;
import java.util.List;

/**
 * @author oltruong
 */
public class BusinessCaseService extends AbstractService {

    @SuppressWarnings("unchecked")
    public List<BusinessCase> findAll() {
        Query query = createNamedQuery("findAllBC");
        return query.getResultList();
    }

    public BusinessCase create(BusinessCase bc) throws ExistingDataException {

        if (!Strings.isNullOrEmpty(bc.getIdentifier())) {
            Query query = createNamedQuery("findBCByNumber");
            query.setParameter("fidentifier", bc.getIdentifier());
            if (!query.getResultList().isEmpty()) {
                throw new ExistingDataException();
            }
        }
        persist(bc);


        //Create WorkLoad
        List<Member> memberList = MemberService.getMemberList();
        if (memberList != null) {
            for (Member member : memberList) {
                WorkLoad workLoad = new WorkLoad(bc, member);
                persist(workLoad);
            }
        }

        return bc;
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
