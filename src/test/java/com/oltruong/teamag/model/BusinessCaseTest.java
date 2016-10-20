package com.oltruong.teamag.model;

import com.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class BusinessCaseTest {


    @Test
    public void getSummary() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        assertThat(businessCase.getSummary()).isEqualTo(businessCase.getIdentifier() + "-" + businessCase.getName());
    }
}
