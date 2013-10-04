package fr.oltruong.teamag.webbean;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

public class RealizedFormWebBean {

    @Inject
    private Logger logger;

    private Calendar dayCursor;

    private int weekNumberMonth;

    private Calendar currentMonth;

    private List<ColumnDayBean> columnsDay = Lists.newArrayListWithExpectedSize(5);

    private List<TaskWeekBean> taskWeeks = Lists.newArrayList();

    private TaskWeekBean selectedTaskWeek;

    public boolean getIsFirstWeek() {
        logger.debug("day cursot" + dayCursor.get(Calendar.DAY_OF_MONTH));
        logger.debug("weekNumberMonth" + weekNumberMonth);
        return weekNumberMonth == 1;
    }

    public boolean getIsLastWeek() {
        logger.debug("day cursot" + dayCursor.get(Calendar.DAY_OF_MONTH));
        logger.debug("weekNumberMonth" + weekNumberMonth);
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
