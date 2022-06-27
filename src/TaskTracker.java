import java.util.List;

public interface TaskTracker {

    void addTask(Task task);

    void addSub(SubTask sub);

    void addEpic(EpicTask epic);

    void removeTask(int id);

    void removeSub(Integer id);

    void removeEpic(int id);

    void updateTask(Task task, int taskId);

    void updateSub(SubTask sub, Integer id);

    void showTasks();

    void totalRemove();

    Task getTask(int id);

    SubTask getSubTask(int id);

    EpicTask getEpicTask(int id);

    boolean checkTaskExistence(int taskId);

    boolean checkEpicExistence(int epicId);

    boolean checkSubExistence(int subId);

/*    List<Task> getHistory();

    void add(Task task);

    void showHistory();*/

}
