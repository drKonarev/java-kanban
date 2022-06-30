package Tasks;

import Managers.InMemoryTaskManager;

public class SubTask extends Tasks.Task {

    protected int epicId;

    public SubTask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        id = InMemoryTaskManager.getNewIndex();
    }


    public int getEpicId() {
        return epicId;
    }

    public int getId() {
        return id;
    }

@Override
    public String toString() {
        String result = "Sub Task {" +
                "title '" + title + '\'' +
                ", description '" + description + '\'' +
                ", status '" + status + '\'' +
                ", id '" + id + "' }";
        return result;
    }

}
