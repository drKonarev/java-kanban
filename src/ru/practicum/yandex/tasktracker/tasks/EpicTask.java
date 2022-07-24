package ru.practicum.yandex.tasktracker.tasks;

import ru.practicum.yandex.tasktracker.managers.InMemoryTaskManager;

import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList<Integer> subTasksList = new ArrayList<>();

    public EpicTask(String title, String description) {
        super(title, description, Status.NEW);
        this.result = "Epic "+ result;
    }


    public ArrayList<Integer> getSubTasks() {
        return subTasksList;
    }

    public ArrayList<Integer> getSubTasksList() {
        return subTasksList;
    }

    public void addSubTask(int taskId) {
        subTasksList.add(taskId);
    }



    @Override //
    public String toString() {


        if (!subTasksList.isEmpty()) { // проверяем, что поле не содержит null
            result = result + ", sub tasks amount '" + subTasksList.size() + "' }"; // выводим не значение, а длину
        } else {
            result = result + ", sub tasks list is empty }; ";// выводим информацию, что поле равно null
        }
        return result;

    }

}
