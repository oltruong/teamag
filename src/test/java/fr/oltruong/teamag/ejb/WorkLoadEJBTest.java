package fr.oltruong.teamag.ejb;

import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.AbsenceDay;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.transformer.AbsenceDayTransformer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;


/**
 * @author Olivier Truong
 */
public class WorkLoadEJBTest extends AbstractEJBTest {

    private WorkLoadEJB workLoadEJB;

    @Before
    public void prepare() {
        super.setup();
        workLoadEJB = new WorkLoadEJB();
        prepareEJB(workLoadEJB);
    }

    @Test
    public void testGetAllAbsenceDay() throws Exception {
        List<AbsenceDay> absenceDayList = EntityFactory.createAbsenceDayList(77);

        when(getMockQuery().getResultList()).thenReturn(absenceDayList);


        List<AbsenceDay> absenceDayListReturned = workLoadEJB.getAllAbsenceDay();

        assertThat(absenceDayList).isEqualTo(absenceDayListReturned);

        checkCreateNameQuery("findAllAbsenceDays");

        verify(getMockQuery()).getResultList();
    }

    @Test
    public void testRemoveAbsence() throws Exception {

        Long idTest = Long.valueOf(3365l);


        List<AbsenceDay> absenceDayList = EntityFactory.createAbsenceDayList(77);
        when(getMockQuery().getResultList()).thenReturn(absenceDayList);

        workLoadEJB.removeAbsence(idTest);

        checkCreateNameQuery("findAbsenceDayByAbsenceId");

        verify(getMockQuery()).setParameter(eq("fAbsenceId"), eq(idTest));
        verify(getMockQuery()).getResultList();

        verify(getMockEntityManager(), times(absenceDayList.size())).remove(any(AbsenceDay.class));

        for (AbsenceDay absenceDay : absenceDayList) {
            verify(getMockEntityManager()).remove(eq(absenceDay));
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAbsence_null() throws Exception {
        workLoadEJB.removeAbsence(null);
    }

    @Test
    public void testRegisterAbsence() throws Exception {
        Absence absence = EntityFactory.createAbsence();
        workLoadEJB.registerAbsence(absence);

        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(absence);
        verify(getMockEntityManager(), times(absenceDayList.size())).persist(any(AbsenceDay.class));

    }

    @Test
    public void testReloadAllAbsenceDay() throws Exception {

    }
}
