package org.example.program8;

/**
 * The CustomerQueue class represents a queue of customers.
 * Each queue has a front and a rear associated with it.
 */
class CustomerQueue<ItemType>
{
    private class Node

    {
        ItemType value;
        Node next;
        Node(ItemType val, Node n)
        {
            value = val;
            next = n;
        }
    }
    private Node front;
    private Node rear;
    private int size = 0;

    public CustomerQueue()
    {
        front = null;
        rear  = null;
    }

    public void enqueue(ItemType newItem)
    {
        if (rear != null)
        {
            rear.next = new Node(newItem, null);
            rear = rear.next;
        }
        else
        {
            rear = new Node(newItem, null);
            front = rear;
        }
        size++;
    }

    public boolean isEmpty()
    {
        return front == null;
    }

    public ItemType peek()
    {
        return front.value;
    }

    public ItemType dequeue()
    {
        ItemType returnItem = front.value;
        front = front.next;
        if (front == null)
            rear = null;
        size--;
        return returnItem;
    }

    public int size() {
        return size;
    }

    public String toString()
    {
        String outString = "";

        Node p = front;
        while (p != null)
        {
            outString += p.value.toString();
            p = p.next;
            if (p != null)
                outString += "\n";
        }
        return outString;
    }


}