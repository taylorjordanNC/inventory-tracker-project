package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.util.Optional;

/** This class controls the add product dialog window.
 This class and the associated window are invoked when the 'Add' button is pressed underneath the product table of the main form. */
public class AddProductController {
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button removePartButton;
    @FXML
    private Button addPartButton;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableView<Part> associatedPartsTable;
    @FXML
    private TextField searchTextField;
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
    private Label nameError;
    @FXML
    private Label invError;
    @FXML
    private Label priceError;
    @FXML
    private Label maxError;
    @FXML
    private Label minError;
    private Inventory inventory;
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> searchPartInventory = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    /** This is the constructor method for the class.
     This constructor sets the AddProductController private ObservableList productInventory to the inventory passed from the main form window.
     @param inventory The product inventory from the main form window. */
    public void AddProductController(ObservableList<Product> inventory){
        this.productInventory = inventory;
    }
    /** This method initializes the add product window.
     This method is invoked upon pressing the 'Add' button underneath the product table of the main form.
     This method adds listeners to all input fields in order to validate all input for the form and protects the program from crashing if a user does not input valid information.
     i.e. if a user uses letters for inventory, an error will be shown to prompt the user to fix the error and the save button will be disabled.
     Try/catch statements were included in every field listener to control for any exceptions in processing invalid information.
     The save button is not enabled unless all input fields are filled properly with valid information.
     The ID field is auto-generated and disabled, and included is a loop to check to ensure that the product ID is not duplicated.
     In a future version, a feature to alert the user that the inventory/min/max fields are invalid may be included here before the save button event handler.
     This way, if the inventory is more than the max, etc., the user will be alerted in real time.
     Also in a future version, a feature to be able to choose if a user is searching via ID or Name could be implemented in order to better filter search results. */
    public void initialize(){
        generatePartsTable();
        generateAssociatedParts();
        productInventory.setAll(inventory.getAllProducts());

        nameError.setVisible(false);
        invError.setVisible(false);
        priceError.setVisible(false);
        maxError.setVisible(false);
        minError.setVisible(false);
        saveButton.setDisable(true);

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

        addPartButton.setDisable(true);
        partTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Part>() {
            @Override
            public void changed(ObservableValue<? extends Part> observableValue, Part part1, Part part2) {
                if(part2 != null){
                    addPartButton.setDisable(false);
                } else {
                    addPartButton.setDisable(true);
                }
            }
        });
        partTableView.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if((t1) && partTableView.getSelectionModel().getSelectedItem() != null){
                    addPartButton.setDisable(false);
                } else if(associatedPartsTable.isFocused()){
                    addPartButton.setDisable(true);
                }
            }
        });
        removePartButton.setDisable(true);
        associatedPartsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Part>() {
            @Override
            public void changed(ObservableValue<? extends Part> observableValue, Part part1, Part part2) {
                if(part2 != null){
                    removePartButton.setDisable(false);
                } else {
                    removePartButton.setDisable(true);
                }
            }
        });
        associatedPartsTable.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if((t1) && associatedPartsTable.getSelectionModel().getSelectedItem() != null){
                    removePartButton.setDisable(false);
                } else if(partTableView.isFocused()){
                    removePartButton.setDisable(true);
                }
            }
        });

        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {
                if(newString.equals("")){
                    partTableView.setItems(partInventory);
                    addPartButton.setDisable(true);
                }
            }
        });
        searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String searchText = searchTextField.getText().trim();
                    if (!searchText.isEmpty()) {
                        searchPartInventory.clear();
                        for (int i=0; i<partInventory.size(); i++){
                            if((partInventory.get(i).getName().contains(searchText)) || (String.valueOf(partInventory.get(i).getPartID()).contains(searchText))){
                                Part part = partInventory.get(i);
                                searchPartInventory.add(part);
                            }
                        }
                        if(searchPartInventory.isEmpty()){
                            partTableView.setItems(partInventory);
                            AlertMessage.errorSearching(1);
                        } else {
                            partTableView.setItems(searchPartInventory);
                        }
                        partTableView.refresh();
                    }
                }
            }
        });
        int min = 1;
        int max = 1000;
        int id_random = (int) (Math.random() * (max-min + 1) + min);
        for(int i=0; i<productInventory.size(); i++){
            int id = productInventory.get(i).getProductID();
            if(id == id_random){
                autoIdTextField.setText(String.valueOf(id_random + 1));
            } else{
                autoIdTextField.setText(String.valueOf(id_random));
            }
        }
        autoIdTextField.setEditable(false);
        autoIdTextField.setStyle("-fx-background-color: LightGray;" + "-fx-border-color: DimGray");
    }
    /** This method ensures no form fields are empty.
     This method is invoked from within the initialize method to check whether there are any empty form fields. */
    private void checkFields() {
        if (!nameTF.getText().trim().isEmpty() && !invTF.getText().trim().isEmpty() && !priceTF.getText().trim().isEmpty()
                && !minTF.getText().trim().isEmpty() && !maxTF.getText().trim().isEmpty()) {
            saveButton.setDisable(false);
        }
    }
    /** This method initializes the table of parts.
     The initialize method invokes this method and sets the table to include all parts in the part inventory. */
    private void generatePartsTable(){
        partInventory.setAll(inventory.getAllParts());
        partTableView.setItems(partInventory);
        partTableView.refresh();
    }
    /** This method initializes the table of the associated parts.
     Since no parts yet exist, the table is empty upon loading the window. */
    private void generateAssociatedParts(){
        associatedPartsTable.setItems(associatedParts);
        partTableView.refresh();
    }
    /** This method handles the removal of associated parts.
     When the 'Remove Associated Part' button is enabled and pressed, this method displays a confirmation alert before removing the selected associated part from the product.
     The remove button will not be enabled or functional until there is a valid associated part selected from the table. */
    @FXML
    public void handleRemovePart(){
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        if(selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Removing part");
            alert.setContentText("Are you sure you want to remove this associated part?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && (result.get() == ButtonType.CANCEL)){
                alert.close();
            } else if(result.isPresent() && (result.get() == ButtonType.OK)){
                associatedParts.remove(selectedPart);
            }
        }
    }
    /** This method is invoked when the save button is pressed.
     This method validates remaining information before the save is conducted and displays an error message if something is input incorrectly.
     i.e. if the cost of associated parts is greater than the price of product, an error alert will show. */
    @FXML
    public void handleSave(){
        if(Integer.parseInt(minTF.getText().trim()) > Integer.parseInt(maxTF.getText().trim())){
            AlertMessage.errorSaving(1);
        } else if(Integer.parseInt(invTF.getText().trim()) < Integer.parseInt(minTF.getText().trim())){
            AlertMessage.errorSaving(2);
        } else if (Integer.parseInt(invTF.getText().trim()) > Integer.parseInt(maxTF.getText().trim())){
            AlertMessage.errorSaving(3);
        } else if(!associatedParts.isEmpty()){
                double totalPartCost;
                for(int i = 0; i < associatedParts.size(); i++) {
                    double sum = 0;
                    sum += associatedParts.get(i).getPrice();
                    totalPartCost = sum;
                    if(totalPartCost > (Double.parseDouble(priceTF.getText()))){
                        AlertMessage.errorSaving(4);
                    } else {
                        saveProduct();
                    }
                }
        } else {
            saveProduct();
        }
    }
    /** This method creates and saves the product to be added to the product inventory.
     This method also will save any associated parts linked to the product.
     After saving, the dialog window is exited and the user is redirected back to the main form window. */
    private void saveProduct(){
        Product product = new Product(Integer.parseInt(autoIdTextField.getText()), nameTF.getText().trim(),
                Double.parseDouble(priceTF.getText().trim()), Integer.parseInt(invTF.getText().trim()),
                Integer.parseInt(minTF.getText().trim()), Integer.parseInt(maxTF.getText().trim()));
        productInventory.add(product);
        product.getAllAssociatedParts().setAll(associatedParts);
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
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
    /** This method handles adding an associated part to a product.
     When the add button is enabled and pressed, the selected part will be added to the table of associated parts.
     This method ensures there is no part duplication within the associated parts table, else it shows an error message to the user.
     In a future version, a feature to listen for the total cost of associated parts could be added. This way, an alert may show that the price cannot be less than the cost of parts before the save button is pressed.
     Similarly, the add button could be bound to this feature so it may be disabled if no more parts can be added. */
    @FXML
    public void handleAddPart(){
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            if(!associatedParts.contains(selectedPart)) {
                associatedParts.add(selectedPart);
            } else if(associatedParts.contains(selectedPart)){
                AlertMessage.errorAssociatedPart(1);
            }
        }
    }
}
