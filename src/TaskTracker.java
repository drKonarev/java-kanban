import java.util.ArrayList;
import java.util.HashMap;

public class TaskTracker {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subs = new HashMap<>();
    private HashMap<Integer, EpicTask> epics = new HashMap<>();
    int index = 0;

enum Status{
    NEW,
    DONE,
    IN_PROGRESS
}

    public void add(Task task) {
        task.setId(++index);
        tasks.put(task.getId(), task);

    }

    public void add(SubTask task) {
        task.setId(++index);
        subs.put(task.getId(), task);

        EpicTask epic = epics.get(task.getEpicId());
        epic.addSubTask(task.getId());

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
        int epicId = subs.get(id).getEpicId();
        EpicTask epic = epics.get(epicId);
        epic.epicTasks.remove(id);
        subs.remove(id);
        updateEpicStatus(epic);
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
        EpicTask epic = epics.get(subs.get(id).getEpicId());
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(EpicTask epic) {
        ArrayList<String> actualStatus = new ArrayList<>();
        for (int subId : epic.getSubTasks()) {
            SubTask sub = subs.get(subId);
            if (subs.get(subId) != null) {
                actualStatus.add(sub.status);
            }
        }
        if (!actualStatus.contains(Status.NEW)) {
            if (actualStatus.contains(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS.toString());
            } else {
                epic.setStatus(Status.DONE.toString());
            }
        } else {
            if (!actualStatus.contains(Status.IN_PROGRESS) && !actualStatus.contains(Status.DONE)) {
                epic.setStatus(Status.NEW.toString());
            } else {
                epic.setStatus(Status.IN_PROGRESS.toString());
            }
        }
    }

    public void showTasks (){
        System.out.println("\n + Список задач : \n");
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
        if (epics.containsKey(id)) System.out.println('\n'+epics.get(id).toString());
        if (subs.containsKey(id)) System.out.println('\n'+subs.get(id).toString());
        if (tasks.containsKey(id)) System.out.println('\n'+tasks.get(id).toString());

    }
}
