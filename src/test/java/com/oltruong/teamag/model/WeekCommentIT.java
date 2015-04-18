package com.oltruong.teamag.model;

import com.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class WeekCommentIT extends AbstractEntityIT {

    @Test
    public void testCreation() {
        WeekComment weekComment = EntityFactory.createWeekComment();
        assertThat(weekComment.getId()).isNull();
        entityManager.persist(weekComment.getMember());
        entityManager.persist(weekComment);
        transaction.commit();
        assertThat(weekComment.getId()).isNotNull();

        WeekComment weekCommentFound = entityManager.find(WeekComment.class, weekComment.getId());

        assertThat(weekCommentFound).isEqualToComparingFieldByField(weekComment);
    }

    @Test(expected = PersistenceException.class)
    public void testCreation_noMember() {
        WeekComment weekComment = EntityFactory.createWeekComment();
        weekComment.setMember(null);
        assertThat(weekComment.getId()).isNull();
        entityManager.persist(weekComment);
    }
}
