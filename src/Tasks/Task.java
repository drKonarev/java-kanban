package Tasks;

import Managers.*;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Status status;

    public enum Status {
        NEW,
        DONE,
        IN_PROGRESS
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task(String title, String description, Status newStatus) {
        this.title = title;
        this.description = description;
        this.status = newStatus;

        setId(InMemoryTaskManager.getNewIndex());
        this.id = getId();
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
        String result =  "Task {" +
                "title '" + title + '\'' +
                ", description '" + description + '\'' +
                ", status '" + status + '\'' +
                ", id '" + id + "' }";
        return result;
    }

}
