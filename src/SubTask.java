public class SubTask extends Task {


    public SubTask(String title, String description, String status) {
        super(title, description, status);
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
