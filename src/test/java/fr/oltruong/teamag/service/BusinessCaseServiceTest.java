package fr.oltruong.teamag.service;

import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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


    @Before
    public void prepare() {
        businessCaseService = new BusinessCaseService();
        prepareService(businessCaseService);
        TestUtils.setPrivateAttribute(businessCaseService, mockWorkLoadService, "workLoadService");

    }

    @Test
    public void testFindAll() throws Exception {
        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(getMockQuery().getResultList()).thenReturn(businessCaseList);

        List<BusinessCase> businessCaseFoundList = businessCaseService.findAll();

        assertThat(businessCaseFoundList).isEqualTo(businessCaseList);
        verify(mockEntityManager).createNamedQuery(eq("findAllBC"));
        verify(getMockQuery()).getResultList();
    }

    @Test
    public void testCreate() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        BusinessCase businessCaseCreated = businessCaseService.create(businessCase);


        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);

        MemberService memberService = new MemberService();

        TestUtils.setPrivateAttribute(memberService, memberList, "memberList");

        assertThat(businessCaseCreated).isEqualTo(businessCase);

        verify(mockEntityManager).createNamedQuery(eq("findBCByNumber"));
        verify(getMockQuery()).setParameter(eq("fidentifier"), isA(String.class));

        verify(mockEntityManager).persist(eq(businessCase));
        verify(mockWorkLoadService).createFromBusinessCase(eq(businessCase));


    }

    @Test(expected = ExistingDataException.class)
    public void testCreate_existingData() throws Exception {

        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(getMockQuery().getResultList()).thenReturn(businessCaseList);

        businessCaseService.create(businessCaseList.get(0));
    }


    @Test
    public void testCreateBusinessCase_noIdentifier() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        businessCase.setIdentifier(null);
        BusinessCase businessCaseCreated = businessCaseService.create(businessCase);

        assertThat(businessCaseCreated).isEqualTo(businessCase);

        verify(mockEntityManager, never()).createNamedQuery(eq("findBCByNumber"));
        verify(getMockQuery(), never()).setParameter(eq("fidentifier"), isA(String.class));
        verify(mockEntityManager).persist(eq(businessCase));
    }


    @Test
    public void testUpdate() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        businessCaseService.update(businessCase);

        verify(mockEntityManager).merge(eq(businessCase));
    }


    @Test
    public void testDelete() {


        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockEntityManager.find(eq(BusinessCase.class), anyLong())).thenReturn(businessCase);
        businessCaseService.delete(randomLong);

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
