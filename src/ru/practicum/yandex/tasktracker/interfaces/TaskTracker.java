package ru.practicum.yandex.tasktracker.interfaces;


import ru.practicum.yandex.tasktracker.tasks.*;

public interface TaskTracker {

    enum TaskType {
        TASK,
        EPIC,
        SUB,

        NULL

    }


    void addTask(Task task);

    void addSub(SubTask sub);

    void addEpic(EpicTask epic);


    void updateTask(Task task);

    void updateSub(SubTask sub);

    void showTasks(Task task);

    void totalRemove();

    Task getAnyTask(int id);

    TaskType checkExistence(int id);


}
