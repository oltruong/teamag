package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.PATCH;
import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.service.WeekCommentService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    public Response get(@HeaderParam("userid") Long userId, @QueryParam("memberId") Long memberId, @QueryParam("weekNumber") int weekNumber, @QueryParam("month") int month, @QueryParam("year") int year) {

        if (memberId == null) {
            memberId = userId;
        }
        WeekComment weekComment = weekCommentService.findWeekComment(memberId, weekNumber, month, year);
        if (weekComment != null) {
            return ok(weekComment);
        } else {
            //Not finding a weekcomment should not be an error
            return noContent();
        }
    }

    @POST
    public Response create(@HeaderParam("userid") Long userId, WeekComment weekComment) {
        weekComment.setMember(MemberService.getMember(userId));
        weekCommentService.persist(weekComment);
        return create(weekComment);
    }


    @PATCH
    public Response patch(@HeaderParam("userid") Long userId, @PathParam("id") Long id, WeekComment weekComment) {
        WeekComment weekCommentDb = weekCommentService.find(id);
        if (weekCommentDb == null) {
            return notFound();
        } else {
            if (!weekComment.getMember().getId().equals(userId)) {
                return forbidden();
            } else {
                weekCommentDb.setComment(weekComment.getComment());
                weekCommentService.merge(weekCommentDb);
                return ok();
            }
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("userid") Long userId, @PathParam("id") Long id) {
        WeekComment weekComment = weekCommentService.find(id);
        if (weekComment == null) {
            return notFound();
        } else {
            if (!weekComment.getMember().getId().equals(userId)) {
                return forbidden();
            } else {
                return delete(getService()::remove, id);
            }
        }

    }


    @Override
    AbstractService getService() {
        return weekCommentService;
    }
}
