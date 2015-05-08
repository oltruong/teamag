package com.oltruong.teamag.webbean;

import java.time.LocalDate;

public class ColumnDayBean implements Comparable<ColumnDayBean> {

    private LocalDate day;

    private Double total = 0d;

    public Double getTotal() {
        return total;
    }

    public void addTotal(Double total) {
        this.total += total;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalDate getDay() {
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
