package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.service.WorkLoadService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class BusinessCaseTest {


    @Test
    public void testGetSummary() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        assertThat(businessCase.getSummary()).isEqualTo(businessCase.getIdentifier() + "-" + businessCase.getName());
    }
}
