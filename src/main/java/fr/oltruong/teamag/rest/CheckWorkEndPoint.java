package fr.oltruong.teamag.rest;

import com.google.common.base.Function;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Table;
import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.interfaces.SupervisorChecked;
import fr.oltruong.teamag.webbean.WorkWebBean;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("checkWork")
@SupervisorChecked
@Stateless
public class CheckWorkEndPoint extends AbstractEndPoint {

    @Inject
    private WorkEJB workEJB;


    @GET
    @Path("/weekComment/{memberId}/{weekNumber}")
    public Response getWeekComment(@PathParam("memberId") Long memberId, @PathParam("weekNumber") int weekNumber) {
        return buildResponseOK(workEJB.findWeekComment(memberId, weekNumber, 2014));
    }

    @GET
    @Path("/byWeek/{memberId}/{weekNumber}/{macroTask}")
    public Response getWeekInformation(@PathParam("memberId") Long memberId, @PathParam("weekNumber") int weekNumber, @PathParam("macroTask") boolean macroTask) {


        List<Work> workList = workEJB.findWorksList(memberId, weekNumber);
        if (macroTask) {
            workList = transformMacro(workList);
        }

        List<WorkWebBean> workWebBeanList = transform(workList);

        return buildResponseOK(workWebBeanList);
    }

    private List<Work> transformMacro(List<Work> workList) {
        List<Work> workListTransformed = null;
        if (workList != null) {
            workListTransformed = Lists.newArrayListWithExpectedSize(workList.size());

            for (Work work : workList) {
                while (work.getTask().getTask() != null) {
                    work.setTask(work.getTask().getTask());
                }
                workListTransformed.add(work);

            }

        }

        //Eliminate double
        Table<Task, DateTime, Work> workTable = HashBasedTable.create();


        for (Work work : workListTransformed) {
            if (workTable.get(work.getTask(), work.getDay()) == null) {
                workTable.put(work.getTask(), work.getDay(), work);
            } else {
                Work existingWork = workTable.get(work.getTask(), work.getDay());
                existingWork.setTotal(existingWork.getTotal() + work.getTotal());
                workTable.put(work.getTask(), work.getDay(), existingWork);
            }
        }

        return Lists.newArrayList(workTable.values());

    }


    private List<WorkWebBean> transform(List<Work> workList) {
        List<WorkWebBean> workWebBeanList = Lists.newArrayListWithExpectedSize(workList.size());

        for (Work work : workList) {
            WorkWebBean workWebBean = new WorkWebBean();
            workWebBean.setAmount(work.getTotal());
            workWebBean.setDay(work.getDay().toDate());
            workWebBean.setTask(work.getTask().getDescription());
            if (work.getTask().getActivity() != null) {
                workWebBean.setTask(work.getTask().getActivity().getName() + "-" + workWebBean.getTask());
            }

            workWebBeanList.add(workWebBean);

        }


        Collections.sort(workWebBeanList, Ordering.natural().onResultOf(
                new Function<WorkWebBean, String>() {
                    public String apply(WorkWebBean from) {
                        return String.valueOf(from.getDay().getTime());
                    }
                }
        ));

        return workWebBeanList;

    }

}
