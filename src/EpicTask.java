import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList<Integer> epicTasks = new ArrayList<>();

    public EpicTask(String title, String description) {
        super(title, description, "NEW");
    }


    public ArrayList<Integer> getSubTasks() {
        return epicTasks;
    }

    public void addSubTask(int taskId) {
        epicTasks.add(taskId);

    }

    @Override //
    public String toString() {
        String result = "Epic Task {" +
                "title '" + title + '\'' +
                ", description '" + description + '\'' +
                ", status '" + status + '\'' +
                ", id '" + id + '\'';

        if (!epicTasks.isEmpty()) { // проверяем, что поле не содержит null
            result = result + ", sub tasks amount '" + epicTasks.size() + "' }"; // выводим не значение, а длину
        } else {
            result = result + ", sub tasks list is empty }; ";// выводим информацию, что поле равно null
        }
        return result;

    }

}
