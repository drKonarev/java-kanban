public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected String status;

    public Task (String title, String description,  String status) {
        this.title = title;
        this.description = description;
        //this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Task {" +
                "title '" + title + '\'' +
                ", description '" + description + '\'' +
                ", status '" + status +'\'' +
                ", id '" + id + "' }";
    }
}
