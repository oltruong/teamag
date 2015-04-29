package com.oltruong.teamag.service;

import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.persistence.EntityExistsException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class BusinessCaseServiceTest extends AbstractServiceTest {


    private BusinessCaseService businessCaseService;

    @Mock
    private WorkLoadService mockWorkLoadService;

    @Mock
    private ActivityService mockActivityService;


    @Before
    public void prepare() {
        businessCaseService = new BusinessCaseService();
        prepareService(businessCaseService);
        TestUtils.setPrivateAttribute(businessCaseService, mockWorkLoadService, "workLoadService");
        TestUtils.setPrivateAttribute(businessCaseService, mockActivityService, "activityService");

    }

    @Test
    public void testFindAll() throws Exception {
        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(mockTypedQuery.getResultList()).thenReturn(businessCaseList);

        List<BusinessCase> businessCaseFoundList = businessCaseService.findAll();

        assertThat(businessCaseFoundList).isEqualTo(businessCaseList);
        checkCreateTypedQuery("findAllBC");
        verify(mockTypedQuery).getResultList();
    }

    @Test
    public void testCreate() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        BusinessCase businessCaseCreated = businessCaseService.persist(businessCase);


        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);

        MemberService memberService = new MemberService();

        TestUtils.setPrivateAttribute(memberService, memberList, "memberList");

        assertThat(businessCaseCreated).isEqualTo(businessCase);

        checkCreateTypedQuery("findBCByNumber");
        verify(mockTypedQuery).setParameter(eq("fidentifier"), isA(String.class));

        verify(mockEntityManager).persist(eq(businessCase));
        verify(mockWorkLoadService).createFromBusinessCase(eq(businessCase));


    }

    @Test(expected = EntityExistsException.class)
    public void testCreate_existingData() throws Exception {

        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(mockTypedQuery.getResultList()).thenReturn(businessCaseList);

        businessCaseService.persist(businessCaseList.get(0));
    }


    @Test
    public void testCreateBusinessCase_noIdentifier() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        businessCase.setIdentifier(null);
        BusinessCase businessCaseCreated = businessCaseService.persist(businessCase);

        assertThat(businessCaseCreated).isEqualTo(businessCase);

        verify(mockEntityManager, never()).createNamedQuery(eq("findBCByNumber"));
        verify(mockTypedQuery, never()).setParameter(eq("fidentifier"), isA(String.class));
        verify(mockEntityManager).persist(eq(businessCase));
    }


    @Test
    public void testUpdate() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        businessCaseService.merge(businessCase);

        verify(mockEntityManager).merge(eq(businessCase));
    }


    @Test
    public void testRemove() {


        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockEntityManager.find(eq(BusinessCase.class), anyLong())).thenReturn(businessCase);
        businessCaseService.remove(randomLong);

        verify(mockWorkLoadService).removeBusinessCase(eq(randomLong));
        verify(mockActivityService).removeBusinessCase(eq(randomLong));
        verify(mockEntityManager).find(eq(BusinessCase.class), eq(randomLong));
        verify(mockEntityManager).remove(eq(businessCase));
    }

    @Test
    public void testFindSingle() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockEntityManager.find(eq(BusinessCase.class), anyLong())).thenReturn(businessCase);

        BusinessCase businessCaseFound = businessCaseService.find(randomLong);

        assertThat(businessCaseFound).isEqualToComparingFieldByField(businessCase);

        verify(mockEntityManager).find(eq(BusinessCase.class), eq(randomLong));

    }
}
