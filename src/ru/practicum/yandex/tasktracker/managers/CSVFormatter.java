package ru.practicum.yandex.tasktracker.managers;


import ru.practicum.yandex.tasktracker.interfaces.HistoryManager;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;
import ru.practicum.yandex.tasktracker.tasks.TaskType;


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

    public static Task taskFromString(String line){
        final String[] lines = line.split(",");
        final int id = Integer.parseInt(lines[0]);
        final TaskType type = TaskType.valueOf(lines[1]);
        final String name = lines[2];
        final Task.Status status = Task.Status.valueOf(lines[3]);
        final String description = lines[4];
        switch (type) {
            case TASK:
                InMemoryTaskManager.index = id;
                return new Task(name, description, status, id);
            case EPIC:
                InMemoryTaskManager.index = id;
                return new EpicTask(name, description, id);
            case SUB:
                InMemoryTaskManager.index = id;
                return new SubTask(name, description, status, Integer.parseInt(lines[5]), id);
        }
        return null; // заглушка
    }

}






