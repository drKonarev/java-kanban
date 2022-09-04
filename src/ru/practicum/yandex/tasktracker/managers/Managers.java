package ru.practicum.yandex.tasktracker.managers;

import ru.practicum.yandex.tasktracker.interfaces.*;


public class Managers {

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


    public static FileBackedTasksManager getDefaultFile() {
        return new FileBackedTasksManager();
    }
}
