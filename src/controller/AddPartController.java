package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

import java.util.Optional;

/** This class controls the add part dialog window.
 This class and the associated window are invoked when the 'Add' button is pressed underneath the part table of the main form. */
public class AddPartController {
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private RadioButton inHouse;
    @FXML
    private RadioButton outSourced;
    @FXML
    private Label toggleLabel;
    @FXML
    private ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    private TextField autoIdTextField;
    @FXML
    private TextField nameTF;
    @FXML
    private TextField invTF;
    @FXML
    private TextField priceTF;
    @FXML
    private TextField maxTF;
    @FXML
    private TextField minTF;
    @FXML
    private TextField toggleTF;
    @FXML
    private Label nameError;
    @FXML
    private Label invError;
    @FXML
    private Label priceError;
    @FXML
    private Label maxError;
    @FXML
    private Label minError;
    @FXML
    private Label toggleError;
    private ObservableList<Part> partInventory;
    /** This method initializes the add part window.
     This method is invoked upon pressing the 'Add' button underneath the part table of the main form.

     This method adds listeners to all input fields in order to validate all input for the form and protects the program from crashing if a user does not input valid information.
     i.e. if a user uses letters for inventory, an error will be shown to prompt the user to fix the error and the save button will be disabled.
     Try/catch statements were included in every field listener to control for any exceptions in processing invalid information.
     I struggled with the program having exceptions when attempting to save improper data types, so I amended the situation by ensuring these exceptions would never occur through
     data validation and button disabling.
     The save button is not enabled unless all input fields are filled properly with valid information.
     The ID field is auto-generated and disabled, and included is a loop to check to ensure that the part ID is not duplicated.

     In a future version, a feature to alert the user that the inventory/min/max fields are invalid may be included here before the save button event handler.
     This way, if the inventory is more than the max, etc., the user will be alerted in real time.
     @param inventory Part inventory from the main window */
    public void initialize(ObservableList<Part> inventory){
        inHouse.setToggleGroup(toggleGroup);
        outSourced.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(inHouse);
        this.partInventory = inventory;
        saveButton.setDisable(true);

        nameError.setVisible(false);
        invError.setVisible(false);
        priceError.setVisible(false);
        maxError.setVisible(false);
        minError.setVisible(false);
        toggleError.setVisible(false);

        nameTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    nameTF.setStyle("-fx-border-color: none");
                    nameError.setVisible(false);
                    checkFields();
                } else {
                    nameTF.setStyle("-fx-border-color: none");
                    nameError.setVisible(true);
                    nameError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    saveButton.setDisable(true);
                }
            }
        });

        invTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    try{
                        int inventory = Integer.parseInt(newValue);
                        invTF.setText(String.valueOf(inventory));
                        invTF.setStyle("-fx-border-color: none");
                        invError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        invTF.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        invError.setVisible(true);
                        invError.setText("Only whole numbers allowed");
                        invError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                }
                if(newValue.isEmpty()){
                    invTF.setStyle("-fx-border-color: none");
                    invError.setVisible(true);
                    invError.setText("Required");
                    invError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    saveButton.setDisable(true);
                }
            }
        });

        priceTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty() && newValue.matches("^\\d*\\.?\\d*$")){
                        priceTF.setStyle("-fx-border-color: none");
                        priceError.setVisible(false);
                        checkFields();
                } else if(!newValue.isEmpty() && !newValue.matches("^\\d*\\.?\\d*$")){
                    priceTF.setStyle("-fx-border-color: red");
                    saveButton.setDisable(true);
                    priceError.setVisible(true);
                    priceError.setText("Only numbers allowed");
                    priceError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
                if(newValue.isEmpty()){
                    priceTF.setStyle("-fx-border-color: none");
                    saveButton.setDisable(true);
                    priceError.setVisible(true);
                    priceError.setText("Required");
                    priceError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
            }
        });

        maxTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    try{
                        Integer max = Integer.parseInt(newValue);
                        maxTF.setText(String.valueOf(max));
                        maxTF.setStyle("-fx-border-color: none");
                        maxError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        maxTF.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        maxError.setVisible(true);
                        maxError.setText("Only whole numbers allowed");
                        maxError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                }
                if(newValue.isEmpty()){
                    maxTF.setStyle("-fx-border-color: none");
                    saveButton.setDisable(true);
                    maxError.setVisible(true);
                    maxError.setText("Required");
                    maxError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
            }
        });

        minTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    try{
                        Integer min = Integer.parseInt(newValue);
                        minTF.setText(String.valueOf(min));
                        minTF.setStyle("-fx-border-color: none");
                        minError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        minTF.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        minError.setVisible(true);
                        minError.setText("Only whole numbers allowed");
                        minError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                }
                if(newValue.isEmpty()){
                    minTF.setStyle("-fx-border-color: none");
                    saveButton.setDisable(true);
                    minError.setVisible(true);
                    minError.setText("Required");
                    minError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
            }
        });

        toggleTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty() && inHouse.isSelected()){
                    try{
                        Integer min = Integer.parseInt(newValue);
                        toggleTF.setText(String.valueOf(min));
                        toggleTF.setStyle("-fx-border-color: none");
                        toggleError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        toggleTF.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        toggleError.setVisible(true);
                        toggleError.setText("Only whole numbers allowed");
                        toggleError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                } else if(!newValue.isEmpty() && outSourced.isSelected()){
                    toggleTF.setStyle("-fx-border-color: none");
                    toggleError.setVisible(false);
                    checkFields();
                }
                if(newValue.isEmpty()){
                    toggleTF.setStyle("-fx-border-color: none");
                    saveButton.setDisable(true);
                    toggleError.setVisible(true);
                    toggleError.setText("Required");
                    toggleError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
            }
        });
        toggleLabel.setText("Machine ID");
        inHouse.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(inHouse.isSelected()){
                    toggleLabel.setText("Machine ID:");
                }
            }
        });
        inHouse.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean old, Boolean newValue) {
                if(newValue && !toggleTF.getText().isEmpty()){
                    try{
                        Integer min = Integer.parseInt(toggleTF.getText());
                        toggleTF.setText(String.valueOf(min));
                        toggleTF.setStyle("-fx-border-color: none");
                        toggleError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        toggleTF.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        toggleError.setVisible(true);
                        toggleError.setText("Only whole numbers allowed");
                        toggleError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                }
            }
        });
        outSourced.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(outSourced.isSelected()){
                    toggleLabel.setText("Company Name:");
                }
            }
        });

        int min = 1;
        int max = 1000;
        int id_random = (int) (Math.random() * (max-min + 1) + min);
        for(int i = 0; i< partInventory.size(); i++){
            int id = partInventory.get(i).getPartID();
            if(!(id_random == id)){
                autoIdTextField.setText(String.valueOf(id_random));
                break;
            } else {
                autoIdTextField.setText(String.valueOf(id_random + 1));
            }
        }
        autoIdTextField.setEditable(false);
    }
    /** This method ensures no form fields are empty.
     This method is invoked from within the initialize method to check whether there are any empty form fields. */
    private void checkFields() {
        if (!nameTF.getText().trim().isEmpty() && !invTF.getText().trim().isEmpty() && !priceTF.getText().trim().isEmpty()
                && !minTF.getText().trim().isEmpty() && !maxTF.getText().trim().isEmpty() && !toggleTF.getText().trim().isEmpty()) {
            saveButton.setDisable(false);
        }
    }
    /** This method exits the window and returns the user to the main form window.
     When the cancel button is pressed, the dialog window is closed. */
    @FXML
    public void handleCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please confirm action");
        alert.setContentText("Changes will not be saved. Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } else if(result.isPresent() && result.get() == ButtonType.CANCEL){
            alert.close();
        }
    }
    /** This method is invoked when the save button is pressed.
     This method validates remaining information before the save is conducted and displays an error message if something is input incorrectly.
     i.e. if the minimum is greater than the maximum, an error alert will show. */
    @FXML
    public void handleSave(){
        if(Integer.parseInt(minTF.getText().trim()) > Integer.parseInt(maxTF.getText().trim())){
            AlertMessage.errorSaving(1);
        } else if(Integer.parseInt(invTF.getText().trim()) < Integer.parseInt(minTF.getText().trim())){
            AlertMessage.errorSaving(2);
        } else if(Integer.parseInt(invTF.getText().trim()) > Integer.parseInt(maxTF.getText().trim())){
            AlertMessage.errorSaving(3);
        } else {
            savePart();
        }
    }
    /** This method creates and saves the part to be added to the part inventory.
     This method checks whether the part is an in house or outsourced part and creates the new part appropriately.
     After saving, the dialog window is exited and the user is redirected back to the main form window. */
    private void savePart(){
        if(inHouse.isSelected()) {
            Part part = new InHouse(Integer.parseInt(autoIdTextField.getText()), nameTF.getText().trim(),
                    Double.parseDouble(priceTF.getText().trim()), Integer.parseInt(invTF.getText().trim()),
                    Integer.parseInt(minTF.getText().trim()), Integer.parseInt(maxTF.getText().trim()),
                    Integer.parseInt(toggleTF.getText().trim()));
            partInventory.add(part);
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } else if(outSourced.isSelected()){
            Part part = new Outsourced(Integer.parseInt(autoIdTextField.getText()), nameTF.getText().trim(),
                    Double.parseDouble(priceTF.getText().trim()), Integer.parseInt(invTF.getText().trim()),
                    Integer.parseInt(minTF.getText().trim()), Integer.parseInt(maxTF.getText().trim()),
                    toggleTF.getText().trim());
            partInventory.add(part);
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }
}
