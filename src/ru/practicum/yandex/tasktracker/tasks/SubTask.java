package ru.practicum.yandex.tasktracker.tasks;



public class SubTask extends Task {

    public int getEpicId() {
        return epicId;
    }

    protected int epicId;

    public SubTask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        this.taskType = TaskType.SUB;
        this. result = "Sub "+ result +"' }";
    }
    public SubTask(String title, String description, Status status, int epicId, int id) {
        super(title, description, status, id);
        this.epicId = epicId;
        this.taskType = TaskType.SUB;
        this. result = "Sub "+ result +"' }";
    }



    public int getId() {
        return id;
    }



    @Override
    public String toString() {
        return result;
    }

}
