package ru.practicum.yandex.tasktracker.interfaces;


import ru.practicum.yandex.tasktracker.tasks.*;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskTracker {
    TreeSet<Task> getPriorityList();

    List<Task> getEachOneTask();

    void addAnyTask(Task task);

    void updateTask(Task task);

    List<SubTask> getSubByEpic(int id);

    void updateSub(SubTask sub);

    void showTasks(Task task);

    void totalRemove();

    HashMap<Integer, EpicTask> getEpics();

    HashMap<Integer, SubTask> getSubs();

    HashMap<Integer, Task> getTasks();

    void remove(Integer id);

    Task getAnyTask(int id);

    TaskType checkExistence(int id);

    List<Integer> getHistory();


    HistoryManager getHistoryManager();

    void printPriorityList();

    void load();
}
