package fr.oltruong.teamag.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slf4j.LoggerFactory;

public class WorkTest {

    @Test
    public void testSetTotalStr() {
        Work work = new Work();
        work.setLogger(LoggerFactory.getLogger(Work.class.getName()));
        work.setTotal(1.1f);

        assertThat(work.getTotalEdit()).isEqualTo(Float.valueOf(1.1f));

        work.setTotalEditStr("5");
        assertThat(work.getTotalEdit()).isEqualTo(Float.valueOf(5f));

        work.setTotalEditStr("sdqsd");
        assertThat(work.getTotalEdit()).isEqualTo(Float.valueOf(5f));

        work.setTotalEditStr(" ");
        assertThat(work.getTotalEdit()).isEqualTo(Float.valueOf(0f));

    }

}
