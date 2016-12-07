package com.oltruong.teamag.model;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.oltruong.teamag.model.builder.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberIT extends AbstractEntityIT {

    @Test
    public void creation() {
        Member member = EntityFactory.createMember();
        entityManager.persist(member);
        assertThat(member.getId()).isNotNull();

        Member memberDB = entityManager.find(Member.class, member.getId());

        assertThat(memberDB).isEqualToComparingFieldByField(member).isEqualTo(member);
    }

    @Test(expected = PersistenceException.class)
    public void exception() {
        Member member = EntityFactory.createMember();
        member.setEmail(null);
        entityManager.persist(member);
        transaction.commit();
    }

    @Test
    public void findMembers() {

        Member member = EntityFactory.createMember();
        entityManager.persist(member);

        transaction.commit();
        @SuppressWarnings("unchecked")
        List<Member> listMembers = entityManager.createNamedQuery("Member.FIND_ALL").getResultList();

        Assertions.assertThat(listMembers).isNotNull().isNotEmpty();


    }



}
