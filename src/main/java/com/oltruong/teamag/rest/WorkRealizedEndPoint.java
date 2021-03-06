package com.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.WorkRealized;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.TaskService;
import com.oltruong.teamag.service.WorkRealizedService;
import com.oltruong.teamag.webbean.TaskWebBean;
import com.oltruong.teamag.webbean.WorkRealizedWrapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;


@Path("workrealized")
@Stateless
public class WorkRealizedEndPoint extends AbstractEndPoint<WorkRealized> {

    @Inject
    private WorkRealizedService workRealizedService;


    @Inject
    private TaskService taskService;


    private Response getResponse(List workRealizedList) {
        Response response;
        if (workRealizedList == null || workRealizedList.isEmpty()) {
            response = noContent();
        } else {
            response = ok(workRealizedList);
        }
        return response;
    }

    private List<WorkRealizedWrapper> buildWorkRealizedWrapper(List<WorkRealized> workRealizedList) {

        List<WorkRealizedWrapper> workRealizedWrapperList = Lists.newArrayList();


        List<Task> tasks = taskService.findTaskWithActivity();
        if (tasks != null && !tasks.isEmpty()) {
            tasks.forEach(task -> workRealizedWrapperList.add(new WorkRealizedWrapper(transformTask(task))));
        }

        if (workRealizedList != null && !workRealizedList.isEmpty()) {


            for (WorkRealized workRealized : workRealizedList) {
                WorkRealizedWrapper workRealizedWrapper = findWrapper(workRealizedWrapperList, workRealized.getTaskId());
                if (workRealizedWrapper == null) {
                    Task task = taskService.find(workRealized.getTaskId());

                    workRealizedWrapper = new WorkRealizedWrapper(transformTask(task));
                    workRealizedWrapperList.add(workRealizedWrapper);
                }
                workRealizedWrapper.addWorkRealized(workRealized);


            }

        }


        workRealizedWrapperList.forEach(workRealizedWrapper -> {
            List<WorkRealized> workRealizedList2 = workRealizedWrapper.getWorkRealizedList();
            int currentYear = LocalDate.now().getYear();
            for (Month month : Month.values()) {
                if (!contains(workRealizedList2, month)) {
                    WorkRealized workRealized = new WorkRealized();
                    workRealized.setMonth(month.getValue());
                    workRealized.setYear(currentYear);
                    workRealized.setTaskId(workRealizedWrapper.getTask().getId());
                    workRealizedList2.add(workRealized);
                }
            }


        });

        return workRealizedWrapperList;

    }

    private boolean contains(List<WorkRealized> workRealizedList2, Month month) {
        if (workRealizedList2 != null && !workRealizedList2.isEmpty()) {
            for (WorkRealized workRealized : workRealizedList2) {
                if (month.getValue() == Integer.valueOf(workRealized.getMonth())) {
                    return true;
                }
            }
        }
        return false;
    }

    private WorkRealizedWrapper findWrapper(List<WorkRealizedWrapper> workRealizedWrapperList, Long taskId) {

        for (WorkRealizedWrapper workRealized : workRealizedWrapperList) {
            if (workRealized.getTask().getId().equals(taskId)) {
                return workRealized;
            }
        }
        return null;
    }

    public TaskWebBean transformTask(Task task) {
        TaskWebBean taskWebBean = new TaskWebBean();
        taskWebBean.setActivity(task.getActivity());
        taskWebBean.setAmount(task.getAmount());
        taskWebBean.setComment(task.getComment());
        taskWebBean.setDelegated(task.getDelegated());
        taskWebBean.setId(task.getId());
        taskWebBean.setName(task.getName());
        taskWebBean.setProject(task.getProject());
        taskWebBean.setTotal(task.getTotal());
        taskWebBean.setDescription(task.getDescription());

        if (task.getTask() != null) {
            taskWebBean.setTask(transformTask(task.getTask()));
        }

        return taskWebBean;
    }

    @GET
    @Path("/member/{memberId}")
    public Response getWorkRealized(@PathParam("memberId") Long memberId) {
        List<WorkRealizedWrapper> workRealizedWrapperList = buildWorkRealizedWrapper(workRealizedService.getWorkRealizedbyMember(memberId));
        workRealizedWrapperList.forEach(workRealizedWrapper -> workRealizedWrapper.getWorkRealizedList().forEach(workRealized -> workRealized.setMemberId(memberId)));
        return getResponse(workRealizedWrapperList);
    }

    @PUT
    public Response updateWorkRealized(List<WorkRealizedWrapper> workRealizedWrapperList) {

        Response response;
        if (workRealizedWrapperList != null) {
            workRealizedWrapperList.forEach(workRealizedWrapper -> {
                workRealizedWrapper.getWorkRealizedList().removeIf(workRealized -> (workRealized.getId() == null && workRealized.getRealized() == 0));
                workRealizedService.createOrUpdate(workRealizedWrapper.getWorkRealizedList());
            });
            response = ok();
        } else {
            response = notAcceptable();
        }

        return response;
    }

    @Override
    AbstractService getService() {
        return workRealizedService;
    }
}
