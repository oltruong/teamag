package fr.oltruong.teamag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table( name = "TM_APP_PARAMETER" )
@Entity
public class ApplicationParameter
{
    @Id
    @GeneratedValue
    private Long id;

    @Column( nullable = false )
    private String smtpHost;

    @Column( nullable = false )
    private String administratorEmail;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getSmtpHost()
    {
        return smtpHost;
    }

    public void setSmtpHost( String smtpHost )
    {
        this.smtpHost = smtpHost;
    }

    public String getAdministratorEmail()
    {
        return administratorEmail;
    }

    public void setAdministratorEmail( String administratorEmail )
    {
        this.administratorEmail = administratorEmail;
    }

}
