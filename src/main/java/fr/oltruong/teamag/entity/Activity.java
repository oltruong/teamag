package fr.oltruong.teamag.entity;

import javax.persistence.*;

@Table(name = "TM_ACTIVITY")
@Entity
@NamedQueries({@NamedQuery(name = "findAllActivities", query = "SELECT a from Activity a order by a.bc.identifier, a.name"),
        @NamedQuery(name = "findActivity", query = "SELECT a from Activity a where a.name=:fname and a.bc=:fbc")})
public class Activity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "BC_FK")
    private BusinessCase bc;

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

}
