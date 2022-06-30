import Interfaces.HistoryManager;
import Interfaces.TaskTracker;

public  class Managers  {

    public static TaskTracker getDefault() {
        return  new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
        }

}
