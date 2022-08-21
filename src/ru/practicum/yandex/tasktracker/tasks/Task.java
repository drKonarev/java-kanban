package ru.practicum.yandex.tasktracker.tasks;

import ru.practicum.yandex.tasktracker.managers.*;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Status status;




    public TaskType getTaskType() {
        return taskType;
    }

    protected TaskType taskType;
    protected String result;

    public enum Status {
        NEW,
        DONE,
        IN_PROGRESS
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public Task(String title, String description, Status newStatus) {
        this.title = title;
        this.description = description;
        this.status = newStatus;
        this.taskType = TaskType.TASK;
        setId(InMemoryTaskManager.getNewIndex());
        this.id = getId();

        this.result = "Task {" +
                "title '" + title + '\'' +
                ", description '" + description + '\'' +
                ", status '" + status + '\'' +
                ", id '" + id;

    }


    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public String toString() {
        return result + "' }";
    }

}
