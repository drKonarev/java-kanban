package Managers;

import java.util.ArrayList;
import java.util.HashMap;

import Interfaces.*;
import Tasks.*;

public class InMemoryTaskManager implements TaskTracker {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subs = new HashMap<>();
    private HashMap<Integer, EpicTask> epics = new HashMap<>();
    public static int index = 1;

    public HistoryManager historyManager = Managers.getDefaultHistory();


    public static int getNewIndex() {
        return index++;
    }



    @Override
    public void addTask(Task task) {
        if (task.getClass() == Task.class) {
            tasks.put((task.getId()), task);

        } else {
            System.out.println("Указан неверный тип для задачи.");
        }
    }

    @Override
    public void addSub(SubTask sub) {
        subs.put(sub.getId(), sub);
        if (checkExistence(sub.getEpicId()) == Class.EPIC) {
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

    public void remove(Integer id) {
        if (checkExistence(id) != null) {
            switch (checkExistence(id)) {
                case TASK:
                    historyManager.remote(id);
                    tasks.remove(id);

                    break;
                case SUB:
                    historyManager.remote(id);
                    int epicId = subs.get(id).getEpicId();
                    EpicTask epic = epics.get(epicId);
                    epic.getSubTasksList().remove(id);
                    subs.remove(id);
                    updateEpicStatus(epic);
                    break;
                case EPIC:
                    epic = epics.get(id);
                    epics.remove(id);
                    historyManager.remote(id);
                    for (int subId : epic.getSubTasks()) {
                        historyManager.remote(subId);
                        subs.remove(subId);
                    }
                    break;


            }
        } else System.out.println(" Невозможно задачу с указанным id. Проверьте id.");
    }

    @Override
    public void updateTask(Task task, int taskId) {
        if (task == null) {
            System.out.println(" Передаваемый объект пуст.");
            return;
        }
        if (checkExistence(taskId) == Class.TASK) {
            tasks.put(taskId, task);
        } else {
            System.out.println(" Невозможно обновить задачу. Проверьте id.");
        }

    }

    @Override
    public void updateSub(SubTask sub, Integer id) {
        if (sub == null) {
            System.out.println(" Невозможно обновить подзадачу.");
            System.out.println(" Передаваемый объект пуст.");
            return;
        }
        if (checkExistence(id) == Class.SUB) {
            sub.setId(id);
            subs.put(id, sub);
            EpicTask epic = epics.get(subs.get(id).getEpicId());
            updateEpicStatus(epic);
        } else {
            System.out.println(" Невозможно обновить подзадачу. Проверьте id.");
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
        historyManager.totalRemote();
    }

    public void showSubList(int epicId) {
        if (checkExistence(epicId) == Class.EPIC) {
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
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("\n С таким id (" + id + ") не найдено ни одной задачи.");
            return null;
        }
    }

    @Override
    public SubTask getSubTask(int id) {
        if (subs.containsKey(id)) {
            historyManager.add(subs.get(id));
            return subs.get(id);
        } else {
            System.out.println("\n С таким id (" + id + ") не найдено ни одной подзадачи.");
            return null;
        }
    }

    @Override
    public EpicTask getEpicTask(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("\n С таким id (" + id + ") не найдено ни одной эпик-задачи.");
            return null;
        }
    }

    @Override
    public Class checkExistence(int id) {
        boolean isExistence = false;
        for (Task task : tasks.values()) {
            if (task.getId() == id) {
                isExistence = true;
                return Class.TASK;
            }
        }
        for (EpicTask epic : epics.values()) {
            if (epic.getId() == id) {
                isExistence = true;
                return Class.EPIC;
            }
        }
        for (SubTask sub : subs.values()) {
            if (sub.getId() == id) {
                isExistence = true;
                return Class.SUB;
            }
        }
        if (isExistence = false) {
            System.out.println("\n Задачи с таким id (" + id + ") нет!");
            return null;
        }
        return null;
    }


}


