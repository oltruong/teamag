package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import com.google.common.collect.Maps;

import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.utils.CalendarUtils;
import fr.oltruong.teamag.webbean.RealizedReportBean;

@ManagedBean
public class ReportingController {

	@EJB
	private WorkEJB workEJB;

	private List<RealizedReportBean> realizedPersons;

	private List<RealizedReportBean> realizedCompanies;

	private Calendar month;

	@PostConstruct
	private void initLists() {

		this.month = CalendarUtils.getFirstDayOfMonth(Calendar.getInstance());
		// this.month = CalendarUtils.getPreviousMonth(
		// CalendarUtils.getFirstDayOfMonth( Calendar.getInstance() ) );

		System.currentTimeMillis();
		List<Work> works = this.workEJB.getWorksMonth(this.month);

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
				tasks.get(tasks.indexOf(work.getTask())).addTotal(
						work.getTotal());
			} else {
				Task newTask = work.getTask().clone();
				newTask.setTotal(work.getTotal());
				tasks.add(newTask);
			}

		}
		this.realizedCompanies = new ArrayList<RealizedReportBean>(map.size());

		final Set<Map.Entry<String, List<Task>>> entries = map.entrySet();
		for (Map.Entry<String, List<Task>> entry : entries) {
			RealizedReportBean report = new RealizedReportBean();
			report.setName(entry.getKey());
			report.setTasks(entry.getValue());
			this.realizedCompanies.add(report);
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
				tasks.get(tasks.indexOf(work.getTask())).addTotal(
						work.getTotal());
			} else {
				Task newTask = work.getTask().clone();
				newTask.setTotal(work.getTotal());
				tasks.add(newTask);
			}

		}
		this.realizedPersons = new ArrayList<RealizedReportBean>(map.size());

		final Set<Map.Entry<Member, List<Task>>> entries = map.entrySet();

		for (Map.Entry<Member, List<Task>> entry : entries) {
			RealizedReportBean report = new RealizedReportBean();
			report.setName(entry.getKey().getName());
			report.setTasks(entry.getValue());
			this.realizedPersons.add(report);
		}

	}

	public List<RealizedReportBean> getRealizedPersons() {
		return this.realizedPersons;
	}

	public void setRealizedPersons(List<RealizedReportBean> realizedPersons) {
		this.realizedPersons = realizedPersons;
	}

	public List<RealizedReportBean> getRealizedCompanies() {
		return this.realizedCompanies;
	}

	public void setRealizedCompanies(List<RealizedReportBean> realizedCompanies) {
		this.realizedCompanies = realizedCompanies;
	}

}
