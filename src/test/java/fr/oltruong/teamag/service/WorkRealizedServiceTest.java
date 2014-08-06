package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.WorkRealized;
import fr.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkRealizedServiceTest extends AbstractServiceTest {


    private List<WorkRealized> workRealizedList;

    private WorkRealizedService workRealizedService;

    @Before
    public void prepare() {
        super.setup();
        workRealizedList = EntityFactory.createList(EntityFactory::createWorkRealized);
        workRealizedService = new WorkRealizedService();
        prepareService(workRealizedService);

        when(mockQuery.getResultList()).thenReturn(workRealizedList);
    }

    @Test
    public void testGetAllWorkRealized() throws Exception {
        List<WorkRealized> workRealizedServiceReturned = workRealizedService.getAllWorkRealized();

        assertThat(workRealizedServiceReturned).isEqualTo(workRealizedList);

        verify(mockEntityManager).createNamedQuery(eq("findAllWorkRealized"));
    }

    @Test
    public void testGetWorkRealizedbyMember() throws Exception {
        List<WorkRealized> workRealizedServiceReturned = workRealizedService.getWorkRealizedbyMember(idTest);

        assertThat(workRealizedServiceReturned).isEqualTo(workRealizedList);

        verify(mockEntityManager).createNamedQuery(eq("findAllWorkRealizedByMember"));
        verify(mockQuery).setParameter(eq("fMemberId"), eq(idTest));

    }

    @Test
    public void testCreateOrUpdate() throws Exception {

        workRealizedList.get(0).setId(idTest);

        workRealizedService.createOrUpdate(workRealizedList);

        verify(mockEntityManager).merge(eq(workRealizedList.get(0)));
        verify(mockEntityManager, never()).persist(eq(workRealizedList.get(0)));

        workRealizedList.remove(0);

        workRealizedList.forEach(workRealized -> {
            verify(mockEntityManager).persist(eq(workRealized));
            verify(mockEntityManager, never()).merge(eq(workRealized));
        });

    }

    @Test
    public void testCreateOrUpdate_null() throws Exception {
        workRealizedService.createOrUpdate(null);
        verify(mockEntityManager, never()).persist(any());
        verify(mockEntityManager, never()).merge(any());

    }

}