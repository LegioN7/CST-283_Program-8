package org.example.program8;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class RetailCheckoutSimulation extends Application {

    private ToggleGroup group;
    private RadioButton checkoutLines1;
    private RadioButton checkoutLines2;
    private RadioButton checkoutLines3;
    private TextArea textArea;
    private Button startButton;
    private Button exitButton;
    private TextField easyCustomerTextField;
    private TextField mediumCustomerTextField;
    private TextField difficultCustomerTextField;

    private SimulationLogic simulation;

    private void createRadioButtons(VBox root) {
        Label lineLabel = new Label("Number of Lines");

        group = new ToggleGroup();

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

    private void createTextFields(HBox hbox) {
        hbox.getChildren().addAll(
                createTextFieldWithLabel("Easy Customer\n(Checkout Time: 1 Minute)", 20),
                createTextFieldWithLabel("Medium Customer\n(Checkout Time: 2 Minutes)", 60),
                createTextFieldWithLabel("Difficult Customer\n(Checkout Time: 5 Minutes)", 20)
        );
    }

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

    private TextField createTextField(int initialValue) {
        TextField textField = new TextField();
        textField.setText(String.valueOf(initialValue));
        textField.setMaxWidth(50);
        return textField;
    }

    private VBox createTextArea() {
        Label simulationResultLabel = new Label("Simulation Results");
        textArea = new TextArea();
        textArea.setEditable(false);
        return new VBox(simulationResultLabel, textArea);
    }

    private Button createStartButton() {
        startButton = new Button("Start Simulation");
        startButton.setOnAction(event -> startSimulation());
        return startButton;
    }

    private Button createExitButton() {
        exitButton = new Button("Exit Simulation");
        exitButton.setOnAction(event -> exitSimulation());
        return exitButton;
    }

    private void startSimulation() {
        int numberOfLines = 1;
        if (checkoutLines2.isSelected()) {
            numberOfLines = 2;
        } else if (checkoutLines3.isSelected()) {
            numberOfLines = 3;
        }

        int easyCustomerPercentage = Integer.parseInt(easyCustomerTextField.getText());
        int mediumCustomerPercentage = Integer.parseInt(mediumCustomerTextField.getText());
        int difficultCustomerPercentage = Integer.parseInt(difficultCustomerTextField.getText());

        simulation.startSimulation(numberOfLines, easyCustomerPercentage, mediumCustomerPercentage, difficultCustomerPercentage);
    }

    private void exitSimulation() {
        System.exit(0);
    }


    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        createRadioButtons(root);

        HBox hbox = new HBox(10);
        createTextFields(hbox);
        root.getChildren().add(hbox);

        root.getChildren().add(createTextArea());
        root.getChildren().add(createStartButton());
        root.getChildren().add(createExitButton());

        simulation = new SimulationLogic(group, easyCustomerTextField, mediumCustomerTextField, difficultCustomerTextField, textArea);

        Scene scene = new Scene(root, 720, 480);
        primaryStage.setTitle("Simulation GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(RetailCheckoutSimulation.class, args);
    }

}
