package fr.oltruong.teamag.entity;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import com.google.common.annotations.VisibleForTesting;

import fr.oltruong.teamag.entity.converter.DateConverter;

@Table(name = "TM_WORK")
@Entity
@NamedQueries({ @NamedQuery(name = "findWorksByMember", query = "SELECT w FROM Work w WHERE w.member.name=:fmemberName and w.month=:fmonth order by w.task.name, w.day"),
        @NamedQuery(name = "deleteWorksByMemberTaskMonth", query = "DELETE FROM Work w WHERE w.member.id=:fmemberId and w.task.id=:ftaskId and w.month=:fmonth"),
        @NamedQuery(name = "findWorksMonth", query = "SELECT w FROM Work w WHERE (w.month=:fmonth AND w.total<>0 ) ORDER by w.member.company, w.member.id, w.task.id"),
        @NamedQuery(name = "countWorksTask", query = "SELECT count(w) FROM Work w WHERE w.task.id=:fTaskId"),
        @NamedQuery(name = "countWorksMemberMonth", query = "SELECT SUM(w.total) FROM Work w WHERE (w.month=:fmonth AND w.member.id=:fmemberId )") })
public class Work {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime month;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime day;

    @JoinColumn(nullable = false)
    private Member member;

    @JoinColumn(nullable = false)
    private Task task;

    private Float total = 0f;

    @Transient
    private Float totalEdit = null;

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

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
        totalEdit = total;
    }

    public Float getTotalEdit() {
        if (totalEdit == null) {
            totalEdit = total;
        }
        return totalEdit;
    }

    public String getTotalEditStr() {
        Float value = getTotalEdit();
        if (value.floatValue() == 0f) {
            return "";
        }
        return value.toString();
    }

    public void setTotalEditStr(String totalEditStr) {
        if (!StringUtils.isBlank(totalEditStr)) {
            try {
                totalEdit = Float.valueOf(totalEditStr);
            } catch (NumberFormatException ex) {
                logger.error("Valeur incorrecte " + totalEditStr);
            }
        } else
        // Blank means 0
        {
            totalEdit = 0f;
        }
    }

    public void setTotalEdit(Float totalEdit) {

        this.totalEdit = totalEdit;
    }

    public String getDayStr() {
        System.out.println(getDay().toString("E mmm dd"));
        return getDay().toString("E mmm dd");
    }

    public boolean hasChanged() {
        return total.floatValue() != totalEdit.floatValue();
    }

    @VisibleForTesting
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
