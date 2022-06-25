public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Вылезти 6b+", "С первой попытки", Task.Status.IN_PROGRESS);
        Task task2 = new Task("Посмотреть кино", "День сурка", Task.Status.NEW);
        EpicTask epic1 = new EpicTask("3-ий спринт", "Сдать с первой попытки");
        EpicTask epic2 = new EpicTask("4-ый спринт", "Уложиться в жесткий дедлайн");
        SubTask sub1 = new SubTask("Теория", "Декомпозировать задание", SubTask.Status.DONE, epic1.getId());
        SubTask sub2 = new SubTask("Практика", "Написать и отладить код", SubTask.Status.DONE, epic1.getId());
        SubTask sub3 = new SubTask("ВСЕ", "Успевай как хочешь ;)", SubTask.Status.IN_PROGRESS, epic2.getId());


        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.addSub(sub1);
        inMemoryTaskManager.addSub(sub2);
        inMemoryTaskManager.addSub(sub3);

        inMemoryTaskManager.showTasks();
        inMemoryTaskManager.showSubList(epic1.id);

        SubTask sub4 = new SubTask("Чтение", "Научиться читать код", SubTask.Status.IN_PROGRESS, epic2.getId());
        inMemoryTaskManager.addSub(sub4);

        inMemoryTaskManager.removeSub(sub4.getId());

        inMemoryTaskManager.updateSub(inMemoryTaskManager.getSubTask(50), 4);

        inMemoryTaskManager.removeEpic(epic1.getId());
        inMemoryTaskManager.removeTask(task2.getId());
        inMemoryTaskManager.removeSub(sub3.getId());

        inMemoryTaskManager.showTasks();


    }


}
