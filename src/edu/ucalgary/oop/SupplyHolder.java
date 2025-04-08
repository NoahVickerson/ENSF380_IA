package edu.ucalgary.oop;

public interface SupplyHolder extends DatabaseInterfaceable{
    public void addSupply(Posession supply);
    public void removeSupply(Posession supply);
    public boolean containsSupply(Posession supply);
}
