package com.oltruong.teamag.service;

import com.oltruong.teamag.model.WeekComment;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class WeekCommentService extends AbstractService<WeekComment> {

    public WeekComment findWeekComment(Long memberId, int weekYear, int month, int year) {

        WeekComment result = null;
        TypedQuery<WeekComment> query = createTypedQuery("WeekComment.FIND_BY_MEMBER_WEEK_MONTH_YEAR");
        query.setParameter("fmemberId", memberId);
        query.setParameter("fweekYear", weekYear);
        query.setParameter("fmonth", month);
        query.setParameter("fyear", year);
        List<WeekComment> weekCommentList = query.getResultList();
        if (!weekCommentList.isEmpty()) {
            result = weekCommentList.get(0);
        }
        return result;
    }


    @Override
    Class<WeekComment> entityProvider() {
        return WeekComment.class;
    }

    @Override
    public List<WeekComment> findAll() {
        throw new UnsupportedOperationException();
    }
}
