package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.oltruong.teamag.entity.BusinessCase;

@Stateless
public class ActivityEJB
{

    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    public List<BusinessCase> findBC()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public BusinessCase createBC( BusinessCase bc )
    {
        // TODO Auto-generated method stub
        return null;
    }

}
