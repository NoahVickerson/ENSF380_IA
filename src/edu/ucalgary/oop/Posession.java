package edu.ucalgary.oop;

public interface Posession extends DatabaseInterfaceable {
    public String getType();
    public int getQuantity();
    public void setQuantity(int quantity);
}
