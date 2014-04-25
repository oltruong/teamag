package fr.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.webbean.WorkWebBean;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("checkWork")
public class CheckWorkEndPoint extends AbstractEndPoint {

    @Inject
    private WorkEJB workEJB;

    @GET
    @Path("/byWeek/{memberId}/{weekNumber}/{macroTask}")
    public Response getWeekInformation(@PathParam("memberId") Long memberId, @PathParam("weekNumber") int weekNumber, @PathParam("macroTask") boolean macroTask) {


//        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//        Multimap<Task, Work> weekInformation = workEJB.findWorksNotNull(memberId, weekNumber);
//        System.out.println("AAAAAAAOOOOOOOOOOOOOOOOOOOOOOOOOOO");
//        return buildResponseOK(weekInformation.asMap().keySet());
        System.out.println(macroTask + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return buildResponseOK(transform(workEJB.findWorksList(memberId, weekNumber)));
    }


    private List<WorkWebBean> transform(List<Work> workList) {
        List<WorkWebBean> workWebBeanList = Lists.newArrayListWithExpectedSize(workList.size());

        for (Work work : workList) {
            WorkWebBean workWebBean = new WorkWebBean();
            workWebBean.setAmount(work.getTotal());
            workWebBean.setDay(work.getDay().toDate());
            // workWebBean.setMember(work.getMember().getName());
            workWebBean.setTask(work.getTask().getDescription());

            workWebBeanList.add(workWebBean);

        }

        return workWebBeanList;

    }

}
