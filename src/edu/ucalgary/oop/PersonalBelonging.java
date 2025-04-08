/**
 * @author Noah Vickerson
 * PersonalBelonging.java 
 * @version 1.0
 * @date Mar 30 2025
 */

package edu.ucalgary.oop;

public class PersonalBelonging extends Supply {
    private String description;

    /**
     * Constructor
     * @param description
     * @param comments
     * @param quantity must be integer greater than 0
     * @throws IllegalArgumentException if above not followed
     */
    public PersonalBelonging(String description, String comments, int quantity) throws IllegalArgumentException {
        super("personal belonging", quantity);
        this.description = description;
        this.setComments(comments);
    }

    /**
     * Constructor
     * @param id    must be unique
     * @param description
     * @param comments
     * @param quantity must be integer greater than 0
     * @throws IllegalArgumentException if above not followed
     */
    public PersonalBelonging(int id, String description, String comments, int quantity) throws IllegalArgumentException {
        super(id, "personal belonging", quantity);
        this.description = description;
        this.setComments(comments);
    }

    /**
     * Returns description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
