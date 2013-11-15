package fr.oltruong.teamag.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class ParameterIT extends AbstractEntityIT {

    @Test
    public void testCreation() {
        Parameter parameter = new Parameter();
        parameter.setName(ParameterName.ADMINISTRATOR_EMAIL);
        parameter.setValue("toto");

        getEntityManager().persist(parameter);
        getTransaction().commit();

        assertThat(parameter.getId()).isNotNull();
    }
}
