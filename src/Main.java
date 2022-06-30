import Managers.*;
import Tasks.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task2 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task3 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task4 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task5 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task6 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task7 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task8 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task9 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task10 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task11 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);

        Task task12 = new Task("Посмотреть кино", "День сурка", Task.Status.NEW);
        EpicTask epic1 = new EpicTask("3-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("4-ый спринт", "Уложиться в жесткий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", SubTask.Status.DONE, epic1.getId());
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", SubTask.Status.DONE, epic1.getId());
        SubTask sub3 = new SubTask("ВСЕ", "Успевай как хочешь ;)", SubTask.Status.IN_PROGRESS, epic2.getId());
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.addTask(task4);
        inMemoryTaskManager.addTask(task5);
        inMemoryTaskManager.addTask(task6);
        inMemoryTaskManager.addTask(task7);
        inMemoryTaskManager.addTask(task8);
        inMemoryTaskManager.addTask(task9);
        inMemoryTaskManager.addTask(task10);
        inMemoryTaskManager.addTask(task11);
        inMemoryTaskManager.addTask(task12);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getTask(3);
        inMemoryTaskManager.getTask(4);
        inMemoryTaskManager.getTask(5);
        inMemoryTaskManager.getTask(6);
        inMemoryTaskManager.getTask(7);
        inMemoryTaskManager.getTask(8);
        inMemoryTaskManager.getTask(9);
        inMemoryTaskManager.getTask(10);
        inMemoryTaskManager.getTask(11);
        inMemoryTaskManager.getTask(12);
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.showTasks(task1);
        inMemoryTaskManager.showTasks(epic1);
        inMemoryTaskManager.remove(12);
        inMemoryTaskManager.remove(46);
        inMemoryTaskManager.showTasks(task1);
        inMemoryTaskManager.showTasks(epic1);


        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.addTask(task1);

        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.addSub(sub1);
        inMemoryTaskManager.addSub(sub2);
        inMemoryTaskManager.addSub(sub3);
        System.out.println(inMemoryTaskManager.historyManager.getHistory()); //проверка методов класса
        // inMemoryTaskManager.historyManager.showHistory(); //проверка методов класса
        inMemoryTaskManager.showTasks(epic1);
        inMemoryTaskManager.showSubList(epic1.getId());

        SubTask sub4 = new SubTask("Чтение", "Научиться читать код", SubTask.Status.IN_PROGRESS, epic2.getId());
        inMemoryTaskManager.addSub(sub4);

        inMemoryTaskManager.removeSub(sub4.getId());

        inMemoryTaskManager.updateSub(inMemoryTaskManager.getSubTask(50), 4);

        inMemoryTaskManager.remove(epic1.getId());
        inMemoryTaskManager.remove(task2.getId());


        inMemoryTaskManager.showTasks(sub1);


    }


}
