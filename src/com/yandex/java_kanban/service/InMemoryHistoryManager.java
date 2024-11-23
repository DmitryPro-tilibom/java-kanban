package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> historyLinkedList = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void addToHistory(Task task) {
        if (historyLinkedList.containsKey(task.getId())) {
            removeNode(historyLinkedList.get(task.getId()));
            historyLinkedList.remove(task.getId());
        }
        Node nodeAdded = linkLast(task);
        historyLinkedList.put(task.getId(), nodeAdded);
    }

    public Node linkLast(Task task) {
        final Node secondLast = tail;
        final Node newNode = new Node(secondLast, task, null);
        tail = newNode;
        if (secondLast != null) {
            secondLast.next = newNode;
        } else {
            head = newNode;
        }
        return newNode;
    }

    public List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyList.add(node.task);
            node = node.next;
        }
        return historyList;
    }

    public void removeNode(Node node) {
        if (node == head) {
            if (head.next != null)
                head = head.next;
            else
                head = null;
        }

        if (node == tail) {
            if (tail.prev != null)
                tail = tail.prev;
            else
                tail = null;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void remove(int id) {
        if (historyLinkedList.containsKey(id)) {
            removeNode(historyLinkedList.get(id));
            historyLinkedList.remove(id);
        }
    }

    static class Node {
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
