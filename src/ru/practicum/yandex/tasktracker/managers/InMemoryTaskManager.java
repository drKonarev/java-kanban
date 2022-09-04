package ru.practicum.yandex.tasktracker.managers;

import java.util.ArrayList;
import java.util.HashMap;

import ru.practicum.yandex.tasktracker.interfaces.*;
import ru.practicum.yandex.tasktracker.tasks.*;

public class InMemoryTaskManager implements TaskTracker {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subs = new HashMap<>();
    protected HashMap<Integer, EpicTask> epics = new HashMap<>();


    protected static int index = 1;

    private HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }


    public static int getNewIndex() {
        return ++index;
    }

    String emptyFile = " Передаваемый объект пуст.";

    @Override
    public void addAnyTask(Task task) {
        switch (task.getTaskType()) {
            case TASK:
                tasks.put((task.getId()), task);
                break;
            case EPIC:
                epics.put(task.getId(), (EpicTask) task);
                break;
            case SUB:
                subs.put(task.getId(), (SubTask) task);
                if (checkExistence((subs.get(task.getId()).getEpicId())) == TaskType.EPIC) {
                    EpicTask epic = epics.get(subs.get(task.getId()).getEpicId());
                    epic.addSubTask(task.getId());
                    updateEpicStatus(epic);
                }
        }
    }




    @Override
    public void remove(Integer id) {
        switch (checkExistence(id)) {
            case TASK:
                historyManager.remove(id);
                tasks.remove(id);

                break;
            case SUB:
                historyManager.remove((Integer) id);
                int epicId = subs.get(id).getEpicId();
                EpicTask epic = epics.get(epicId);
                epic.getSubTasksList().remove(id);
                subs.remove(id);
                updateEpicStatus(epic);
                break;
            case EPIC:
                epic = epics.get(id);
                epics.remove(id);
                historyManager.remove(id);
                for (int subId : epic.getSubTasks()) {
                    historyManager.remove(subId);
                    subs.remove(subId);
                }
                break;
            default:
                System.out.println(" Невозможно удалить задачу с указанным id. Проверьте id.");


        }

    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            System.out.println(emptyFile);
            return;
        }
        if (checkExistence(task.getId()) == TaskType.TASK) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println(" Невозможно обновить задачу. Проверьте id.");
        }

    }

    @Override
    public void updateSub(SubTask sub) {
        if (sub == null) {
            System.out.println(" Невозможно обновить подзадачу.");
            System.out.println(emptyFile);
            return;
        }
        if (checkExistence(sub.getId()) == TaskType.SUB) {
            sub.setId(sub.getId());
            subs.put(sub.getId(), sub);
            EpicTask epic = epics.get(subs.get(sub.getId()).getEpicId());
            updateEpicStatus(epic);
        } else {
            System.out.println(" Невозможно обновить подзадачу. Проверьте id.");
        }
    }

    private void updateEpicStatus(EpicTask epic) {
        if (epic == null) {
            System.out.println(emptyFile);
            return;
        }
        ArrayList<EpicTask.Status> actualStatus = new ArrayList<>();
        for (int subId : epic.getSubTasks()) {
            SubTask sub = subs.get(subId);
            if (subs.get(subId) != null) {
                actualStatus.add(sub.getStatus());
            }
        }
        if (!actualStatus.contains(Task.Status.NEW)) {
            if (actualStatus.contains(Task.Status.IN_PROGRESS)) {
                epic.setStatus(Task.Status.IN_PROGRESS);
            } else {
                epic.setStatus(Task.Status.DONE);
            }
        } else {
            if (!actualStatus.contains(Task.Status.IN_PROGRESS) && !actualStatus.contains(Task.Status.DONE)) {
                epic.setStatus(Task.Status.NEW);
            } else {
                epic.setStatus(Task.Status.IN_PROGRESS);
            }
        }
    }


    @Override
    public void showTasks(Task object) {
        switch (object.getClass().getSimpleName()) {
            case "Task":
                System.out.println("\n + Список задач : \n");
                for (Task task : tasks.values()) {
                    System.out.println("#" + task.getTitle() + " (id: " + task.getId() + ")");
                }
                break;
            case "EpicTask":
                System.out.println("\n Список эпик-задач :  \n");
                for (EpicTask epicTask : epics.values()) {
                    System.out.println("$" + epicTask.getTitle() + " (id: " + epicTask.getId() + ")");
                }
                break;
            case "SubTask":
                System.out.println("\n Список подзадач :  \n");
                for (SubTask subTask : subs.values()) {
                    System.out.println("*" + subTask.getTitle() + " (id: " + subTask.getId() + ")");
                }
                break;


        }
    }

    @Override
    public void totalRemove() {
        tasks.clear();
        subs.clear();
        epics.clear();
        historyManager.removeAllHistory();
        System.out.println("Выполнено полное удаление!");
    }

    public void showSubList(int epicId) {
        if (checkExistence(epicId).equals(TaskType.EPIC)) {
            EpicTask epic = epics.get(epicId);
            System.out.println("\n Список всех подзадач указанного эпика: \n");
            for (int subId : epic.getSubTasks()) {
                System.out.println(subs.get(subId).getTitle() + " (id: " + subId + ")");
            }
        } else {
            System.out.println("Невозможно показать список подзадач эпик-задачи.");
        }
    }


    @Override
    public Task getAnyTask(int id) {
        switch (checkExistence(id)) {
            case TASK:
                historyManager.add(tasks.get(id));
                return tasks.get(id);

            case SUB:
                historyManager.add(subs.get(id));
                return subs.get(id);

            case EPIC:
                historyManager.add(epics.get(id));
                return epics.get(id);

            default:
                System.out.println("\n С таким id (" + id + ") не найдено ни одной задачи.");
                return null;

        }
    }


    @Override
    public TaskType checkExistence(int id) {
        if (tasks.containsKey(id)) {
            return TaskType.TASK;
        } else {
            if (epics.containsKey(id)) {
                return TaskType.EPIC;
            } else {
                if (subs.containsKey(id)) {
                    return TaskType.SUB;
                } else {
                    System.out.println("\n Задачи с таким id (" + id + ") нет!");
                    return TaskType.NULL;
                }
            }
        }

    }


}


