package fr.oltruong.teamag.entity;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberIT extends AbstractEntityIT {

    @Test
    public void testCreation() {
        Member member = EntityFactory.createMember();
        getEntityManager().persist(member);
        assertThat(member.getId()).isNotNull();
    }

    @Test(expected = PersistenceException.class)
    public void testException() {
        Member member = EntityFactory.createMember();
        member.setEmail(null);
        getEntityManager().persist(member);
        getTransaction().commit();
    }

    @Test
    public void testFindMembers() {

        Member member = EntityFactory.createMember();
        getEntityManager().persist(member);

        getTransaction().commit();
        @SuppressWarnings("unchecked")
        List<Member> listMembers = getEntityManager().createNamedQuery("findMembers").getResultList();

        assertThat(listMembers).isNotNull().isNotEmpty();


    }

    @Test
    public void testFindMemberByNameAndPassword() throws Exception {
        Member member = EntityFactory.createMember();
        getEntityManager().persist(member);
        getTransaction().commit();

        Query query = getEntityManager().createNamedQuery("findByNamePassword");
        query.setParameter("fname", member.getName());
        query.setParameter("fpassword", Hashing.sha256().hashString("toto", Charsets.UTF_8).toString());

        List<Member> memberList = query.getResultList();
        assertThat(memberList).isNotEmpty().hasSize(1).contains(member);

        query.setParameter("fpassword", "sss");

        assertThat(query.getResultList()).isEmpty();


    }


}
