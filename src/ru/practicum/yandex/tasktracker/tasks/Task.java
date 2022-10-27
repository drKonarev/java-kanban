package ru.practicum.yandex.tasktracker.tasks;

import ru.practicum.yandex.tasktracker.managers.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.practicum.yandex.tasktracker.managers.InMemoryTaskManager.formatter;


public class Task {

    protected String title;
    protected String description;
    protected int id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected TaskType taskType;


    public enum Status {
        NEW,
        DONE,
        IN_PROGRESS
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        if (duration == null) return Duration.ZERO;
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) {
            return LocalDateTime.MAX;
        } else return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Task(String title, String description, Status newStatus) {
        this.title = title;
        this.description = description;
        this.status = newStatus;
        this.taskType = TaskType.TASK;
        setId(InMemoryTaskManager.getNewIndex());
        this.id = getId();
    }


    public Task(String title, String description, Status newStatus, int id) {
        this.title = title;
        this.description = description;
        this.status = newStatus;
        this.taskType = TaskType.TASK;
        this.id = id;
    }

    public Task(String title, String description, Status newStatus, int id, String startTime, int duration) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
        endTime = this.startTime.plus(this.duration);
        this.title = title;
        this.description = description;
        this.status = newStatus;
        this.taskType = TaskType.TASK;
        this.id = id;
    }

    public Task(String title, String description, Status newStatus, String startTime, int duration) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
        endTime = this.startTime.plus(this.duration);
        this.title = title;
        this.description = description;
        this.status = newStatus;
        this.taskType = TaskType.TASK;
        setId(InMemoryTaskManager.getNewIndex());

    }

    public Task(String title, String description, Status newStatus, String startTime,String endTime, int duration) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
        this.endTime = LocalDateTime.parse(endTime, formatter);
        this.title = title;
        this.description = description;
        this.status = newStatus;
        this.taskType = TaskType.TASK;
        setId(InMemoryTaskManager.getNewIndex());

    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getEndTime() {
        if (endTime == null) return LocalDateTime.MAX;
        return endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public String toString() {
        return "Task {" +
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
        Task otherTask = (Task) obj;
        return Objects.equals(title, otherTask.title) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status) &&
                Objects.equals(taskType, otherTask.taskType) &&
                id == otherTask.id &&
                getStartTime().isEqual(otherTask.getStartTime()) &&
                getDuration().equals(otherTask.getDuration());

    }

    public String getTitleAndIdAndTiming() {
        return this.getTitle() + " (id: " + this.getId() + ")" + " - " + this.getStartTime().format(formatter);
    }
}
