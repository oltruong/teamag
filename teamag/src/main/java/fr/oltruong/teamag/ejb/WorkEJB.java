package fr.oltruong.teamag.ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.utils.CalendarUtils;

@Stateless
public class WorkEJB extends AbstractEJB {

    public Map<Task, List<Work>> findWorks(Member member, Calendar month) {

	Map<Task, List<Work>> worksByTask = new HashMap<Task, List<Work>>();

	Query query = entityManager.createNamedQuery("findWorksByMember");
	query.setParameter("fmemberName", member.getName());
	query.setParameter("fmonth", month);

	@SuppressWarnings("unchecked")
	List<Work> listWorks = query.getResultList();

	if (CollectionUtils.isEmpty(listWorks)) {
	    System.out.println("Creating new works for member "
		    + member.getName());
	    listWorks = createWorks(member, month);
	}

	worksByTask = transformWorkList(listWorks);

	return worksByTask;
    }

    @SuppressWarnings("unchecked")
    public List<Task> findAllTasks() {
	return entityManager.createNamedQuery("findAllTasks").getResultList();
    }

    public int getSumWorks(Member member, Calendar month) {
	Query query = entityManager.createNamedQuery("countWorksMemberMonth");
	query.setParameter("fmemberId", member.getId());
	query.setParameter("fmonth", month);
	Number sumOfPrice = (Number) query.getSingleResult();
	System.out.println("total " + sumOfPrice);
	return sumOfPrice.intValue();
    }

    private Map<Task, List<Work>> transformWorkList(List<Work> listWorks) {

	Map<Task, List<Work>> worksByTask = new HashMap<Task, List<Work>>();
	if (listWorks != null) {
	    for (Work work : listWorks) {
		if (!worksByTask.containsKey(work.getTask())) {
		    worksByTask.put(work.getTask(), new ArrayList<Work>());
		}
		worksByTask.get(work.getTask()).add(work);
	    }

	}

	return worksByTask;
    }

    private List<Work> createWorks(Member member, Calendar month) {

	List<Work> workList = null;

	List<Task> taskList = findMemberTasks(member);
	if (CollectionUtils.isNotEmpty(taskList)) {

	    List<Calendar> workingDays = CalendarUtils.getWorkingDays(month);

	    workList = new ArrayList<Work>(taskList.size() * workingDays.size());
	    for (Task task : taskList) {
		for (Calendar day : workingDays) {
		    Work work = new Work();
		    work.setDay(day);
		    work.setMember(member);
		    work.setMonth(month);
		    work.setTask(task);

		    entityManager.persist(work);

		    workList.add(work);
		}
	    }

	} else {
	    System.out.println("Aucune activite");
	}
	return workList;

    }

    public void removeTask(Task task, Member member, Calendar month) {
	Query query = entityManager
		.createNamedQuery("deleteWorksByMemberTaskMonth");
	query.setParameter("fmemberId", member.getId());
	query.setParameter("ftaskId", task.getId());
	query.setParameter("fmonth", month);

	int rowsNumberDeleted = query.executeUpdate();

	System.out.println("Works supprimés : " + rowsNumberDeleted);

	// Suppression pour la tâche de l'utilisateur

	Task taskDb = entityManager.find(Task.class, task.getId());

	Member memberDb = entityManager.find(Member.class, member.getId());

	taskDb.getMembers().remove(memberDb);

	if (taskDb.getMembers().isEmpty() && taskHasNoWorks(taskDb)) {
	    System.out
		    .println("La tâche n'a aucun objet attaché dessus. Suppression de la tâche");
	    entityManager.remove(taskDb);
	} else {
	    System.out.println("Mise à jour de la tâche");
	    entityManager.persist(taskDb);
	}
    }

    private boolean taskHasNoWorks(Task taskDb) {

	Query query = entityManager.createNamedQuery("countWorksTask");
	query.setParameter("fTaskId", taskDb.getId());
	int total = ((Number) query.getSingleResult()).intValue();
	return total == 0;
    }

    public List<Task> findMemberTasks(Member member) {
	Query query = entityManager.createNamedQuery("findAllTasks");

	@SuppressWarnings("unchecked")
	List<Task> allTaskList = query.getResultList();

	List<Task> taskList = new ArrayList<Task>();

	for (Task task : allTaskList) {
	    System.out.println("tache " + task.getId());

	    if (task.getMembers() != null && !task.getMembers().isEmpty()) {

		System.out.println("tache Name"
			+ task.getMembers().get(0).getName());
		System.out.println("tache Id"
			+ task.getMembers().get(0).getId());
		System.out.println("Member id" + member.getId());

		if (task.getMembers().contains(member)) {
		    System.out.println("la tâche a bien comme member "
			    + member.getName());
		    taskList.add(task);
		}
	    }
	}

	return taskList;
    }

    public void createTask(Calendar month, Member member, Task task)
	    throws ExistingDataException {
	Query query = entityManager.createNamedQuery("findTaskByName");
	query.setParameter("fname", task.getName());
	query.setParameter("fproject", task.getProject());

	Task taskDB = null;
	@SuppressWarnings("unchecked")
	List<Task> allTaskList = query.getResultList();

	if (CollectionUtils.isNotEmpty(allTaskList)) {
	    System.out.println("La tâche existe déjà");
	    Task myTask = allTaskList.get(0);
	    if (myTask.getMembers().contains(member)) {
		System.out.println("Déjà affectée à la personne");
		throw new ExistingDataException();
	    } else {
		System.out.println("Affectation à la personne "
			+ member.getId());
		myTask.addMember(member);
		entityManager.merge(myTask);
		taskDB = myTask;
	    }
	} else

	{
	    System.out.println("Création d'une nouvelle tâche");

	    // Reset task ID
	    task.setId(null);
	    task.addMember(member);
	    entityManager.persist(task);
	    taskDB = task;

	}

	entityManager.flush();

	// Création des objets Work
	System.out.println("Création des objets WORK");
	List<Calendar> workingDayList = CalendarUtils.getWorkingDays(month);

	for (Calendar day : workingDayList) {
	    Work work = new Work();
	    work.setDay(day);
	    work.setMember(member);
	    work.setMonth(month);
	    work.setTask(taskDB);

	    entityManager.persist(work);
	}

    }

    public void updateWorks(List<Work> workList) {
	for (Work work : workList) {
	    work.setTotal(work.getTotalEdit());
	    entityManager.merge(work);
	}
    }

    @SuppressWarnings("unchecked")
    public List<Work> getWorksMonth(Calendar month) {
	Query query = entityManager.createNamedQuery("findWorksMonth");
	query.setParameter("fmonth", month);

	return query.getResultList();
    }

}
