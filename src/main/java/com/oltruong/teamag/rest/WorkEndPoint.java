package com.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.interfaces.PATCH;
import com.oltruong.teamag.model.Work;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.WorkService;
import com.oltruong.teamag.webbean.TaskWebBean;
import com.oltruong.teamag.webbean.WorkPatch;
import com.oltruong.teamag.webbean.WorkWebBean;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("works")
@Stateless
@AdminChecked
public class WorkEndPoint extends AbstractEndPoint {

    @Inject
    WorkService workService;

    @GET
    public Response getWorksBySearchCriteria(@HeaderParam("userid") Long memberId, @QueryParam("taskId") Long taskId, @QueryParam("month") int month, @QueryParam("year") int year) {
        List<Work> workList;
        if (taskId != null) {
            workList = workService.findWorkByTask(taskId);
        } else {
            DateTime monthDateTime = new DateTime(year, month, 1, 0, 0);
            workList = workService.findWorkListByMemberMonth(memberId, monthDateTime);
        }

        List<WorkWebBean> workWebBeanList = transform(workList);


        return ok(workWebBeanList);
    }

    @PATCH
    public Response updateMultiple(List<WorkPatch> workWebBeanList) {
        List<Work> workList = Lists.newArrayListWithExpectedSize(workWebBeanList.size());
        for (WorkPatch workWebBean : workWebBeanList) {
            Work work = workService.find(workWebBean.getId());
            if (!work.getTotal().equals(workWebBean.getTotal())) {
                work.setTotal(workWebBean.getTotal());
                workList.add(work);
            }
        }


        workService.mergeList(workList);
        return ok();
    }


    private List<WorkWebBean> transform(List<Work> workList) {
        List<WorkWebBean> workWebBeanList = Lists.newArrayListWithExpectedSize(workList.size());
        for (Work work : workList) {
            WorkWebBean workWebBean = new WorkWebBean();
            workWebBean.setAmount(work.getTotal());
            workWebBean.setDay(work.getDay().toDate());
            workWebBean.setMember(work.getMember().getName());
            TaskWebBean taskWebBean = new TaskWebBean();
            taskWebBean.setId(work.getTask().getId());
            taskWebBean.setProject(work.getTask().getProject());
            taskWebBean.setName(work.getTask().getName());
            workWebBean.setTaskBean(taskWebBean);
            workWebBean.setProject(work.getTask().getProject());
            workWebBean.setId(work.getId());
            workWebBeanList.add(workWebBean);

        }

        return workWebBeanList;

    }


    @Override
    AbstractService getService() {
        return workService;
    }
}
