package com.oltruong.teamag.model;

import com.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ParameterIT extends AbstractEntityIT {

    @Test
    public void creation() {
        Parameter parameter = EntityFactory.createParameter();
        assertThat(parameter.getId()).isNull();
        entityManager.persist(parameter);
        transaction.commit();

        assertThat(parameter.getId()).isNotNull();
    }
}
