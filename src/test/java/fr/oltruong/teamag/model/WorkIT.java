package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.builder.EntityFactory;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkIT extends AbstractEntityIT {
    @Test
    public void testCreation() {
        Work work = EntityFactory.createWork();


        entityManager.persist(work.getTask().getMembers().get(0));
        work.setMember(work.getTask().getMembers().get(0));
        entityManager.persist(work.getTask());

        entityManager.persist(work);

        transaction.commit();


        assertThat(work.getId()).isNotNull();
    }

    @Test
    public void testNamedQuery() {

        Work work = EntityFactory.createWork();

        entityManager.persist(work.getTask().getMembers().get(0));
        work.setMember(work.getTask().getMembers().get(0));
        entityManager.persist(work.getTask());

        entityManager.persist(work);

        transaction.commit();

        Query query = entityManager.createNamedQuery("findWorksByMemberMonth");
        query.setParameter("fmemberId", work.getMember().getId());
        query.setParameter("fmonth", DateTime.now().withDayOfMonth(1));

        @SuppressWarnings("unchecked")
        List<Work> listWorks = query.getResultList();

        assertThat(listWorks).isNotNull().isNotEmpty();


    }


    @Test
    public void testNamedQuery_findWorkDaysByMemberMonthNotNull() {

        Work work = EntityFactory.createWork();

        entityManager.persist(work.getTask().getMembers().get(0));
        work.setMember(work.getTask().getMembers().get(0));
        entityManager.persist(work.getTask());

        entityManager.persist(work);

        transaction.commit();

        Query query = entityManager.createNamedQuery("findWorkDaysByMemberMonth");
        query.setParameter("fmemberId", work.getMember().getId());
        query.setParameter("fmonth", DateTime.now().withDayOfMonth(1));

        List<Object[]> objects = query.getResultList();

        assertThat(objects).isNotNull().isNotEmpty();

        assertThat(objects.get(0)[0]).isNotNull().isEqualTo(work.getDay().withTimeAtStartOfDay());
        assertThat(objects.get(0)[1]).isNotNull().isEqualTo(work.getTotal());


    }

}
