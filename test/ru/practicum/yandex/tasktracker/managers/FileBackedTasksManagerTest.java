package ru.practicum.yandex.tasktracker.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static ru.practicum.yandex.tasktracker.managers.FileBackedTasksManager.*;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(), new FileBackedTasksManager());
    }

    @Test
    void loadFromEmptyFile() {

        Assertions.assertThrows(FileNotFoundException.class,
                () -> {
                    new FileReader(new File(pathFile, "NoExistFile.csv"));
                });


    }

    @Test
    void equals2Managers() {
        manager.getAnyTask(1);
        manager.getAnyTask(2);
        manager.getAnyTask(3);
        manager.save();
        emptyManager = loadFromFile(file);

        Assertions.assertEquals(manager, emptyManager);
    }


}