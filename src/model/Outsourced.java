package model;

/** This class creates Outsourced parts and extends the abstract Part class. */
public class Outsourced extends Part {
    private String companyName;

    /** This is the constructor method for the Outsourced class.
     This method allows users to create Parts of type Outsourced.
     @param id The integer ID for the part
     @param name The name of the part
     @param price The cost of the part
     @param stock The current inventory of the part
     @param min The minimum allowed stock amount
     @param max The maximum allowed stock amount
     @param companyName The company name for the part */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** This is a getter for the Outsourced part company name.
     This allows a user to retrieve the company name of a given part.
     @return The company name for the part. */
    public String getCompanyName() {
        return companyName;
    }
    /** This is a setter for the Outsourced part company name.
     This allows a user to set the company name of a given part.
     @param companyName The company name of the part */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
