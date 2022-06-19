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

    public Task(String title, String description, Status newStatus) {
        this.title = title;
        this.description = description;
        this.status = newStatus;
        id = ++TaskTracker.index;
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
        return "Task {" +
                "title '" + title + '\'' +
                ", description '" + description + '\'' +
                ", status '" + status + '\'' +
                ", id '" + id + "' }";
    }

}
