package fr.oltruong.teamag.entity;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class WorkLoadIT extends AbstractEntityIT {

    @Before
    public void prepareTest() {
        super.setup();
    }

    @Test
    public void testCreation() {
        WorkLoad workLoad = EntityFactory.createWorkLoad();

        assertThat(workLoad.getId()).isNull();

        persist(workLoad.getBusinessCase());
        persist(workLoad.getMember());

        persist(workLoad);
        commit();

        assertThat(workLoad.getId()).isNotNull();


        WorkLoad workLoadDB = getEntityManager().find(WorkLoad.class, workLoad.getId());


        assertThat(workLoadDB).isEqualTo(workLoad);
    }
}
