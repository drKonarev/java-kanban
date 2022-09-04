package ru.practicum.yandex.tasktracker.interfaces;


import ru.practicum.yandex.tasktracker.managers.ManagerSaveException;
import ru.practicum.yandex.tasktracker.tasks.*;

public interface TaskTracker {


    void addAnyTask (Task task);


    void updateTask(Task task);

    void updateSub(SubTask sub);

    void showTasks(Task task);

    void totalRemove();

    void remove (Integer id);

    Task getAnyTask(int id);

    TaskType checkExistence(int id);


}
