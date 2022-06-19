public class SubTask extends Task {

    protected int epicId;

    public SubTask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        id = ++TaskTracker.index;
    }


    public int getEpicId() {
        return epicId;
    }

    public int getId() {
        return id;
    }


    public String toString() {
        return "Sub Task {" +
                "title '" + title + '\'' +
                ", description '" + description + '\'' +
                ", status '" + status + '\'' +
                ", id '" + id + "' }";
    }

}
