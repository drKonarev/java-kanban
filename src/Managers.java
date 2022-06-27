public  class Managers <T>  {

    public static TaskTracker getDefault() {
        return  new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
        }

}
