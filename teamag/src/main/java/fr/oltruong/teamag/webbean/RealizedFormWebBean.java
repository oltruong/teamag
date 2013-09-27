package fr.oltruong.teamag.webbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

public class RealizedFormWebBean {
    private Calendar dayCursor;

    private int weekNumberMonth;

    private Calendar currentMonth;

    private List<ColumnDayBean> columnsDay = new ArrayList<ColumnDayBean>(5);

    private List<TaskWeekBean> taskWeeks = new ArrayList<TaskWeekBean>();

    private TaskWeekBean selectedTaskWeek;

    public boolean getIsFirstWeek() {

	// FIXME quick hack
	System.out.println("day cursot" + dayCursor.get(Calendar.DAY_OF_MONTH));
	System.out.println("weekNumberMonth" + weekNumberMonth);
	return weekNumberMonth == 1;
	// return CalendarUtils.isFirstWeek( dayCursor );
    }

    public boolean getIsLastWeek() {
	System.out.println("day cursot" + dayCursor.get(Calendar.DAY_OF_MONTH));
	System.out.println("weekNumberMonth" + weekNumberMonth);
	return weekNumberMonth == 5;
	// return CalendarUtils.isLastWeek( dayCursor );
    }

    public void addColumnDay(ColumnDayBean columnDay) {
	columnsDay.add(columnDay);
    }

    public List<ColumnDayBean> getColumnsDay() {
	return columnsDay;
    }

    public int getWeekNumber() {

	return dayCursor.get(Calendar.WEEK_OF_YEAR);
    }

    public List<TaskWeekBean> getTaskWeeks() {
	return taskWeeks;
    }

    public void setTaskWeeks(List<TaskWeekBean> taskWeeks) {
	this.taskWeeks = taskWeeks;
    }

    public TaskWeekBean getSelectedTaskWeek() {
	return selectedTaskWeek;
    }

    public void setSelectedTaskWeek(TaskWeekBean selectedTaskWeek) {
	this.selectedTaskWeek = selectedTaskWeek;
    }

    public void incrementWeek() {
	weekNumberMonth += 1;
	dayCursor.add(Calendar.WEEK_OF_MONTH, 1);

    }

    public void decrementWeek() {
	weekNumberMonth -= 1;
	dayCursor.add(Calendar.WEEK_OF_MONTH, -1);
    }

    public Calendar getCurrentMonth() {
	return currentMonth;
    }

    public String getCurrentMonthStr() {
	return DateFormatUtils.format(currentMonth, "MMMMM");
    }

    public void setCurrentMonth(Calendar currentMonth) {
	this.currentMonth = currentMonth;
    }

    public void setDayCursor(Calendar dayCursor) {
	this.dayCursor = dayCursor;
	this.weekNumberMonth = dayCursor.get(Calendar.WEEK_OF_MONTH);
    }

}
