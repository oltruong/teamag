package com.oltruong.teamag.model;

import com.oltruong.teamag.model.converter.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "TM_WORK", indexes = {@Index(name = "work_month", columnList = "month"), @Index(name = "work_member", columnList = "member_id"), @Index(name = "work_task", columnList = "task_id")})
@Entity
@NamedQueries({@NamedQuery(name = "Work.FIND_BY_MEMBER_MONTH", query = "SELECT w FROM Work w WHERE w.member.id=:fmemberId and w.month=:fmonth order by w.task.name, w.day"),
        @NamedQuery(name = "Work.FIND_BY_MEMBER_MONTH_NOT_NULL", query = "SELECT w FROM Work w WHERE w.member.id=:fmemberId and w.month=:fmonth and w.task IN (SELECT w.task from Work w where w.member.id=:fmemberId and w.month=:fmonth and w.total<>0)  order by w.task.name, w.day"),
        @NamedQuery(name = "Work.FIND_WORKDAYS_BY_MEMBER_MONTH", query = "SELECT w.day, sum(w.total) FROM Work w WHERE w.member.id=:fmemberId and w.month=:fmonth group by w.day order by w.day"),
        @NamedQuery(name = "Work.DELETE_BY_MEMBERTaskMonth", query = "DELETE FROM Work w WHERE w.member.id=:fmemberId and w.task.id=:ftaskId and w.month=:fmonth"),
        @NamedQuery(name = "Work.DELETE_BY_MEMBER", query = "DELETE FROM Work w WHERE w.member.id=:fmemberId"),
        @NamedQuery(name = "Work.FIND_BY_MONTH", query = "SELECT w FROM Work w WHERE (w.month=:fmonth AND w.total<>0 ) ORDER by w.member.name, w.member.company, w.task.project, w.task.name"),
        @NamedQuery(name = "Work.FIND_BY_TASK_MEMBER", query = "SELECT w FROM Work w WHERE (w.total<>0 and w.task.id=:fTaskId) ORDER by w.member.name,w.day"),
        @NamedQuery(name = "Work.COUNT_BY_TASK", query = "SELECT count(w) FROM Work w WHERE w.task.id=:fTaskId"),
        @NamedQuery(name = "Work.FIND_ABSENCE_BY_MEMBER", query = "SELECT w FROM Work w WHERE (w.member.id=:fmemberId and w.day=:fday and w.task.name='Absence') order by w.task.id"),
        @NamedQuery(name = "Work.SUM_BY_MONTH_MEMBER", query = "SELECT SUM(w.total) FROM Work w WHERE (w.month=:fmonth AND w.member.id=:fmemberId )")})
public class Work implements IModel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    //  @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime month;

    @Column(nullable = false)
    //@Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime day;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false, name = "TASK_ID")
    private Task task;

    @Column(nullable = false)
    private Double total = 0d;

    @Transient
    private Double totalEdit = null;

    @Transient
    @Inject
    private Logger logger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getMonth() {
        return month;
    }

    public void setMonth(DateTime month) {
        this.month = month;
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
        totalEdit = total;
    }

    public Double getTotalEdit() {
        if (totalEdit == null) {
            totalEdit = total;
        }
        return totalEdit;
    }

    public String getTotalEditStr() {
        Double value = getTotalEdit();
        if (value.floatValue() == 0f) {
            return "";
        }
        return value.toString();
    }

    public void setTotalEditStr(String totalEditStr) {
        if (!StringUtils.isBlank(totalEditStr)) {
            String totalEditFormatted = totalEditStr.replace(",", ".");

            try {
                totalEdit = Double.valueOf(totalEditFormatted);
            } catch (NumberFormatException ex) {
                logger.error("Incorrect value " + totalEditStr);
            }
        } else {   // Blank means 0
            totalEdit = 0d;
        }
    }

    public void setTotalEdit(Double totalEdit) {

        this.totalEdit = totalEdit;
    }

    public String getDayStr() {
        return getDay().toString("E mmm dd");
    }

    public boolean hasChanged() {
        return total.floatValue() != totalEdit.floatValue();
    }


}
