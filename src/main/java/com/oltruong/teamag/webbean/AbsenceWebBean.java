package com.oltruong.teamag.webbean;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


public class AbsenceWebBean {


    private Long id;

    private DateTime beginDateTime;

    private int beginType;

    private DateTime endDateTime;

    private String beginDateString;
    private String endDateString;

    private int endType;

    private String memberName;

    private String color;

    private Long beginDateLong;
    private Long endDateLong;

    public DateTime getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(DateTime beginDateTime) {
        this.beginDateTime = beginDateTime;
        this.beginDateLong = beginDateTime.toDate().getTime();
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
        this.endDateLong = endDateTime.toDate().getTime();

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
        } else {
            endDateTime = null;
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
        } else {
            beginDateTime = null;
        }
    }

    public String getBeginDateString() {
        return beginDateString;
    }

    public void setBeginDateString(String beginDateString) {
        this.beginDateString = beginDateString;

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        this.beginDateTime = formatter.parseDateTime(beginDateString);

    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {

        this.endDateString = endDateString;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        this.endDateTime = formatter.parseDateTime(endDateString);
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
