package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.util.Optional;

/** This class is the controller for the main form FXML file.
 This class controls the first window that appears to the user when the application is loaded. */

public class MainFormController {
    private Inventory inventory;
    @FXML
    private Button addPartButton;
    @FXML
    private Button modPartButton;
    @FXML
    private Button deletePartButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button addProductButton;
    @FXML
    private Button modProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private TextField textFieldProductSearch;
    @FXML
    private TextField textFieldPartSearch;
    @FXML
    private GridPane mainFormWindow;
    @FXML
    private GridPane addPartWindow;
    @FXML
    private Button formCancelButton;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableView<Product> productTableView;

    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> searchPartInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private ObservableList<Product> searchProductInventory = FXCollections.observableArrayList();

    /** This is the initialize method.
     This method initializes all aspects of the main form window that is loaded for the user upon starting the application.
     Included in this method are listeners that disable or enable the modify buttons based on if there is a part or product selected to modify. */
    @FXML
    public void initialize(){
        generatePartsTable();
        generateProductsTable();
        textFieldPartSearch.setFocusTraversable(false);

        deletePartButton.setDisable(true);
        modPartButton.setDisable(true);
        partTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Part>() {
            @Override
            public void changed(ObservableValue<? extends Part> observableValue, Part part1, Part part2) {
                if(part2 != null){
                    modPartButton.setDisable(false);
                    deletePartButton.setDisable(false);
                } else {
                    modPartButton.setDisable(true);
                    deletePartButton.setDisable(true);
                }
            }
        });
        partTableView.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if((t1) && partTableView.getSelectionModel().getSelectedItem() != null){
                    modPartButton.setDisable(false);
                    deletePartButton.setDisable(false);
                } else if(productTableView.isFocused()){
                    modPartButton.setDisable(true);
                    deletePartButton.setDisable(true);
                }
            }
        });
        deleteProductButton.setDisable(true);
        modProductButton.setDisable(true);
        productTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observableValue, Product product1, Product product2) {
                if(product2 != null){
                    modProductButton.setDisable(false);
                    deleteProductButton.setDisable(false);
                } else {
                    modProductButton.setDisable(true);
                    deleteProductButton.setDisable(true);
                }
            }
        });
        productTableView.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if((t1) && productTableView.getSelectionModel().getSelectedItem() != null){
                    modProductButton.setDisable(false);
                    deleteProductButton.setDisable(false);
                } else if (partTableView.isFocused()){
                    modProductButton.setDisable(true);
                    deleteProductButton.setDisable(true);
                }
            }
        });
        textFieldPartSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {
                if(newString.equals("")){
                    partTableView.setItems(partInventory);
                    modPartButton.setDisable(true);
                    deletePartButton.setDisable(true);
                }
            }
        });
        textFieldPartSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    searchPartTable();
                }
            }
        });
        textFieldProductSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {
                if(newString.equals("")){
                    productTableView.setItems(productInventory);
                    modProductButton.setDisable(true);
                    deleteProductButton.setDisable(true);
                }
            }
        });
        textFieldProductSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    searchProductTable();
                }
            }
        });
    }
    /** This method initializes the table of parts.
    The initialize method invokes this method and sets the table to include all parts in the inventory. */
    private void generatePartsTable(){
        partInventory.addAll(inventory.getAllParts());
        partTableView.setItems(partInventory);
        partTableView.refresh();

    }
    /** This method initializes the table of products.
     The initialize method invokes this method and sets the table to include all products in the inventory. */
    private void generateProductsTable(){
        productInventory.addAll(inventory.getAllProducts());
        productTableView.setItems(productInventory);
        productTableView.refresh();
    }
    /** This method closes the main form window.
     When the exit button is clicked, this method is invoked, exiting the window. */
    @FXML
    public void handleExit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please confirm action");
        alert.setContentText("Are you sure you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            partTableView.setItems(partInventory);
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        } else if(result.isPresent() && result.get() == ButtonType.CANCEL){
            alert.close();
        }
    }
    /** This method opens the add part dialog window.
     When the 'Add' button is clicked under the part table, the add part window opens, and the part inventory is passed to the new dialog.
     The try/catch statement captures any potential errors in opening the dialog window. */
    @FXML
    public void showAddPart() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainFormWindow.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/AddPart.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            AddPartController controller = fxmlLoader.getController();
            controller.initialize(partInventory);
        } catch (IOException e) {
            System.out.println("Could not load the form.");
            e.printStackTrace();
        }
        dialog.setResizable(true);
        dialog.showAndWait();
    }
    /** This method opens the modify part dialog window.
     When the 'Modify' button is clicked under the part table, the modify part window opens, and the part inventory is passed to the new dialog.
     The try/catch statement captures any potential errors in opening the dialog window.

     In order to not have any issues initializing the modify part window, I had to pass the necessary information the new dialog would need.
     I passed the part inventory as well as the selected part by the user into a method within the ModifyPartController class. This ensured the new window had everything necessary for functionality. */
    @FXML
    public void showModPart(){
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainFormWindow.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
                ModifyPartController controller = fxmlLoader.getController();
                controller.initData(selectedPart);
                controller.ModifyPartController(partInventory, searchPartInventory);
            } catch (IOException e) {
                System.out.println("Could not load the form.");
                e.printStackTrace();
            }
            dialog.setResizable(true);
            dialog.showAndWait();
        }
    }
    /** This method deletes the selected part from the table.
     The try/catch statement captures any potential errors in deletion.
     Before deletion, a confirmation alert window is shown to user. */
    @FXML
    public void handleDeletePart(){
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Selected Product");
            alert.setHeaderText("Delete: " + selectedPart.getName());
            alert.setContentText("Are you sure? Press OK to confirm.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                try{
                    partInventory.remove(selectedPart);
                    searchPartInventory.remove(selectedPart);
                }
                catch(Exception e){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Error");
                    alert2.setContentText("Part could not be deleted.");
                    alert.showAndWait();
                }
            }
        }
    }
    /** This method opens the add product dialog window.
     When the 'Add' button is clicked under the product table, the add product window opens, and the product inventory is passed to the new dialog.
     The try/catch statement captures any potential errors in opening the dialog window. */
    @FXML
    public void showAddProduct(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.isResizable();
        dialog.initOwner(mainFormWindow.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/AddProduct.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            AddProductController controller = fxmlLoader.getController();
            controller.AddProductController(productInventory);
        } catch (IOException e) {
            System.out.println("Could not load the form.");
            e.printStackTrace();
        }
        dialog.setResizable(true);
        dialog.showAndWait();
    }
    /** This method opens the modify product dialog window.
     When the 'Modify' button is clicked under the product table, the modify product window opens, and the product inventory is passed to the new dialog.
     The try/catch statement captures any potential errors in opening the dialog window. */
    @FXML
    public void showModProduct(){
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainFormWindow.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
                ModifyProductController controller = fxmlLoader.getController();
                controller.initData(selectedProduct);
                controller.initialize(selectedProduct);
                controller.ModifyProductController(productInventory, searchProductInventory);
            } catch (IOException e) {
                System.out.println("Could not load the form.");
                e.printStackTrace();
            }
            dialog.setResizable(true);
            dialog.showAndWait();
        }
    }
    /** This method deletes the selected product from the table.
     The try/catch statement captures any potential errors in deletion.
     Before deletion, a confirmation alert window is shown to user. */
    @FXML
    public void handleDeleteProduct(){
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if(selectedProduct != null) {
            if(selectedProduct.getAllAssociatedParts().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Selected Product");
                alert.setHeaderText("Delete: " + selectedProduct.getName());
                alert.setContentText("Are you sure? Press OK to confirm.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && (result.get() == ButtonType.OK)) {
                    try{
                        productInventory.remove(selectedProduct);
                        searchProductInventory.remove(selectedProduct);
                    }
                    catch(Exception e){
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Error");
                        alert2.setContentText("Product could not be deleted.");
                        alert.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("This product contains one or more associated parts and cannot be deleted.");
                alert.showAndWait();
                }
            }
    }
    /** This method handles the search of the part inventory.
     When the user inputs search parameters and presses enter or the search button provided, the part inventory is appropriately searched.
     If no parts are found with the given search terms, an error message is shown.
     In a future version, a feature to be able to choose if a user is searching via ID or Name could be implemented in order to better filter search results. */
    @FXML
    public void searchPartTable(){
        String searchText = textFieldPartSearch.getText().trim();
        if (!searchText.isEmpty()){
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
        }
        partTableView.refresh();
    }
    /** This method handles the clearing of a part search.
     This method provides functionality to the Refresh button to clear the search bar and the table view. */
    @FXML
    public void handleClearSearchParts(){
        partTableView.setItems(partInventory);
        textFieldPartSearch.clear();
        modPartButton.setDisable(true);
        deletePartButton.setDisable(true);
    }
    /** This method handles the search of the product inventory.
     When the user inputs search parameters and presses enter or the search button provided, the product inventory is appropriately searched.
     If no products are found with the given search terms, an error message is shown.
     In a future version, a feature to be able to choose if a user is searching via ID or Name could be implemented in order to better filter search results. */
    @FXML
    public void searchProductTable(){
        String searchText = textFieldProductSearch.getText().trim();
        if (!searchText.isEmpty()){
            searchProductInventory.clear();
            for (int i=0; i<productInventory.size(); i++){
                if((productInventory.get(i).getName().contains(searchText)) || (String.valueOf(productInventory.get(i).getProductID()).contains(searchText))){
                    Product product = productInventory.get(i);
                    searchProductInventory.add(product);
                }
            }
            if(searchProductInventory.isEmpty()){
                productTableView.setItems(productInventory);
                AlertMessage.errorSearching(2);
            } else {
                productTableView.setItems(searchProductInventory);
            }
            productTableView.refresh();
        }
    }
    /** This method handles the clearing of a product search.
     This method provides functionality to the Refresh button to clear the search bar and the table view. */
    @FXML
    public void handleClearSearchProducts(){
        productTableView.setItems(productInventory);
        textFieldProductSearch.clear();
        modProductButton.setDisable(true);
        deleteProductButton.setDisable(true);
    }
}
