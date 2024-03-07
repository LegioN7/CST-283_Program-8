package org.example.program8;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

/**
 * This class represents a JavaFX application for a retail store checkout simulation.
 * The simulation measures the working time for a retail store checkout area.
 * It provides a GUI for the user to interact with the simulation.
 */
public class RetailCheckoutSimulation extends Application {


    /**
     * Radio button for selecting one checkout line.
     */
    private RadioButton checkoutLines1;

    /**
     * Radio button for selecting two checkout lines.
     */
    private RadioButton checkoutLines2;

    /**
     * Radio button for selecting three checkout lines.
     */
    private RadioButton checkoutLines3;

    /**
     * Slider for adjusting the customer arrival frequency.
     */
    private Slider customerArrivalSlider;

    /**
     * Text area for displaying the simulation results.
     */
    private TextArea textArea;

    /**
     * Button for starting the simulation.
     */
    private Button startButton;

    /**
     * Button for resetting the simulation.
     */
    private Button resetButton;

    /**
     * Button for exiting the simulation.
     */
    private Button exitButton;

    /**
     * Text field for entering the percentage of easy customers.
     */
    private TextField easyCustomerTextField;

    /**
     * Text field for entering the percentage of medium customers.
     */
    private TextField mediumCustomerTextField;

    /**
     * Text field for entering the percentage of difficult customers.
     */
    private TextField difficultCustomerTextField;

    /**
     * The simulation logic.
     */
    private SimulationLogic simulation;

    /**
     * Creates radio buttons for selecting the number of checkout lines.
     * @param root The VBox to which the radio buttons are added.
     */
    private void createRadioButtons(VBox root) {
        Label lineLabel = new Label("Number of Lines");

        ToggleGroup group = new ToggleGroup();

        checkoutLines1 = new RadioButton("1 Line");
        checkoutLines2 = new RadioButton("2 Lines");
        checkoutLines3 = new RadioButton("3 Lines");

        checkoutLines1.setToggleGroup(group);
        checkoutLines2.setToggleGroup(group);
        checkoutLines3.setToggleGroup(group);

        checkoutLines2.setSelected(true);

        HBox radioButtons = new HBox(10, checkoutLines1, checkoutLines2, checkoutLines3);
        root.getChildren().addAll(lineLabel, radioButtons);
    }

    /**
     * Creates text fields for entering the percentages of easy, medium, and difficult customers.
     * @param hbox The HBox to which the text fields are added.
     */
    private void createTextFields(HBox hbox) {
        hbox.getChildren().addAll(
                createTextFieldWithLabel("Easy Customer\n(Checkout Time: 1 Minute)", 20),
                createTextFieldWithLabel("Medium Customer\n(Checkout Time: 2 Minutes)", 60),
                createTextFieldWithLabel("Difficult Customer\n(Checkout Time: 5 Minutes)", 20)
        );
    }

    /**
     * Creates a text field with a label.
     * @param title The title of the label.
     * @param initialValue The initial value of the text field.
     * @return A VBox containing the label and the text field.
     */
    private VBox createTextFieldWithLabel(String title, int initialValue) {
        Label titleLabel = new Label(title);
        TextField textField = createTextField(initialValue);
        if (title.startsWith("Easy")) {
            easyCustomerTextField = textField;
        } else if (title.startsWith("Medium")) {
            mediumCustomerTextField = textField;
        } else if (title.startsWith("Difficult")) {
            difficultCustomerTextField = textField;
        }
        return new VBox(titleLabel, textField);
    }

    /**
     * Creates a text field.
     * @param initialValue The initial value of the text field.
     * @return The created text field.
     */
    private TextField createTextField(int initialValue) {
        TextField textField = new TextField();
        textField.setText(String.valueOf(initialValue));
        textField.setMaxWidth(50);
        return textField;
    }

    /**
     * Creates a slider for adjusting the customer arrival frequency.
     * @param root The VBox to which the slider is added.
     */
    private void createCustomerArrivalSlider(VBox root) {
        Label sliderLabel = new Label("Customer Arrival Frequency");
        Label sliderValueLabel = new Label("1 customer every 60 seconds");

        customerArrivalSlider = new Slider();
        customerArrivalSlider.setMin(10);
        customerArrivalSlider.setMax(360);
        customerArrivalSlider.setValue(60);
        customerArrivalSlider.setShowTickLabels(true);
        customerArrivalSlider.setShowTickMarks(true);
        customerArrivalSlider.setMajorTickUnit(60);
        customerArrivalSlider.setSnapToTicks(true);

        customerArrivalSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerArrivalFrequency(customerArrivalSlider);
            sliderValueLabel.setText("1 customer every " + newValue.intValue() + " seconds");
        });

        VBox sliderBox = new VBox(sliderLabel, sliderValueLabel, customerArrivalSlider);
        root.getChildren().add(sliderBox);
    }

    /**
     * Resets the simulation by clearing the text area and resetting the buttons.
     */
    private void resetSimulation() {
        // Clear the text area
        textArea.clear();

        // Enable the start and exit buttons
        startButton.setDisable(false);
        exitButton.setDisable(false);

        // Reset the simulation
        simulation = new SimulationLogic();

        // Reset the UI Selections
        checkoutLines1.setSelected(false);
        checkoutLines2.setSelected(true);
        checkoutLines3.setSelected(false);

        customerArrivalSlider.setValue(60);

        easyCustomerTextField.setText("20");
        mediumCustomerTextField.setText("60");
        difficultCustomerTextField.setText("20");
    }

    /**
     * Sets the customer arrival frequency.
     * @param frequency The slider representing the customer arrival frequency.
     */
    private void setCustomerArrivalFrequency(Slider frequency) {
        customerArrivalSlider.setValue(frequency.getValue());
    }

    /**
     * Creates a text area for displaying the simulation results.
     * @return A VBox containing the label and the text area.
     */
    private VBox createTextArea() {
        Label simulationResultLabel = new Label("Simulation Results");
        textArea = new TextArea();
        textArea.setEditable(false);
        return new VBox(simulationResultLabel, textArea);
    }

    /**
     * Creates a button for starting the simulation.
     * @return The created start button.
     */
    private Button createStartButton() {
        startButton = new Button("Start Simulation");
        startButton.setOnAction(event -> startSimulation());
        return startButton;
    }

    private Button createResetButton () {
        resetButton = new Button("Reset Simulation");
        resetButton.setOnAction(event -> resetSimulation());
        return resetButton;
    }

    /**
     * Creates a button for exiting the simulation.
     * @return The created exit button.
     */
    private Button createExitButton() {
        exitButton = new Button("Exit Simulation");
        exitButton.setOnAction(event -> exitSimulation());
        return exitButton;
    }

    /**
     * Starts the simulation.
     */
    private void startSimulation() {
        int numberOfLines = 0;

        if (checkoutLines1.isSelected()) {
            numberOfLines = 1;
        } else if (checkoutLines2.isSelected()) {
            numberOfLines = 2;
        } else if (checkoutLines3.isSelected()) {
            numberOfLines = 3;
        }

        resetSimulation();

        int customerArrivalFrequency = (int) customerArrivalSlider.getValue();

        try {
            int easyCustomerPercentage = Integer.parseInt(easyCustomerTextField.getText());
            int mediumCustomerPercentage = Integer.parseInt(mediumCustomerTextField.getText());
            int difficultCustomerPercentage = Integer.parseInt(difficultCustomerTextField.getText());

            simulation.startSimulation(numberOfLines, customerArrivalFrequency, easyCustomerPercentage, mediumCustomerPercentage, difficultCustomerPercentage);
        } catch (NumberFormatException e) {
            textArea.appendText("Invalid percentage entered\n");
        }

        // Disable the start button and enable the reset button
        startButton.setDisable(true);
        resetButton.setDisable(false);

        updateUI();
    }

    /**
     * Updates the user interface after the simulation has been run.
     */
    private void updateUI() {
        textArea.setText(simulation.getSimulationResults());
    }

    /**
     * Exits the simulation.
     */
    private void exitSimulation() {
        System.exit(0);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned, and after the system is ready for the application to begin running.
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        createRadioButtons(root);

        createCustomerArrivalSlider(root);

        HBox hbox = new HBox(10);
        createTextFields(hbox);
        root.getChildren().add(hbox);

        root.getChildren().add(createTextArea());
        root.getChildren().add(createStartButton());
        root.getChildren().add(createResetButton());
        root.getChildren().add(createExitButton());

        // Disable the reset button until the simulation has been started
        resetButton.setDisable(true);

        simulation = new SimulationLogic();

        Scene scene = new Scene(root, 720, 520);
        primaryStage.setTitle("Simulation GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main method for the JavaFX application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(RetailCheckoutSimulation.class, args);
    }

}
