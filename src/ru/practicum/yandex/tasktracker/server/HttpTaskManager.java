package ru.practicum.yandex.tasktracker.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.practicum.yandex.tasktracker.managers.FileBackedTasksManager;
import ru.practicum.yandex.tasktracker.managers.ManagerSaveException;
import ru.practicum.yandex.tasktracker.managers.Managers;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private static final String TASKS_KEY = "tasks";
    private static final String EPICS_KEY = "epics";
    private static final String SUBS_KEY = "subs";
    private static final String HISTORY_KEY = "history";
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
        } catch (Exception ex) {
            throw new ManagerSaveException();
        }
    }

    private void load() {

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(client.load(TASKS_KEY), taskType);
        for (Task task : tasks) {
            int id = task.getId();
            this.addAnyTask(task);
            if (id > index) {
                setIndex(id);
            }
        }

        Type epicType = new TypeToken<ArrayList<EpicTask>>() {
        }.getType();
        List<EpicTask> epics = gson.fromJson(client.load(EPICS_KEY), epicType);
        for (EpicTask epic : epics) {
            int id = epic.getId();
            this.addAnyTask(epic);
            if (id > index) {
                setIndex(id);
            }
        }

        Type subType = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        List<SubTask> subs = gson.fromJson(client.load(SUBS_KEY), subType);
        for (SubTask sub : subs) {
            int id = sub.getId();
            this.addAnyTask(sub);
            if (id > index) {
                setIndex(id);
            }
        }

        Type historyType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> history = gson.fromJson(client.load(HISTORY_KEY), historyType);
        for (Integer taskId : history) {
            getHistoryManager().getHistory().add(taskId);
        }

    }


}
