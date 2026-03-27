package com.pao.laboratory03.bonus;

import static com.pao.laboratory03.bonus.Status.TODO;

public class Task {
    private final String id;
    private final String title;
    private Status status;
    private final Priority priority;
    private String assignee;
    public Task(String id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = TODO;
        this.assignee = null;
    }
    public Task(String id, String title, Priority priority, Status status, String assignee) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
        this.assignee = assignee;
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public Status getStatus() {
        return status;
    }
    public Priority getPriority() {
        return priority;
    }
    public String getAssignee() {
        return assignee;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    @Override
    public String toString() {
        return "Task{" + "id='" + id + ", title='" + title+ ", priority=" + priority + ", status=" + status +
                ", assignee=" + assignee + '}';
    }
}
