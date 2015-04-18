package com.oltruong.teamag.service;

import com.oltruong.teamag.model.WeekComment;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class WeekCommentService extends AbstractService {

    public WeekComment findWeekComment(Long memberId, int weekYear, int year) {

        WeekComment result = null;
        Query query = createNamedQuery("findWeekComment");
        query.setParameter("fmemberId", memberId);
        query.setParameter("fweekYear", weekYear);
        query.setParameter("fyear", year);

        @SuppressWarnings("unchecked")
        List<WeekComment> weekCommentList = query.getResultList();
        if (!weekCommentList.isEmpty()) {
            result = weekCommentList.get(0);
        }
        return result;
    }

    public WeekComment create(WeekComment weekComment) {
        persist(weekComment);
        return weekComment;
    }

    public void update(WeekComment weekComment) {
        merge(weekComment);
    }

    public void removeWeekComment(WeekComment weekComment) {
        WeekComment weekCommentDb = find(WeekComment.class, weekComment.getId());
        remove(weekCommentDb);
    }
}
