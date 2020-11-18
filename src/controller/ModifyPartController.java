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

/** This class controls the modify part dialog window.
 This class and the associated window are invoked when the 'Modify' button is pressed underneath the part table of the main form. */
public class ModifyPartController {
    @FXML
    private RadioButton inHouse;
    @FXML
    private RadioButton outSourced;
    @FXML
    private Label toggleLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField invTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField maxTextField;
    @FXML
    private TextField minTextField;
    @FXML
    private TextField toggleTextField;
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

    private Inventory inventory;
    private ObservableList<Part> partInventory;
    private ObservableList<Part> searchPartInventory;
    private MainFormController mainController = new MainFormController();

    /** This is the constructor method for the class.
     This constructor sets the ModifyPartController private ObservableList partInventory to the inventory passed from the main form window.
     @param inventory Part inventory from the main form controller
     @param searchInventory The list of searched parts from the main form */
    public void ModifyPartController(ObservableList<Part> inventory, ObservableList<Part> searchInventory) {
        this.partInventory = inventory;
        this.searchPartInventory = searchInventory;
    }
    /** This method initializes the modify part window.
     This method is invoked upon pressing the 'Modify' button underneath the part table of the main form.
     This method adds listeners to all input fields in order to validate all input for the form and protects the program from crashing if a user does not input valid information.
     Try/catch statements were included in every field listener to control for any exceptions in processing invalid information.
     i.e. if a user uses letters for inventory, an error will be shown to prompt the user to fix the error and the save button will be disabled.
     The save button is not enabled unless all input fields are filled properly with valid information.
     In a future version, a feature to alert the user that the inventory/min/max fields are invalid may be included here before the save button event handler.
     This way, if the inventory is more than the max, etc., the user will be alerted in real time. */
    public void initialize(){
        inHouse.setToggleGroup(toggleGroup);
        outSourced.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(inHouse);

        nameError.setVisible(false);
        invError.setVisible(false);
        priceError.setVisible(false);
        maxError.setVisible(false);
        minError.setVisible(false);
        toggleError.setVisible(false);

        inHouse.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(inHouse.isSelected()){
                    toggleLabel.setText("Machine ID:");
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
        idTextField.setEditable(false);

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    nameTextField.setStyle("-fx-border-color: none");
                    nameError.setVisible(false);
                    checkFields();
                } else {
                    nameTextField.setStyle("-fx-border-color: none");
                    nameError.setVisible(true);
                    nameError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    saveButton.setDisable(true);
                }
            }
        });

        invTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    try{
                        int inventory = Integer.parseInt(newValue);
                        invTextField.setText(String.valueOf(inventory));
                        invTextField.setStyle("-fx-border-color: none");
                        invError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        invTextField.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        invError.setVisible(true);
                        invError.setText("Only whole numbers allowed");
                        invError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                }
                if(newValue.isEmpty()){
                    invTextField.setStyle("-fx-border-color: none");
                    invError.setVisible(true);
                    invError.setText("Required");
                    invError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    saveButton.setDisable(true);
                }
            }
        });

        priceTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty() && newValue.matches("^\\d*\\.?\\d*$")){
                    priceTextField.setStyle("-fx-border-color: none");
                    priceError.setVisible(false);
                    checkFields();
                } else if(!newValue.isEmpty() && !newValue.matches("^\\d*\\.?\\d*$")){
                    priceTextField.setStyle("-fx-border-color: red");
                    saveButton.setDisable(true);
                    priceError.setVisible(true);
                    priceError.setText("Only numbers allowed");
                    priceError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
                if(newValue.isEmpty()){
                    priceTextField.setStyle("-fx-border-color: none");
                    saveButton.setDisable(true);
                    priceError.setVisible(true);
                    priceError.setText("Required");
                    priceError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
            }
        });

        maxTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    try{
                        Integer max = Integer.parseInt(newValue);
                        maxTextField.setText(String.valueOf(max));
                        maxTextField.setStyle("-fx-border-color: none");
                        maxError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        maxTextField.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        maxError.setVisible(true);
                        maxError.setText("Only whole numbers allowed");
                        maxError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                }
                if(newValue.isEmpty()){
                    maxTextField.setStyle("-fx-border-color: none");
                    saveButton.setDisable(true);
                    maxError.setVisible(true);
                    maxError.setText("Required");
                    maxError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
            }
        });

        minTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    try{
                        Integer min = Integer.parseInt(newValue);
                        minTextField.setText(String.valueOf(min));
                        minTextField.setStyle("-fx-border-color: none");
                        minError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        minTextField.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        minError.setVisible(true);
                        minError.setText("Only whole numbers allowed");
                        minError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                }
                if(newValue.isEmpty()){
                    minTextField.setStyle("-fx-border-color: none");
                    saveButton.setDisable(true);
                    minError.setVisible(true);
                    minError.setText("Required");
                    minError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
            }
        });

        toggleTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if((!newValue.isEmpty() || !oldValue.isEmpty()) && inHouse.isSelected()){
                    try{
                        Integer min = Integer.parseInt(newValue);
                        toggleTextField.setText(String.valueOf(min));
                        toggleTextField.setStyle("-fx-border-color: none");
                        toggleError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        toggleTextField.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        toggleError.setVisible(true);
                        toggleError.setText("Only whole numbers allowed");
                        toggleError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                } else if(!newValue.isEmpty()  && outSourced.isSelected()){
                    toggleTextField.setStyle("-fx-border-color: none");
                    toggleError.setVisible(false);
                    checkFields();
                }
                if(newValue.isEmpty()){
                    toggleTextField.setStyle("-fx-border-color: none");
                    saveButton.setDisable(true);
                    toggleError.setVisible(true);
                    toggleError.setText("Required");
                    toggleError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                }
            }
        });

        inHouse.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean old, Boolean newValue) {
                if(newValue && !toggleTextField.getText().isEmpty()){
                    try{
                        Integer min = Integer.parseInt(toggleTextField.getText());
                        toggleTextField.setText(String.valueOf(min));
                        toggleTextField.setStyle("-fx-border-color: none");
                        toggleError.setVisible(false);
                        checkFields();
                    } catch(Exception e){
                        toggleTextField.setStyle("-fx-border-color: red");
                        saveButton.setDisable(true);
                        toggleError.setVisible(true);
                        toggleError.setText("Only whole numbers allowed");
                        toggleError.setStyle("-fx-text-fill: red;" + "-fx-font-size: 11px;");
                    }
                }
            }
        });
    }
    /** This method ensures no form fields are empty.
     This method is invoked from within the initialize method to check whether there are any empty form fields. */
    private void checkFields() {
        if (!nameTextField.getText().trim().isEmpty() && !invTextField.getText().trim().isEmpty() && !priceTextField.getText().trim().isEmpty()
                && !minTextField.getText().trim().isEmpty() && !maxTextField.getText().trim().isEmpty() && !toggleTextField.getText().trim().isEmpty()) {
            saveButton.setDisable(false);
        }
    }
    /** This method initializes all form fields.
     This method takes the information passed from the parameter and initializes the information in the appropriate fields.
     @param selectedPart The part selected by the user in the main form window to modify */
    public void initData(Part selectedPart){
        idTextField.setText(String.valueOf(selectedPart.getPartID()));
        nameTextField.setText(selectedPart.getName());
        invTextField.setText(String.valueOf(selectedPart.getStock()));
        priceTextField.setText(String.valueOf(selectedPart.getPrice()));
        maxTextField.setText(String.valueOf(selectedPart.getMax()));
        minTextField.setText(String.valueOf(selectedPart.getMin()));

        if(selectedPart instanceof InHouse){
            toggleGroup.selectToggle(inHouse);
            toggleTextField.setText(String.valueOf(((InHouse) selectedPart).getMachineID()));
            toggleLabel.setText("Machine ID");
        } else if(selectedPart instanceof Outsourced){
            toggleGroup.selectToggle(outSourced);
            toggleTextField.setText((((Outsourced) selectedPart).getCompanyName()));
            toggleLabel.setText("Company Name");
        }
    }
    /** This method exits the window and returns the user to the main form window.
     When the cancel button is pressed, the dialog window is closed. */
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
     i.e. if the minimum is greater than the maximum, an error alert will show.
     Before invoking the savePart() method, this method shows the user a confirmation alert. */
    public void handleSave(){
        if(Integer.parseInt(minTextField.getText().trim()) > Integer.parseInt(maxTextField.getText().trim())){
            AlertMessage.errorSaving(1);
        } else if(Integer.parseInt(invTextField.getText().trim()) < Integer.parseInt(minTextField.getText().trim())){
            AlertMessage.errorSaving(2);
        } else if(Integer.parseInt(invTextField.getText().trim()) > Integer.parseInt(maxTextField.getText().trim())){
            AlertMessage.errorSaving(3);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Please Confirm");
            alert.setHeaderText("Part will be modified.");
            alert.setContentText("Are you sure you want to save these changes?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && (result.get() == ButtonType.CANCEL)){
                alert.close();
            } else if(result.isPresent() && (result.get() == ButtonType.OK)){
                savePart();
            }
        }
    }
    /** This method saves the modified part.
     The method finds the original part in the inventory and replaces it with the new modified part.
     This method checks whether the part is an in house or outsourced part and creates the new part appropriately.
     After saving, the dialog window is exited and the user is redirected back to the main form window. */
    private void savePart(){
        if(inHouse.isSelected()) {
            Part editedPart = new InHouse(Integer.parseInt(idTextField.getText()), nameTextField.getText().trim(),
                    Double.parseDouble(priceTextField.getText().trim()), Integer.parseInt(invTextField.getText().trim()),
                    Integer.parseInt(minTextField.getText().trim()), Integer.parseInt(maxTextField.getText().trim()),
                    Integer.parseInt(toggleTextField.getText().trim()));

            if(searchPartInventory.isEmpty()) {
                for (int i = 0; i < partInventory.size(); i++) {
                    Part existingPart = partInventory.get(i);
                    if (existingPart.getPartID() == editedPart.getPartID()) {
                        partInventory.remove(existingPart);
                        partInventory.add(editedPart);
                        Stage stage = (Stage) saveButton.getScene().getWindow();
                        stage.close();
                    }
                }
            } else {
                for (int i = 0; i < searchPartInventory.size(); i++) {
                    Part existingPart = searchPartInventory.get(i);
                    if (existingPart.getPartID() == editedPart.getPartID()) {
                        searchPartInventory.remove(existingPart);
                        searchPartInventory.add(editedPart);
                    }
                }
                for (int i = 0; i < partInventory.size(); i++) {
                    Part existingPart = partInventory.get(i);
                    if (existingPart.getPartID() == editedPart.getPartID()) {
                        partInventory.remove(existingPart);
                        partInventory.add(editedPart);
                    }
                }
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }
        } else if(outSourced.isSelected()){
            Part editedPart = new Outsourced(Integer.parseInt(idTextField.getText()), nameTextField.getText().trim(),
                    Double.parseDouble(priceTextField.getText().trim()), Integer.parseInt(invTextField.getText().trim()),
                    Integer.parseInt(minTextField.getText().trim()), Integer.parseInt(maxTextField.getText().trim()),
                    toggleTextField.getText().trim());
            if(searchPartInventory.isEmpty()) {
                for (int i = 0; i < partInventory.size(); i++) {
                    Part existingPart = partInventory.get(i);
                    if (existingPart.getPartID() == editedPart.getPartID()) {
                        partInventory.remove(existingPart);
                        partInventory.add(editedPart);
                        Stage stage = (Stage) saveButton.getScene().getWindow();
                        stage.close();
                    }
                }
            } else {
                for (int i = 0; i < searchPartInventory.size(); i++) {
                    Part existingPart = searchPartInventory.get(i);
                    if (existingPart.getPartID() == editedPart.getPartID()) {
                        searchPartInventory.remove(existingPart);
                        searchPartInventory.add(editedPart);
                    }
                }
                for (int i = 0; i < partInventory.size(); i++) {
                    Part existingPart = partInventory.get(i);
                    if (existingPart.getPartID() == editedPart.getPartID()) {
                        partInventory.remove(existingPart);
                        partInventory.add(editedPart);
                    }
                }
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }
        }
    }
}
