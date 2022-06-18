public class Main {

    public static void main(String[] args) {
        TaskTracker taskTracker = new TaskTracker();
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", "IN_PROGRESS");
        Task task2 = new Task("Посмотреть кино", "День сурка", "DONE");
        EpicTask epic1 = new EpicTask("3-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("4-ый спринт", "Уложиться в жесткий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", "DONE", 3);
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", "IN_PROGRESS", 3);
        SubTask sub3 = new SubTask("ВСЕ", "Успевай как хочешь ;)", "IN_PROGRESS", 4);

        taskTracker.add(task1);
        taskTracker.add(task2);
        taskTracker.add(epic1);
        taskTracker.add(epic2);
        taskTracker.add(sub1);
        taskTracker.add(sub2);
        taskTracker.add(sub3);

        taskTracker.showTasks();
        taskTracker.showSubList(3);

        SubTask sub4 = new SubTask("Практика", "Написать и отладить код", "DONE", 3);
        taskTracker.updateSub(sub4, sub2.getId());

        taskTracker.getAnyTask(3);

        taskTracker.removeEpic(4);

        taskTracker.removeTask(2);

        taskTracker.removeSub(6);

        taskTracker.showTasks();
        taskTracker.getAnyTask(3);
    }


}
