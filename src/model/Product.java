package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class controls the creation of products. */
public class Product{
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productID;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
/** This is the constructor method for the Product class.
 This method allows users to create instances of Product with the given parameters.
 @param productID The product ID of the product
 @param name The name of the product
 @param price The price of the product
 @param stock The current inventory for the product
 @param min The minimum allowed stock for the product
 @param max The maximum allowed stock for the product */
    public Product(int productID, String name, double price, int stock, int min, int max) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
    /** This method is a getter for the product ID.
     This method retrieves the product ID of a given product.
     @return The product ID. */
    public int getProductID() {
        return productID;
    }
    /** This method is a setter for the product ID.
     This method declares the product ID for a given product.
     @param productID The product ID for the product */
    public void setProductID(int productID) {
        this.productID = productID;
    }
    /** This method is a getter for the name variable.
     This method retrieves the name of a given product.
     @return The name of the product. */
    public String getName() {
        return name;
    }
    /** This method is a setter for the name variable.
     This method declares the name of a given product.
     @param name The name of the product */
    public void setName(String name) {
        this.name = name;
    }
    /** This method is a getter for the price variable.
     This method retrieves the price of a given product.
     @return The price of the product. */
    public double getPrice() {
        return price;
    }
    /** This method is a setter for the price variable.
     This method declares the price of a given product.
     @param price The price of the product */
    public void setPrice(double price) {
        this.price = price;
    }
    /** This method is a getter for the stock variable.
     This method retrieves the stock/inventory of a given product.
     @return The stock amount for the product. */
    public int getStock() {
        return stock;
    }
    /** This method is a setter for the stock variable.
     This method declares the stock/inventory of a given product.
     @param stock Current stock amount of the product */
    public void setStock(int stock) {
        this.stock = stock;
    }
    /** This method is a getter for the min variable.
     This method retrieves the minimum stock amount allowed for a given product.
     @return The minimum stock amount for the product. */
    public int getMin() {
        return min;
    }
    /** This method is a setter for the min variable.
     This method declares the minimum stock amount allowed for a given product.
     @param min The minimum stock amount allowed */
    public void setMin(int min) {
        this.min = min;
    }
    /** This method is a getter for the max variable.
     This method retrieves the maximum stock amount allowed for a given product.
     @return The maximum stock amount for the product. */
    public int getMax() {
        return max;
    }
    /** This method is a setter for the max variable.
     This method declares the maximum stock amount allowed for a given product.
     @param max The maximum stock amount allowed */
    public void setMax(int max) {
        this.max = max;
    }
    /** This method adds parts to the associated parts list for a product.
     This method allows the user to add a part to a given product's associated parts.
     @param part Part that the user wants to add to the associated parts list */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    /** This method deletes a given associated part.
     This method allows a user to delete an associated part from the product's associated parts list.
     @param selectedAssociatedPart Associated part selected by the user
     @return Boolean value to confirm deletion. */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(associatedParts.contains(selectedAssociatedPart)){
            associatedParts.remove(selectedAssociatedPart);
            return true;
        } else {
            return false;
        }
    }
    /** This method is a getter for the associated parts list.
     This method allows the user to retrieve the associated parts list.
     @return The observable array list of associated parts. */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
