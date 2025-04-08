package edu.ucalgary.oop;

public interface Occupant extends DatabaseInterfaceable {
    public OccupantHolder getCurrentLocation();
    public void setCurrentLocation(OccupantHolder location);
    public String getFirstName();
    public String getLastName();
}
