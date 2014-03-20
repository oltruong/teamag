package fr.oltruong.teamag.entity;

import fr.oltruong.teamag.entity.converter.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.*;

@Table(name = "TM_WORK")
@Entity
@NamedQueries({@NamedQuery(name = "findWorksByMemberMonth", query = "SELECT w FROM Work w WHERE w.member.id=:fmemberId and w.month=:fmonth order by w.task.name, w.day"),
        @NamedQuery(name = "deleteWorksByMemberTaskMonth", query = "DELETE FROM Work w WHERE w.member.id=:fmemberId and w.task.id=:ftaskId and w.month=:fmonth"),
        @NamedQuery(name = "deleteWorksByMember", query = "DELETE FROM Work w WHERE w.member.id=:fmemberId"),
        @NamedQuery(name = "findWorksMonth", query = "SELECT w FROM Work w WHERE (w.month=:fmonth AND w.total<>0 ) ORDER by w.member.name, w.member.company, w.task.project, w.task.name"),
        @NamedQuery(name = "countWorksTask", query = "SELECT count(w) FROM Work w WHERE w.task.id=:fTaskId"),
        @NamedQuery(name = "countWorksMemberMonth", query = "SELECT SUM(w.total) FROM Work w WHERE (w.month=:fmonth AND w.member.id=:fmemberId )")})
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

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false, name = "TASK_ID")
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
            String totalEditFormatted = totalEditStr.replace(",", ".");

            try {
                totalEdit = Float.valueOf(totalEditFormatted);
            } catch (NumberFormatException ex) {
                logger.error("Incorrect value " + totalEditStr);
            }
        } else {   // Blank means 0
            totalEdit = 0f;
        }
    }

    public void setTotalEdit(Float totalEdit) {

        this.totalEdit = totalEdit;
    }

    public String getDayStr() {
        return getDay().toString("E mmm dd");
    }

    public boolean hasChanged() {
        return total.floatValue() != totalEdit.floatValue();
    }

}
