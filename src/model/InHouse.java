package model;

/** This class creates In House parts and extends the abstract Part class. */
public class InHouse extends Part {
    private int machineID;

    /** This is the constructor method for the InHouse class.
     This method allows users to create instances of Part of type InHouse with the given parameters.
     @param id The integer ID for the part
     @param name The name of the part
     @param price The cost of the part
     @param stock The current inventory of the part
     @param min The minimum allowed stock amount
     @param max The maximum allowed stock amount
     @param machineID The machineID for the part */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /** This is a getter for the InHouse part machine ID.
     This allows a user to retrieve the machine ID of a given part.
     @return The machine ID for the part. */
    public int getMachineID() {
        return machineID;
    }
    /** This is a setter for the InHouse part machine ID.
     This allows users to set the machine ID of a given part.
     @param machineID The machine ID of the part */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}


