package ru.practicum.yandex.tasktracker.managers;

import ru.practicum.yandex.tasktracker.interfaces.*;


public class Managers {

    public static TaskTracker getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


    public static FileBackedTasksManager getDefaultFile() {
        return new FileBackedTasksManager();
    }
}
