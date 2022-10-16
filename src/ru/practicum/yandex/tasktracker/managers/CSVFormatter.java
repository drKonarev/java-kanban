package ru.practicum.yandex.tasktracker.managers;


import ru.practicum.yandex.tasktracker.interfaces.HistoryManager;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;
import ru.practicum.yandex.tasktracker.tasks.TaskType;


import java.util.ArrayList;
import java.util.List;

import static ru.practicum.yandex.tasktracker.tasks.Task.formatter;


public class CSVFormatter {


    protected static List<Integer> history = new ArrayList<>();

    public static String taskToString(Task task) {
        return task.getId() + ", " +
                task.getTaskType().name() + ", " +
                task.getTitle() + ", " +
                task.getStatus() + ", " +
                task.getDescription() + ", " +
                task.getStartTime().format(formatter) + ", " +
                task.getDuration().toMinutes();
    }

    public static String historyToString(HistoryManager historyManager) {
        history.clear();
        history.addAll(historyManager.getHistory());
        if (history.isEmpty()) {
            return "Story is empty!";
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
        if (value == null || value.equals("Story is empty!")) {
            return history;
        } else {
            String[] values = value.split(",");
            for (String id : values) {
                history.add(Integer.parseInt(id));
            }
            return history;
        }
    }

    public static Task taskFromString(String line) {
        final String[] lines = line.split(",");
        final int id = Integer.parseInt(lines[0].trim());
        final TaskType type = TaskType.valueOf(lines[1].trim());
        final String name = lines[2].trim();
        final Task.Status status = Task.Status.valueOf(lines[3].trim());
        final String description = lines[4].trim();
        final String startTime = lines[5].trim();
        final int duration = Integer.parseInt(lines[6].trim());
        switch (type) {
            case TASK:
                InMemoryTaskManager.index = id;
                return new Task(name, description, status, id, startTime, duration);
            case EPIC:
                InMemoryTaskManager.index = id;
                return new EpicTask(name, description, id);
            case SUB:
                InMemoryTaskManager.index = id;
                return new SubTask(name, description, status, Integer.parseInt(lines[7].trim()), id, startTime, duration);
        }
        return null; // заглушка
    }

}






