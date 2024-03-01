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


import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationLogic {


    private ToggleGroup group;
    private TextField easyCustomerTextField;
    private TextField mediumCustomerTextField;
    private TextField difficultCustomerTextField;
    private TextArea textArea;

    private List<CustomerQueue<Customer>> checkoutLines;


    Random random = new Random();


    // Simulation Duration in seconds
    final double SIMULATION_DURATION = 43200.0;

    // Customer Probability in seconds
    // 1 in 360 seconds
    final double CUSTOMER_PROBABILITY_MIN = 1.0 / 360.0;

    // 1 every 10 seconds
    final double CUSTOMER_PROBABILITY_MAX = 1.0 / 10.0;

    // Total customers checked out per line
    int[] total_checked_out_per_line;


    // Simulation Timer
    int time;

    // Total customers waiting to check out
    int customer_waiting = 0;
    // Total average of customer wait time
    long total_waiting_time = 0;

    // Total customers who checked out
    int total_customer_checkedout = 0;

    // Total checkout time
    long total_checkout_time= 0;
    int total_customersleft = 0;


    public SimulationLogic(ToggleGroup group, TextField easyCustomerTextField, TextField mediumCustomerTextField, TextField difficultCustomerTextField, TextArea textArea) {
        this.group = group;
        this.easyCustomerTextField = easyCustomerTextField;
        this.mediumCustomerTextField = mediumCustomerTextField;
        this.difficultCustomerTextField = difficultCustomerTextField;
        this.textArea = textArea;
    }

    private void resetSimulation() {
        textArea.clear();
        total_checked_out_per_line = null;
        customer_waiting = 0;
        total_waiting_time = 0;
        total_customer_checkedout = 0;
        total_checkout_time = 0;
    }

    public void startSimulation(int numberOfLines, int easyCustomerPercentage, int mediumCustomerPercentage, int difficultCustomerPercentage) {
        resetSimulation();
        if (numberOfLines < 1 || numberOfLines > 3) {
            throw new IllegalArgumentException("Number of lines must be between 1 and 3");
        }
        if (easyCustomerPercentage > 100 || mediumCustomerPercentage > 100 || difficultCustomerPercentage > 100) {
            throw new IllegalArgumentException("Individual customer percentages must not exceed 100");
        }
        if (easyCustomerPercentage + mediumCustomerPercentage + difficultCustomerPercentage > 100) {
            throw new IllegalArgumentException("The sum of customer percentages must not exceed 100");
        }
        checkoutSimulation(numberOfLines, easyCustomerPercentage, mediumCustomerPercentage, difficultCustomerPercentage);
        simulationOutput();
    }

    public void checkoutSimulation(int numberOfLines, int easyCustomerPercentage, int mediumCustomerPercentage, int difficultCustomerPercentage) {
        checkoutLines = initializeSimulation(numberOfLines);

        for (time = 1; time <= SIMULATION_DURATION; time++) {
            double customerArrivalProbability = random.nextBoolean() ? CUSTOMER_PROBABILITY_MAX : CUSTOMER_PROBABILITY_MIN;

            if (customerArrivalProbability >= random.nextDouble()) {
                int shortestLineIndex = getShortestLineIndex(checkoutLines);
                handleCustomerArrival(checkoutLines.get(shortestLineIndex), easyCustomerPercentage, mediumCustomerPercentage, difficultCustomerPercentage);
            }
            
            for (int i = 0; i < checkoutLines.size(); i++) {
                CustomerQueue<Customer> checkoutLine = checkoutLines.get(i);
                if (!checkoutLine.isEmpty() && time >= checkoutLine.peek().checkoutTime) {
                    Customer customer = checkoutLine.dequeue();
                    total_checkout_time += customer.checkoutTime;
                    total_waiting_time += time - customer.arrivalTime;
                    total_customer_checkedout++;
                    total_checked_out_per_line[i]++;
                }
            }
        }

        for (CustomerQueue<Customer> queue : checkoutLines) {
            while (!queue.isEmpty()) {
                queue.dequeue();
                total_customersleft++;
            }
        }

        total_waiting_time = total_checkout_time;
    }



    private int getShortestLineIndex(List<CustomerQueue<Customer>> checkoutLines) {
        List<Integer> shortestLines = new ArrayList<>();
        int shortestLineLength = checkoutLines.get(0).size();
        shortestLines.add(0);

        for (int i = 1; i < checkoutLines.size(); i++) {
            int currentLineLength = checkoutLines.get(i).size();
            if (currentLineLength < shortestLineLength) {
                shortestLineLength = currentLineLength;
                shortestLines.clear();
                shortestLines.add(i);
            } else if (currentLineLength == shortestLineLength) {
                shortestLines.add(i);
            }
        }

        int randomIndex = random.nextInt(shortestLines.size());
        return shortestLines.get(randomIndex);
    }


    private void handleCustomerArrival(CustomerQueue<Customer> checkoutLine, int easyCustomerPercentage, int mediumCustomerPercentage, int difficultCustomerPercentage) {
        boolean customerCreatedInCurrentInterval = false;

        if (time % 10 == 0) {
            int randomCustomer = random.nextInt(2);

            if (randomCustomer == 1) {
                createCustomer(checkoutLine, easyCustomerPercentage, mediumCustomerPercentage, difficultCustomerPercentage);
                customerCreatedInCurrentInterval = true;
            }
        }

        if (time % 360 == 0 && !customerCreatedInCurrentInterval) {
            int randomCustomer = random.nextInt(2);

            if (randomCustomer == 1) {
                createCustomer(checkoutLine, easyCustomerPercentage, mediumCustomerPercentage, difficultCustomerPercentage);
            }
        }
    }

    private void createCustomer(CustomerQueue<Customer> checkoutLine, int easyCustomerPercentage, int mediumCustomerPercentage, int difficultCustomerPercentage) {
        double customerTypeProbability = random.nextDouble();

        if (customerTypeProbability <= (double) easyCustomerPercentage / 100) {
            checkoutLine.enqueue(new Customer(time, 60));
            customer_waiting++;
        } else if (customerTypeProbability <= (double) (easyCustomerPercentage + mediumCustomerPercentage) / 100) {
            checkoutLine.enqueue(new Customer(time, 120));
            customer_waiting++;
        } else if (customerTypeProbability <= (double) (easyCustomerPercentage + mediumCustomerPercentage + difficultCustomerPercentage) / 100) {
            checkoutLine.enqueue(new Customer(time, 300));
            customer_waiting++;
        }
    }

    private List<CustomerQueue<Customer>> initializeSimulation(int openLines) {
        total_checked_out_per_line = new int[openLines];
        checkoutLines = new ArrayList<>();
        for (int i = 0; i < openLines; i++) {
            checkoutLines.add(new CustomerQueue<>());
        }
        return checkoutLines;
    }


    private void simulationOutput(){
        StringBuilder output = new StringBuilder();
        output.append("Average Customer Wait Time: ").append(formatTime((double)total_waiting_time / total_customer_checkedout)).append("\n");
        output.append("Average Checkout Time: ").append(formatTime((double)total_checkout_time / total_customer_checkedout)).append("\n");
        output.append("Total Customers Checked Out: ").append(total_customer_checkedout).append("\n");
        for (int i = 0; i < total_checked_out_per_line.length; i++) {
            output.append("Total Customers Checked Out in Line ").append(i+1).append(": ").append(total_checked_out_per_line[i]).append("\n");
        }

        output.append("Total Customers Left: ").append(total_customersleft).append("\n");

        textArea.setText(output.toString());
    }

    private String formatTime(double timeInSeconds) {
        int minutes = (int) timeInSeconds / 60;
        int seconds = (int) timeInSeconds % 60;
        return minutes + " minutes " + seconds + " seconds";
    }
}