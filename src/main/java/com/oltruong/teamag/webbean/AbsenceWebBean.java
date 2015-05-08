package com.oltruong.teamag.webbean;

import java.time.LocalDate;
import org.joda.time.format.LocalDateFormat;
import org.joda.time.format.LocalDateFormatter;

import java.util.Date;

/**
 * @author Olivier Truong
 */
public class AbsenceWebBean {


    private Long id;

    private LocalDate beginLocalDate;

    private int beginType;

    private LocalDate endLocalDate;

    private String beginDateString;
    private String endDateString;

    private int endType;

    private String memberName;

    private String color;

    private Long beginDateLong;
    private Long endDateLong;

    public LocalDate getBeginLocalDate() {
        return beginLocalDate;
    }

    public void setBeginLocalDate(LocalDate beginLocalDate) {
        this.beginLocalDate = beginLocalDate;
        this.beginDateLong = beginLocalDate.toDate().getTime();
    }

    public int getBeginType() {
        return beginType;
    }

    public void setBeginType(int beginType) {
        this.beginType = beginType;
    }

    public LocalDate getEndLocalDate() {
        return endLocalDate;
    }

    public void setEndLocalDate(LocalDate endLocalDate) {
        this.endLocalDate = endLocalDate;
        this.endDateLong = endLocalDate.toDate().getTime();

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
        if (endLocalDate != null) {
            endDate = endLocalDate.toDate();
        }
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate != null) {
            endLocalDate = new LocalDate(endDate).withTimeAtStartOfDay();
        } else {
            endLocalDate = null;
        }
    }

    public Date getBeginDate() {
        Date beginDate = null;
        if (beginLocalDate != null) {
            beginDate = beginLocalDate.toDate();
        }
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        if (beginDate != null) {
            beginLocalDate = new LocalDate(beginDate).withTimeAtStartOfDay();
        } else {
            beginLocalDate = null;
        }
    }

    public String getBeginDateString() {
        return beginDateString;
    }

    public void setBeginDateString(String beginDateString) {
        this.beginDateString = beginDateString;

        LocalDateFormatter formatter = LocalDateFormat.forPattern("yyyy-MM-dd");
        this.beginLocalDate = formatter.parseLocalDate(beginDateString);

    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {

        this.endDateString = endDateString;
        LocalDateFormatter formatter = LocalDateFormat.forPattern("yyyy-MM-dd");
        this.endLocalDate = formatter.parseLocalDate(endDateString);
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getBeginDateLong() {
        return beginDateLong;
    }

    public void setBeginDateLong(Long beginDateLong) {
        this.beginDateLong = beginDateLong;
    }

    public Long getEndDateLong() {
        return endDateLong;
    }

    public void setEndDateLong(Long endDateLong) {
        this.endDateLong = endDateLong;
    }
}
