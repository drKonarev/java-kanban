package ru.practicum.yandex.tasktracker.tasks;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.yandex.tasktracker.managers.InMemoryTaskManager;
import ru.practicum.yandex.tasktracker.managers.Managers;


class EpicTaskTest {
    InMemoryTaskManager manager =(InMemoryTaskManager) Managers.getDefault();
    EpicTask sut;// system under test
    SubTask test1 = new SubTask("test1", "test1", Task.Status.NEW, 1, "15. 10. 2022; 12:00", 60);
    SubTask test2 = new SubTask("test2", "test2", Task.Status.NEW, 1, "16. 10. 2022; 12:00", 60);
    SubTask test3 = new SubTask("test3", "test3", Task.Status.DONE, 1, "17. 10. 2022; 12:00", 60);
    SubTask test4 = new SubTask("test4", "test4", Task.Status.DONE, 1, "18. 10. 2022; 12:00", 60);
    SubTask test5 = new SubTask("test4", "test4", Task.Status.IN_PROGRESS, 1, "19. 10. 2022; 12:00", 60);


    @BeforeEach
    public void beforeEach() {
        sut = new EpicTask("test", "test", 1);
        manager.addAnyTask(sut);
    }

    @Test
    public void emptySubList() {
        Assertions.assertEquals(Task.Status.NEW, sut.getStatus());
    }

    @Test
    public void allSubsAreNew() {
        manager.addAnyTask(test1);
        manager.addAnyTask(test2);
        Assertions.assertEquals(Task.Status.NEW, sut.getStatus());
    }

    @Test
    public void allSubsAreDone() {

        manager.addAnyTask(test3);
        manager.addAnyTask(test4);

        Assertions.assertEquals(Task.Status.DONE, sut.getStatus());

    }

    @Test
    public void subsAreDoneAndNew() {
        manager.addAnyTask(test1);
        manager.addAnyTask(test4);

        Assertions.assertEquals(Task.Status.IN_PROGRESS, sut.getStatus());
    }

    @Test
    public void statusInProgress() {
        manager.addAnyTask(test1);
        manager.addAnyTask(test5);
        Assertions.assertEquals(Task.Status.IN_PROGRESS, sut.getStatus());

    }
}