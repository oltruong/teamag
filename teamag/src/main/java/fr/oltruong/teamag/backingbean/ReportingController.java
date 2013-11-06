package fr.oltruong.teamag.backingbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.google.common.collect.Maps;

import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.utils.Constants;
import fr.oltruong.teamag.webbean.RealizedReportBean;

@SessionScoped
@ManagedBean
public class ReportingController {

    @Inject
    private WorkEJB workEJB;

    private List<RealizedReportBean> realizedPersons;

    private List<RealizedReportBean> realizedCompanies;

    private MutableDateTime month;

    public String previousMonth() {
        month.addMonths(-1);
        initLists();
        return "reporting";
    }

    public String nextMonth() {
        month.addMonths(1);
        initLists();
        return "reporting";
    }

    @PostConstruct
    private void init() {

        month = DateTime.now().withTimeAtStartOfDay().withDayOfMonth(1).toMutableDateTime();

        initLists();

    }

    private void initLists() {
        List<Work> works = workEJB.getWorksMonth(month.toDateTime());

        initRealizedPersons(works);

        initRealizedCompanies(works);
    }

    private void initRealizedCompanies(List<Work> works) {
        Map<String, List<Task>> map = Maps.newHashMap();

        for (Work work : works) {
            if (!map.containsKey(work.getMember().getCompany())) {
                map.put(work.getMember().getCompany(), new ArrayList<Task>());
            }
            List<Task> tasks = map.get(work.getMember().getCompany());
            if (tasks.contains(work.getTask())) {
                tasks.get(tasks.indexOf(work.getTask())).addTotal(work.getTotal());
            } else {
                Task newTask = work.getTask().clone();
                newTask.setTotal(work.getTotal());
                tasks.add(newTask);
            }

        }
        realizedCompanies = new ArrayList<RealizedReportBean>(map.size());

        final Set<Map.Entry<String, List<Task>>> entries = map.entrySet();
        for (Map.Entry<String, List<Task>> entry : entries) {
            RealizedReportBean report = new RealizedReportBean();
            report.setName(entry.getKey());
            report.setTasks(entry.getValue());
            realizedCompanies.add(report);
        }
    }

    private void initRealizedPersons(List<Work> works) {
        Map<Member, List<Task>> map = Maps.newHashMap();

        for (Work work : works) {
            if (!map.containsKey(work.getMember())) {
                map.put(work.getMember(), new ArrayList<Task>());
            }
            List<Task> tasks = map.get(work.getMember());
            if (tasks.contains(work.getTask())) {
                tasks.get(tasks.indexOf(work.getTask())).addTotal(work.getTotal());
            } else {
                Task newTask = work.getTask().clone();
                newTask.setTotal(work.getTotal());
                tasks.add(newTask);
            }

        }

        realizedPersons = new ArrayList<RealizedReportBean>(map.size());

        final Set<Map.Entry<Member, List<Task>>> entries = map.entrySet();

        for (Map.Entry<Member, List<Task>> entry : entries) {
            RealizedReportBean report = new RealizedReportBean();
            report.setName(entry.getKey().getName());
            report.setTasks(entry.getValue());
            realizedPersons.add(report);
        }

    }

    public List<RealizedReportBean> getRealizedPersons() {
        return realizedPersons;
    }

    public void setRealizedPersons(List<RealizedReportBean> realizedPersons) {
        this.realizedPersons = realizedPersons;
    }

    public List<RealizedReportBean> getRealizedCompanies() {
        return realizedCompanies;
    }

    public void setRealizedCompanies(List<RealizedReportBean> realizedCompanies) {
        this.realizedCompanies = realizedCompanies;
    }

    public String getMonthString() {
        return month.toString(Constants.MONTH_YEAR_FORMAT);
    }

    public String getPreviousMonthString() {
        return month.toDateTime().minusMonths(1).toString(Constants.MONTH_YEAR_FORMAT);
    }

    public String getNextMonthString() {
        return month.toDateTime().plusMonths(1).toString(Constants.MONTH_YEAR_FORMAT);
    }

}
