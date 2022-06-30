import java.util.List;
import Tasks.*;

public interface HistoryManager {

    void add( Task task);

    List<Task> getHistory();
}
