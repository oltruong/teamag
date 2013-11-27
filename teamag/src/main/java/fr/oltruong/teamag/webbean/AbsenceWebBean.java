package fr.oltruong.teamag.webbean;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author Olivier Truong
 */
public class AbsenceWebBean {


    private Long id;

    private DateTime beginDateTime;

    private int beginType;

    private DateTime endDateTime;

    private int endType;

    public DateTime getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(DateTime beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public int getBeginType() {
        return beginType;
    }

    public void setBeginType(int beginType) {
        this.beginType = beginType;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getEndType() {
        return endType;
    }

    public void setEndType(int endType) {
        this.endType = endType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    //Methods used by JSF component

    public Date getEndDate() {
        Date endDate = null;
        if (endDateTime != null) {
            endDate = endDateTime.toDate();
        }
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate != null) {
            endDateTime = new DateTime(endDate).withTimeAtStartOfDay();
        }
    }

    public Date getBeginDate() {
        Date beginDate = null;
        if (beginDateTime != null) {
            beginDate = beginDateTime.toDate();
        }
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        if (beginDate != null) {
            beginDateTime = new DateTime(beginDate).withTimeAtStartOfDay();
        }
    }

}
