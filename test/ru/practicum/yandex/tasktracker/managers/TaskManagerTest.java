package ru.practicum.yandex.tasktracker.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.yandex.tasktracker.interfaces.TaskTracker;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;


import static ru.practicum.yandex.tasktracker.tasks.Task.Status.*;

public abstract class TaskManagerTest {

     TaskTracker manager;
     TaskTracker emptyManager;

    EpicTask epic;
    SubTask sub;
    Task task;


    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        emptyManager = new InMemoryTaskManager();
        setup(manager);

    }

    public void setup(TaskTracker manager) {
        epic = new EpicTask("epic", "epic", 1);
        manager.addAnyTask(epic);

        sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60);
        manager.addAnyTask(sub);

        task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
        manager.addAnyTask(task);
    }

    @Test
    void addNotTimingSub() {
        SubTask sub1 = new SubTask("sub1", "sub1", NEW, 1, 6, "16. 10. 2022; 12:30", 60);
        manager.addAnyTask(sub1);

        Assertions.assertEquals(epic.getSubTasks().size(), 1);

    }

    @Test
    void addEmptyTimingTask() {
        Task task1 = new Task("task1", "task1", DONE, 4);
        manager.addAnyTask(task1);

        Assertions.assertEquals(manager.getAnyTask(task1.getId()), task1);

    }

    @Test
    void checkEpicStatus() {
        SubTask sub2 = new SubTask("sub2", "sub2", DONE, 1, 4, "14. 10. 2022; 12:00", 60);
        manager.addAnyTask(sub2);

        Assertions.assertEquals(IN_PROGRESS, manager.getAnyTask(1).getStatus());
    }

    @Test
    void addEpic() {
        EpicTask test = (EpicTask) manager.getAnyTask(epic.getId());
        Assertions.assertEquals(epic.toString(), test.toString());
    }

    @Test
    void addSubWithNotExistenceEpic() {
        SubTask sub3 = new SubTask("sub3", "sub3", IN_PROGRESS, 11, 5, "12. 10. 2022; 12:00", 60);
        manager.addAnyTask(sub3);
        Assertions.assertNull(manager.getAnyTask(5));
    }

    @Test
    void addSub() {
        SubTask test = (SubTask) manager.getAnyTask(sub.getId());
        Assertions.assertEquals(sub.toString(), test.toString());
    }

    @Test
    void addTask() {
        Assertions.assertEquals(task.toString(), manager.getAnyTask(task.getId()).toString());
    }


    @Test
    void updateTask() {
        Task test2 = new Task("test2", "test2", NEW, 3, "15. 10. 2022; 17:00", 60);
        manager.updateTask(test2);

        Assertions.assertEquals(test2.toString(), manager.getAnyTask(3).toString());
    }

    @Test
    void updateSubWithNull() {
        manager.updateSub(null);
    }

    @Test
    void updateSub() {
        SubTask test2 = new SubTask("test2", "test2", NEW, 1, 2, "10. 10. 2022; 12:00", 60);
        manager.updateSub(test2);

        Assertions.assertEquals(test2.toString(), manager.getAnyTask(2).toString());
    }

    @Test
    void addEmptyTask() {
        manager.totalRemove();
        manager.addAnyTask(null);

        Assertions.assertEquals(manager, emptyManager);

    }

    @Test
    void totalRemove() {
        manager.totalRemove();
        Assertions.assertEquals(emptyManager.toString(), manager.toString());
    }


    @Test
    void getNoOneTask() {
        Assertions.assertNull(manager.getAnyTask(25));

    }

}
