package ru.practicum.yandex.tasktracker.managers;

import org.junit.jupiter.api.*;
import com.google.gson.Gson;
import ru.practicum.yandex.tasktracker.interfaces.TaskTracker;
import ru.practicum.yandex.tasktracker.server.HttpTaskManager;
import ru.practicum.yandex.tasktracker.server.KVServer;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static ru.practicum.yandex.tasktracker.tasks.Task.Status.DONE;
import static ru.practicum.yandex.tasktracker.tasks.Task.Status.NEW;

class HttpTaskManagerTest extends TaskManagerTest {

    private KVServer kvServer;
    private HttpTaskServer taskServer;
    private TaskTracker manager;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {

        kvServer = Managers.getKVServer();

        kvServer.start();

        gson = Managers.getGson();

        manager = new HttpTaskManager(KVServer.PORT);

        taskServer = new HttpTaskServer(manager);
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
    void load() {
        task = new Task("task", "task", DONE, 1, "15. 10. 2022; 12:00", 60);
        manager.addAnyTask(task);
        epic = new EpicTask("epic", "epic", 2);
        manager.addAnyTask(epic);
        sub = new SubTask("sub", "sub", NEW, 2, 3, "16. 10. 2022; 12:00", 60);
        manager.addAnyTask(sub);
        //сохраняем задачу
        manager.getAnyTask(task.getId());

        Assertions.assertEquals(1, manager.getHistoryManager().getHistory().size());
        Assertions.assertEquals(task, manager.getAnyTask(task.getId()));
        Assertions.assertNotNull(task);
        // загрузить данные

    }

    @Test
    void imitateRequest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        task = new Task("task", "task", DONE, 3, "15. 10. 2022; 12:00", 60);
        HttpRequest taskCreate = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + KVServer.PORT + "/tasks/task/")) // TASK_SERVER_PORT  KVServer.PORT
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        client.send(taskCreate, HttpResponse.BodyHandlers.ofString());

        epic = new EpicTask("epic", "epic", 1);
        HttpRequest epicCreate = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + KVServer.PORT + "/tasks/epictask/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        client.send(epicCreate, HttpResponse.BodyHandlers.ofString());

        sub = new SubTask("sub", "sub", NEW, 1, "16. 10. 2022; 12:00", 60);
        HttpRequest subCreate = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:" + KVServer.PORT + "/tasks/subtask/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub)))
                .build();
        client.send(subCreate, HttpResponse.BodyHandlers.ofString());

        HttpRequest getTaskRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:"+KVServer.PORT+"/tasks/task/?id=1"))
                .GET()
                .build();
        client.send(getTaskRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getEpicRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:"+KVServer.PORT+"/tasks/epic/?id=2"))
                .GET()
                .build();
        client.send(getEpicRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getSubtasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:"+KVServer.PORT+"/tasks/subtask/?id=3"))
                .GET()
                .build();
        client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString());




    }
}