package fr.oltruong.teamag.entity;

import fr.oltruong.teamag.utils.TestUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkIT extends AbstractEntityIT {
    @Test
    public void testCreation() {
        Work work = createWork();


        getEntityManager().persist(work.getTask().getMembers().get(0));
        work.setMember(work.getTask().getMembers().get(0));
        getEntityManager().persist(work.getTask());

        getEntityManager().persist(work);

        getTransaction().commit();


        assertThat(work.getId()).isNotNull();
    }

    @Test
    public void testNamedQuery() {

        Work work = createWork();

        getEntityManager().persist(work.getTask().getMembers().get(0));
        work.setMember(work.getTask().getMembers().get(0));
        getEntityManager().persist(work.getTask());

        getEntityManager().persist(work);

        getTransaction().commit();

        Query query = getEntityManager().createNamedQuery("findWorksByMemberMonth");
        query.setParameter("fmemberId", work.getMember().getId());
        query.setParameter("fmonth", DateTime.now().withDayOfMonth(1));

        @SuppressWarnings("unchecked")
        List<Work> listWorks = query.getResultList();

        assertThat(listWorks).isNotNull().isNotEmpty();


    }

    private Member createMember() {
        Member member = new Member();

        member.setName("Carot" + Calendar.getInstance().getTimeInMillis());
        member.setCompany("my company");
        member.setEmail("email@dummy.com");
        member.setEstimatedWorkDays(0f);
        return member;
    }

    private Task createTask() {
        Task task = new Task();

        task.setName("Activity");
        task.setProject("my project");

        task.addMember(createMember());

        return task;
    }

    private Work createWork() {
        Work work = new Work();

        TestUtils.setPrivateAttribute(work, LoggerFactory.getLogger(Work.class.getName()), "logger");
        work.setMember(createMember());
        work.setDay(DateTime.now());
        work.setTask(createTask());
        work.setMonth(DateTime.now().withDayOfMonth(1));
        return work;
    }
}
