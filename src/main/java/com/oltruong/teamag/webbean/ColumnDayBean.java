package com.oltruong.teamag.webbean;

import org.joda.time.DateTime;

public class ColumnDayBean implements Comparable<ColumnDayBean> {

    private DateTime day;

    private Double total = 0d;

    public Double getTotal() {
        return total;
    }

    public void addTotal(Double total) {
        this.total += total;
    }

    public void setDay(DateTime day) {
        this.day = day;
    }

    public DateTime getDay() {
        return day;
    }

    public String getDayHeader() {
        return day.toString("E dd");
    }

    public String getDayNumber() {

        return "day" + day.getDayOfWeek();
    }

    @Override
    public int compareTo(ColumnDayBean arg0) {

        return day.compareTo(arg0.getDay());
    }

    @Override
    public boolean equals(Object obj) {

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
