public class Main {

    public static void main(String[] args) {
        TaskTracker taskTracker = new TaskTracker();
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task2 = new Task("Посмотреть кино", "День сурка", Task.Status.NEW);
        EpicTask epic1 = new EpicTask("3-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("4-ый спринт", "Уложиться в жесткий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", SubTask.Status.DONE, epic1.getId());
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", SubTask.Status.DONE, epic1.getId());
        SubTask sub3 = new SubTask("ВСЕ", "Успевай как хочешь ;)", SubTask.Status.IN_PROGRESS, epic2.getId());


        taskTracker.addTask((EpicTask) task1);
        taskTracker.addEpic(epic1);
        taskTracker.addEpic(epic2);
        taskTracker.addSub(sub1);
        taskTracker.addSub(sub2);
        taskTracker.addSub(sub3);

        taskTracker.showTasks();
        taskTracker.showSubList(epic1.id);

        SubTask sub4 = new SubTask("Чтение", "Научиться читать код", SubTask.Status.IN_PROGRESS, epic2.getId());
        taskTracker.addSub(sub4);

        taskTracker.removeSub(sub4.getId());

        taskTracker.updateSub(taskTracker.getSubTask(50), 4);

        taskTracker.removeEpic(epic1.getId());
        taskTracker.removeTask(task2.getId());
        taskTracker.removeSub(sub3.getId());

        taskTracker.showTasks();


    }


}
