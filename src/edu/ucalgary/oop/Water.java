package edu.ucalgary.oop;

public class Water extends Supply {
    private String allocationDate;

    public Water(String comments, int quantity) throws IllegalArgumentException {
        super("Water", quantity);

        this.setComments(comments);
    }

    public Water(String comments, int quantity, String allocationDate) throws IllegalArgumentException {
        super("Water", quantity);
        if (!isValidDateFormat(allocationDate)) {
            throw new IllegalArgumentException("Invalid date format");
        }

        this.setComments(comments);
        this.allocationDate = allocationDate;
    }

    public String getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(String date) throws IllegalArgumentException {
        if (!isValidDateFormat(date)) {
            throw new IllegalArgumentException("Invalid date format");
        }
        this.allocationDate = date;
    }

    public boolean isConsumed(String curDate) throws IllegalArgumentException {
        if (!isValidDateFormat(curDate)) {
            throw new IllegalArgumentException("Invalid date format");
        }

        String[] date = allocationDate.split("-");
        String[] currentDate = curDate.split("-");
        int timestamp = Integer.parseInt(date[0] + date[1] + date[2]);
        int currentTimestamp = Integer.parseInt(currentDate[0] + currentDate[1] + currentDate[2]);
        return timestamp - currentTimestamp < 0;
    }

    private static boolean isValidDateFormat(String date) {
        return date.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
    }
}
