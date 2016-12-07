package com.oltruong.teamag.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.WorkLoad;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class WorkLoadServiceTest extends AbstractServiceTest {

    private WorkLoadService workLoadService;


    @Before
    public void prepare() {
        super.setup();
        workLoadService = new WorkLoadService();
        prepareService(workLoadService);
    }

    @Test
    public void findOrCreateAllWorkLoad_find() {
        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad);


        when(mockTypedQuery.getResultList()).thenReturn(workLoadList);

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

        TestUtils.setPrivateAttribute(new MemberService(), memberList, "memberList");

        List<WorkLoad> workLoadReturnedList = workLoadService.findOrCreateAllWorkLoad(businessCaseList);

        assertThat(workLoadReturnedList).containsAll(workLoadList).hasSize(memberList.size() * businessCaseList.size());
        checkCreateTypedQuery("WorkLoad.FIND_ALL");


    }

    @Test
    public void findOrCreateAllWorkLoad_createNull() {
        when(mockTypedQuery.getResultList()).thenReturn(null);
        testFindOrCreateAllWorkLoad_create();
    }

    @Test
    public void findOrCreateAllWorkLoad_create_empty() {
        when(mockTypedQuery.getResultList()).thenReturn(Lists.newArrayList());

        testFindOrCreateAllWorkLoad_create();

    }

    private void testFindOrCreateAllWorkLoad_create() {

        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);
        List<BusinessCase> bcList = EntityFactory.createList(EntityFactory::createBusinessCase);


        TestUtils.setPrivateAttribute(new MemberService(), memberList, "memberList");


        List<WorkLoad> workLoadReturnedList = workLoadService.findOrCreateAllWorkLoad(bcList);


        assertThat(workLoadReturnedList).doesNotHaveDuplicates().hasSize(memberList.size() * bcList.size());
        verify(mockEntityManager, times(memberList.size() * bcList.size())).persist(isA(WorkLoad.class));

        checkCreateTypedQuery("WorkLoad.FIND_ALL");
    }


    @Test
    public void testUpdateWorkLoad() {
        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad);

        workLoadService.mergeList(workLoadList);


        //Dunno why IntellIj won't accept a single line here...
        workLoadList.forEach(workLoad -> {
            verify(mockEntityManager).merge(eq(workLoad));
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWorkLoadNull() {
        workLoadService.mergeList(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateWorkLoadWithRealizedNull() {
        workLoadService.updateWorkLoadWithRealized(null, null);
    }

    //FIXME
    @Ignore
    public void updateWorkLoadWithRealized() {

        final double newRealized = 365d;

        Query mockQueryBC = mock(Query.class);
        when(mockEntityManager.createNamedQuery(eq("findAllBC"))).thenReturn(mockQueryBC);


        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad);


        when(mockTypedQuery.getResultList()).thenReturn(workLoadList);

        List<BusinessCase> businessCaseList = Lists.newArrayListWithCapacity(workLoadList.size());
        businessCaseList.forEach(bc -> bc.setId(EntityFactory.createRandomLong()));

        List<Member> memberList = Lists.newArrayListWithCapacity(workLoadList.size());
        memberList.forEach(member -> member.setId(EntityFactory.createRandomLong()));
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

        when(mockTypedQuery.getResultList()).thenReturn(workLoadList);

        Table<Member, BusinessCase, Double> values = HashBasedTable.create();

        TestUtils.setPrivateAttribute(new MemberService(), memberList, "memberList");
        workLoadList.forEach(workLoad -> {
            values.put(workLoad.getMember(), workLoad.getBusinessCase(), newRealized);
        });


        workLoadService.updateWorkLoadWithRealized(values, businessCaseList);

        workLoadList.forEach(workLoad -> {
            assertThat(workLoad.getRealized()).isEqualTo(newRealized);
            verify(mockEntityManager).merge(eq(workLoad));
        });
    }

    @Test
    public void createFromBusinessCase() {
        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);
        TestUtils.setPrivateAttribute(new MemberService(), memberList, "memberList");

        BusinessCase businessCase = EntityFactory.createBusinessCase();

        workLoadService.createFromBusinessCase(businessCase);

        ArgumentCaptor<WorkLoad> workLoadArgumentCaptor = ArgumentCaptor.forClass(WorkLoad.class);
        verify(mockEntityManager, times(memberList.size())).persist(workLoadArgumentCaptor.capture());

        List<WorkLoad> workLoadList = workLoadArgumentCaptor.getAllValues();
        for (int i = 0; i < memberList.size(); i++) {
            final WorkLoad workLoad = workLoadList.get(i);
            assertThat(workLoad.getBusinessCase()).isEqualTo(businessCase);
            assertThat(workLoad.getMember()).isEqualTo(memberList.get(i));
        }

    }

    @Test
    public void createFromBusinessCase_noMember() {
        TestUtils.setPrivateAttribute(new MemberService(), null, "memberList");
        workLoadService.createFromBusinessCase(EntityFactory.createBusinessCase());
        verify(mockEntityManager, never()).persist(any());
    }

    @Test
    public void createFromMember() {

    }

    @Test
    public void testRemoveBusinessCase() {
        workLoadService.removeBusinessCase(randomLong);

        verify(mockEntityManager).createNamedQuery(eq("WorkLoad.DELETE_FROM_BUSINESSCASE"));
        verify(mockNamedQuery).setParameter(eq("fBusinessCaseId"), eq(randomLong));
        verify(mockNamedQuery).executeUpdate();
    }
}
