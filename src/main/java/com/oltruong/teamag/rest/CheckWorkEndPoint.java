package com.oltruong.teamag.rest;

import com.google.common.base.Function;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Table;
import com.oltruong.teamag.interfaces.SupervisorChecked;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.Work;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.WorkService;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.webbean.WorkWebBean;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("checkWork")
@SupervisorChecked
@Stateless
public class CheckWorkEndPoint extends AbstractEndPoint<Work> {

    @Inject
    private WorkService workService;


    private int parseWeekNumber(int weekNumber) {

        int weekNumberResult = weekNumber;
        if (weekNumberResult == -1) {
            weekNumberResult = CalendarUtils.getCurrentWeekNumber();
        }
        return weekNumberResult;
    }

    @GET
    @Path("/byWeek/{memberId}/{weekNumber}/{macroTask}")
    public Response getWeekInformation(@PathParam("memberId") Long memberId, @PathParam("weekNumber") int weekNumber, @PathParam("macroTask") boolean macroTask) {

        weekNumber = parseWeekNumber(weekNumber);

        List<Work> workList = workService.findWorksNotNullByWeek(memberId, weekNumber);
        if (macroTask) {
            workList = transformMacro(workList);
        }

        List<WorkWebBean> workWebBeanList = transform(workList);

        return ok(workWebBeanList);
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
        Table<Task, LocalDate, Work> workTable = HashBasedTable.create();


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
            workWebBean.setDay(Date.from(work.getDay().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
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

    @Override
    AbstractService getService() {
        return workService;
    }
}
