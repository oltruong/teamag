package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.WorkLoad;
import fr.oltruong.teamag.transformer.AbsenceDayTransformer;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void prepare() {
        super.setup();
        workLoadService = new WorkLoadService();
        prepareService(workLoadService);
    }

    @Test
    public void testGetAllAbsenceDay() throws Exception {
        List<AbsenceDay> absenceDayList = EntityFactory.createList(EntityFactory::createAbsenceDay);

        when(getMockQuery().getResultList()).thenReturn(absenceDayList);


        List<AbsenceDay> absenceDayListReturned = workLoadService.getAllAbsenceDay();

        assertThat(absenceDayList).isEqualTo(absenceDayListReturned);

        checkCreateNameQuery("findAllAbsenceDays");

        verify(getMockQuery()).getResultList();

    }

    @Test
    public void testRemoveAbsence() {

        Long idTest = Long.valueOf(3365l);


        List<AbsenceDay> absenceDayList = EntityFactory.createList(EntityFactory::createAbsenceDay);
        when(getMockQuery().getResultList()).thenReturn(absenceDayList);

        workLoadService.removeAbsence(idTest);

        checkCreateNameQuery("findAbsenceDayByAbsenceId");

        verify(getMockQuery()).setParameter(eq("fAbsenceId"), eq(idTest));
        verify(getMockQuery()).getResultList();

        absenceDayList.forEach(absenceDay -> verify(getMockEntityManager()).remove(eq(absenceDay)));


    }

    @Test
    public void testRemoveAbsence_listNull() {
        when(getMockQuery().getResultList()).thenReturn(null);

        workLoadService.removeAbsence(Long.valueOf(123l));
        verify(getMockEntityManager(), never()).remove(any());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAbsence_null() {
        workLoadService.removeAbsence(null);
    }

    @Test
    public void testRegisterAbsence() {
        Absence absence = EntityFactory.createAbsence();
        workLoadService.registerAbsence(absence);

        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(absence);

        absenceDayList.forEach(absenceDay -> verify(getMockEntityManager()).persist(refEq(absenceDay)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterAbsence_null() {
        workLoadService.registerAbsence(null);
    }


    @Test
    public void testReloadAllAbsenceDay() {

        Query mockQueryAbsenceDay = mock(Query.class);
        Query mockQueryAbsence = mock(Query.class);

        when(getMockEntityManager().createNamedQuery(eq("findAllAbsenceDays"))).thenReturn(mockQueryAbsenceDay);
        when(getMockEntityManager().createNamedQuery(eq("findAllAbsences"))).thenReturn(mockQueryAbsence);

        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        when(mockQueryAbsence.getResultList()).thenReturn(absenceList);

        List<AbsenceDay> absenceDayList = EntityFactory.createList(EntityFactory::createAbsenceDay);
        when(mockQueryAbsenceDay.getResultList()).thenReturn(absenceDayList);

        workLoadService.reloadAllAbsenceDay();

        verify(getMockEntityManager()).createNamedQuery(eq("findAllAbsenceDays"));
        absenceDayList.forEach(absenceDay -> verify(getMockEntityManager()).remove(eq(absenceDay)));


        absenceList.forEach(absence -> AbsenceDayTransformer.transformAbsence(absence).forEach(absenceDay -> verify(getMockEntityManager()).persist(refEq(absenceDay))));

    }

    @Test
    public void testReloadAllAbsenceDay_Null() {
        Query mockQueryAbsenceDay = mock(Query.class);
        Query mockQueryAbsence = mock(Query.class);

        when(getMockEntityManager().createNamedQuery(eq("findAllAbsenceDays"))).thenReturn(mockQueryAbsenceDay);
        when(getMockEntityManager().createNamedQuery(eq("findAllAbsences"))).thenReturn(mockQueryAbsence);

        when(mockQueryAbsenceDay.getResultList()).thenReturn(null);
        when(mockQueryAbsence.getResultList()).thenReturn(null);

        workLoadService.reloadAllAbsenceDay();
        verify(getMockEntityManager(), never()).remove(any());
        verify(getMockEntityManager(), never()).persist(any());
    }


    @Test
    public void testFindOrCreateAllWorkLoad_find() {
        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad);
        when(getMockQuery().getResultList()).thenReturn(workLoadList);

        List<WorkLoad> workLoadReturnedList = workLoadService.findOrCreateAllWorkLoad();

        assertThat(workLoadReturnedList).isEqualTo(workLoadList);
        verify(getMockEntityManager()).createNamedQuery(eq("findAllWorkLoad"));

    }

    @Test
    public void testFindOrCreateAllWorkLoad_create() {
        when(getMockQuery().getResultList()).thenReturn(null);

        Query mockQueryBC = mock(Query.class);
        when(getMockEntityManager().createNamedQuery(eq("findAllBC"))).thenReturn(mockQueryBC);


        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);
        List<BusinessCase> bcList = EntityFactory.createList(EntityFactory::createBusinessCase);

        when(mockQueryBC.getResultList()).thenReturn(bcList);

        TestUtils.setPrivateAttribute(new MemberService(), memberList, "memberList");


        List<WorkLoad> workLoadReturnedList = workLoadService.findOrCreateAllWorkLoad();


        assertThat(workLoadReturnedList).doesNotHaveDuplicates().hasSize(memberList.size() * bcList.size());
        verify(getMockEntityManager(), times(memberList.size() * bcList.size())).persist(isA(WorkLoad.class));

        verify(getMockEntityManager()).createNamedQuery(eq("findAllWorkLoad"));
        verify(getMockEntityManager()).createNamedQuery(eq("findAllBC"));

    }


    @Test
    public void testUpdateWorkLoad() {
        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad);

        workLoadService.updateWorkLoad(workLoadList);


        //Dunno why IntellIj won't accept a single line here...
        workLoadList.forEach(workLoad -> {
            verify(getMockEntityManager()).merge(eq(workLoad));
        });
    }

    @Test
    public void testUpdateWorkLoad_null() {
        workLoadService.updateWorkLoad(null);
        verify(getMockEntityManager(), never()).merge(any());
    }
}
