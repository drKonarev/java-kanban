import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskTracker{
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subs = new HashMap<>();
    private HashMap<Integer, EpicTask> epics = new HashMap<>();
    static int index = 0;
@Override
    public void addTask(Task task) {
        if (task.getClass()==Task.class) {
            tasks.put((task.getId()), task);
        } else {
            System.out.println("Указан неверный тип для задачи.");
            return;
        }
    }
    @Override
    public void addSub(SubTask sub) {
        subs.put(sub.getId(), sub);
        if (checkEpicExistence(sub.getEpicId())) {
            EpicTask epic = epics.get(sub.getEpicId());
            epic.addSubTask(sub.getId());
            updateEpicStatus(epic);
        } else {
            System.out.println(" Невозможно добавить подзадачу.");
        }

    }
    @Override
    public void addEpic(EpicTask epic) {
        epics.put(epic.getId(), epic);
    }
    @Override
    public void removeTask(int id) {
        if (checkTaskExistence(id)) {
            tasks.remove(id);
        } else {
            System.out.println(" Невозможно удалить задачу.");
        }
    }
    @Override
    public void removeSub(Integer id) {
        if (checkSubExistence(id)) {
            int epicId = subs.get(id).getEpicId();
            EpicTask epic = epics.get(epicId);
            epic.subTasksList.remove(id);
            subs.remove(id);
            updateEpicStatus(epic);
        } else {
            System.out.println(" Невозможно удалить подзадачу.");
        }
    }
    @Override
    public void removeEpic(int id) {
        if (checkEpicExistence(id)) {
            EpicTask epic = epics.get(id);
            epics.remove(id);

            for (int subId : epic.getSubTasks()) {
                subs.remove(subId);
            }
        } else {
            System.out.println(" Невозможно удалить эпик-задачу.");

        }
    }
    @Override
    public void updateTask(Task task, int taskId) {
        if (task == null) {
            System.out.println(" Передаваемый объект пуст.");
            return;
        }
        if (checkTaskExistence(taskId)) {
            tasks.put(taskId, task);
        } else {
            System.out.println(" Невозможно обновить задачу.");
        }

    }
    @Override
    public void updateSub(SubTask sub, Integer id) {
        if (sub == null) {
            System.out.println(" Невозможно обновить подзадачу.");
            System.out.println(" Передаваемый объект пуст.");
            return;
        }
        if (checkSubExistence(id)) {
            sub.setId(id);
            subs.put(id, sub);
            EpicTask epic = epics.get(subs.get(id).getEpicId());
            updateEpicStatus(epic);
        } else {
            System.out.println(" Невозможно обновить подзадачу.");
        }
    }

    private void updateEpicStatus(EpicTask epic) {
        if (epic == null) {
            System.out.println(" Передаваемый объект пуст.");
            return;
        }
        ArrayList<EpicTask.Status> actualStatus = new ArrayList<>();
        for (int subId : epic.getSubTasks()) {
            SubTask sub = subs.get(subId);
            if (subs.get(subId) != null) {
                actualStatus.add(sub.getStatus());
            }
        }
        if (!actualStatus.contains(EpicTask.Status.NEW)) {
            if (actualStatus.contains(EpicTask.Status.IN_PROGRESS)) {
                epic.setStatus(EpicTask.Status.IN_PROGRESS);
            } else {
                epic.setStatus(EpicTask.Status.DONE);
                //EpicTask.Status status = EpicTask.Status.IN_PROGRESS;
            }
        } else {
            if (!actualStatus.contains(EpicTask.Status.IN_PROGRESS) && !actualStatus.contains(EpicTask.Status.DONE)) {
                epic.setStatus(EpicTask.Status.NEW);
            } else {
                epic.setStatus(EpicTask.Status.IN_PROGRESS);
            }
        }
    }
    @Override
    public void showTasks() {
        System.out.println("\n + Список задач : \n");
        for (Task task : tasks.values()) {
            System.out.println("#" + task.title + " (id: " + task.id + ")");
        }
        System.out.println("\n Список эпик-задач :  \n");
        for (EpicTask epicTask : epics.values()) {
            System.out.println("$" + epicTask.title + " (id: " + epicTask.id + ")");
        }
        System.out.println("\n Список подзадач :  \n");
        for (SubTask subTask : subs.values()) {
            System.out.println("*" + subTask.title + " (id: " + subTask.id + ")");
        }
    }
    @Override
    public void totalRemove() {
        tasks.clear();
        subs.clear();
        epics.clear();
    }

    public void showSubList(int epicId) {
        if (checkEpicExistence(epicId)) {
            EpicTask epic = epics.get(epicId);
            System.out.println("\n Список всех подзадач указанного эпика: \n");
            for (int subId : epic.getSubTasks()) {
                System.out.println(subs.get(subId).title + " (id: " + subId + ")");
            }
        } else {
            System.out.println("Невозможно показать список подзадач эпик-задачи.");
        }
    }
    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) return tasks.get(id);
        else {
            System.out.println("\n С таким id (" + id + ") не найдено ни одной задачи.");
            return null;
        }
    }
    @Override
    public SubTask getSubTask(int id) {
        if (subs.containsKey(id)) return subs.get(id);
        else {
            System.out.println("\n С таким id (" + id + ") не найдено ни одной подзадачи.");
            return null;
        }
    }
    @Override
    public EpicTask getEpicTask(int id) {
        if (epics.containsKey(id)) return epics.get(id);
        else {
            System.out.println("\n С таким id (" + id + ") не найдено ни одной эпик-задачи.");
            return null;
        }
    }
    @Override
    public boolean checkTaskExistence(int taskId) {
        for (Task task : tasks.values()) {
            if (task.id == taskId) {
                return true;
            } else {
                System.out.println("\n Задачи с таким id (" + taskId + ") нет!");
                return false;
            }
        }
        return false;
    }
    @Override
    public boolean checkEpicExistence(int epicId) {
        for (EpicTask epic : epics.values()) {
            if (epic.id == epicId) {
                return true;
            }

        }
        System.out.println("\n Эпик-задачи с таким id (" + epicId + ") нет!");
        return false;
    }
    @Override
    public boolean checkSubExistence(int subId) {
        for (SubTask sub : subs.values()) {
            if (sub.id == subId) {
                return true;
            } else {
                System.out.println("\n Подзадачи с таким id (" + subId + ") нет!");
                return false;
            }
        }
        return false;
    }

}


