package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkRealizedIT extends AbstractEntityIT {


    @Test
    public void testCreateAndFind() {
        WorkRealized workRealized = createWorkRealized();


        assertThat(workRealized.getId()).isNotNull();


        WorkRealized workRealizedDB = entityManager.find(WorkRealized.class, workRealized.getId());
        assertThat(workRealizedDB).isEqualToComparingFieldByField(workRealized).isEqualTo(workRealized);
        assertThat(entityManager.find(WorkRealized.class, workRealized.getId() + 1)).isNull();
    }

    private WorkRealized createWorkRealized() {
        WorkRealized workRealized = EntityFactory.createWorkRealized();

        assertThat(workRealized.getId()).isNull();

        persist(workRealized.getMember());
        persist(workRealized.getTask());

        persist(workRealized);
        commit();
        return workRealized;
    }


    @Test(expected = PersistenceException.class)
    public void testCreate_taskNull() {
        WorkRealized workRealized = EntityFactory.createWorkRealized();
        workRealized.setTask(null);
        persist(workRealized.getMember());
        persist(workRealized);
        commit();
    }


    @Test
    public void test_namedQuery_FindAllWorkRealized() throws Exception {

        WorkRealized workRealized = createWorkRealized();
        transaction.begin();
        WorkRealized workRealized2 = createWorkRealized();

        Query query = entityManager.createNamedQuery("findAllWorkRealized");
        assertThat(query.getResultList()).containsExactly(workRealized, workRealized2);

    }


    @Test
    public void test_namedQuery_FindAllWorkRealizedByMember() throws Exception {

        WorkRealized workRealized = createWorkRealized();
        transaction.begin();
        WorkRealized workRealized2 = createWorkRealized();

        transaction.begin();
        WorkRealized workRealizedWithoutMember = EntityFactory.createWorkRealized();
        workRealizedWithoutMember.setMember(null);
        workRealizedWithoutMember.getTask().setMembers(null);
        persist(workRealizedWithoutMember.getTask());
        persist(workRealizedWithoutMember);
        commit();

        Query query = entityManager.createNamedQuery("findAllWorkRealizedByMember");
        query.setParameter("fMemberId", workRealized.getMember().getId());
        assertThat(query.getResultList()).containsExactly(workRealized);

    }
}