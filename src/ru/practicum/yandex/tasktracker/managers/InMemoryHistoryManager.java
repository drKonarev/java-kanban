package ru.practicum.yandex.tasktracker.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import ru.practicum.yandex.tasktracker.interfaces.*;
import ru.practicum.yandex.tasktracker.tasks.*;

public class InMemoryHistoryManager implements HistoryManager {
    protected List<Integer> story = new ArrayList<>();
    private HashMap<Integer, Node> nodeMap = new HashMap<>();
    Node tail = null;
    Node head = null;

    //private void setStory(List)

    private static class Node {
        private Node prev;
        private Node next;
        private Task task;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }


    private void linkLast(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            story.remove((Integer) task.getId());
            story.add(task.getId());
            return;
        }
        final Node node = new Node(null, task, null);

        if (head == null && tail == null) {//нет ни одного нода
            head = node;

        }
        if (head != null && tail == null) {// есть 1 нод - голова
            head.next = node;
            tail = node;
            tail.prev = head;

        }
        if (tail != null) {// есть >= 2-х нодов
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        story.add(node.task.getId());
        nodeMap.put(task.getId(), node);
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }


    @Override
    public List<Integer> getHistory() {
        return story;
    }

    @Override
    public void remove(int id) {

        Node node = nodeMap.get(id);
        if (node == null) return;

        if (node.next == null && node.prev == null) nodeMap.clear();

        if (node.prev == null && node.next != null) {
            node.next.prev = null;
            head = node.next;
        }
        if (node.prev != null && node.next == null) {
            node.prev.next = null;
            tail = node.prev;
        }
        if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        story.remove(node.task.getId());
        nodeMap.remove(id);
    }

    @Override
    public void removeAllHistory() {
        nodeMap.clear();
    }


    @Override
    public void setStory(List<Integer> newStory) {
        story.clear();
        story.addAll(newStory);
    }
}