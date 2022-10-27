
import ru.practicum.yandex.tasktracker.interfaces.TaskTracker;
import ru.practicum.yandex.tasktracker.managers.*;
import ru.practicum.yandex.tasktracker.server.HttpTaskManager;
import ru.practicum.yandex.tasktracker.server.KVServer;
import ru.practicum.yandex.tasktracker.tasks.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        KVServer kvServer = Managers.getKVServer();

        kvServer.start();

        TaskTracker manager = Managers.getDefault();

        HttpTaskServer taskServer = new HttpTaskServer(manager);

        taskServer.start();

        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);//,"11. 10. 2022; 12:20", 600 );
        Task task2 = new Task("Вылезти 7b+", "Со второй попытки", Task.Status.IN_PROGRESS, "11. 10. 2022; 23:20", 60);
        EpicTask epic1 = new EpicTask("5-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("6-ый спринт", "Уложиться в мягкий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", Task.Status.DONE, epic2.getId(), "10. 10. 2022; 12:20", 600);
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", Task.Status.DONE, epic2.getId(), "10. 10. 2022; 18:20", 600);
        SubTask sub3 = new SubTask("Отладка", "Успевай как хочешь ;)", Task.Status.IN_PROGRESS, epic2.getId(), "11. 10. 2022; 23:40", 600);
        manager.addAnyTask(task1);
        manager.addAnyTask(task2);
        manager.addAnyTask(epic1);
        manager.addAnyTask(epic2);

        System.out.println(manager.getAnyTask(5).toString());
        manager.addAnyTask(sub1);
        System.out.println(manager.getAnyTask(5).toString());
        manager.addAnyTask(sub2);
        System.out.println(manager.getAnyTask(5).toString());
        manager.addAnyTask(sub3);
        System.out.println(manager.getAnyTask(5).toString());

        manager.printPriorityList();

        manager.showTasks(task1);

        manager.getAnyTask(1);
        manager.getAnyTask(2);
        System.out.println(manager.getHistoryManager().getHistory());


        System.out.println(manager.getHistoryManager().getHistory());

        manager.getAnyTask(6);
        manager.getAnyTask(7);
        System.out.println(manager.getHistoryManager().getHistory());

        SubTask sub4 = new SubTask("Чтение", "Научиться читать код", SubTask.Status.IN_PROGRESS, epic1.getId(), "14. 10. 2022; 12:20", 60);// 14
        manager.addAnyTask(sub4);
        manager.getAnyTask(8);
        manager.printPriorityList();

        manager.updateSub((SubTask) manager.getAnyTask(50));
        manager.getAnyTask(4);
        System.out.println(manager.getHistoryManager().getHistory());

        manager.remove(7);
        manager.getAnyTask(1);
        System.out.println(manager.getHistoryManager().getHistory());

        manager.showTasks(task1);
        manager.showTasks(epic1);
        manager.getAnyTask(4);
        manager.getAnyTask(4);
        manager.showTasks(sub1);
        manager.remove(4);
        System.out.println(epic1);
        System.out.println("\n" + "История просмотренных задач: " + "\n" + manager.getHistoryManager().getHistory());

        kvServer.stop();
        taskServer.stop();
    }


}
