package ru.practicum.yandex.tasktracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EpicTask extends Task {

    protected ArrayList<Integer> subTasksList = new ArrayList<>();

    public EpicTask(String title, String description) {
        super(title, description, Status.NEW);
        this.taskType = TaskType.EPIC;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ZERO;
    }

    public EpicTask(String title, String description, int id) {
        super(title, description, Status.NEW, id);
        this.taskType = TaskType.EPIC;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ZERO;
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasksList;
    }


    public void addSubTask(int taskId) {
        subTasksList.add(taskId);

    }


    @Override
    public String toString() {

        if (!subTasksList.isEmpty()) { // проверяем, что поле не содержит null
            return "EpicTask {" +
                    "title '" + this.getTitle() + '\'' +
                    ", description '" + this.getDescription() + '\'' +
                    ", status '" + this.getStatus() + '\'' +
                    ", id '" + this.getId() + '\'' +
                    ", start time '" + this.getStartTime().format(formatter) + '\'' +
                    ", end time '" + this.getEndTime().format(formatter) + '\'' +
                    ", sub tasks amount '" +
                    subTasksList.size() + "' }"; // выводим не значение, а длину
        } else {
            return "EpicTask {" +
                    "title '" + this.getTitle() + '\'' +
                    ", description '" + this.getDescription() + '\'' +
                    ", status '" + this.getStatus() + '\'' +
                    ", id '" + this.getId() + '\'' +
                    ", sub tasks list is empty , timing is unavailable }; ";// выводим информацию, что поле равно null
        }

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
        EpicTask otherTask = (EpicTask) obj;
        return Objects.equals(title, otherTask.title) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status) &&
                Objects.equals(taskType, otherTask.taskType) &&
                id == otherTask.id &&
                Arrays.equals(subTasksList.toArray(), otherTask.subTasksList.toArray()) &&
                startTime.isEqual(otherTask.startTime) &&
                duration.equals(otherTask.duration);

    }


}
