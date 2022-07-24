
import ru.practicum.yandex.tasktracker.managers.*;
import ru.practicum.yandex.tasktracker.tasks.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task2 = new Task("Вылезти 7b+", "Со второй попытки", Task.Status.IN_PROGRESS);
        EpicTask epic1 = new EpicTask("5-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("6-ый спринт", "Уложиться в мягкий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", Task.Status.DONE, epic2.getId());
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", Task.Status.DONE, epic2.getId());
        SubTask sub3 = new SubTask("Отладка", "Успевай как хочешь ;)", Task.Status.IN_PROGRESS, epic2.getId());
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.addSub(sub1);
        inMemoryTaskManager.addSub(sub2);
        inMemoryTaskManager.addSub(sub3);

        inMemoryTaskManager.getAnyTask(1);
        inMemoryTaskManager.getAnyTask(2);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        inMemoryTaskManager.getAnyTask(5);
        inMemoryTaskManager.getAnyTask(6);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        inMemoryTaskManager.getAnyTask(6);
        inMemoryTaskManager.getAnyTask(7);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        SubTask sub4 = new SubTask("Чтение", "Научиться читать код", SubTask.Status.IN_PROGRESS, epic1.getId());// 14
        inMemoryTaskManager.addSub(sub4);
        inMemoryTaskManager.getAnyTask(8);

        inMemoryTaskManager.updateSub((SubTask) inMemoryTaskManager.getAnyTask(50));
        inMemoryTaskManager.getAnyTask(4);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        inMemoryTaskManager.remove(4);
        inMemoryTaskManager.getAnyTask(1);
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

        inMemoryTaskManager.showTasks(task1);
        inMemoryTaskManager.showTasks(epic1);

        inMemoryTaskManager.remove(2);
        System.out.println("\n" + "История просмотренных задач: " + "\n" + inMemoryTaskManager.getHistoryManager().getHistory());


    }


}
