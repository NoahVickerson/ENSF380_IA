package edu.ucalgary.oop;
import java.util.Arrays;
import java.sql.*;

public class Location implements DatabaseInterfaceable{
	private String name;
	private String address;
	private final int id;
	private static int counter = 0;
	
	private DisasterVictim[] occupants = new DisasterVictim[0];
	private Supply[] supplies = new Supply[0];
	
	public Location(String name, String address){
		this.name = name;
		this.address = address;
		this.id = counter++;
	}

	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public DisasterVictim[] getOccupants(){
		return this.occupants;
	}
	
	public void setOccupants(DisasterVictim[] occupants){
		this.occupants = occupants;
	}
	
	public Supply[] getSupplies(){
		return this.supplies;
	}
	
	public void setSupplies(Supply[] supplies){
		this.supplies = supplies;
	}
	
	public void addOccupant(DisasterVictim occupant){

		for(DisasterVictim occupantInList : this.occupants){
			if(occupantInList.getLastName() == occupant.getLastName() && occupantInList.getFirstName() == occupant.getFirstName()){
				return;
			}
		}

		this.occupants = Arrays.copyOf(this.occupants, this.occupants.length + 1);
		this.occupants[this.occupants.length - 1] = occupant;
	}
	
	public void removeOccupant(DisasterVictim occupant){
		if(this.occupants.length == 0){
			return;
		}

		DisasterVictim[] smallerOccupants = new DisasterVictim[this.occupants.length - 1];
		int index = 0;
		boolean foundOccupant = false;
		for(DisasterVictim occupantInList : this.occupants){
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
	
	public void addSupply(Supply supply) throws IllegalArgumentException { 
		if(supply.getType().equals("personal belonging")){
			throw new IllegalArgumentException("Cannot add personal belongings to a location");
		}

		for(Supply supplyInList : this.supplies){
			if(supplyInList.getId() == supply.getId()){
				return;
			}
		}
		
		this.supplies = Arrays.copyOf(this.supplies, this.supplies.length + 1);
		this.supplies[this.supplies.length - 1] = supply;
		
	}
	
	public void removeSupply(Supply supply){

		Supply[] smallerSupplies = new Supply[this.supplies.length - 1];
		int index = 0;
		boolean foundSupply = false;
		for(Supply supplyInList : this.supplies){
			if(supplyInList.getId() != supply.getId()){
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

	boolean containsSupply(Supply supplyToCheck){
		for(Supply supplyInList : this.supplies){
			if(supplyInList.getId() == supplyToCheck.getId()){
				return true;
			}
		}
		return false;
	}

	public void addEntry() throws SQLException {
		String query = "INSERT INTO Location (name, address) VALUES (?, ?)";
		String[] values = {name, address};
		String[] types = {"string", "string"};
		DbConnector db = DbConnector.getInstance();
		db.deadEndQuery(query, values, types);
	}

	public void updateEntry() throws SQLException {
		String query = "UPDATE Location SET name = ?, address = ? WHERE id = ?";
		String[] values = {name, address, String.valueOf(id)};
		String[] types = {"string", "string", "int"};
		DbConnector db = DbConnector.getInstance();
		db.deadEndQuery(query, values, types);
	}

}
		
			
	
	