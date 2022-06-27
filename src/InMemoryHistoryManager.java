import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> story = new ArrayList<>();

    @Override
    public void add(Task task) {
        if(task ==null){return;}
        if (story.size() <= 10) {
            story.add(0, task);
        } else {
            story.remove(story.size() - 1);
            story.add(0, task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return story;
    }

/*    public void showHistory() {
        System.out.println("Вот список последних вызванных задач:");
        for (Task task : story) {
            System.out.println("- " + task.title + " | " + task.getClass().getSimpleName() + " | " + task.getId());
        }
    }*/


}
