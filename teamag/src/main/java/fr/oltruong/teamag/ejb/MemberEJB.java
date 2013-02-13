package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;

@Stateless
public class MemberEJB
{
    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    // ======================================
    // = Public Methods =
    // ======================================

    @SuppressWarnings( "unchecked" )
    public List<Member> findMembers()
    {
        Query query = em.createNamedQuery( "findMembers" );
        return query.getResultList();
    }

    public Member findByName( String name )
    {
        Query query = em.createNamedQuery( "findByName" );
        query.setParameter( "fname", name );
        @SuppressWarnings( "unchecked" )
        List<Member> liste = query.getResultList();
        if ( liste == null || liste.isEmpty() )
        {
            return null;
        }
        else
        {
            return liste.get( 0 );
        }
    }

    @SuppressWarnings( "unchecked" )
    public Member createMember( Member member )
    {

        System.out.println( "Recherche de la tâche absence" );

        // Adding default task
        Query query = em.createNamedQuery( "findTaskByName" );
        query.setParameter( "fname", "Absence" );
        query.setParameter( "fproject", "" );

        Task task = null;
        List<Task> tasks = query.getResultList();

        if ( tasks != null && !tasks.isEmpty() )
        {
            System.out.println( "Tâche trouvée." );
            task = tasks.get( 0 );
        }

        if ( task == null )
        {
            System.out.println( "Tâche non trouvée. Elle est créée" );
            Task newTask = new Task();
            newTask.setName( "Absence" );
            em.persist( newTask );
            task = newTask;
        }

        em.persist( member );

        task.addMember( member );
        em.persist( task );

        return member;
    }

}
