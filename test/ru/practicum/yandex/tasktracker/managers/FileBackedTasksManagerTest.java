package ru.practicum.yandex.tasktracker.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.yandex.tasktracker.interfaces.TaskTracker;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static ru.practicum.yandex.tasktracker.managers.FileBackedTasksManager.*;


public class FileBackedTasksManagerTest extends TaskManagerTest {
    @BeforeEach
    void before() {
        manager = new FileBackedTasksManager();
        emptyManager = new FileBackedTasksManager();
        setup(manager);
    }

    @Test
    void loadFromEmptyFile() {
        Assertions.assertThrows(FileNotFoundException.class,
                () -> new FileReader(new File(pathFile, "NoExistFile.csv")));
    }

    @Test
    void equals2Managers() {
        manager.getAnyTask(1);
        manager.getAnyTask(2);
        manager.getAnyTask(3);

        emptyManager = loadFromFile(file);

        Assertions.assertEquals(manager.getEachOneTask(), emptyManager.getEachOneTask());
        Assertions.assertEquals(manager.getHistoryManager().getHistory(), emptyManager.getHistoryManager().getHistory());
        Assertions.assertEquals(manager.getPriorityList(), emptyManager.getPriorityList());
    }


}