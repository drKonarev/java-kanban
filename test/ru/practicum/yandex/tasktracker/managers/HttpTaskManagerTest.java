package ru.practicum.yandex.tasktracker.managers;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import com.google.gson.Gson;
import ru.practicum.yandex.tasktracker.interfaces.TaskTracker;
import ru.practicum.yandex.tasktracker.server.HttpTaskManager;
import ru.practicum.yandex.tasktracker.server.KVServer;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

import static ru.practicum.yandex.tasktracker.managers.HttpTaskServer.TASK_SERVER_PORT;
import static ru.practicum.yandex.tasktracker.tasks.Task.Status.DONE;
import static ru.practicum.yandex.tasktracker.tasks.Task.Status.NEW;

class HttpTaskManagerTest extends TaskManagerTest {

    private KVServer kvServer;
    private HttpTaskServer taskServer;
    private TaskTracker manager;
    private TaskTracker emptyManager;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {

        kvServer = Managers.getKVServer();

        kvServer.start();

        gson = Managers.getGson();

        manager = new HttpTaskManager(KVServer.PORT);

        taskServer = new HttpTaskServer(manager);

        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
        taskServer.stop();
    }

    @Disabled
    @Test
    void save() {
    }

    @Test
    void addTaskTest() {
        task = new Task("task", "task", DONE, 1, "15. 10. 2022; 12:00", 60);
        manager.addAnyTask(task);
        epic = new EpicTask("epic", "epic", 2);
        manager.addAnyTask(epic);
        sub = new SubTask("sub", "sub", NEW, 2, 3, "16. 10. 2022; 12:00", 60);

        manager.addAnyTask(sub);
        manager.getAnyTask(task.getId());

        Assertions.assertEquals(1, manager.getHistoryManager().getHistory().size());
        Assertions.assertEquals(task, manager.getAnyTask(task.getId()));
        Assertions.assertNotNull(task);
    }

    @Test
    void load() {

        task = new Task("task", "task", DONE, 1, "15. 10. 2022; 12:00", 60);
        manager.addAnyTask(task);

        epic = new EpicTask("epic", "epic", 2);
        manager.addAnyTask(epic);

        sub = new SubTask("sub", "sub", NEW, 2, 3, "16. 10. 2022; 12:00", 60);
        manager.addAnyTask(sub);

        manager.getAnyTask(task.getId());

        emptyManager = new HttpTaskManager(KVServer.PORT);

        emptyManager.load();

        Assertions.assertEquals(manager.getHistory(), emptyManager.getHistory());
        Assertions.assertEquals(manager.getEachOneTask(), emptyManager.getEachOneTask());
        Assertions.assertEquals(manager.getHistoryManager().getHistory(), emptyManager.getHistoryManager().getHistory());
    }

    @Test
    void imitateRequest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
        HttpRequest taskCreate = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/task/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        HttpResponse<String> response = client.send(taskCreate, HttpResponse.BodyHandlers.ofString());
        System.out.println("taskCreate " + response.statusCode());


        HttpRequest getTaskRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/task/?id=" + task.getId()))
                .GET()
                .build();
        response = client.send(getTaskRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("getTask " + response.statusCode());

        Task testTask = gson.fromJson(client.send(getTaskRequest, HttpResponse.BodyHandlers.ofString()).body(), Task.class);
        Assertions.assertEquals(task, testTask);

        epic = new EpicTask("epic", "epic", 1);
        HttpRequest epicCreate = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/epictask/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        response = client.send(epicCreate, HttpResponse.BodyHandlers.ofString());
        System.out.println("epicCreate " + response.statusCode());

        HttpRequest getEpicRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/epictask/?id=" + epic.getId()))
                .GET()
                .build();
        response = client.send(getEpicRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("getEpic " + response.statusCode());

        EpicTask testEpic = gson.fromJson(client.send(getEpicRequest, HttpResponse.BodyHandlers.ofString()).body(), EpicTask.class);
        Assertions.assertEquals(epic, testEpic);

        sub = new SubTask("sub", "sub", NEW, 1, "16. 10. 2022; 12:00", 60);
        HttpRequest subCreate = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/subtask/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub)))
                .build();
        response = client.send(subCreate, HttpResponse.BodyHandlers.ofString());
        System.out.println("subCreate " + response.statusCode());

        HttpRequest getSubRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/subtask/?id=" + sub.getId()))
                .GET()
                .build();
        response = client.send(subCreate, HttpResponse.BodyHandlers.ofString());
        System.out.println("subCreate " + response.statusCode());
        SubTask testSub = gson.fromJson(client.send(getSubRequest, HttpResponse.BodyHandlers.ofString()).body(), SubTask.class);
        Assertions.assertEquals(sub, testSub);


        HttpRequest getTasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/task/"))
                .GET()
                .build();
        response = client.send(getTaskRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("getTaskS " + response.statusCode());

        HttpRequest getEpicsRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/epictask/"))
                .GET()
                .build();
        response = client.send(getEpicsRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("getEpicS " + response.statusCode());

        HttpRequest getSubtasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/subtask/"))
                .GET()
                .build();
        response = client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("getSubS" + response.statusCode());


        Type newType = new TypeToken<HashMap<Integer, Task>>() {
        }.getType();
        HashMap<Integer, Task> tasksResponse = gson.fromJson(
                client.send(getTasksRequest, HttpResponse.BodyHandlers.ofString()).body(), newType);
        Assertions.assertEquals(1, tasksResponse.size());

        newType = new TypeToken<HashMap<Integer, EpicTask>>() {
        }.getType();
        HashMap<Integer, EpicTask> epicsResponse = gson.fromJson(
                client.send(getEpicsRequest, HttpResponse.BodyHandlers.ofString()).body(), newType);
        Assertions.assertEquals(1, epicsResponse.size());

        newType = new TypeToken<HashMap<Integer, SubTask>>() {
        }.getType();
        HashMap<Integer, SubTask> subtasksResponse = gson.fromJson(
                client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString()).body(), newType);
        Assertions.assertEquals(1, subtasksResponse.size());



    }
}