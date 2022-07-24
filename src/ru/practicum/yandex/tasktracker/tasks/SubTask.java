package ru.practicum.yandex.tasktracker.tasks;

import ru.practicum.yandex.tasktracker.managers.InMemoryTaskManager;

public class SubTask extends Task {

    protected int epicId;

    public SubTask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        this. result = "Sub "+ result +"' }";
    }


    public int getEpicId() {
        return epicId;
    }

    public int getId() {
        return id;
    }



    @Override
    public String toString() {
        return result;
    }

}
