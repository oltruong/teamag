package fr.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.ejb.WorkLoadEJB;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.entity.WorkLoad;
import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.webbean.WorkLoadContainer;
import fr.oltruong.teamag.webbean.WorkLoadMemberContainer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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

    @EJB
    WorkLoadEJB workLoadEJB;


    @GET
    public Response getWorkLoad() {

        return Response.ok(buildWorkLoadContainer()).build();
    }

    private WorkLoadMemberContainer buildWorkLoadContainer() {
        WorkLoadMemberContainer workLoadContainer = new WorkLoadMemberContainer();
        workLoadContainer.setMemberList(MemberEJB.getMemberList());


        List<WorkLoad> workLoadList = workLoadEJB.findAllWorkLoad();
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

        for (WorkLoadContainer workLoadContainer : workLoadContainerList) {
            workLoadEJB.updateWorkLoad(workLoadContainer.getWorkLoadList());
        }

        return buildResponseOK();
    }


}
