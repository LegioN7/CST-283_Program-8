package org.example.program8;

import java.util.NoSuchElementException;

/**
 * This class represents a queue of customers.
 * It provides methods to enqueue, dequeue, peek, and check if the queue is empty.
 *
 * @param <ItemType> The type of items that this queue holds.
 */
class CustomerQueue<ItemType> {

    Node front;
    private Node rear;
    private int size;
    /**
     * Constructs an empty CustomerQueue.
     */
    public CustomerQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Adds a new item to the end of the queue.
     *
     * @param newItem     The item to add.
     * @param currentTime The current time.
     */
    public void enqueue(ItemType newItem, int currentTime) {
        if (rear != null) {
            rear.next = new Node(newItem, null);
            rear = rear.next;
        } else {
            rear = new Node(newItem, null);
            front = rear;
        }
        size++;
        Customer customer = (Customer) newItem;
        customer.setArrivalTime(currentTime);
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Returns the item at the front of the queue without removing it.
     *
     * @return The item at the front of the queue.
     */
    public ItemType peek() {
        return front.value;
    }

    /**
     * Removes and returns the item at the front of the queue.
     *
     * @param currentTime The current time.
     * @return The item that was removed.
     */
    public ItemType dequeue(int currentTime) {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty. Cannot dequeue.");
        }

        ItemType returnItem = front.value;
        front = front.next;
        if (front == null)
            rear = null;
        size--;
        return returnItem;
    }

    /**
     * Returns the number of items in the queue.
     *
     * @return The number of items in the queue.
     */
    public int size() {
        return size;
    }

    /**
     * Returns a string representation of the queue.
     *
     * @return A string representation of the queue.
     */
    public String toString() {
        StringBuilder outString = new StringBuilder();

        Node p = front;
        while (p != null) {
            outString.append(p.value.toString());
            p = p.next;
            if (p != null)
                outString.append("\n");
        }
        return outString.toString();
    }

    /**
     * This class represents a node in the queue.
     * Each node holds a value and a reference to the next node in the queue.
     */
    class Node {
        ItemType value;
        Node next;

        /**
         * Constructs a new Node with the given value and next node.
         *
         * @param val The value of the node.
         * @param n   The next node in the queue.
         */
        Node(ItemType val, Node n) {
            value = val;
            next = n;
        }
    }

}