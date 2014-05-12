package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.model.WorkRealized;
import fr.oltruong.teamag.service.WorkRealizedService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("workrealized")
@Stateless
public class WorkRealizedEndPoint extends AbstractEndPoint {

    @Inject
    private WorkRealizedService workRealizedService;

    @GET
    public Response getWorkRealized() {
        List<WorkRealized> workRealizedList = workRealizedService.getAllWorkRealized();
        return getResponse(workRealizedList);
    }

    private Response getResponse(List<WorkRealized> workRealizedList) {
        Response response = null;
        if (workRealizedList == null || workRealizedList.isEmpty()) {
            response = buildResponseNoContent();
        } else {
            response = buildResponseOK(workRealizedList);
        }
        return response;
    }

    @GET
    @Path("/{memberId}")
    public Response getWorkRealized(@PathParam("memberId") Long memberId) {
        return getResponse(workRealizedService.getWorkRealizedbyMember(memberId));
    }

}
