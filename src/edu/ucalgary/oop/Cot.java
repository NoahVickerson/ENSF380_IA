/**
 * @author Noah Vickerson
 * Cot.java 
 * @version 1.0
 * @date Apr 1 2025
 */

package edu.ucalgary.oop;

import java.util.regex.*;


public class Cot extends Supply {
    private String room;
    private String grid;

    /**
     * Cot constructor
     * @param room: the room the cot is in (format Letter)
     * @param grid: the grid the cot is in (format Letter + Number)
     * @param comments: comments on the cot
     * @throws IllegalArgumentException for invalid room, grid
     */
    public Cot(String room, String grid, String comments) throws IllegalArgumentException {
        super("Cot", 1);
        if (!isValidGrid(grid)){
            throw new IllegalArgumentException("inv_grid");
        }else if (!isValidRoom(room)) {
            throw new IllegalArgumentException("inv_room");
        }
        this.room = room;
        this.grid = grid;
        this.comments = comments;
    }

    /**
     * Cot constructor
     * @param id: the id of the cot
     * @param room: the room the cot is in (format Letter)
     * @param grid: the grid the cot is in (format Letter + Number)
     * @param comments: comments on the cot
     * @throws IllegalArgumentException for invalid room, grid, id
     */
    public Cot(int id, String room, String grid, String comments) throws IllegalArgumentException {
        super(id, "Cot", 1);
        if (!isValidGrid(grid)){
            throw new IllegalArgumentException("inv_grid");
        }else if (!isValidRoom(room)) {
            throw new IllegalArgumentException("inv_room");
        }
        this.room = room;
        this.grid = grid;
        this.comments = comments;
    }

    /**
     * Get the room the cot is in
     * @return room
     */
    public String getRoom() {
        return room;
    }

    /**
     * Get the grid the cot is in
     * @return grid
     */
    public String getGrid() {
        return grid;
    }

    /** 
     * Set the room the cot is in
     * @param room (format Letter)
     * @throws IllegalArgumentException for invalid room
     */
    public void setRoom(String room) throws IllegalArgumentException {
        if (!isValidRoom(room)) {
            throw new IllegalArgumentException("inv_room");
        }
        this.room = room;
    }

    /**
     * Set the grid the cot is in
     * @param grid (format Letter + Number)
     * @throws IllegalArgumentException for invalid grid
     */
    public void setGrid(String grid) throws IllegalArgumentException {
        if (!isValidGrid(grid)) {
            throw new IllegalArgumentException("inv_grid");
        }
        this.grid = grid;
    }

    /**
     * Check if the grid is valid
     * @param grid
     * @return true if valid, false otherwise
     */
    private static boolean isValidGrid(String grid) {
        Pattern pattern = Pattern.compile("^[A-Za-z]\\d+$");
        Matcher matcher = pattern.matcher(grid);
        return matcher.matches();
    }

    /**
     * Check if the room is valid
     * @param room
     * @return true if valid, false otherwise
     */
    private static boolean isValidRoom(String room) {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(room);
        return matcher.matches();
    }
}
