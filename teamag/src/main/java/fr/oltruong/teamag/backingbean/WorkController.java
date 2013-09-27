package fr.oltruong.teamag.backingbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fr.oltruong.teamag.ejb.EmailEJB;
import fr.oltruong.teamag.ejb.MailBean;
import fr.oltruong.teamag.ejb.ParameterEJB;
import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.qualifier.UserLogin;
import fr.oltruong.teamag.utils.CalendarUtils;
import fr.oltruong.teamag.webbean.ColumnDayBean;
import fr.oltruong.teamag.webbean.RealizedFormWebBean;
import fr.oltruong.teamag.webbean.TaskWeekBean;

@SessionScoped
@ManagedBean
public class WorkController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Inject
    @UserLogin
    private Instance<Member> memberInstance;

    private Task newTask = new Task();

    private Map<Task, List<Work>> works;

    private RealizedFormWebBean realizedBean;

    @EJB
    private WorkEJB workEJB;

    @EJB
    private EmailEJB mailEJB;

    @EJB
    private ParameterEJB parameterEJB;

    public String doCreateActivity() {

	this.logger.info("Adding a new activity");

	if (StringUtils.isBlank(this.newTask.getName())) {
	    FacesMessage msg = null;
	    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
		    "Ajout impossible", "Merci de fournir un nom à la tâche !");
	    FacesContext.getCurrentInstance().addMessage(null, msg);

	} else {

	    try {
		this.workEJB.createTask(this.realizedBean.getCurrentMonth(),
			getMember(), this.newTask);

		this.works = this.workEJB.findWorks(getMember(), CalendarUtils
			.getFirstDayOfMonth(Calendar.getInstance()));
		initTaskWeek();

		FacesMessage msg = null;
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
			"Tâche créée", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	    } catch (ExistingDataException e) {
		FacesMessage msg = null;
		msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
			"Tâche existante", "Aucune modification");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	    }
	}

	return "realized.xhtml";
    }

    public String deleteTask() {
	this.logger.info("Deleting task "
		+ this.realizedBean.getSelectedTaskWeek().getTask().getName());

	this.workEJB.removeTask(this.realizedBean.getSelectedTaskWeek()
		.getTask(), getMember(), this.realizedBean.getCurrentMonth());
	init();
	return "realized.xhtml";
    }

    public String previousWeek() {
	this.logger.finest("Click Previous week");
	this.realizedBean.decrementWeek();
	initTaskWeek();
	return "realized.xhtml";
    }

    public String nextWeek() {
	System.out.println("Click next week");
	this.realizedBean.incrementWeek();
	initTaskWeek();
	return "realized.xhtml";
    }

    public String update() {

	System.out.println("Calling update method");
	List<Work> changedWorks = findChangedWorks(this.realizedBean
		.getTaskWeeks());
	this.workEJB.updateWorks(changedWorks);

	FacesMessage msg = null;
	if (changedWorks.isEmpty()) {
	    msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
		    "Aucun changement détecté", "");

	} else {
	    System.out.println(changedWorks.size() + " changements trouvés");
	    msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		    "Mise à jour effectuée", "");
	    sendNotification();
	    initTaskWeek();
	}
	FacesContext.getCurrentInstance().addMessage(null, msg);

	return "realized.xhtml";
    }

    public List<String> completeProject(String query) {
	List<Task> tasks = this.workEJB.findAllTasks();

	List<String> results = new ArrayList<String>(tasks.size());
	if (!StringUtils.isBlank(query) && query.length() > 1) {

	    for (Task task : tasks) {
		if (StringUtils.containsIgnoreCase(task.getProject(), query)
			&& !results.contains(task.getProject())) {
		    results.add(task.getProject());
		}

	    }
	}
	return results;
    }

    public List<String> completeName(String query) {
	List<Task> tasks = this.workEJB.findAllTasks();

	List<String> results = new ArrayList<String>(tasks.size());
	if (!StringUtils.isBlank(query) && query.length() > 1) {
	    for (Task task : tasks) {
		// Do not propose task that the member already has
		if (!task.getMembers().contains(getMember())) {
		    if (StringUtils.containsIgnoreCase(task.getName(), query)
			    && !results.contains(task.getName())) {
			results.add(task.getName());
		    }
		}
	    }
	}
	return results;
    }

    private void sendNotification() {
	int total = this.workEJB.getSumWorks(getMember(),
		this.realizedBean.getCurrentMonth());

	int nbWorkingDays = CalendarUtils.getWorkingDays(
		this.realizedBean.getCurrentMonth()).size();
	if (total == nbWorkingDays) {

	    MailBean email = buildEmail();
	    this.mailEJB.sendEmail(email);
	}
    }

    private MailBean buildEmail() {
	MailBean email = new MailBean();
	email.setContent("Réalisé complet");
	email.setRecipient(this.parameterEJB.getAdministratorEmail());
	email.setSubject("Réalisé de" + getMember().getName());
	return email;
    }

    public String init() {
	System.out.println("UINITTT pour" + getMember().getName());
	this.realizedBean = new RealizedFormWebBean();
	this.realizedBean.setDayCursor(Calendar.getInstance());

	Calendar firstDayOfMonth = CalendarUtils.getFirstDayOfMonth(Calendar
		.getInstance());
	this.realizedBean.setCurrentMonth(firstDayOfMonth);
	this.works = this.workEJB.findWorks(getMember(), firstDayOfMonth);

	initTaskWeek();
	return "realized";
    }

    private void initTaskWeek() {
	if (this.works != null) {
	    Integer weekNumber = this.realizedBean.getWeekNumber();

	    Map<String, ColumnDayBean> mapColumns = new HashMap<String, ColumnDayBean>(
		    5);

	    List<TaskWeekBean> taskWeekList = new ArrayList<TaskWeekBean>(
		    this.works.keySet().size());
	    for (Task task : this.works.keySet()) {
		TaskWeekBean taskWeek = new TaskWeekBean();
		taskWeek.setTask(task);
		for (Work work : this.works.get(task)) {

		    if (work.getDay().get(Calendar.WEEK_OF_YEAR) == weekNumber) {

			ColumnDayBean columnDay = new ColumnDayBean();
			columnDay.setDay(work.getDay());
			taskWeek.addWork(columnDay.getDayNumber(), work);

			if (mapColumns.get(work.getDayStr()) == null) {
			    columnDay.addTotal(work.getTotal());
			    mapColumns.put(work.getDayStr(), columnDay);
			} else {
			    mapColumns.get(work.getDayStr()).addTotal(
				    work.getTotal());
			}

		    }
		}
		taskWeekList.add(taskWeek);

	    }

	    this.realizedBean.getColumnsDay().clear();
	    for (ColumnDayBean col : mapColumns.values()) {
		this.realizedBean.addColumnDay(col);

	    }
	    Collections.sort(this.realizedBean.getColumnsDay());
	    this.realizedBean.setTaskWeeks(taskWeekList);
	    Collections.sort(this.realizedBean.getTaskWeeks());

	} else {
	    System.out.println("Aucune taskMonth :'(");
	}

    }

    private List<Work> findChangedWorks(List<TaskWeekBean> taskWeeks) {
	List<Work> worksChanged = new ArrayList<Work>();
	for (TaskWeekBean taskWeek : taskWeeks) {
	    for (Work work : taskWeek.getWorks()) {
		if (work.hasChanged()) {
		    worksChanged.add(work);
		}
	    }
	}

	return worksChanged;
    }

    // Getters and setters

    public RealizedFormWebBean getRealizedBean() {
	return this.realizedBean;
    }

    public void setRealizedBean(RealizedFormWebBean realizedBean) {
	this.realizedBean = realizedBean;
    }

    public Member getMember() {
	return this.memberInstance.get();
    }

    public Task getNewTask() {
	return this.newTask;
    }

    public void setNewTask(Task newActivity) {
	this.newTask = newActivity;
    }

}
