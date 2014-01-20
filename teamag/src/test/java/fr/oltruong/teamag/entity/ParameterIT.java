package fr.oltruong.teamag.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class ParameterIT extends AbstractEntityIT {

    @Test
    public void testCreation() {
        Parameter parameter = EntityFactory.createParameter();
        assertThat(parameter.getId()).isNull();
        getEntityManager().persist(parameter);
        getTransaction().commit();

        assertThat(parameter.getId()).isNotNull();
    }
}
