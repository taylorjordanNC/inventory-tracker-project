package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/** This class consolidates error messages used throughout the application. */
public class AlertMessage {

    /**
     * This method shows errors related to invalid input upon attempting to save a part or product.
     * This method is invoked to show the appropriate alert message throughout the application.
     *
     * @param code The code that calls the appropriate alert.
     */
    public static void errorSaving(int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        switch (code) {
            case 1: {
                alert.setContentText("Minimum stock cannot be greater than maximum stock.");
                break;
            }
            case 2: {
                alert.setContentText("Inventory cannot be less than the minimum.");
                break;
            }
            case 3: {
                alert.setContentText("Inventory cannot exceed maximum.");
                break;
            }
            case 4: {
                alert.setContentText("The total cost of the associated parts may not exceed the total cost of the product.");
                break;
            }
        }
        alert.showAndWait();
    }

    /**
     * This method shows errors related adding associated parts to a product.
     * This method is invoked to show the appropriate alert message in the add and modify product dialogs.
     *
     * @param code The code that calls the appropriate alert.
     */
    public static void errorAssociatedPart(int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        switch (code) {
            case 1: {
                alert.setHeaderText("Product already contains this part.");
                alert.setContentText("Cannot add duplicate parts.");
                break;
            }
        }
        alert.showAndWait();
    }

    /**
     * This method shows errors related to searching for a part or product within a table.
     * This method is invoked to show the appropriate alert message when searching for a part or product.
     *
     * @param code The code that calls the appropriate alert.
     */
    public static void errorSearching(int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        switch (code) {
            case 1: {
                alert.setContentText("Part not found");
                break;
            }
            case 2: {
                alert.setContentText("Product not found");
                break;
            }
        }
        alert.showAndWait();
    }
}

