package org.example.program8;

/**
 * This class represents a customer in a retail checkout simulation.
 * It holds information about the customer's checkout difficulty, arrival time, enqueue time, checkout start time, and checkout end time.
 */
public class Customer {
    private static int idCounter = 0;
    private Difficulty checkoutDifficulty;
    private final int id;
    private int arrivalTime;
    private int enqueueTime;
    private int dequeueTime;
    private int checkoutStartTime;
    private int checkoutEndTime;

    /**
     * Constructs a new Customer with a unique ID.
     */
    public Customer() {
        id = idCounter++;
    }

    /**
     * Resets the ID for the next customer.
     */
    public static void resetId() {
        idCounter = 0;
    }

    /**
     * Returns the ID of the customer.
     *
     * @return The ID of the customer.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the checkout difficulty of the customer.
     *
     * @return The checkout difficulty of the customer.
     */
    public Difficulty getCheckoutDifficulty() {
        return checkoutDifficulty;
    }

    /**
     * Sets the checkout difficulty of the customer.
     *
     * @param checkoutDifficulty The checkout difficulty to set.
     */
    public void setCheckoutDifficulty(Difficulty checkoutDifficulty) {
        this.checkoutDifficulty = checkoutDifficulty;
    }

    /**
     * Returns the arrival time of the customer.
     *
     * @return The arrival time of the customer.
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the arrival time of the customer.
     *
     * @param arrivalTime The arrival time to set.
     */
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Returns the enqueue time of the customer.
     *
     * @return The enqueue time of the customer.
     */
    public int getEnqueueTime() {
        return enqueueTime;
    }

    /**
     * Sets the enqueue time of the customer.
     *
     * @param enqueueTime The enqueue time to set.
     */
    public void setEnqueueTime(int enqueueTime) {
        this.enqueueTime = enqueueTime;
    }


    /**
     * Returns the enqueue time of the customer.
     *
     * @return The enqueue time of the customer.
     */
    public int getDequeueTime() {
        return dequeueTime;
    }

    /**
     * Sets the dequeue time of the customer.
     *
     * @param dequeueTime The dequeue time to set.
     */

    public void setDequeueTime(int dequeueTime) {
        this.dequeueTime = dequeueTime;
    }


    /**
     * Returns the checkout start time of the customer.
     *
     * @return The checkout start time of the customer.
     */
    public int getCheckoutStartTime() {
        return checkoutStartTime;
    }

    /**
     * Sets the checkout start time of the customer.
     *
     * @param checkoutStartTime The checkout start time to set.
     */
    public void setCheckoutStartTime(int checkoutStartTime) {
        this.checkoutStartTime = checkoutStartTime;
    }

    /**
     * Returns the checkout end time of the customer.
     *
     * @return The checkout end time of the customer.
     */
    public int getCheckoutEndTime() {
        return checkoutEndTime;
    }

    /**
     * Sets the checkout end time of the customer.
     *
     * @param checkoutEndTime The checkout end time to set.
     */
    public void setCheckoutEndTime(int checkoutEndTime) {
        this.checkoutEndTime = checkoutEndTime;
    }

    /**
     * Returns the checkout duration of the customer.
     *
     * @return The checkout duration of the customer.
     */
    public int getCheckoutDuration() {
        return checkoutEndTime - checkoutStartTime;
    }

    /**
     * Returns the waiting time of the customer in the checkout line.
     *
     * @return The waiting time of the customer in the checkout line.
     */
    public int getWaitingTime() {
        return checkoutStartTime - enqueueTime;
    }

    /**
     * This enum represents the difficulty of checking out a customer.
     * It can be EASY, MEDIUM, or DIFFICULT.
     */
    public enum Difficulty {
        EASY, MEDIUM, DIFFICULT
    }
}