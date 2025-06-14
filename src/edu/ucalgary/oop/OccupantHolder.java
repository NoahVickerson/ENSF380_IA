package edu.ucalgary.oop;

public interface OccupantHolder extends DatabaseInterfaceable {
    public String getName();
    public void setName(String name);
    public void addOccupant(Occupant occupant);
    public void removeOccupant(Occupant occupant);
    public Occupant[] getOccupants();
    public void setOccupants(Occupant[] occupants);
}
