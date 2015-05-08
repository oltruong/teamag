package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.WeekCommentService;
import java.time.LocalDate;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * @author oltruong
 */
@Path("weekComment")
@SecurityChecked
@Stateless
public class WeekCommentEndPoint extends AbstractEndPoint<WeekComment> {

    @Inject
    private WeekCommentService weekCommentService;


    @GET
    public Response getWeekComment(@QueryParam("memberId") Long memberId, @QueryParam("weekNumber") int weekNumber) {
        return get(() -> weekCommentService.findWeekComment(memberId, weekNumber, LocalDate.now().getYear()));
    }


    @Override
    AbstractService getService() {
        return weekCommentService;
    }
}
