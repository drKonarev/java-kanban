package ru.practicum.yandex.tasktracker.managers;

import com.sun.net.httpserver.*;
import com.google.gson.Gson;
import ru.practicum.yandex.tasktracker.interfaces.TaskTracker;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int TASK_SERVER_PORT = 8080;
    private final HttpServer server;
    private Gson gson;
    final TaskTracker taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskTracker taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", TASK_SERVER_PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    private void handler(HttpExchange httpExchange) {
        try {
            System.out.println(httpExchange.getRequestURI());
            final String path = httpExchange.getRequestURI().getPath().replaceFirst("/tasks/", "");
            switch (path) {
                case "task/":
                    handleTask(httpExchange);
                    break;
                case "history":
                case "":
                    handlePriorityAndHistory(httpExchange);
                    break;
                case "subtasks/epic":
                    handleSubtaskEpic(httpExchange);
                    break;
                case "subtask/":
                    handleSubTask(httpExchange);
                    break;
                case "epictask/":
                    handleEpicTask(httpExchange);
                    break;
                default:
                    System.out.println("Unknown request method");
                    httpExchange.sendResponseHeaders(404, -1);
            }
        } catch (Exception e) {

            System.out.println("Error while processed request");
        } finally {
            httpExchange.close();
        }
    }

    private void handleEpicTask(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "DELETE":
                int id = Integer.parseInt(query.substring(2));
                taskManager.remove(id);
                sendText(httpExchange, "EpicTask is deleted");
                break;
            case "POST":
                if (Objects.nonNull(httpExchange.getRequestBody())) {
                    addTaskFromString(epicTaskFromJsonString(readText(httpExchange)));
                    sendText(httpExchange, "Successful add task!");
                } else {
                    System.out.println("Request body is empty!");
                    httpExchange.sendResponseHeaders(503, -1);
                }
                break;
            case "GET":
                if (Objects.nonNull(query)) {
                    id = Integer.parseInt(query.substring(3));
                    EpicTask epic = (EpicTask) taskManager.getAnyTask(id);
                    String response = gson.toJson(epic);
                    sendText(httpExchange, response);
                } else {
                    String response = gson.toJson(taskManager.getEpics());
                    sendText(httpExchange, response);
                }
                break;
            default:
                System.out.println("Unknown request method");
                httpExchange.sendResponseHeaders(404, -1);
        }
    }

    private EpicTask epicTaskFromJsonString(String line) {
        final String[] lines = line.split(",");
        final int id = Integer.parseInt(lines[3].trim().split(":")[1]);
        final String name = formatString(lines[1].trim());
        final String description = formatString(lines[1].trim());
        InMemoryTaskManager.index = id;
        return new EpicTask(name, description, id);
    }

    private void handleSubTask(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "DELETE":
                int id = Integer.parseInt(query.substring(2));
                taskManager.remove(id);
                sendText(httpExchange, "SubTask is deleted");
                break;
            case "GET":
                if (Objects.nonNull(query)) { // если передается запрос формата task/id
                    id = Integer.parseInt(query.substring(3));
                    SubTask sub = (SubTask) taskManager.getAnyTask(id);
                    String response = gson.toJson(sub);
                    sendText(httpExchange, response);
                } else {
                    String response = gson.toJson(taskManager.getSubs());
                    sendText(httpExchange, response);
                }
                break;
            case "POST":
                if (Objects.nonNull(httpExchange.getRequestBody())) {
                    addTaskFromString(subTaskFromJsonString(readText(httpExchange)));
                    sendText(httpExchange, "Successful add task!");
                } else {
                    System.out.println("Request body is empty!");
                    httpExchange.sendResponseHeaders(503, -1);
                }
                break;
            default:
                System.out.println("Unknown request method");
                httpExchange.sendResponseHeaders(404, -1);
        }
    }

    private SubTask subTaskFromJsonString(String line) {
        final String[] lines = line.split(",");
        final int id = Integer.parseInt(lines[3].trim().split(":")[1]);
        final String name = formatString(lines[1].trim());
        final Task.Status status = Task.Status.valueOf(formatString(lines[4].trim()));
        final String description = formatString(lines[2].trim());
        final String startTime = stringToTime(lines[7].trim());
        final int duration = Integer.parseInt((lines[5].trim().split(":")[2]))/60;
        final int epicId = Integer.parseInt(lines[0].trim().split(":")[1]);

        InMemoryTaskManager.index = id;
        return new SubTask(name, description, status, epicId, id, startTime, duration);
    }

    private void handleSubtaskEpic(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        if ("GET".equals(requestMethod)) {
            int id = Integer.parseInt(query.substring(3));
            List<SubTask> sub = taskManager.getSubByEpic(id);
            String response = gson.toJson(sub);
            sendText(httpExchange, response);
        } else {
            System.out.println("Unknown request method");
            httpExchange.sendResponseHeaders(404, -1);
        }
    }


    private void handlePriorityAndHistory(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            String path = httpExchange.getRequestURI().getPath().replaceFirst("/tasks/", "");
            String response;
            if (path.equals("history")) {
                response = gson.toJson(taskManager.getHistory());
            } else {
                response = gson.toJson(taskManager.getPriorityList());
            }
            sendText(httpExchange, response);
        } else {
            System.out.println("Unknown request method");
            httpExchange.sendResponseHeaders(404, -1);
        }
    }


    private void handleTask(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "GET":
                if (Objects.nonNull(query)) { // если передается запрос формата task/id
                    int id = Integer.parseInt(query.substring(3));
                    Task task = taskManager.getAnyTask(id);
                    String response = gson.toJson(task);
                    sendText(httpExchange, response);
                } else { // если передается запрос формата task/
                    String response = gson.toJson(taskManager.getTasks());
                    sendText(httpExchange, response);
                }
                break;
            case "DELETE":
                if (Objects.nonNull(query)) { // если передается запрос формата task/id
                    int id = Integer.parseInt(query.substring(2));
                    taskManager.remove(id);
                    sendText(httpExchange, "Task is deleted");
                } else { // если передается запрос формата task/
                    taskManager.totalRemove();
                    sendText(httpExchange, "Total remove complete");
                }
                break;
            case "POST":
                if (Objects.nonNull(httpExchange.getRequestBody())) {
                    addTaskFromString(taskFromJsonString(readText(httpExchange)));
                    sendText(httpExchange, "Successful add task!");
                } else {
                    System.out.println("Request body is empty!");
                    httpExchange.sendResponseHeaders(503, -1);
                }
                break;
            default:
                System.out.println("Unknown request method");
                httpExchange.sendResponseHeaders(404, -1);

        }
    }

    private void addTaskFromString(Task task) {
        if (task == null) {
            System.out.println("Object is empty!");
            return;
        }
        taskManager.addAnyTask(task);
    }

    private Task taskFromJsonString(String line) {

        final String[] lines = line.split(",");
        final int id = Integer.parseInt(lines[2].trim().split(":")[1]);
        final String name = formatString(lines[0].trim());
        final Task.Status status = Task.Status.valueOf(formatString(lines[3].trim()));
        final String description = formatString(lines[1].trim());
        final String startTime = stringToTime(lines[6].trim());
        final int duration = Integer.parseInt((lines[4].trim().split(":")[2]))/60;


        InMemoryTaskManager.index = id;
        return new Task(name, description, status, id, startTime, duration);
    }

    private String formatString(String line) {
        String[] lines = line.split(":");
        return trimLine(lines[1]);
    }

    private String trimLine(String line) {
        StringBuilder str = new StringBuilder(line);
        str.deleteCharAt(0);
        str.deleteCharAt(str.length() - 1);
        while (str.indexOf("\"") != -1) {
            str.deleteCharAt(str.indexOf("\""));
        }
        if (str.indexOf("}") != -1) {
            str.deleteCharAt(str.indexOf("}"));
        }
        return str.toString();
    }

    private String stringToTime(String line) {
        if (line.isEmpty()) return LocalDateTime.MAX.toString();
        String[] lines = line.split(":");
        return trimLine(lines[1] + ":" + lines[2]);
    }

    public void stop() {
        System.out.println("Останавливаем сервер на порту " + TASK_SERVER_PORT);
        server.stop(0);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + TASK_SERVER_PORT);
        System.out.println("Открой в браузере http://localhost:" + TASK_SERVER_PORT + "/tasks/");
        server.start();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }
}
