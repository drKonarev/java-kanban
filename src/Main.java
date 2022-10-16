
import ru.practicum.yandex.tasktracker.managers.*;
import ru.practicum.yandex.tasktracker.tasks.*;

public class Main {

    public static void main(String[] args) { //"dd, MM , yyyy; HH:mm"
        InMemoryTaskManager inMemoryTaskManager = Managers.getDefault();//(InMemoryTaskManager)
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);//,"11. 10. 2022; 12:20", 600 );
        Task task2 = new Task("Вылезти 7b+", "Со второй попытки", Task.Status.IN_PROGRESS, "11. 10. 2022; 23:20", 60);
        EpicTask epic1 = new EpicTask("5-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("6-ый спринт", "Уложиться в мягкий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", Task.Status.DONE, epic2.getId(), "10. 10. 2022; 12:20", 600);
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", Task.Status.DONE, epic2.getId(), "10. 10. 2022; 18:20", 600);
        SubTask sub3 = new SubTask("Отладка", "Успевай как хочешь ;)", Task.Status.IN_PROGRESS, epic2.getId(), "11. 10. 2022; 23:40", 600);
        inMemoryTaskManager.addAnyTask(task1);
        inMemoryTaskManager.addAnyTask(task2);
        inMemoryTaskManager.addAnyTask(epic1);
        inMemoryTaskManager.addAnyTask(epic2);

        System.out.println(inMemoryTaskManager.getAnyTask(5).toString());
        inMemoryTaskManager.addAnyTask(sub1);
        System.out.println(inMemoryTaskManager.getAnyTask(5).toString());
        inMemoryTaskManager.addAnyTask(sub2);
        System.out.println(inMemoryTaskManager.getAnyTask(5).toString());
        inMemoryTaskManager.addAnyTask(sub3);
        System.out.println(inMemoryTaskManager.getAnyTask(5).toString());

        inMemoryTaskManager.printPriorityList();

        inMemoryTaskManager.showTasks(task1);

        inMemoryTaskManager.getAnyTask(1);
        inMemoryTaskManager.getAnyTask(2);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

//        System.out.println(inMemoryTaskManager.getAnyTask(6).toString());
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        inMemoryTaskManager.getAnyTask(6);
        inMemoryTaskManager.getAnyTask(7);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        SubTask sub4 = new SubTask("Чтение", "Научиться читать код", SubTask.Status.IN_PROGRESS, epic1.getId(), "14. 10. 2022; 12:20", 60);// 14
        inMemoryTaskManager.addAnyTask(sub4);
        inMemoryTaskManager.getAnyTask(8);
        inMemoryTaskManager.printPriorityList();

        inMemoryTaskManager.updateSub((SubTask) inMemoryTaskManager.getAnyTask(50));
        inMemoryTaskManager.getAnyTask(4);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        inMemoryTaskManager.remove(7);
        inMemoryTaskManager.getAnyTask(1);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        inMemoryTaskManager.showTasks(task1);
        inMemoryTaskManager.showTasks(epic1);
        inMemoryTaskManager.getAnyTask(4);
        inMemoryTaskManager.getAnyTask(4);
        inMemoryTaskManager.showTasks(sub1);
        inMemoryTaskManager.remove(4);
        System.out.println(epic1);
        System.out.println("\n" + "История просмотренных задач: " + "\n" + inMemoryTaskManager.getHistoryManager().getHistory());


    }


}
