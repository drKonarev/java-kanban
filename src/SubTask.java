public class SubTask extends Task {

    protected int epicId;

    public SubTask(String title, String description, String status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId (){
        return epicId;
    }

    public int getId() {
        return id;
    }


    public String toString() {
        return "Sub Task {" + // имя класса
                "title '" + title + '\'' + // поле1=значение1
                ", description '" + description + '\'' + // поле2=значение2
                ", status '" + status + '\'' +
                ", id '" + id + "' }";
    }

}
