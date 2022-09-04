package ru.practicum.yandex.tasktracker.managers;


import ru.practicum.yandex.tasktracker.interfaces.TaskTracker;
import ru.practicum.yandex.tasktracker.tasks.EpicTask;
import ru.practicum.yandex.tasktracker.tasks.SubTask;
import ru.practicum.yandex.tasktracker.tasks.Task;


import java.io.*;
import java.nio.file.Path;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskTracker {

    public static Path filePath = Path.of("src/");
    public static String pathFile = filePath.toAbsolutePath().getParent() + "/resources/";
    public static File file = new File(pathFile, "backedFile.csv");


    public static void main(String[] args) {

        FileBackedTasksManager manager = Managers.getDefaultFile();
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task2 = new Task("Вылезти 7b+", "Со второй попытки", Task.Status.IN_PROGRESS);
        EpicTask epic1 = new EpicTask("5-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("6-ый спринт", "Уложиться в мягкий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", Task.Status.DONE, epic2.getId());
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", Task.Status.DONE, epic1.getId());
        SubTask sub3 = new SubTask("Отладка", "Успевай как хочешь ;)", Task.Status.IN_PROGRESS, epic2.getId());
        manager.addAnyTask(task1);
        manager.addAnyTask(task2);
        manager.addAnyTask(epic1);
        manager.addAnyTask(epic2);
        manager.addAnyTask(sub1);
        manager.addAnyTask(sub2);
        manager.addAnyTask(sub3);
        manager.getAnyTask(2);
        manager.getAnyTask(4);
        FileBackedTasksManager newManager = loadFromFile(file);
        newManager.addAnyTask(new Task("Вылезти 6c+", "С ", Task.Status.DONE));
        newManager.addAnyTask(new EpicTask("26-ий спринт", "Сдать "));
        newManager.addAnyTask(new SubTask("Сложность", "Декодинг", Task.Status.DONE, 5));
        newManager.getAnyTask(4);
        System.out.println(newManager.getHistoryManager().getHistory());
        newManager.remove(4);
        System.out.println(newManager.getHistoryManager().getHistory());
        newManager.getAnyTask(4);
        //newManager.totalRemove();
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
            line = br.readLine(); // пропускаем первую строку
            while (!(line.isBlank())) {
                taskManager.addTaskFromFile(CSVFormatter.taskFromString(line));
                line = br.readLine();// прочитали пустую строку
            }
            taskManager.getHistoryManager().setStory(CSVFormatter.historyFromString(br.readLine()));
          // для добавления мапы с нодами нужно создать метод, который бы создавал из истории ноду

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return taskManager;
    }


    private void addTaskFromFile(Task task) {
        if (task == null) {
            System.out.println("Передаваемый объект пуст!");
            return;
        }
        switch (task.getTaskType()) {
            case TASK:
                tasks.put(task.getId(), task);
                break;
            case SUB:
                subs.put(task.getId(), (SubTask) task);
                break;
            case EPIC:
                epics.put(task.getId(), (EpicTask) task);
        }
    }


    @Override
    public void addAnyTask(Task task) {
        super.addAnyTask(task);
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
    public void totalRemove() {
        super.totalRemove();
        save();
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
}
