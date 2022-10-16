package ru.practicum.yandex.tasktracker.managers;

import com.sun.source.tree.Tree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;

import java.util.Set;
import java.util.TreeSet;

import static ru.practicum.yandex.tasktracker.tasks.Task.Status.DONE;
import static ru.practicum.yandex.tasktracker.tasks.Task.Status.NEW;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager(), new InMemoryTaskManager());
    }

    @Test
    void testPriorityList() {

        Assertions.assertEquals(manager.getPriorityList(), Set.of(
                sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60),
                task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60)
        ));
    }
}