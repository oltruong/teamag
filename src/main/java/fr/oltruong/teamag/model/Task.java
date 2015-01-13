package fr.oltruong.teamag.model;

import com.google.common.collect.Lists;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "TM_TASK")
@NamedQueries({@NamedQuery(name = "Task.FIND_ALL", query = "SELECT t from Task t order by t.name, t.project"), @NamedQuery(name = "findTaskByProject", query = "SELECT t from Task t where t.project=:fproject"),
        @NamedQuery(name = "findAllTasksWithActivity", query = "SELECT t from Task t where t.activity is not null order by t.name, t.project"),
        @NamedQuery(name = "findTaskByName", query = "SELECT t from Task t where (t.name=:fname and t.project=:fproject)"), @NamedQuery(name = "Task.FIND_NONTYPE", query = "SELECT t from Task t JOIN t.members m WHERE m NOT IN (select m from Member where m.memberType=:memberType)"), @NamedQuery(name = "Task.FIND_MEMBER", query = "SELECT t from Task t JOIN t.members m WHERE m.id=:memberId")})
@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String project = "";

    @ManyToMany
    private List<Member> members = Lists.newArrayListWithCapacity(1);


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TASK_PARENT_FK")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_FK")
    private Activity activity;

    @Column(nullable = false)
    private Boolean delegated = Boolean.FALSE;

    private String comment;

    private Double amount;

    @Transient
    private Double total;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
        }
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void addTotal(Double value) {
        if (total == null) {
            total = value;
        } else {
            total += value;
        }
    }

    public int compareTo(Task task) {

        return (project + name).compareTo(task.project + task.name);
    }


    public boolean isNonAdmin() {
        return !isAdmin();
    }

    public boolean isAdmin() {
        boolean verdict = false;
        if (this.getMembers() != null && !this.getMembers().isEmpty()) {
            for (Member member : this.getMembers()) {
                verdict |= member.isAdministrator();
            }
        } else {
            verdict = true;
        }
        return verdict;
    }

    @Override
    public boolean equals(Object otherTask) {
        if (!(otherTask instanceof Task)) {
            return false;
        }
        Task member0 = (Task) otherTask;
        return this.id.equals(member0.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public Task clone() {
        Task cloneTask = new Task();
        cloneTask.setId(id);
        cloneTask.setName(name);
        cloneTask.setProject(project);
        return cloneTask;

    }


    public String getDescription() {
        return this.getProject() + "-" + this.getName();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Boolean getDelegated() {
        return delegated;
    }

    public void setDelegated(Boolean delegated) {
        this.delegated = delegated;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
