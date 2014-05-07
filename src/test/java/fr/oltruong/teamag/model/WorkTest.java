package fr.oltruong.teamag.model;

import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkTest {

    @Test
    public void testSetTotalStr() {
        Work work = new Work();

        TestUtils.setPrivateAttribute(work, LoggerFactory.getLogger(Work.class.getName()), "logger");

        work.setTotal(1.1d);

        assertThat(work.getTotalEdit()).isEqualTo(Double.valueOf(1.1d));

        work.setTotalEditStr("5");
        assertThat(work.getTotalEdit()).isEqualTo(Double.valueOf(5f));

        work.setTotalEditStr("sdqsd");
        assertThat(work.getTotalEdit()).isEqualTo(Double.valueOf(5f));

        work.setTotalEditStr(" ");
        assertThat(work.getTotalEdit()).isEqualTo(Double.valueOf(0f));

    }

}
