package fr.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.WorkLoad;
import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.service.WorkLoadService;
import fr.oltruong.teamag.webbean.WorkLoadContainer;
import fr.oltruong.teamag.webbean.WorkLoadMemberContainer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("workload")
@Stateless
@AdminChecked
public class WorkLoadEndPoint extends AbstractEndPoint {

    @Inject
    WorkLoadService workLoadService;


    @GET
    public Response getWorkLoad() {

        return Response.ok(buildWorkLoadContainer()).build();
    }

    private WorkLoadMemberContainer buildWorkLoadContainer() {
        WorkLoadMemberContainer workLoadContainer = new WorkLoadMemberContainer();
        workLoadContainer.setMemberList(MemberService.getMemberList());


        List<WorkLoad> workLoadList = workLoadService.findOrCreateAllWorkLoad();
        BusinessCase businessCase = null;
        WorkLoadContainer currentContainer = null;

        for (WorkLoad workLoad : workLoadList) {


            if (!workLoad.getBusinessCase().equals(businessCase)) {
                businessCase = workLoad.getBusinessCase();

                WorkLoadContainer container = new WorkLoadContainer();
                container.setBusinessCase(businessCase);

                List<WorkLoad> workLoads = Lists.newArrayList();
                workLoads.add(workLoad);
                container.setWorkLoadList(workLoads);
                workLoadContainer.addWorkLoadContainer(container);

                currentContainer = container;
            } else {
                List<WorkLoad> workLoads = currentContainer.getWorkLoadList();
                workLoads.add(workLoad);
                currentContainer.setWorkLoadList(workLoads);
            }
        }
        return workLoadContainer;
    }


    @PUT
    public Response updateWorkLoad(List<WorkLoadContainer> workLoadContainerList) {

        if (workLoadContainerList != null) {
            workLoadContainerList.forEach(workLoadContainer -> workLoadService.updateWorkLoad(workLoadContainer.getWorkLoadList()));
        }

        return buildResponseOK();
    }


}
