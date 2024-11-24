package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeById = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void addToHistory(Task task) {
        removeNode(nodeById.remove(task.getId()));
        Node nodeAdded = linkLast(task);
        nodeById.put(task.getId(), nodeAdded);
    }

    public Node linkLast(Task task) {
        final Node newNode = new Node(tail, task, null);
        if(tail == null){
            head = newNode;
        }
        else {
            tail.next = newNode;
        }
        tail = newNode;
        return newNode;
    }

    public List<Task> getTasks() {
        List<Task> historyList = new LinkedList<>();
        Node node = head;
        while (node != null) {
            historyList.add(node.task);
            node = node.next;
        }
        return historyList;
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        final Node next = node.next;
        final Node prev = node.prev;
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void remove(int id) {
        removeNode(nodeById.remove(id));
    }

    private static class Node {
        private Node prev;
        private Task task;
        private Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
