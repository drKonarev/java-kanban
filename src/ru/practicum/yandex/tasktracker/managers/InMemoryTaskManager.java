package ru.practicum.yandex.tasktracker.managers;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.junit.jupiter.api.Disabled;
import ru.practicum.yandex.tasktracker.interfaces.*;
import ru.practicum.yandex.tasktracker.tasks.*;

public class InMemoryTaskManager implements TaskTracker {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy; HH:mm");

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubs() {
        return subs;
    }

    public HashMap<Integer, EpicTask> getEpics() {
        return epics;
    }

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subs = new HashMap<>();
    protected HashMap<Integer, EpicTask> epics = new HashMap<>();

    protected TreeSet<Task> priorityList = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public TreeSet<Task> getPriorityList() {
        return priorityList;
    }

    public void printPriorityList() {
        if (priorityList.isEmpty()) {
            System.out.println("No one task wasn't add in manager!");
            return;
        }
        System.out.println("\n Tasks sorted by startTime: \n");
        priorityList.stream()
                .map(Task::getTitleAndIdAndTiming)
                .forEach(System.out::println);
    }

    public static void setIndex(int index) {
        InMemoryTaskManager.index = index;
    }

    protected static int index = 1;

    private HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }


    public static int getNewIndex() {
        return ++index;
    }

    String emptyFile = " Передаваемый объект пуст.";

    protected boolean checkAndAddList(Task task) {
        if (priorityList.isEmpty()) {
            priorityList.add(task);
            return true;
        }
        for (Task current : priorityList) {
            if ((task.getStartTime().isBefore(current.getEndTime()) &&
                    task.getStartTime().isAfter(current.getStartTime())) ||
                    (task.getEndTime().isBefore(current.getEndTime()) &&
                            task.getEndTime().isAfter(current.getStartTime())) ||
                    (task.getStartTime().isBefore(current.getStartTime()) &&
                            task.getEndTime().isAfter(current.getEndTime()))
            ) {
                System.out.println("Current task start earlier then last task end");
                return false;
            }
        }
        priorityList.add(task);
        return true;

    }

    @Override
    public void addAnyTask(Task task) {
        if (task == null) {
            System.out.println("Task is empty, cannot add task.");
            return;
        }
        switch (task.getTaskType()) {
            case TASK:
                if (checkAndAddList(task)) {
                    tasks.put((task.getId()), task);
                    break;
                } else return;
            case EPIC:
                epics.put(task.getId(), (EpicTask) task);
                break;
            case SUB:
                if (checkAndAddList(task)) {
                    subs.put(task.getId(), (SubTask) task);
                    if (checkExistence((subs.get(task.getId()).getEpicId())) == TaskType.EPIC) {
                        EpicTask epic = epics.get(subs.get(task.getId()).getEpicId());
                        epic.addSubTask(task.getId());
                        updateEpicStatus(epic);
                        updateEpicTiming(epic);
                    } else {
                        subs.remove(task.getId());
                    }
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
                historyManager.remove(id);
                int epicId = subs.get(id).getEpicId();
                EpicTask epic = epics.get(epicId);
                epic.getSubTasks().remove(id);
                subs.remove(id);
                updateEpicStatus(epic);
                updateEpicTiming(epic);
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
    public List<SubTask> getSubByEpic(int id) {
        List<SubTask> list = new ArrayList<>();
        if (checkExistence(id) == TaskType.EPIC) {
            if (epics.get(id).getSubTasks().isEmpty()) {
                System.out.println("This epic doesn't contain subs.");
                return list;
            }
            for (int idSub : epics.get(id).getSubTasks()) {
                list.add(subs.get(idSub));
            }
        } else {
            System.out.println("Type this task isn't EPIC.");

        }
        return list;
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
            updateEpicTiming(epic);
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
    public List<Task> getEachOneTask() {
        List<Task> allTasks = new ArrayList<Task>();

        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subs.values());

        return allTasks;
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

    @Override
    public List<Integer> getHistory() {
        return this.getHistoryManager().getHistory();
    }

    @Override
    public String toString() {
        return "Tasks amount - " + tasks.size() + "\n" +
                "SubTasks amount - " + subs.size() + "\n" +
                "EpicTasks amount - " + epics.size() + "\n";
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (tasks != null && epics != null && subs != null) {
            hash = tasks.hashCode() + epics.hashCode() + subs.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        InMemoryTaskManager otherManager = (InMemoryTaskManager) obj;
        return tasks.size() == otherManager.tasks.size() &&
                epics.size() == otherManager.epics.size() &&
                subs.size() == otherManager.subs.size() &&
                equalMaps(epics, otherManager.epics) &&
                equalMaps(tasks, otherManager.tasks) &&
                equalMaps(subs, otherManager.subs);

    }

    @Disabled
    @Override
    public void load() {
    }

    public static <K, V> boolean equalMaps(HashMap<K, V> m1, HashMap<K, V> m2) {
        if (m1.size() != m2.size()) return false;
        for (K key : m1.keySet())
            if (!m1.get(key).equals(m2.get(key)))
                return false;
        return true;
    }

    void updateEpicTiming(EpicTask epic) {
        if (epic.getSubTasks().isEmpty()) {
            System.out.println("Список подзадач пуст!");
            return;
        }
        Duration sumDuration = Duration.ofMinutes(0);

        epic.setStartTime(subs.get(epic.getSubTasks().get(0)).getStartTime());
        epic.setEndTime(subs.get(epic.getSubTasks().get(0)).getEndTime());

        for (int subId : epic.getSubTasks()) {
            if (subs.get(subId).getStartTime().isBefore(epic.getStartTime())) {//проверка на саммое раннее начало задачи
                epic.setStartTime(subs.get(subId).getStartTime());
            }
            if (subs.get(subId).getEndTime().isAfter(epic.getStartTime())) {// проверка на самое позднее окончание задачи
                epic.setEndTime(subs.get(subId).getEndTime());
            }
            sumDuration = sumDuration.plus(subs.get(subId).getDuration());
            epic.setDuration(sumDuration);
        }
        epic.setEndTime(epic.getStartTime().plus(epic.getDuration()));
    }

}




