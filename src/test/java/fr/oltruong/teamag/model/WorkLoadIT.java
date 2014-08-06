package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class WorkLoadIT extends AbstractEntityIT {


    @Test
    public void testCreation() {
        WorkLoad workLoad = EntityFactory.createWorkLoad();

        assertThat(workLoad.getId()).isNull();

        persist(workLoad.getBusinessCase());
        persist(workLoad.getMember());

        persist(workLoad);
        commit();

        assertThat(workLoad.getId()).isNotNull();


        WorkLoad workLoadDB = entityManager.find(WorkLoad.class, workLoad.getId());


        assertThat(workLoadDB).isEqualToComparingFieldByField(workLoad).isEqualTo(workLoad);
    }
}
