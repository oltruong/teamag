package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class TaskTest {

    @Test
    public void testClone() {

        Task task = EntityFactory.createTask();
        task.setId(Long.valueOf(LocalDate.now().getMonthValue()));
        Task taskCloned = task.clone();

        assertThat(taskCloned).isEqualToIgnoringGivenFields(task, "members").isEqualTo(task);
    }
}
