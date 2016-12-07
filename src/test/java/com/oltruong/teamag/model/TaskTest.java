package com.oltruong.teamag.model;

import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.model.enumeration.MemberType;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TaskTest {


    private Task task;

    @Before
    public void setup() {
        task = EntityFactory.createTask();
    }

    @Test
    public void testClone() {

        task.setId(EntityFactory.createRandomLong());
        task.setActivity(EntityFactory.createActivity());
        Task taskCloned = task.clone();

        assertThat(taskCloned).isEqualToIgnoringGivenFields(task, "members", "comment", "amount", "activity").isEqualTo(task);
        assertThat(taskCloned.compareTo(task)).isEqualTo(0);
    }

    @Test
    public void testEquals() {
        assertThat(task.equals(Integer.valueOf(0))).isFalse();
    }

    @Test
    public void addTotal() {
        task.setTotal(null);
        task.addTotal(Double.valueOf(1.85d));
        assertThat(task.getTotal()).isEqualTo(1.85d);
        task.addTotal(Double.valueOf(0.15d));
        assertThat(task.getTotal()).isEqualTo(2d);
    }

    @Test
    public void isNonAdminNoMember() {
        task.setMembers(null);
        assertThat(task.isNonAdmin()).isFalse();
    }

    @Test
    public void isAdminNoMember() {
        task.getMembers().clear();
        assertThat(task.isAdmin()).isTrue();
    }

    @Test
    public void isAdmin() {
        task.getMembers().clear();
        Member member = EntityFactory.createMember();
        member.setMemberType(MemberType.BASIC);
        task.addMember(member);
        assertThat(task.isAdmin()).isFalse();
    }


    @Test
    public void isAbsence() {

        assertThat(testTaskAdmin(null, null)).isFalse();
        assertThat(testTaskAdmin(null, "Absence")).isFalse();
        assertThat(testTaskAdmin("hello", "Absence")).isFalse();
        assertThat(testTaskAdmin("", "Absence")).isTrue();


    }

    private boolean testTaskAdmin(String project, String name) {

        Task myTask = new Task();
        myTask.setProject(project);
        myTask.setName(name);
        return (myTask.isAbsenceTask());
    }


}
