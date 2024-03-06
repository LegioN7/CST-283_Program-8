package org.example.program8;

// CST-283
// Aaron Pelto
// Winter 2024

/*
    Write a computer simulation to measure working time for a retail store checkout area.
    Assume customers complete their shopping and then seek a checkout line to pay. Constraints and variables for this
    simulation are:
        The simulation timer should be seconds.
            Consider all events that could occur during one second of the work day.
        The store has three checkout lines.
            Allow the simulation to be set to run with 1, 2, or 3 open.
    All the settings for customer arrivals at the checkout area range from a very slow probability of 1 in 360 seconds
    to an extremely (and perhaps unrealistically) busy 1 every 10 seconds.
    Execute your simulation for an entire 12-hour work day: 43200 seconds
    If the number of queues exceeds one, a prudent customer will always select the shortest one to enter.
        Be sure to include this in your simulation.

    Your simulation output should include the following:
        Average customer wait time (for each queue as well as further averaged for all customers)
        Idle time for workers (for each worker managing a queue as well as for all workers during the day)

    Package your simulation within its own class
    Create a GUI “front-end” driver.

    Your GUI should include at least the following:
    Radio button or similar component to allow selection of 1, 2, or 3 open lines
        A slider to vary customer arrival frequency
        A text area or labels to clearly show the simulation results.
        A button to launch the simulation


    Finally, to add more variability and realism to the scenario

    Don’t assume that all customers require the same amount of checkout time once they are dequeued and on the checkout conveyor.
        Set a randomly chosen variable time:
            20% of customers will require very little time checking out (1 minute)
            60% will require a medium amount of checkout time (two minutes)
            20% will require a long time (five minutes).
            These can be set as constants, but it is also optional to allow these constraints to be entered via your user interface.

    Note:
        The provided dynamic queue class is recommended for this exercise.
        Be sure to verify your implementation of the queue class includes the capability of reporting the length of a queue.

 */


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the logic for a retail store checkout simulation.
 * The simulation measures the working time for a retail store checkout area.
 * It assumes customers complete their shopping and then seek a checkout line to pay.
 * The store has up to three checkout lines, and the simulation can be set to run with one, two, or three open.
 * The settings for customer arrivals at the checkout area range from a very slow probability of 1 in 360 seconds
 * to an extremely busy 1 every 10 seconds.
 * The simulation is executed for an entire 12-hour work day: 43200 seconds.
 * If the number of queues exceeds one, a prudent customer will always select the shortest one to enter.
 * The simulation output includes the average customer wait time
 * and the idle time for workers
 */
public class SimulationLogic {
    /**
     * Checkout time in seconds for easy customers.
     */
    private static final int EASY_CHECKOUT_TIME = 60;
    /**
     * Checkout time in seconds for medium customers.
     */
    private static final int MEDIUM_CHECKOUT_TIME = 120;
    /**
     * Checkout time in seconds for difficult customers.
     */
    private static final int DIFFICULT_CHECKOUT_TIME = 360;
    /**
     * Duration of the simulation in seconds. Represents a 12-hour work day.
     */
    private static final double SIMULATION_DURATION = 43200.0;
    /**
     * List of checkout lines in the simulation.
     */
    List<CustomerQueue<Customer>> checkoutLines;


    // Customer Variables
    /**
     * Total number of customers created in the simulation.
     */
    int totalCustomersCreated = 0;
    /**
     * Count of easy customers created in the simulation.
     */
    int easyCustomersCount = 0;
    /**
     * Count of medium customers created in the simulation.
     */
    int mediumCustomersCount = 0;
    /**
     * Count of difficult customers created in the simulation.
     */
    int difficultCustomersCount = 0;
    /**
     * Total number of customers checked out in the simulation.
     */
    int totalCustomersCheckedOut = 0;
    /**
     * Array storing the total number of customers checked out per line in the simulation.
     */
    int[] totalCustomersCheckedOutPerLine;

    /**
     * Total waiting time for all customers in the simulation.
     */
    int totalCustomerWaitingTime = 0;

    /**
     * Array storing the total waiting time for customers per line in the simulation.
     */
    int[] totalCustomerWaitingTimePerLine;
    /**
     * Total checkout time for all customers in the simulation.
     */
    int totalCustomerCheckoutTime = 0;

    /**
     * Array storing the total checkout time for customers per line in the simulation.
     */
    int[] totalCustomerCheckoutTimePerLine;

    /**
     * Total idle time for all workers in the simulation.
     */
    int totalWorkerIdleTime = 0;
    /**
     * Array storing the total idle time for workers per line in the simulation.
     */
    int[] totalWorkerIdleTimePerLine;


    /**
     * Resets the simulation by setting the relevant counters to zero.
     */
    private void resetSimulation() {
        // Reset the customer variables
        totalCustomersCheckedOutPerLine = null;
        totalCustomerWaitingTime = 0;
        totalCustomersCheckedOut = 0;
        totalCustomerCheckoutTime = 0;
    }

    /**
     * Starts the simulation with the given parameters.
     *
     * @param numberOfLines               The number of checkout lines to simulate.
     * @param customerArrivalFrequency    The frequency of customer arrivals.
     * @param easyCustomerPercentage      The percentage of customers that are easy to check out.
     * @param mediumCustomerPercentage    The percentage of customers that are medium to check out.
     * @param difficultCustomerPercentage The percentage of customers that are difficult to check out.
     */
    public void startSimulation(int numberOfLines, int customerArrivalFrequency, int easyCustomerPercentage, int mediumCustomerPercentage, int difficultCustomerPercentage) {
        // Initialize the array for the idle time of workers per line
        totalWorkerIdleTimePerLine = new int[numberOfLines];

        // Initialize the array for the waiting time of customers per line
        totalCustomerWaitingTimePerLine = new int[numberOfLines];

        // Initialize the array for the total checkout time of customers per line
        totalCustomerCheckoutTimePerLine = new int[numberOfLines];

        // Reset the simulation
        resetSimulation();

        // Create the checkout lines
        checkoutLines = new ArrayList<>();
        // Initialize the array for the total number of customers checked out per line
        totalCustomersCheckedOutPerLine = new int[numberOfLines];
        for (int i = 0; i < numberOfLines; i++) {
            checkoutLines.add(new CustomerQueue<>());
        }

        // Create the customer array list
        List<Customer> customers = new ArrayList<>();

        // Loop through the simulation duration of 12 hours
        for (int time = 1; time <= SIMULATION_DURATION; time++) {
            // Create a new customer if the modulo of the time and the customer arrival frequency is zero
            if (time % customerArrivalFrequency == 0) {
                // Create a new customer
                Customer customer = createCustomer();
                // Add the customer to the list
                customers.add(customer);
                // Assign a checkout difficulty to each customer
                assignCustomerCheckoutDifficulty(customers, easyCustomerPercentage, mediumCustomerPercentage, difficultCustomerPercentage);
                // Set the arrival time for the customer (Creation time)
                customer.setArrivalTime(time);
                // Check if the checkout lines are empty
                checkEmptyCheckoutLines(time);
                // Assign the customer to the shortest queue
                assignToShortestQueue(customer, time);
                // Set the customer's enqueue time
                customer.setEnqueueTime(time);
                // Process the checkout lines
                processCheckoutLines(time);
                // Increment the total number of customers created
                totalCustomersCreated++;
            }
        }
    }

    /**
     * Creates a new customer.
     *
     * @return A new customer.
     */
    public Customer createCustomer() {
        // Create a new customer
        return new Customer();
    }

    /**
     * Assigns a checkout difficulty to each customer in the given list.
     *
     * @param customers                   The list of customers.
     * @param easyCustomerPercentage      The percentage of customers that are easy to check out.
     * @param mediumCustomerPercentage    The percentage of customers that are medium to check out.
     * @param difficultCustomerPercentage The percentage of customers that are difficult to check out.
     */
    private void assignCustomerCheckoutDifficulty(List<Customer> customers, int easyCustomerPercentage, int mediumCustomerPercentage, int difficultCustomerPercentage) {
        // Calculate the number of customers for each difficulty
        int easyCustomers = customers.size() * easyCustomerPercentage / 100;
        int mediumCustomers = customers.size() * mediumCustomerPercentage / 100;
        int difficultCustomers = customers.size() - easyCustomers - mediumCustomers;

        // Reset the customer counts
        easyCustomersCount = 0;
        mediumCustomersCount = 0;
        difficultCustomersCount = 0;

        // Loop through the customers and assign a checkout difficulty to each customer
        for (int i = 0; i < customers.size(); i++) {
            if (i < easyCustomers) {
                customers.get(i).setCheckoutDifficulty(Customer.Difficulty.EASY);
                easyCustomersCount++;
            } else if (i < easyCustomers + mediumCustomers) {
                customers.get(i).setCheckoutDifficulty(Customer.Difficulty.MEDIUM);
                mediumCustomersCount++;
            } else if (i < easyCustomers + mediumCustomers + difficultCustomers) {
                customers.get(i).setCheckoutDifficulty(Customer.Difficulty.DIFFICULT);
                difficultCustomersCount++;
            }
        }
    }

    /**
     * Assigns the given customer to the shortest queue.
     *
     * @param customer    The customer to assign.
     * @param CurrentTime The current time.
     */
    private void assignToShortestQueue(Customer customer, int CurrentTime) {
        // Get the shortest queue
        CustomerQueue<Customer> shortestQueue = checkoutLines.getFirst();

        // Loop through the checkout lines and find the shortest queue
        for (CustomerQueue<Customer> queue : checkoutLines) {
            // Check if the current queue is shorter than the shortest queue
            if (queue.size() < shortestQueue.size()) {
                // Set the current queue as the shortest queue
                shortestQueue = queue;
            }
        }

        // Enqueue the customer to the shortest queue
        shortestQueue.enqueue(customer, CurrentTime);
        customer.setEnqueueTime(CurrentTime);
    }

    /**
     * Checks if the checkout lines are empty and increments the idle time for the workers.
     *
     * @param time The current time.
     */
    private void checkEmptyCheckoutLines(int time) {
        int i = 0;

        // Loop through the checkout lines
        for (CustomerQueue<Customer> checkoutLine : checkoutLines) {
                if (checkoutLine.isEmpty()) {
                // Increment the idle time for the worker
                totalWorkerIdleTimePerLine[i] += time;
                totalWorkerIdleTime += time;
            }
            i++;
        }
    }

    /**
     * Processes the checkout lines by checking if the customers are ready to be checked out.
     *
     * @param time The current time.
     */
    private void processCheckoutLines(int time) {
        // Loop through the checkout lines
        for (CustomerQueue<Customer> checkoutLine : checkoutLines) {
            // Check if the checkout line is not empty
            if (!checkoutLine.isEmpty()) {
                // Peek at the customer at the front of the queue
                Customer customer = checkoutLine.peek();

                // Check if the customer is easy, medium, or difficult to check out
                if (customer.getCheckoutDifficulty() == Customer.Difficulty.EASY) {
                    // Set the checkout start time for the customer
                    if (customer.getCheckoutStartTime() == 0) {
                        customer.setCheckoutStartTime(time);
                    }
                    // Dequeue the customer if the checkout time has been reached for 60 seconds
                    if (time >= customer.getCheckoutStartTime() + EASY_CHECKOUT_TIME) {
                        totalCustomerCheckoutTime += EASY_CHECKOUT_TIME;
                    }
                } else if (customer.getCheckoutDifficulty() == Customer.Difficulty.MEDIUM) {
                    // Set the checkout start time for the customer
                    if (customer.getCheckoutStartTime() == 0) {
                        customer.setCheckoutStartTime(time);
                    }
                    // Dequeue the customer if the checkout time has been reached for 120 seconds
                    if (time >= customer.getCheckoutStartTime() + MEDIUM_CHECKOUT_TIME) {
                        dequeueCustomer(checkoutLine, checkoutLines.indexOf(checkoutLine), time);
                        totalCustomerCheckoutTime += MEDIUM_CHECKOUT_TIME;
                    }
                } else if (customer.getCheckoutDifficulty() == Customer.Difficulty.DIFFICULT) {
                    // Set the checkout start time for the customer
                    if (customer.getCheckoutStartTime() == 0) {
                        customer.setCheckoutStartTime(time);
                    }
                    // Dequeue the customer if the checkout time has been reached for 360 seconds
                    if (time >= customer.getCheckoutStartTime() + DIFFICULT_CHECKOUT_TIME) {
                        dequeueCustomer(checkoutLine, checkoutLines.indexOf(checkoutLine), time);
                        totalCustomerCheckoutTime += DIFFICULT_CHECKOUT_TIME;
                    }
                }
            }
        }
    }

    /**
     * Dequeues a customer from the given queue.
     *
     * @param queue       The queue to dequeue a customer from.
     * @param lineIndex   The index of the line.
     * @param currentTime The current time.
     */
    private void dequeueCustomer(CustomerQueue<Customer> queue, int lineIndex, int currentTime) {
        // Dequeue the customer
        Customer customer = queue.dequeue(currentTime);
        // Set the dequeue time for the customer
        customer.setDequeueTime(currentTime);
        // Set the checkout end time for the customer
        customer.setCheckoutEndTime(currentTime);

        // Add the waiting time to the total customer waiting time
        totalCustomerWaitingTime += customer.getWaitingTime();

        // Add the waiting time to the total customer waiting time for the line
        totalCustomerWaitingTimePerLine[lineIndex] += customer.getWaitingTime();

        // Add the checkout duration to the total customer checkout time
        totalCustomerCheckoutTimePerLine[lineIndex] += customer.getCheckoutDuration();

        // Increment the total customers checked out
        totalCustomersCheckedOut++;
        // Increment the total customers checked out for the line
        totalCustomersCheckedOutPerLine[lineIndex]++;
        // Add the checkout duration to the total customer checkout time
        totalCustomerCheckoutTime += customer.getCheckoutDuration();
    }

    /**
     * Generates statistics about the customers.
     *
     * @return A string containing the customer statistics.
     */
    private String generateCustomerStats() {
        return "\n\nTotal Number of Customers Created: " + totalCustomersCreated +
                "\n\tTotal Number of Easy Customers: " + easyCustomersCount +
                "\n\tTotal Number of Medium Customers: " + mediumCustomersCount +
                "\n\tTotal Number of Difficult Customers: " + difficultCustomersCount;
    }

    /**
     * Generates statistics about a specific checkout line.
     *
     * @param lineIndex The index of the line.
     * @return A string containing the checkout line statistics.
     */
    private String generateCheckoutLineStats(int lineIndex) {
        double averageWaitTime = 0;
        double averageCheckoutTime = 0;

        if (totalCustomersCheckedOutPerLine[lineIndex] > 0) {
            averageWaitTime = (double) totalCustomerWaitingTimePerLine[lineIndex] / totalCustomersCheckedOutPerLine[lineIndex];
            averageCheckoutTime = (double) totalCustomerCheckoutTimePerLine[lineIndex] / totalCustomersCheckedOutPerLine[lineIndex];
        }

        return "\n\nCheckout Line " + (lineIndex + 1) + ":" +
                "\n\tTotal Customers Checked Out: " + totalCustomersCheckedOutPerLine[lineIndex] +
                "\n\tAverage Wait Time: " + formatTime(averageWaitTime) +
                "\n\tAverage Checkout Time: " + formatTime(averageCheckoutTime);
    }

    /**
     * Generates statistics about the workers.
     *
     * @return A string containing the worker statistics.
     */
    private String generateWorkerStats() {
        StringBuilder output = new StringBuilder("\n\nWorker Results");
        if (!checkoutLines.isEmpty()) {
            output.append("\n\tAverage Idle Time for Workers: ")
                    .append(formatTime((double) totalWorkerIdleTime / checkoutLines.size()));
            for (int i = 0; i < checkoutLines.size(); i++) {
                output.append("\n\tCheckout Line ").append(i + 1).append(" Idle Time: ")
                        .append(formatTime(totalWorkerIdleTimePerLine[i]));
            }
        }
        return output.toString();
    }

    /**
     * Gets the results of the simulation.
     *
     * @return A string containing the simulation results.
     */
    public String getSimulationResults() {
        StringBuilder output = new StringBuilder();
        output.append("Simulation Results");

        output.append(generateCustomerStats());

        for (int i = 0; i < totalCustomersCheckedOutPerLine.length; i++) {
            output.append(generateCheckoutLineStats(i));
        }

        output.append(generateWorkerStats());

        return output.toString();
    }

    /**
     * Formats the given time in seconds to a string in the format "minutes seconds".
     *
     * @param timeInSeconds The time in seconds.
     * @return A string representing the time in the format "minutes seconds".
     */
    private String formatTime(double timeInSeconds) {
        int minutes = (int) timeInSeconds / 60;
        int seconds = (int) timeInSeconds % 60;
        return minutes + " minutes " + seconds + " seconds";
    }
}