import java.util.ArrayList;
import java.util.HashMap;

public class TaskTracker {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subs = new HashMap<>();
    private HashMap<Integer, EpicTask> epics = new HashMap<>();
    int index = 0;



    public void add(Task task) {
        task.setId(++index);
        tasks.put(task.getId(), task);

    }

    public void add(SubTask task, int epicId) {
        task.setId(++index);
        subs.put(task.getId(), task);// положи эту задачу в мапу с сабами с айди

        EpicTask epic = epics.get(epicId);
        epic.addSubTask(task.getId()); //положи саб в эпик с айди, который мы указали

        updateEpicStatus(epic);
    }

    public void add(EpicTask task) {
        task.setId(++index);
        epics.put(task.getId(), task);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeSub(Integer id) {
        subs.remove(id);
        for (EpicTask epic : epics.values()) {
            if (epic.epicTasks.contains(id)) {
                epic.epicTasks.remove(id);
                updateEpicStatus(epic);
            }
        }
    }

    public void removeEpic(int id) {
        EpicTask epic = epics.get(id);
        epics.remove(id);

        for (int subId : epic.getSubTasks()) {
            subs.remove(subId);
        }
    }

    public void updateTask(Task task, int taskId) {
        tasks.put(taskId, task);
    }

    public void updateSub(SubTask sub, Integer id) {
        sub.setId(id);
        subs.put(id, sub);
        for (EpicTask epic : epics.values()) {
            if (epic.epicTasks.contains(id)) {
                updateEpicStatus(epic);
            }
        }
    }

    private void updateEpicStatus(EpicTask epic) {
        ArrayList<String> actualStatus = new ArrayList<>();
        for (int subId : epic.getSubTasks()) {
            SubTask sub = subs.get(subId);
            if (subs.get(subId) != null) {
                actualStatus.add(sub.status);
            } else {
                continue;
            }
        }
        if (!actualStatus.contains("NEW")) {
            if (actualStatus.contains("IN_PROGRESS")) {
                epic.setStatus("IN_PROGRESS");
            } else {
                epic.setStatus("DONE");
            }
        } else {
            if (!actualStatus.contains("IN_PROGRESS") && !actualStatus.contains("DONE")) {
                epic.setStatus("NEW");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        }
    }

    public void showTasks (){
        System.out.println(" Список задач : \n");
        for(Task task: tasks.values() ){
            System.out.println("#" + task.title);
        }
        System.out.println("\n Список эпик-задач :  \n");
        for(EpicTask epicTask: epics.values() ){
            System.out.println("$" + epicTask.title);
        }
        System.out.println("\n Список подзадач :  \n");
        for(SubTask subTask: subs.values() ){
            System.out.println("*" + subTask.title);
        }
    }

    public void totalRemove (){
        tasks.clear();
        subs.clear();
        epics.clear();
    }

    public void showSubList (int epicId){
        EpicTask epic =  epics.get(epicId);
        System.out.println("\n Список всех подзадач указанного эпика: \n");
            for(int subId : epic.getSubTasks()) {
                System.out.println(subs.get(subId).title);
            }
    }

    public void getAnyTask (int id){
        if (epics.containsKey(id)) System.out.println(epics.get(id).toString());
        if (subs.containsKey(id)) System.out.println(subs.get(id).toString());
        if (tasks.containsKey(id)) System.out.println(tasks.get(id).toString());

    }
}
