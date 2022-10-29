package ru.practicum.yandex.tasktracker.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.source.tree.Tree;
import ru.practicum.yandex.tasktracker.managers.FileBackedTasksManager;
import ru.practicum.yandex.tasktracker.managers.ManagerLoadException;
import ru.practicum.yandex.tasktracker.managers.ManagerSaveException;
import ru.practicum.yandex.tasktracker.managers.Managers;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class HttpTaskManager extends FileBackedTasksManager {

    private static final String TASKS_KEY = "tasks";
    private static final String EPICS_KEY = "epics";
    private static final String SUBS_KEY = "subs";
    private static final String HISTORY_KEY = "history";

    private static final String PRIORITY_KEY = "priority";
    private KVTaskClient client;
    private Gson gson;


    public HttpTaskManager(int port) {
        super();
        gson = Managers.getGson();
        client = new KVTaskClient(port);
    }

    @Override
    public void save() throws ManagerSaveException {
        try {
            String tasksJson = gson.toJson(getTasks());
            client.put(TASKS_KEY, tasksJson);

            String epicsJson = gson.toJson(getEpics());

            client.put(EPICS_KEY, epicsJson);

            String subsJson = gson.toJson(getSubs());
            client.put(SUBS_KEY, subsJson);

            String historyJson = gson.toJson(getHistory());
            client.put(HISTORY_KEY, historyJson);

            String priorityJson = gson.toJson(getPriorityList());
            client.put(PRIORITY_KEY, priorityJson);

        } catch (Exception ex) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public void load() {
        try {
            Type taskType = new TypeToken<HashMap<Integer, Task>>() {
            }.getType();
            this.tasks = gson.fromJson(client.load(TASKS_KEY), taskType);

            Type epicType = new TypeToken<HashMap<Integer, EpicTask>>() {
            }.getType();
            this.epics = gson.fromJson(client.load(EPICS_KEY), epicType);

            Type subType = new TypeToken<HashMap<Integer, SubTask>>() {
            }.getType();
            this.subs = gson.fromJson(client.load(SUBS_KEY), subType);

            Type historyType = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            List<Integer> history = gson.fromJson(client.load(HISTORY_KEY), historyType);
            this.getHistoryManager().getHistory().addAll(history);

            Type priorityType = new TypeToken<List<Task>>() {
            }.getType();
            List<Task> priorityList = gson.fromJson(client.load(PRIORITY_KEY), priorityType);
            this.priorityList.addAll(priorityList);
        } catch (Exception ex) {
            throw new ManagerLoadException();
        }


    }


}
