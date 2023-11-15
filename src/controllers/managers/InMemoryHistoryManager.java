package controllers.managers;

import controllers.interfaces.HistoryManager;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> historyMap = new HashMap<>();

    private Node head;

    private Node tail;

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        for (Node i = head; i != null; i = i.next) {
            tasks.add(i.data);
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if(node == null) {
            System.out.println("Task not found in list.");
            return;
        }
        if(node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if(node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        node.next = null;
        node.prev = null;

    }



    private void linkLast(Task t) {
        final Node newNode = new Node(t, null, tail);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }



    @Override
    public void remove(int id) {
        Node current = head;
        while (current != null) {
            if (current.data.getId() == id) {
                Node next = current.next;
                removeNode(current);
                current = next;
            } else {
                current = current.next;
            }
        }

    }

    @Override
    public void add(Task task) {
            if(task == null) return;
            if (historyMap.containsKey(task.getId())) {
                remove(task.getId());
            }
            historyMap.put(task.getId(), tail);
            linkLast(task);
    }
}
class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Task data, Node next, Node prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}