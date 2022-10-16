package ru.practicum.yandex.tasktracker.managers;

import ru.practicum.yandex.tasktracker.interfaces.HistoryManager;
import ru.practicum.yandex.tasktracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.practicum.yandex.tasktracker.managers.InMemoryTaskManager.equalMaps;

public class InMemoryHistoryManager implements HistoryManager {
    protected List<Integer> story = new ArrayList<>();

    HashMap<Integer, Node> nodeMap = new HashMap<>();
    Node tail = null;

    public Node getHead() {
        return head;
    }

    Node head = null;


    static class Node {
        private Node prev;
        private Node next;
        private Task task;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            Node otherNode = (Node) obj;
            if (prev == null && next != null) {
                return task.equals(otherNode.task) &&
                        (otherNode.prev == null) &&
                        next.equals(otherNode.next);
            }
            if (prev == null && next == null) {
                return task.equals(otherNode.task) &&
                        (otherNode.prev == null) &&
                        (otherNode.next == null);
            }
            if (prev != null && next == null) {
                return task.equals(otherNode.task) &&
                        prev.equals(otherNode.prev) &&
                        (otherNode.next == null);
            }
            return task.equals(otherNode.task) &&
                    prev.equals(otherNode.prev) &&
                    next.equals(otherNode.next);
        }

        @Override
        public int hashCode() {
            int hash = 15;
            if (task != null) {
                hash += task.hashCode();
            }
            return hash;
        }

    }


    void linkLast(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            story.remove((Integer) task.getId());
            story.add(task.getId());
            return;
        }
        final Node node = new Node(null, task, null);

        if (head == null && tail == null) {//нет ни одного нода
            head = node;

        } else {
            if (head != null && tail == null) {// есть 1 нод - голова
                head.next = node;
                tail = node;
                tail.prev = head;

            } else {
                if (tail != null) {// есть >= 2-х нодов
                    tail.next = node;
                    node.prev = tail;
                    tail = node;
                }
            }
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

        while (story.contains(node.task.getId())) {
            story.remove((Integer) node.task.getId());
        }

        if (node.next == null && node.prev == null) {
            nodeMap.clear();
            return;
        }

        if (node.prev == null && node.next != null) {
            node.next.prev = null;
            head = node.next;
            nodeMap.remove(id);
            return;
        }
        if (node.prev != null && node.next == null) {
            node.prev.next = null;
            tail = node.prev;
            nodeMap.remove(id);
            return;
        }
        if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            nodeMap.remove(id);
        }
    }

    @Override
    public void removeAllHistory() {
        nodeMap.clear();
        story.clear();
    }


    @Override
    public void setStory(List<Integer> newStory) {
        story.clear();
        story.addAll(newStory);
        HashMap<Integer, Node> bufferMap = new HashMap<>();
        for (int id : story) {
            for (int nodeId : nodeMap.keySet()) {
                if (nodeId == id) {
                    bufferMap.put(id, nodeMap.get(id));
                }
            }
        }
        nodeMap.clear();
        nodeMap.putAll(bufferMap);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (story != null && nodeMap != null) {
            hash = story.hashCode() + nodeMap.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        InMemoryHistoryManager otherManager = (InMemoryHistoryManager) obj;
        return nodeMap.size() == otherManager.nodeMap.size() &&
                equalMaps(nodeMap, otherManager.nodeMap) &&
                story.equals(otherManager.story);


    }


}