package fr.oltruong.teamag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "TM_ACTIVITY")
@Entity
@NamedQueries({@NamedQuery(name = "findAllActivities", query = "SELECT a from Activity a order by a.name"),
        @NamedQuery(name = "findActivity", query = "SELECT a from Activity a where a.name=:fname and a.bc=:fbc")})
public class Activity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "BC_FK")
    private BusinessCase bc;


    @Column(nullable = false)
    private Boolean delegated = Boolean.FALSE;


    private Double amount;

    private String comment;

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

    public BusinessCase getBc() {
        return bc;
    }

    public void setBc(BusinessCase bc) {
        this.bc = bc;
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
