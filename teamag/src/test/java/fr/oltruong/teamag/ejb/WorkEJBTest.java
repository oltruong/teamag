package fr.oltruong.teamag.ejb;

import static org.junit.Assert.assertNotNull;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.Member;

public class WorkEJBTest
{
    // ======================================
    // = Attributes =
    // ======================================
    private static EJBContainer ec;

    private static Context ctx;

    // ======================================
    // = Lifecycle Methods =
    // ======================================

    @BeforeClass
    public static void initContainer()
        throws Exception
    {
        ec = EJBContainer.createEJBContainer();
        ctx = ec.getContext();
    }

    @AfterClass
    public static void closeContainer()
        throws Exception
    {
        if ( ec != null )
        {
            ec.close();
        }
    }

    // ======================================
    // = Unit tests =
    // ======================================

    @Test
    public void createMember()
        throws NamingException
    {

        // Creates an instance of member
        Member member = new Member();

        member.setName( "Carot" );
        member.setCompany( "My company" );

        // Looks up for the EJB
        MemberEJB memberEJB = (MemberEJB) ctx.lookup( "java:global/classes/MemberEJB" );

        // Persists the member to the database
        member = memberEJB.createMember( member );
        assertNotNull( "ID should not be null", member.getId() );

        Activity activity = new Activity();
        activity.setName( "my activity!!!" );

        activity.setProject( "my project" );

        activity.addMember( member );

        WorkEJB workEJB = (WorkEJB) ctx.lookup( "java:global/classes/WorkEJB" );

        workEJB.createActivity( activity );

        assertNotNull( "ID should not be null", activity.getId() );
    }
}
