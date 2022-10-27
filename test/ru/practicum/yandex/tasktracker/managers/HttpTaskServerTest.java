package ru.practicum.yandex.tasktracker.managers;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.yandex.tasktracker.managers.HttpTaskServer.TASK_SERVER_PORT;

class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private TaskTracker taskManager;
    private Task task;
    private SubTask sub;

    private KVServer kvServer;
    private EpicTask epic;
    private Gson gson;

    @BeforeEach
    public void before() throws IOException {

        kvServer = Managers.getKVServer();

        kvServer.start();

        taskManager = Managers.getDefault();

        httpTaskServer = new HttpTaskServer(taskManager);

        gson = Managers.getGson();

        task = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS, "11. 10. 2022; 12:20", 600);
        httpTaskServer.taskManager.addAnyTask(task);

        epic = new EpicTask("5-ий спринт", "Сдать с первой попытки");
        httpTaskServer.taskManager.addAnyTask(epic);

        sub = new SubTask("Теория", "Декомпозировать задание", Task.Status.DONE, epic.getId(), "10. 10. 2022; 12:20", 600);
        httpTaskServer.taskManager.addAnyTask(sub);

        httpTaskServer.start();
    }

    @AfterEach
    public void after() {
        kvServer.stop();
        httpTaskServer.stop();

    }

    @Test
    public void getSubTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:"+TASK_SERVER_PORT+"/tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        SubTask test = gson.fromJson(response.body(), SubTask.class);

        Assertions.assertEquals(200, response.statusCode()); //проверяем возвращаемый код
        assertNotNull(test, "Not found task with this id.");
        Assertions.assertEquals(sub, test);
    }

    @Test
    public void getEpicTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:"+TASK_SERVER_PORT+"/tasks/epictask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        EpicTask test = gson.fromJson(response.body(), EpicTask.class);

        Assertions.assertEquals(200, response.statusCode()); //проверяем возвращаемый код
        assertNotNull(test, "Not found task with this id.");
        Assertions.assertEquals(epic, test);
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {

        //отправляем запрос на получение задачи, чтобы она появилась в истории

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:"+TASK_SERVER_PORT+"/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:"+TASK_SERVER_PORT+"/tasks/history");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode()); //проверяем возвращаемый код


        List<Integer> history = gson.fromJson(response.body(), ArrayList.class);
        assertNotNull(history);
        Assertions.assertEquals(1, history.size());

    }

    @Test
    public void totalRemoveTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/history");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode()); //проверяем возвращаемый код


        List<Integer> history = gson.fromJson(response.body(), ArrayList.class);
        Assertions.assertEquals(200, response.statusCode());
        assertNotNull(history);
        Assertions.assertEquals(0, history.size());
    }

    @Test
    public void createTaskTest() throws IOException, InterruptedException {
        Task newTask = new Task("Выиграть соревы", "Попасть в топ-3", Task.Status.NEW, "19. 10. 2022; 12:20", 600);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:" + TASK_SERVER_PORT + "/tasks/task/");

        String json = gson.toJson(newTask);
        System.out.println(json);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        System.out.println(json);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

    }

    @Test
    public void createSubTaskTest() throws IOException, InterruptedException {
        SubTask newTask = new SubTask("Выиграть соревы", "Попасть в топ-3", Task.Status.NEW, epic.getId(), "19. 10. 2022; 12:20", 600);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:"+TASK_SERVER_PORT+"/tasks/subtask/");

        String json = gson.toJson(newTask);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        System.out.println(json);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void createEpicTaskTest() throws IOException, InterruptedException {
        EpicTask newTask = new EpicTask("Выиграть соревы", "Попасть в топ-3");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:"+TASK_SERVER_PORT+"/tasks/epictask/");

        String json = gson.toJson(newTask);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        System.out.println(json);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void getTaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:"+TASK_SERVER_PORT+"/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task test = gson.fromJson(response.body(), Task.class);

        Assertions.assertEquals(200, response.statusCode()); //проверяем возвращаемый код
        assertNotNull(test, "Not found task with this id.");
        Assertions.assertEquals(task, test);

    }

}