package com.oltruong.teamag.backingbean;

import com.google.common.collect.Maps;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.Work;
import com.oltruong.teamag.service.WorkService;
import com.oltruong.teamag.utils.TeamagConstants;
import com.oltruong.teamag.webbean.RealizedReportBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SessionScoped
@ManagedBean
public class ReportingController {

    @Inject
    private WorkService workService;
    private List<RealizedReportBean> realizedPersons;
    private List<RealizedReportBean> realizedCompanies;
    private LocalDate month;

    private static final String VIEWNAME = "reporting";


    public String previousMonth() {
        month = month.minusMonths(1);
        return refreshView();
    }

    public String nextMonth() {
        month = month.plusMonths(1);
        return refreshView();
    }


    public String init() {

        month = LocalDate.now().withDayOfMonth(1);

        return refreshView();
    }

    private String refreshView() {
        initLists();
        return VIEWNAME;
    }

    private void initLists() {
        List<Work> works = workService.getWorksMonth(month);

        initRealizedPersons(works);

        initRealizedCompanies(works);
    }

    private void initRealizedCompanies(List<Work> works) {
        Map<String, List<Task>> map = Maps.newLinkedHashMap();

        for (Work work : works) {
            if (!map.containsKey(work.getMember().getCompany())) {
                map.put(work.getMember().getCompany(), new ArrayList<>());
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
        realizedCompanies = new ArrayList<>(map.size());

        final Set<Map.Entry<String, List<Task>>> entries = map.entrySet();
        for (Map.Entry<String, List<Task>> entry : entries) {
            RealizedReportBean report = new RealizedReportBean();
            report.setName(entry.getKey());
            report.setTasks(entry.getValue());
            realizedCompanies.add(report);
        }
    }

    private void initRealizedPersons(List<Work> works) {
        Map<Member, List<Task>> map = Maps.newLinkedHashMap();

        for (Work work : works) {
            if (!map.containsKey(work.getMember())) {
                map.put(work.getMember(), new ArrayList<>());
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

        realizedPersons = new ArrayList<>(map.size());

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
        return DateTimeFormatter.ofPattern(TeamagConstants.MONTH_YEAR_FORMAT).format(month);
    }

    public String getPreviousMonthString() {
        return DateTimeFormatter.ofPattern(TeamagConstants.MONTH_YEAR_FORMAT).format(month.minusMonths(1));
    }

    public String getNextMonthString() {
        return DateTimeFormatter.ofPattern(TeamagConstants.MONTH_YEAR_FORMAT).format(month.plusMonths(1));
    }

}
