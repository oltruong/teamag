package fr.oltruong.teamag.entity;

import javax.persistence.*;

@Table(name = "TM_BUSINESSCASE")
@Entity

@NamedQueries({@NamedQuery(name = "findAllBC", query = "SELECT b from BusinessCase b order by b.identifier"), @NamedQuery(name = "findBCByNumber", query = "SELECT b FROM BusinessCase b WHERE b.identifier=:fidentifier")})

public class BusinessCase {


    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String identifier;

    @Column(nullable = false)
    private String name;

    @Column
    private String comment;

    @Column
    private Float amount = 0f;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAmount() {
        return this.amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
