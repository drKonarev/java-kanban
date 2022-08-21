package ru.practicum.yandex.tasktracker.managers;


import ru.practicum.yandex.tasktracker.interfaces.HistoryManager;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;


import java.util.ArrayList;
import java.util.List;


public class CSVFormatter {


    protected static List<Integer> history = new ArrayList<>();

    public static String taskToString(Task task) {
        return task.getId() + "," +
                task.getTaskType().name() + "," +
                task.getTitle() + "," +
                task.getStatus() + "," +
                task.getDescription();
    }

    public static String historyToString(HistoryManager historyManager) {
        history.clear();
        history.addAll(historyManager.getHistory());
        if (history.isEmpty()) {
            return "История вызова задач пока что пуста!";
        }
        StringBuilder line = new StringBuilder();
        for (int i : history) {
            line.append(i).append(",");
        }
        line.deleteCharAt(line.length() - 1);
        return line.toString();
    }

    public static List<Integer> historyFromString(String value) {
        history.clear();
        String[] values = value.split(",");
        for (String id : values) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }

    public static Task taskFromString(String line) {
        String[] lines = line.split(",");
        int id = Integer.parseInt(lines[0]);
        String nameTask = lines[2];
        Task.Status taskStatus = Task.Status.NEW;
        if (lines[3].equals("IN_PROGRESS")) {
            taskStatus = Task.Status.IN_PROGRESS;
        } else {
            if (lines[3].equals("DONE")) {
                taskStatus = Task.Status.DONE;
            }
        }
        String description = lines[4];
        Task task = new Task(nameTask, description, taskStatus);
        task.setId(id);
        InMemoryTaskManager.index = id;
        return task;
    }

    public static SubTask subFromString(String line) {
        String[] lines = line.split(",");
        int id = Integer.parseInt(lines[0]);
        String nameTask = lines[2];
        Task.Status taskStatus = Task.Status.NEW;
        if (lines[3].equals("IN_PROGRESS")) {
            taskStatus = Task.Status.IN_PROGRESS;
        } else {
            if (lines[3].equals("DONE")) {
                taskStatus = Task.Status.DONE;
            }
        }
        String description = lines[4];
        SubTask task = new SubTask(nameTask, description, taskStatus, Integer.parseInt(lines[5]));
        task.setId(id);
        InMemoryTaskManager.index = id;

        return task;
    }

    public static EpicTask epicFromString(String line) {
        String[] lines = line.split(",");
        int id = Integer.parseInt(lines[0]);
        String nameTask = lines[2];
        String description = lines[4];
        EpicTask task = new EpicTask(nameTask, description);
        task.setId(id);
        InMemoryTaskManager.index = id;
        return task;
    }
}






