package fr.oltruong.teamag.backingbean;

import com.google.common.collect.Maps;
import fr.oltruong.teamag.service.WorkService;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.Work;
import fr.oltruong.teamag.utils.TeamagConstants;
import fr.oltruong.teamag.webbean.RealizedReportBean;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SessionScoped
@ManagedBean
public class ReportingController {

    @Inject
    private WorkService workEJB;
    private List<RealizedReportBean> realizedPersons;
    private List<RealizedReportBean> realizedCompanies;
    private MutableDateTime month;

    private static final String VIEWNAME = "reporting";


    public String previousMonth() {
        month.addMonths(-1);
        return refreshView();
    }

    public String nextMonth() {
        month.addMonths(1);
        return refreshView();
    }


    public String init() {

        month = DateTime.now().withTimeAtStartOfDay().withDayOfMonth(1).toMutableDateTime();

        return refreshView();
    }

    private String refreshView() {
        initLists();
        return VIEWNAME;
    }

    private void initLists() {
        List<Work> works = workEJB.getWorksMonth(month.toDateTime());

        initRealizedPersons(works);

        initRealizedCompanies(works);
    }

    private void initRealizedCompanies(List<Work> works) {
        Map<String, List<Task>> map = Maps.newLinkedHashMap();

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
        Map<Member, List<Task>> map = Maps.newLinkedHashMap();

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
        return month.toString(TeamagConstants.MONTH_YEAR_FORMAT);
    }

    public String getPreviousMonthString() {
        return month.toDateTime().minusMonths(1).toString(TeamagConstants.MONTH_YEAR_FORMAT);
    }

    public String getNextMonthString() {
        return month.toDateTime().plusMonths(1).toString(TeamagConstants.MONTH_YEAR_FORMAT);
    }

}
