package com.oltruong.teamag.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class TeamagUtilsTest {

    @Test
    public void testHashPassword() throws Exception {

        assertThat(TeamagUtils.hashPassword("")).isEqualTo("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        assertThat(TeamagUtils.hashPassword("toto256")).isEqualTo("20d7944f68f3de2c7c93a9c56ba82b3caf62fe5d00cea94286c37d4df0f6fa8f");
    }

    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(TeamagUtils.class);
    }

}
