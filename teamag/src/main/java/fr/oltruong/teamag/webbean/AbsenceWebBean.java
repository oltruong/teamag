package fr.oltruong.teamag.webbean;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author Olivier Truong
 */
public class AbsenceWebBean {


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


    //Methods used by JSF component

    public Date getEndDate() {
        return endDateTime.toDate();
    }

    public void setEndDate(Date endDate) {
        endDateTime = new DateTime(endDate);
    }

    public Date getBeginDate() {
        return beginDateTime.toDate();
    }

    public void setBeginDate(Date beginDate) {
        beginDateTime = new DateTime(beginDate);
    }

}
