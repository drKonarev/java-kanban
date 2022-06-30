package Interfaces;

import Managers.InMemoryTaskManager;
import Tasks.*;

public interface TaskTracker {

     enum Class{
        TASK,
        EPIC,
        SUB
    }

    void addTask(Task task);

    void addSub(SubTask sub);

    void addEpic(EpicTask epic);


    void updateTask(Task task, int taskId);

    void updateSub(SubTask sub, Integer id);

    void showTasks(Task task);

    void totalRemove();

    Task getTask(int id);

    SubTask getSubTask(int id);

    EpicTask getEpicTask(int id);



    Class checkExistence(int id);


}
