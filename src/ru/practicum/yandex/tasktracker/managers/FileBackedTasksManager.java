package ru.practicum.yandex.tasktracker.managers;


import ru.practicum.yandex.tasktracker.interfaces.HistoryManager;
import ru.practicum.yandex.tasktracker.interfaces.TaskTracker;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;
import ru.practicum.yandex.tasktracker.tasks.TaskType;


import java.io.*;
import java.nio.file.Path;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskTracker {

    public static Path filePath = Path.of("src/");
    public static String pathFile = filePath.toAbsolutePath().getParent() + "/resources/";
    public static File file = new File(pathFile, "backedFile.csv");


    public static void main(String[] args) {

        FileBackedTasksManager manager = new FileBackedTasksManager();
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task2 = new Task("Вылезти 7b+", "Со второй попытки", Task.Status.IN_PROGRESS);
        EpicTask epic1 = new EpicTask("5-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("6-ый спринт", "Уложиться в мягкий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", Task.Status.DONE, epic2.getId());
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", Task.Status.DONE, epic1.getId());
        SubTask sub3 = new SubTask("Отладка", "Успевай как хочешь ;)", Task.Status.IN_PROGRESS, epic2.getId());
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSub(sub1);
        manager.addSub(sub2);
        manager.addSub(sub3);
        manager.getAnyTask(2);
        manager.getAnyTask(4);
        FileBackedTasksManager newManager = loadFromFile(file);
        newManager.addTask(new Task("Вылезти 6c+", "С ", Task.Status.DONE));
        newManager.addEpic(new EpicTask("26-ий спринт", "Сдать "));
        newManager.addSub(new SubTask("Сложность", "Декодинг", Task.Status.DONE, 5));
        newManager.getAnyTask(4);
        System.out.println(newManager.getHistoryManager().getHistory());
    }


    private void save() throws ManagerSaveException {
        try (FileWriter fr = new FileWriter(file)) {
            fr.write("id, type, name, status, description, epicId" + System.lineSeparator());
            for (Task task : tasks.values()) {
                fr.write(CSVFormatter.taskToString(task) + System.lineSeparator());
            }
            for (EpicTask epic : epics.values()) {
                fr.write(CSVFormatter.taskToString(epic) + System.lineSeparator());
            }
            for (SubTask sub : subs.values()) {
                fr.write(CSVFormatter.taskToString(sub) + "," + sub.getEpicId() + System.lineSeparator());
            }
            fr.write(System.lineSeparator() + CSVFormatter.historyToString(getHistoryManager()));

        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
            throw new ManagerSaveException();
        }
    }


    private static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (!(line.isBlank())) {
                if (line.contains("EPIC")) {
                    taskManager.epics.put(CSVFormatter.epicFromString(line).getId(), CSVFormatter.epicFromString(line));
                } else if (line.contains("SUB")) {
                    taskManager.subs.put(CSVFormatter.subFromString(line).getId(), CSVFormatter.subFromString(line));
                    taskManager.epics.get(CSVFormatter.subFromString(line).getEpicId()).addSubTask(CSVFormatter.subFromString(line).getId()); // кладем в эпик сабтаску

                } else if (line.contains("TASK")) {
                    taskManager.tasks.put(CSVFormatter.taskFromString(line).getId(), CSVFormatter.taskFromString(line));
                }
                line = br.readLine();// прочитали пустую строку
            }
            taskManager.getHistoryManager().setStory(CSVFormatter.historyFromString(br.readLine()));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return taskManager;
    }


    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }


    @Override
    public void addSub(SubTask sub) {
        super.addSub(sub);
        save();
    }

    @Override
    public void addEpic(EpicTask epic) {
        super.addEpic(epic);
        save();

    }

    @Override
    public void remove(Integer id) {
        super.remove(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();

    }

    @Override
    public void updateSub(SubTask sub) {
        super.updateSub(sub);
        save();
    }

    @Override
    public void showTasks(Task object) {
        super.showTasks(object);
    }

    @Override
    public void totalRemove() {
        super.totalRemove();
        save();
    }

    @Override
    public void showSubList(int epicId) {
        super.showSubList(epicId);
    }

    @Override
    public Task getAnyTask(int id) {
        switch (checkExistence(id)) {
            case TASK:
                getHistoryManager().add(tasks.get(id));
                save();
                return tasks.get(id);

            case SUB:
                getHistoryManager().add(subs.get(id));
                save();
                return subs.get(id);

            case EPIC:
                getHistoryManager().add(epics.get(id));
                save();
                return epics.get(id);

            default:
                System.out.println("\n С таким id (" + id + ") не найдено ни одной задачи.");
                return null;

        }
    }

    @Override
    public TaskType checkExistence(int id) {
        return super.checkExistence(id);
    }
}
