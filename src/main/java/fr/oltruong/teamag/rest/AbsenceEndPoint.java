package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.transformer.AbsenceWebBeanTransformer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("absence")
@Stateless
//@SecurityChecked
public class AbsenceEndPoint extends AbstractEndPoint {

    @EJB
    AbsenceEJB absenceEJB;


    @GET
    @Path("/{id}")
    public Response getAbsences(@PathParam("id") Long memberId) {
        return buildResponseOK(AbsenceWebBeanTransformer.transformList(absenceEJB.findAbsencesByMemberId(memberId)));
    }


}
