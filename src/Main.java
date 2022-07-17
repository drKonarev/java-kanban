import Managers.*;
import Tasks.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task2 = new Task("Вылезти 7b+", "Со второй попытки", Task.Status.IN_PROGRESS);
        EpicTask epic1 = new EpicTask("5-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("6-ый спринт", "Уложиться в мягкий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", SubTask.Status.DONE, epic2.getId());
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", SubTask.Status.DONE, epic2.getId());
        SubTask sub3 = new SubTask("Отладка", "Успевай как хочешь ;)", SubTask.Status.IN_PROGRESS, epic2.getId());
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addEpic(epic1);//4
        inMemoryTaskManager.addEpic(epic2);//6
        inMemoryTaskManager.addSub(sub1);//8
        inMemoryTaskManager.addSub(sub2);//10
        inMemoryTaskManager.addSub(sub3);//12

        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.getSubTask(8);
        inMemoryTaskManager.getSubTask(12);
        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.getSubTask(12);
        inMemoryTaskManager.getSubTask(8);
        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        SubTask sub4 = new SubTask("Чтение", "Научиться читать код", SubTask.Status.IN_PROGRESS, epic1.getId());
        inMemoryTaskManager.addSub(sub4);
        inMemoryTaskManager.getSubTask(14);

        inMemoryTaskManager.updateSub(inMemoryTaskManager.getSubTask(50), 4);
        inMemoryTaskManager.getEpicTask(6);
        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.remove(6);
        inMemoryTaskManager.getTask(1);
        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.showTasks(task1);
        inMemoryTaskManager.showTasks(epic1);

        inMemoryTaskManager.remove(2);
        System.out.println("\n" + "История просмотренных задач: " + "\n" + inMemoryTaskManager.historyManager.getHistory());


    }


}
