package edu.ucalgary.oop;

public class PersonalBelonging extends Supply {
    private String description;

    public PersonalBelonging(String description, String comments, int quantity) throws IllegalArgumentException {
        super("personal belonging", quantity);
        this.description = description;
        this.setComments(comments);
    }

    public PersonalBelonging(int id, String description, String comments, int quantity) throws IllegalArgumentException {
        super(id, "personal belonging", quantity);
        this.description = description;
        this.setComments(comments);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
