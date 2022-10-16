package ru.practicum.yandex.tasktracker.tasks;

import java.util.Objects;

public class SubTask extends Task {

    public int getEpicId() {
        return epicId;
    }

    protected int epicId;

    public SubTask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        this.taskType = TaskType.SUB;
    }

    public SubTask(String title, String description, Status status, int epicId, int id) {
        super(title, description, status, id);
        this.epicId = epicId;
        this.taskType = TaskType.SUB;
    }

    public SubTask(String title, String description, Status status, int epicId, String startTime, int duration) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
        this.taskType = TaskType.SUB;
    }

    public SubTask(String title, String description, Status status, int epicId, int id, String startTime, int duration) {
        super(title, description, status, id, startTime, duration);
        this.epicId = epicId;
        this.taskType = TaskType.SUB;
    }


    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "SubTask {" +
                "title '" + this.getTitle() + '\'' +
                ", description '" + this.getDescription() + '\'' +
                ", status '" + this.getStatus() + '\'' +
                ", id '" + this.getId() + '\'' +
                ", start time '" + this.getStartTime().format(formatter) + '\'' +
                ", end time '" + this.getEndTime().format(formatter) + '\'' +
                " }";
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (title != null && description != null) {
            hash = (title.hashCode() + description.hashCode() * id);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        SubTask otherTask = (SubTask) obj;
        return Objects.equals(title, otherTask.title) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status) &&
                Objects.equals(taskType, otherTask.taskType) &&
                id == otherTask.id &&
                epicId == otherTask.epicId &&
                startTime.isEqual(otherTask.startTime) &&
                duration.equals(otherTask.duration);

    }
}
