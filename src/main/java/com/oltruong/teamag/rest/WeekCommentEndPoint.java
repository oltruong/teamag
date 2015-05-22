package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.WeekCommentService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * @author oltruong
 */
@Path("weekcomments")
@SecurityChecked
@Stateless
public class WeekCommentEndPoint extends AbstractEndPoint<WeekComment> {

    @Inject
    private WeekCommentService weekCommentService;


    @GET
    public Response getWeekComment(@HeaderParam("userid") Long userId, @QueryParam("memberId") Long memberId, @QueryParam("weekNumber") int weekNumber, @QueryParam("month") int month, @QueryParam("year") int year) {

        if (memberId == null) {
            memberId = userId;
        }

        WeekComment weekComment = weekCommentService.findWeekComment(memberId, weekNumber, month, year);
        if (weekComment != null) {
            return ok(weekComment);
        } else {
            return noContent();
        }
    }


    @Override
    AbstractService getService() {
        return weekCommentService;
    }
}
