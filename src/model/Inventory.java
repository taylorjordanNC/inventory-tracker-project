package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/** This class controls and manages inventory of all parts and products. */
public class Inventory{
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    /** This is the constructor method for this class.
     This method allows for the creation of inventory instances. */
    public Inventory() {
    }
    /** This method adds a part to the part inventory.
     This method allows a user to add a part to the allParts inventory array list.
     @param newPart Part to be added to allParts list. */
    public static void addPart(Part newPart){
        if(newPart != null) {
            allParts.add(newPart);
        }
    }
    /** This method adds a product to the product inventory.
     This method allows a user to add a product to the allProducts inventory array list.
     @param newProduct Part to be added to allProducts list. */
    public static void addProduct(Product newProduct) {
        if (newProduct != null) {
            allProducts.add(newProduct);
        }
    }
    /** This method searches for a given part ID.
     This method allows the user to search the allParts inventory using the part ID and returns the part.
     @param partId The part ID to be searched.
     @return The part searched for. */
    public static Part lookupPart(int partId){
        if(!allParts.isEmpty()){
        for(int i=0; i<allParts.size(); i++){
            if(allParts.get(i).getPartID() == partId){
                return allParts.get(i);
            } }
        } return null;
    }
    /** This method searches for a given product ID.
     This method allows the user to search the allProducts inventory using the product ID and returns the product.
     @param productId The product ID to be searched.
     @return The product searched for. */
    public static Product lookupProduct(int productId) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getProductID() == productId) {
                    return allProducts.get(i);
                } }
        } return null;
    }
    /** This method searches for a given part name.
     This method allows the user to search using the part name and returns the part inventory list.
     @param partName The name of the part to be searched
     @return The observable array list allParts */
    public static ObservableList<Part> lookupPart(String partName){
        return allParts;
    }
    /** This method searches for a given product name.
     This method allows the user to search using the product name and returns the product inventory list.
     @param productName The name of the product to be searched
     @return The observable array list allProducts */
    public static ObservableList<Product> lookupProduct(String productName){
        return allProducts;
    }
    /** This method updates a part.
     The intention of this method is to update a part, although the method is empty as I have chosen to implement a different method to accomplish the update.
     @param index Index number of the part within the array list
     @param selectedPart The part selected for update. */
    public static void updatePart(int index, Part selectedPart){ }
    /** This method updates a product.
     The intention of this method is to update a product, although the method is empty as I have chosen to implement a different method to accomplish the update.
     @param index Index number of the product within the array list
     @param selectedProduct The product selected for update. */
    public static void updateProduct(int index, Product selectedProduct){ }
    /** This method deletes a part.
     This method deletes the selected part parameter from the allParts inventory.
     @param selectedPart The part selected for deletion
     @return Boolean value to confirm deletion. */
    public static boolean deletePart(Part selectedPart){
        if(allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }
    /** This method deletes a product.
     This method deletes the selected product parameter from the allProducts inventory.
     @param selectedProduct The product selected for deletion
     @return Boolean value to confirm deletion. */
    public static boolean deleteProduct(Product selectedProduct){
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }
    /** This method is a getter for the allParts inventory list.
     This method retrieves the part inventory array list for the user.
     @return The observable array list allParts. */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    /** This method is a getter for the allProducts inventory list.
     This method retrieves the product inventory array list for the user.
     @return The observable array list allProducts. */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
