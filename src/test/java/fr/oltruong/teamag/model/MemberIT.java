package fr.oltruong.teamag.model;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import fr.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberIT extends AbstractEntityIT {

    @Test
    public void testCreation() {
        Member member = EntityFactory.createMember();
        entityManager.persist(member);
        assertThat(member.getId()).isNotNull();

        Member memberDB = entityManager.find(Member.class, member.getId());

        assertThat(memberDB).isEqualToComparingFieldByField(member).isEqualTo(member);
    }

    @Test(expected = PersistenceException.class)
    public void testException() {
        Member member = EntityFactory.createMember();
        member.setEmail(null);
        entityManager.persist(member);
        transaction.commit();
    }

    @Test
    public void testFindMembers() {

        Member member = EntityFactory.createMember();
        entityManager.persist(member);

        transaction.commit();
        @SuppressWarnings("unchecked")
        List<Member> listMembers = entityManager.createNamedQuery("findMembers").getResultList();

        assertThat(listMembers).isNotNull().isNotEmpty();


    }

    @Test
    public void testFindMemberByNameAndPassword() throws Exception {
        Member member = EntityFactory.createMember();
        entityManager.persist(member);
        transaction.commit();

        Query query = entityManager.createNamedQuery("findByNamePassword");
        query.setParameter("fname", member.getName());
        query.setParameter("fpassword", Hashing.sha256().hashString("toto", Charsets.UTF_8).toString());

        List<Member> memberList = query.getResultList();
        assertThat(memberList).isNotEmpty().hasSize(1).contains(member);

        query.setParameter("fpassword", "sss");

        assertThat(query.getResultList()).isEmpty();


    }


}
