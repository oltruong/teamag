package com.oltruong.teamag.model;

import com.oltruong.teamag.model.builder.EntityFactory;
import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkIT extends AbstractEntityIT {

    private Work work;

    @Before
    public void prepare() {
        work = EntityFactory.createWork();
        entityManager.persist(work.getTask().getMembers().get(0));
        work.setMember(work.getTask().getMembers().get(0));
        entityManager.persist(work.getTask());
        entityManager.persist(work);
    }

    @Test
    public void testCreation() {
        transaction.commit();
        assertThat(work.getId()).isNotNull();
    }


    @Test
    public void testNamedQuery() {
        transaction.commit();

        Query query = entityManager.createNamedQuery("Work.FIND_BY_MEMBER_MONTH");
        query.setParameter("fmemberId", work.getMember().getId());
        query.setParameter("fmonth", DateTime.now().withDayOfMonth(1));

        @SuppressWarnings("unchecked")
        List<Work> listWorks = query.getResultList();

        Assertions.assertThat(listWorks).isNotNull().isNotEmpty();


    }


    @Test
    public void testNamedQuery_findWorkdaysByMemberMonth() {
        transaction.commit();

        Query query = entityManager.createNamedQuery("Work.FIND_WORKDAYS_BY_MEMBER_MONTH");
        query.setParameter("fmemberId", work.getMember().getId());
        query.setParameter("fmonth", DateTime.now().withDayOfMonth(1));

        List<Object[]> objects = query.getResultList();

        assertThat(objects).isNotNull().isNotEmpty();

        assertThat(objects.get(0)[0]).isNotNull().isEqualTo(work.getDay().withTimeAtStartOfDay());
        assertThat(objects.get(0)[1]).isNotNull().isEqualTo(work.getTotal());


    }


    @Test
    public void testNamedQuery_findTaskByMemberMonth() {
        Work work2 = EntityFactory.createWork();
        work2.setTask(work.getTask());
        work2.setMember(work.getMember());

        entityManager.persist(work2);


        Work work3 = EntityFactory.createWork();
        work3.getTask().getMembers().clear();
        work3.getTask().getMembers().add(work.getTask().getMembers().get(0));
        work3.setMember(work.getMember());

        entityManager.persist(work3.getTask());
        entityManager.persist(work3);
        transaction.commit();

        Query query = entityManager.createNamedQuery("Work.FIND_TASKS_BY_MEMBER_MONTH");
        query.setParameter("fmemberId", work.getMember().getId());
        query.setParameter("fmonth", DateTime.now().withDayOfMonth(1));

        List<Object[]> objects = query.getResultList();

        assertThat(objects).isNotNull().isNotEmpty();

        assertThat(objects.get(0)[0]).isNotNull().isEqualTo(work.getTask());
        assertThat(objects.get(0)[1]).isNotNull().isEqualTo(work.getTotal() + work2.getTotal());
        assertThat(objects.get(1)[0]).isNotNull().isEqualTo(work3.getTask());
        assertThat(objects.get(1)[1]).isNotNull().isEqualTo(work3.getTotal());


    }

    @Test
    public void testNamedQuery_findTaskByMemberMonth_empty() {
        Work work2 = EntityFactory.createWork();
        work2.setTask(work.getTask());
        work2.setMember(work.getMember());

        entityManager.persist(work2);


        Work work3 = EntityFactory.createWork();
        work3.getTask().getMembers().clear();
        work3.getTask().getMembers().add(work.getTask().getMembers().get(0));
        work3.setMember(work.getMember());

        entityManager.persist(work3.getTask());
        entityManager.persist(work3);
        transaction.commit();

        Query query = entityManager.createNamedQuery("Work.FIND_TASKS_BY_MEMBER_MONTH");
        query.setParameter("fmemberId", work.getMember().getId());
        query.setParameter("fmonth", DateTime.now().withDayOfMonth(1).minusMonths(1));

        List<Object[]> objects = query.getResultList();

        assertThat(objects).isNotNull().isEmpty();

    }

    @Test
    public void testNamedQuery_findTaskByMember() {
        Work work2 = EntityFactory.createWork();
        work2.setTask(work.getTask());
        work2.setMember(work.getMember());

        entityManager.persist(work2);


        Work work3 = EntityFactory.createWork();
        work3.getTask().getMembers().clear();
        work3.getTask().getMembers().add(work.getTask().getMembers().get(0));
        work3.setMember(work.getMember());

        entityManager.persist(work3.getTask());
        entityManager.persist(work3);
        transaction.commit();

        Query query = entityManager.createNamedQuery("Work.FIND_TASKS_BY_MEMBER");
        query.setParameter("fmemberId", work.getMember().getId());

        List<Object[]> objects = query.getResultList();

        assertThat(objects).isNotNull().isNotEmpty();

        assertThat(objects.get(0)[0]).isNotNull().isEqualTo(work.getTask());
        assertThat(objects.get(0)[1]).isNotNull().isEqualTo(work.getTotal() + work2.getTotal());
        assertThat(objects.get(1)[0]).isNotNull().isEqualTo(work3.getTask());
        assertThat(objects.get(1)[1]).isNotNull().isEqualTo(work3.getTotal());


    }

}
