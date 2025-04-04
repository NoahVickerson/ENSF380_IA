package edu.ucalgary.oop;

import java.util.regex.*;


public class Cot extends Supply {
    private String room;
    private String grid;

    public Cot(String room, String grid, String comments) throws IllegalArgumentException {
        super("Cot", 1);
        if (!isValidGrid(grid) || !isValidRoom(room)) {
            throw new IllegalArgumentException("Grid should be a letter followed by a number");
        }
        this.room = room;
        this.grid = grid;
        this.comments = comments;
    }

    public String getRoom() {
        return room;
    }

    public String getGrid() {
        return grid;
    }

    public void setRoom(String room) throws IllegalArgumentException {
        if (!isValidRoom(room)) {
            throw new IllegalArgumentException("Room should be a number");
        }
        this.room = room;
    }

    public void setGrid(String grid) throws IllegalArgumentException {
        if (!isValidGrid(grid)) {
            throw new IllegalArgumentException("Grid should be a letter followed by a number");
        }
        this.grid = grid;
    }

    private static boolean isValidGrid(String grid) {
        Pattern pattern = Pattern.compile("^[A-Za-z]\\d+$");
        Matcher matcher = pattern.matcher(grid);
        return matcher.matches();
    }

    private static boolean isValidRoom(String room) {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(room);
        return matcher.matches();
    }
}
