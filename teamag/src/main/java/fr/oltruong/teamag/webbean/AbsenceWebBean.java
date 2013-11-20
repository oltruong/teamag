package fr.oltruong.teamag.webbean;

import java.util.Date;

/**
 * @author Olivier Truong
 */
public class AbsenceWebBean {


    private Date beginDate;

    private int beginType;

    private Date endDate;

    private int endType;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public int getBeginType() {
        return beginType;
    }

    public void setBeginType(int beginType) {
        this.beginType = beginType;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getEndType() {
        return endType;
    }

    public void setEndType(int endType) {
        this.endType = endType;
    }
}
