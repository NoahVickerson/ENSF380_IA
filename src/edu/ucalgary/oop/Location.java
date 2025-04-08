/**
 * @author Noah Vickerson
 * Location.java 
 * @version 1.3
 * @date Apr 2 2025
 */

package edu.ucalgary.oop;
import java.util.Arrays;
import java.sql.*;

public class Location implements OccupantSupplyHolder {
	private String name;
	private String address;
	private final int id;
	private static int counter = 0;
	
	private Occupant[] occupants = new DisasterVictim[0];
	private Posession[] supplies = new Posession[0];
	
	/**
	 * Constructor
	 * @param name
	 * @param address
	 */
	public Location(String name, String address){
		this.name = name;
		this.address = address;
		counter++;
		this.id = counter;
	}

	/**
	 * Constructor
	 * @param id must be unique
	 * @param name
	 * @param address
	 * @throws IllegalArgumentException for non unique ids
	 */
	public Location(int id, String name, String address) throws IllegalArgumentException {
		if(id < counter){
			throw new IllegalArgumentException("repeat_id");
		}
		this.name = name;
		this.address = address;
		this.id = id;
		if(counter < id){
			counter = id;
		}
	}

	/**
	 * Returns the id
	 * @return
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * Returns the name
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Sets the name
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Returns the address
	 * @return
	 */
	public String getAddress(){
		return this.address;
	}
	
	/**
	 * Sets the address
	 * @param address
	 */
	public void setAddress(String address){
		this.address = address;
	}
	
	/**
	 * Returns the occupants
	 * @return occupants
	 */
	public Occupant[] getOccupants(){
		return this.occupants;
	}
	
	/**
	 * Sets the occupants
	 * @param occupants
	 */
	public void setOccupants(Occupant[] occupants){
		this.occupants = occupants;
	}
	
	/**
	 * Returns the supplies
	 * @return supplies
	 */
	public Posession[] getSupplies(){
		return this.supplies;
	}
	
	/**
	 * Sets the supplies
	 * @param supplies
	 */
	public void setSupplies(Posession[] supplies){
		this.supplies = supplies;
	}
	
	/**
	 * Adds an occupant
	 * @param occupant
	 */
	public void addOccupant(Occupant occupant){

		for(Occupant occupantInList : this.occupants){
			if(occupantInList.getLastName() == occupant.getLastName() && occupantInList.getFirstName() == occupant.getFirstName()){
				return;
			}
		}

		this.occupants = Arrays.copyOf(this.occupants, this.occupants.length + 1);
		this.occupants[this.occupants.length - 1] = occupant;
	}
	
	/**
	 * Removes an occupant
	 * @param occupant
	 */
	public void removeOccupant(Occupant occupant){
		if(this.occupants.length == 0){
			return;
		}

		Occupant[] smallerOccupants = new Occupant[this.occupants.length - 1];
		int index = 0;
		boolean foundOccupant = false;
		for(Occupant occupantInList : this.occupants){
			if(occupantInList.getLastName() != occupant.getLastName() && occupantInList.getFirstName() != occupant.getFirstName()){
				smallerOccupants[index] = occupantInList;
				index++;
			}else{
				foundOccupant = true;
			}
		}

		if(!foundOccupant){
			return;
		}
		this.occupants = smallerOccupants;
	}
	
	/**
	 * Adds a supply
	 * @param supply
	 * @throws IllegalArgumentException if supply is of type personal belonging
	 */
	public void addSupply(Posession supply) throws IllegalArgumentException { 
		if(supply.getType().equals("personal belonging")){
			throw new IllegalArgumentException("loc_add_belongings");
		}

		for(Posession supplyInList : this.supplies){
			if(supplyInList.getId() == supply.getId()){
				return;
			}
		}
		
		this.supplies = Arrays.copyOf(this.supplies, this.supplies.length + 1);
		this.supplies[this.supplies.length - 1] = supply;
		
	}
	
	/**
	 * Removes a supply
	 * @param supply
	 */
	public void removeSupply(Posession supply){

		Posession[] smallerSupplies = new Posession[this.supplies.length - 1];
		int index = 0;
		boolean foundSupply = false;
		for(Posession supplyInList : this.supplies){
			if(supplyInList.getId() != supply.getId() && index < smallerSupplies.length){
				smallerSupplies[index] = supplyInList;
				index++;
			}else{
				foundSupply = true;
			}
		}

		if(!foundSupply){
			return;
		}
		this.supplies = smallerSupplies;
		
	}

	/**
	 * Checks if a supply is in the list
	 * @param supplyToCheck
	 * @return
	 */
	public boolean containsSupply(Posession supplyToCheck){
		for(Posession supplyInList : this.supplies){
			if(supplyInList.getId() == supplyToCheck.getId()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds this location to the database
	 * @throws SQLException
	 */
	public void addEntry() throws SQLException {
		String query = "INSERT INTO Location (location_id, name, address) VALUES (?, ?, ?)";
		String[] values = {String.valueOf(id), name, address};
		String[] types = {"int", "string", "string"};
		DbConnector db = DbConnector.getInstance();
		db.deadEndQuery(query, values, types);
	}

	/**
	 * Updates this location in the database
	 * @throws SQLException
	 */
	public void updateEntry() throws SQLException {
		String query = "UPDATE Location SET name = ?, address = ? WHERE location_id = ?";
		String[] values = {name, address, String.valueOf(id)};
		String[] types = {"string", "string", "int"};
		DbConnector db = DbConnector.getInstance();
		db.deadEndQuery(query, values, types);

		for(Posession s : this.supplies) {
            s.updateEntry();
            query = "INSERT INTO SupplyAllocation (supply_id, person_id, location_id) VALUES (?, ?, ?)";
            String[] newValues = {String.valueOf(s.getId()), "null", String.valueOf(id)};
            String[] newTypes = {"int", "int", "int"};
            db.deadEndQuery(query, newValues, newTypes);
        }
	}

}
		
			
	
	