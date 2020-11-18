package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
/** This is the main class of the application. */
public class MainProgram extends Application {
/** This is the start method that is the entry point for the application.
 This method loads data into the application, if provided. */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Inventory inventory = new Inventory();
        addTestData(inventory);

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        primaryStage.setScene(new Scene(root, 1100, 500));
        primaryStage.show();
    }

/** This is the main method.
 This method is called when the java program is ran. */
    public static void main(String[] args) {
        launch(args);
    }

    /** This is a method to add sample data to the application.
     @param inventory The inventory in which to add the sample data. */
    void addTestData(Inventory inventory){

        Part a1 = new InHouse(1, "Part A1", 5.99, 10, 5, 50, 100);
        Part a2 = new InHouse(2, "Part A2", 2.99, 7, 5, 50, 101);
        Part a3 = new InHouse(3, "Part A3", 7.99, 19, 5, 50, 102);
        inventory.addPart(a1);
        inventory.addPart(a2);
        inventory.addPart(a3);
        inventory.addPart(new InHouse(4, "Part B1", 18.39, 8, 2, 25, 200));
        inventory.addPart(new InHouse(5, "Part B2", 29.99, 5, 2, 25, 201));

        Part h1 = new Outsourced(100, "Part H1", 6.49, 7, 5, 50, "Company A");
        Part h2 = new Outsourced(101, "Part H2", 8.49, 28, 5, 50, "Company B");
        Part h3 = new Outsourced(102, "Part H3", 26.49, 59, 5, 100, "Company C");
        inventory.addPart(h1);
        inventory.addPart(h2);
        inventory.addPart(h3);
        inventory.addPart(new Outsourced(300, "Part G1", 10.99, 12, 10, 75, "Company D"));
        inventory.addPart(new Outsourced(301, "Part G2", 12.99, 11, 10, 75, "Company E"));

        Product product1 = new Product(100, "Product 1", 19.99, 20, 5, 100);
        product1.addAssociatedPart(a1);
        product1.addAssociatedPart(h2);
        inventory.addProduct(product1);
        Product product2 = new Product(101, "Product 2", 24.99, 25, 5, 100);
        product2.addAssociatedPart(h1);
        product2.addAssociatedPart(a3);
        inventory.addProduct(product2);
        Product product3 = new Product(102, "Product 3", 99.99, 10, 5, 50);
        product3.addAssociatedPart(h3);
        product3.addAssociatedPart(a2);
        inventory.addProduct(product3);
        inventory.addProduct(new Product(505, "Product X", 89.99, 7, 5, 20));
        inventory.addProduct(new Product(605, "Product Y", 74.99, 17, 10, 50));

    }
}
