package fr.oltruong.teamag.webbean;

import fr.oltruong.teamag.entity.Activity;

/**
 * @author Olivier Truong
 */
public class TaskWebBean {

    private Long id;

    private String name;

    private String project;

    private TaskWebBean task;
    private Activity activity;

    private Boolean delegated = Boolean.FALSE;

    private String comment;

    private Double amount;

    private Double total;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public TaskWebBean getTask() {
        return task;
    }

    public void setTask(TaskWebBean task) {
        this.task = task;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Boolean getDelegated() {
        return delegated;
    }

    public void setDelegated(Boolean delegated) {
        this.delegated = delegated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
