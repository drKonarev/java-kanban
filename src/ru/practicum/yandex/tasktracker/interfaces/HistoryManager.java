package ru.practicum.yandex.tasktracker.interfaces;

import java.util.List;

import ru.practicum.yandex.tasktracker.tasks.*;

public interface HistoryManager {

    void add(Task task);

    List<Integer> getHistory();

    void remove(int id);

    void removeAllHistory();

    void setStory(List<Integer> newStory);


}
