package ru.practicum.yandex.tasktracker.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.practicum.yandex.tasktracker.adapters.LocalDateTimeAdapter;
import ru.practicum.yandex.tasktracker.interfaces.*;
import ru.practicum.yandex.tasktracker.server.HttpTaskManager;
import ru.practicum.yandex.tasktracker.server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;


public class Managers {

    public static TaskTracker getDefault() {
        //return new InMemoryTaskManager();
        return new HttpTaskManager(KVServer.PORT);
    }



    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFile() {
        return new FileBackedTasksManager();
    }

    public static KVServer getKVServer() throws IOException {
        return new KVServer();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
