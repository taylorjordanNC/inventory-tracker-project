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

/** This class controls the modify product dialog window.
 This class and the associated window are invoked when the 'Modify' button is pressed underneath the product table of the main form. */
public class ModifyProductController {
    private Product selectedProduct;
    private Inventory inventory;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableView<Part> associatedPartsTable;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
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
    private Button addPartButton;
    @FXML
    private Button removePartButton;
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

    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> searchPartInventory = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private ObservableList<Product> searchProductInventory = FXCollections.observableArrayList();
    /** This is the constructor method for the class.
     This constructor sets the ModifyProductController private ObservableList productInventory to the inventory passed from the main form window.
     @param inventory Product inventory from the main form controller
     @param searchInventory The list of searched products from the main form */
    public void ModifyProductController(ObservableList<Product> inventory, ObservableList<Product> searchInventory){
        this.productInventory = inventory;
        this.searchProductInventory = searchInventory;
    }
    /** This method initializes the modify product window.
     This method is invoked upon pressing the 'Modify' button underneath the product table of the main form.
     This method adds listeners to all input fields in order to validate all input for the form and protects the program from crashing if a user does not input valid information.
     Try/catch statements were included in every field listener to control for any exceptions in processing invalid information.
     i.e. if a user uses letters for inventory, an error will be shown to prompt the user to fix the error and the save button will be disabled.
     The save button is not enabled unless all input fields are filled properly with valid information.
     In a future version, a feature to alert the user that the inventory/min/max fields are invalid may be included here before the save button event handler.
     This way, if the inventory is more than the max, etc., the user will be alerted in real time.
     Also in a future version, a feature to be able to choose if a user is searching via ID or Name could be implemented in order to better filter search results.

     @param product The selected product passed from the main form. */
    public void initialize(Product product){
        generatePartsTable();
        generateAssociatedParts(product);
        this.selectedProduct = product;

        nameError.setVisible(false);
        invError.setVisible(false);
        priceError.setVisible(false);
        maxError.setVisible(false);
        minError.setVisible(false);

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
        autoIdTextField.setEditable(false);
        autoIdTextField.setStyle("-fx-background-color: LightGray; -fx-border-color: DimGray");
    }
    /** This method ensures no form fields are empty.
     This method is invoked from within the initialize method to check whether there are any empty form fields. */
    private void checkFields() {
        if (!nameTF.getText().trim().isEmpty() && !invTF.getText().trim().isEmpty() && !priceTF.getText().trim().isEmpty()
                && !minTF.getText().trim().isEmpty() && !maxTF.getText().trim().isEmpty()) {
            saveButton.setDisable(false);
        }
    }
    /** This method initializes all form fields.
     This method takes the information passed from the parameter and initializes the information in the appropriate fields.
     @param selectedProduct The product selected by the user in the main form window to modify */
    public void initData(Product selectedProduct) {
        autoIdTextField.setText(String.valueOf(selectedProduct.getProductID()));
        nameTF.setText(selectedProduct.getName());
        invTF.setText(String.valueOf(selectedProduct.getStock()));
        priceTF.setText(String.valueOf(selectedProduct.getPrice()));
        maxTF.setText(String.valueOf(selectedProduct.getMax()));
        minTF.setText(String.valueOf(selectedProduct.getMin()));
    }
    /** This method initializes the table of parts.
     The initialize method invokes this method and sets the table to include all parts in the part inventory. */
    private void generatePartsTable(){
        partInventory.setAll(inventory.getAllParts());
        partTableView.setItems(partInventory);
        partTableView.refresh();
    }
    /** This method initializes the table of the associated parts.
     The table will show any associated parts the product may contain.
     @param product The selected part passed from the main form. */
    private void generateAssociatedParts(Product product){
        associatedParts.setAll(product.getAllAssociatedParts());
        associatedPartsTable.setItems(associatedParts);
        associatedPartsTable.refresh();
    }
    /** This method is invoked when the save button is pressed.
     This method validates remaining information before the save is conducted and displays an error message if something is input incorrectly.
     i.e. if the minimum is greater than the maximum, an error alert will show.
     Before invoking the savePart() method, this method shows the user a confirmation alert. */
    public void handleSave(){
        if(Integer.parseInt(minTF.getText().trim()) > Integer.parseInt(maxTF.getText().trim())){
            AlertMessage.errorSaving(1);
        } else if(Integer.parseInt(invTF.getText().trim()) < Integer.parseInt(minTF.getText().trim())){
            AlertMessage.errorSaving(2);
        } else if (Integer.parseInt(invTF.getText().trim()) > Integer.parseInt(maxTF.getText().trim())){
            AlertMessage.errorSaving(3);
        } else if(!associatedParts.isEmpty()){
            double totalPartCost = 0;
            for (int i = 0; i < associatedParts.size(); i++) {
                double sum = 0;
                sum += associatedParts.get(i).getPrice();
                totalPartCost = sum;
            }
            if(totalPartCost > (Double.parseDouble(priceTF.getText()))){
                AlertMessage.errorSaving(4);
            } else{
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Please Confirm");
                alert1.setHeaderText("Product will be modified.");
                alert1.setContentText("Are you sure you want to save these changes?");
                Optional<ButtonType> result = alert1.showAndWait();
                if(result.isPresent() && (result.get() == ButtonType.CANCEL)){
                    alert1.close();
                } else if(result.isPresent() && (result.get() == ButtonType.OK)){
                    saveProduct();
                }
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setTitle("Please Confirm");
            alert2.setHeaderText("Product will be modified.");
            alert2.setContentText("Are you sure you want to save these changes?");
            Optional<ButtonType> result = alert2.showAndWait();
            if(result.isPresent() && (result.get() == ButtonType.CANCEL)){
                alert2.close();
            } else if(result.isPresent() && (result.get() == ButtonType.OK)){
                saveProduct();
            }
        }
    }
    /** This method saves the modified product.
     The method finds the original product in the inventory and replaces it with the new modified product.
     After saving, the dialog window is exited and the user is redirected back to the main form window. */
    private void saveProduct() {
        Product editedProduct = new Product(Integer.parseInt(autoIdTextField.getText()), nameTF.getText().trim(),
                Double.parseDouble(priceTF.getText().trim()), Integer.parseInt(invTF.getText().trim()),
                Integer.parseInt(minTF.getText().trim()), Integer.parseInt(maxTF.getText().trim()));
        if (searchProductInventory.isEmpty()) {
            for (int i = 0; i < productInventory.size(); i++) {
                Product existingProduct = productInventory.get(i);
                if (existingProduct.getProductID() == editedProduct.getProductID()) {
                    productInventory.remove(existingProduct);
                    productInventory.add(editedProduct);
                    editedProduct.getAllAssociatedParts().setAll(associatedParts);
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                }
            }
        } else {
            for (int i = 0; i < searchProductInventory.size(); i++) {
                Product existingProduct = searchProductInventory.get(i);
                if (existingProduct.getProductID() == editedProduct.getProductID()) {
                    searchProductInventory.remove(existingProduct);
                    searchProductInventory.add(editedProduct);
                    editedProduct.getAllAssociatedParts().setAll(associatedParts);
                }
            }
            for (int i = 0; i < productInventory.size(); i++) {
                Product existingProduct = productInventory.get(i);
                if (existingProduct.getProductID() == editedProduct.getProductID()) {
                    productInventory.remove(existingProduct);
                    productInventory.add(editedProduct);
                    editedProduct.getAllAssociatedParts().setAll(associatedParts);
                }
            }
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
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
    /** This method handles adding an associated part to a product.
     When the add button is enabled and pressed, the selected part will be added to the table of associated parts.
     This method ensures there is no part duplication within the associated parts table, else it shows an error message to the user.
     In a future version, a feature to listen for the total cost of associated parts could be added. This way, an alert may show that the price cannot be less than the cost of parts before the save button is pressed.
     Similarly, the add button could be bound to this feature so it may be disabled if no more parts can be added. */
    @FXML
    public void handleAddPart() {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            if(!associatedParts.contains(selectedPart)) {
                selectedProduct.addAssociatedPart(selectedPart);
                associatedParts = selectedProduct.getAllAssociatedParts();
                associatedPartsTable.setItems(associatedParts);
            } else if(selectedProduct.getAllAssociatedParts().contains(selectedPart)){
                AlertMessage.errorAssociatedPart(1);
            }
        }
    }
    /** This method handles the removal of associated parts.
     When the 'Remove Associated Part' button is enabled and pressed, this method displays a confirmation alert before removing the selected associated part from the product.
     The remove button will not be enabled or functional until there is a valid associated part selected from the table. */
    @FXML
    public void handleRemovePart(){
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        if(selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Please Confirm Action");
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
}
