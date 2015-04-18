package com.oltruong.teamag.model;

import com.oltruong.teamag.model.builder.EntityFactory;
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
        entityManager.persist(parameter);
        transaction.commit();

        assertThat(parameter.getId()).isNotNull();
    }
}
