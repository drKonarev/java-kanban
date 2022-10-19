package ru.practicum.yandex.tasktracker.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;
import ru.practicum.yandex.tasktracker.managers.InMemoryHistoryManager.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ru.practicum.yandex.tasktracker.tasks.Task.Status.DONE;
import static ru.practicum.yandex.tasktracker.tasks.Task.Status.NEW;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager manager = (InMemoryHistoryManager) Managers.getDefaultHistory();

    /*EpicTask epic = new EpicTask("epic", "epic", 1);
    manager.add(epic);
    SubTask sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60);
    manager.add(sub);
    Task  task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
    manager.add(task);*/
    @Test
    void emptyStoryAdd() {
        EpicTask epic = new EpicTask("epic", "epic", 1);
        manager.add(epic);

        Assertions.assertEquals(manager.getHistory(), List.of(1));
    }

    @Test
    void notEmptyStoryAdd() {
        EpicTask epic = new EpicTask("epic", "epic", 1);
        manager.add(epic);
        SubTask sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60);
        manager.add(sub);
        Task task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
        manager.add(task);

        Assertions.assertEquals(manager.getHistory(), List.of(1, 2, 3));
    }

    @Test
    void check1Node() {
        EpicTask epic = new EpicTask("epic", "epic", 1);
        manager.add(epic);

        Assertions.assertEquals(manager.getHead(), new Node(null, epic, null));
    }

    @Test
    void check2Node() {
        EpicTask epic = new EpicTask("epic", "epic", 1);
        manager.add(epic);
        SubTask sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60);
        manager.add(sub);

        // Assertions.assertEquals(manager.head, new Node(null, epic, new Node()));
        Assertions.assertEquals(manager.nodeMap.keySet(), Set.of(1, 2));
    }


    @Test
    void check3Node() {
        EpicTask epic = new EpicTask("epic", "epic", 1);
        manager.add(epic);
        SubTask sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60);
        manager.add(sub);
        Task task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
        manager.add(task);


        Assertions.assertEquals(manager.nodeMap.keySet(), Set.of(1, 2, 3));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void removeNode(Integer argument) {
        EpicTask epic = new EpicTask("epic", "epic", 1);
        manager.add(epic);
        SubTask sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60);
        manager.add(sub);
        Task task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
        manager.add(task);
        manager.remove(argument);
        switch (argument) {
            case (1):
                Assertions.assertEquals(manager.nodeMap.keySet(), Set.of(2, 3));
                break;
            case (2):
                Assertions.assertEquals(manager.nodeMap.keySet(), Set.of(1, 3));
                break;
            case (3):
                Assertions.assertEquals(manager.nodeMap.keySet(), Set.of(1, 2));
                break;
        }
    }


    @Test
    void returnEmptyStory() {
        Assertions.assertEquals(manager.getHistory(), new ArrayList<>());

    }

    @Test
    void addCopyTestStory() {
        EpicTask epic = new EpicTask("epic", "epic", 1);
        manager.add(epic);
        SubTask sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60);
        manager.add(sub);
        Task task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
        manager.add(task);
        EpicTask epic2 = new EpicTask("epic", "epic", 1);
        manager.add(epic2);

        Assertions.assertEquals(manager.getHistory(), List.of(2, 3, 1));
    }

    @Test
    void addCopyTestNodeMap() {
        EpicTask epic = new EpicTask("epic", "epic", 1);
        manager.add(epic);
        SubTask sub = new SubTask("sub", "sub", NEW, 1, 2, "16. 10. 2022; 12:00", 60);
        manager.add(sub);
        Task task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
        manager.add(task);
        EpicTask epic2 = new EpicTask("epic", "epic", 1);
        manager.add(epic2);

        Assertions.assertEquals(manager.nodeMap.keySet(), Set.of(1, 2, 3));
    }
}