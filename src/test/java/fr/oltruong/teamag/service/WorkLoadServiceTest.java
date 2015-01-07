package fr.oltruong.teamag.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.WorkLoad;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.transformer.AbsenceDayTransformer;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Olivier Truong
 */
public class WorkLoadServiceTest extends AbstractServiceTest {

    private WorkLoadService workLoadService;

    @Mock
    private WorkService mockWorkService;


    @Before
    public void prepare() {
        super.setup();
        workLoadService = new WorkLoadService();
        prepareService(workLoadService);
        TestUtils.setPrivateAttribute(workLoadService, mockWorkService, "workService");
    }

    @Test
    public void testFindOrCreateAllWorkLoad_find() {
        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad);


        when(getMockQuery().getResultList()).thenReturn(workLoadList);

        List<BusinessCase> businessCaseList = Lists.newArrayListWithCapacity(workLoadList.size());
        List<Member> memberList = Lists.newArrayListWithCapacity(workLoadList.size());

        workLoadList.forEach(workLoad -> {

            workLoad.getBusinessCase().setId(EntityFactory.createRandomLong());
            workLoad.getMember().setId(EntityFactory.createRandomLong());

            if (!businessCaseList.contains(workLoad.getBusinessCase())) {
                businessCaseList.add(workLoad.getBusinessCase());
            }

            if (!memberList.contains(workLoad.getMember())) {
                memberList.add(workLoad.getMember());
            }
        });

        Query mockQueryBC = mock(Query.class);
        when(mockEntityManager.createNamedQuery(eq("findAllBC"))).thenReturn(mockQueryBC);
        when(mockQueryBC.getResultList()).thenReturn(businessCaseList);
        TestUtils.setPrivateAttribute(new MemberService(), memberList, "memberList");

        List<WorkLoad> workLoadReturnedList = workLoadService.findOrCreateAllWorkLoad();

        assertThat(workLoadReturnedList).containsAll(workLoadList).hasSize(memberList.size() * businessCaseList.size());
        verify(mockEntityManager).createNamedQuery(eq("findAllWorkLoad"));

    }

    @Test
    public void testFindOrCreateAllWorkLoad_create_null() {
        when(getMockQuery().getResultList()).thenReturn(null);

        testFindOrCreateAllWorkLoad_create();

    }

    @Test
    public void testFindOrCreateAllWorkLoad_create_empty() {
        when(getMockQuery().getResultList()).thenReturn(Lists.newArrayList());

        testFindOrCreateAllWorkLoad_create();

    }

    private void testFindOrCreateAllWorkLoad_create() {
        Query mockQueryBC = mock(Query.class);
        when(mockEntityManager.createNamedQuery(eq("findAllBC"))).thenReturn(mockQueryBC);


        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);
        List<BusinessCase> bcList = EntityFactory.createList(EntityFactory::createBusinessCase);

        when(mockQueryBC.getResultList()).thenReturn(bcList);

        TestUtils.setPrivateAttribute(new MemberService(), memberList, "memberList");


        List<WorkLoad> workLoadReturnedList = workLoadService.findOrCreateAllWorkLoad();


        assertThat(workLoadReturnedList).doesNotHaveDuplicates().hasSize(memberList.size() * bcList.size());
        verify(mockEntityManager, times(memberList.size() * bcList.size())).persist(isA(WorkLoad.class));

        verify(mockEntityManager).createNamedQuery(eq("findAllWorkLoad"));
        verify(mockEntityManager).createNamedQuery(eq("findAllBC"));
    }


    @Test
    public void testUpdateWorkLoad() {
        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad);

        workLoadService.updateWorkLoad(workLoadList);


        //Dunno why IntellIj won't accept a single line here...
        workLoadList.forEach(workLoad -> {
            verify(mockEntityManager).merge(eq(workLoad));
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWorkLoad_null() {
        workLoadService.updateWorkLoad(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateWorkLoadWithRealized_null() {
        workLoadService.updateWorkLoadWithRealized(null);
    }

    @Test
    public void updateWorkLoadWithRealized() {

        final double newRealized = 365d;

        Query mockQueryBC = mock(Query.class);
        when(mockEntityManager.createNamedQuery(eq("findAllBC"))).thenReturn(mockQueryBC);


        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad);


        when(getMockQuery().getResultList()).thenReturn(workLoadList);

        List<BusinessCase> businessCaseList = Lists.newArrayListWithCapacity(workLoadList.size());
        List<Member> memberList = Lists.newArrayListWithCapacity(workLoadList.size());

        workLoadList.forEach(workLoad -> {

            workLoad.getBusinessCase().setId(EntityFactory.createRandomLong());
            workLoad.getMember().setId(EntityFactory.createRandomLong());

            if (!businessCaseList.contains(workLoad.getBusinessCase())) {
                businessCaseList.add(workLoad.getBusinessCase());
            }

            if (!memberList.contains(workLoad.getMember())) {
                memberList.add(workLoad.getMember());
            }
        });

        when(mockQuery.getResultList()).thenReturn(workLoadList);

        Table<Member, BusinessCase, Double> values = HashBasedTable.create();

        workLoadList.forEach(workLoad -> {
            values.put(workLoad.getMember(), workLoad.getBusinessCase(), newRealized);
        });

        workLoadService.updateWorkLoadWithRealized(values);

        workLoadList.forEach(workLoad -> {
            assertThat(workLoad.getRealized()).isEqualTo(newRealized);
            verify(mockEntityManager).merge(eq(workLoad));
        });
    }
}
