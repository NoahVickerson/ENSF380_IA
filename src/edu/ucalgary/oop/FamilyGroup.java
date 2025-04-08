/**
 * @author Noah Vickerson
 * FamilyGroup.java 
 * @version 1.1
 * @date Apr 1 2025
 */

package edu.ucalgary.oop;

import java.util.Arrays;

public class FamilyGroup {
    private Person[] familyMembers;
    private final int id;
    private static int counter = 0;

    /**
     * Default constructor
     */
    public FamilyGroup() {
        familyMembers = new Person[] {};
        counter++;
        id = counter;
    }

    /**
     * Constructor creates a new group with two people
     * @param person1
     * @param person2
     * @throws IllegalArgumentException if persons are the same
     */
    public FamilyGroup(Person person1, Person person2) throws IllegalArgumentException {
        if (person1 == person2) {
            throw new IllegalArgumentException("same_person");
        }
        familyMembers = new Person[] {person1, person2};
        person1.setFamilyGroup(this);
        person2.setFamilyGroup(this);
        counter++;
        id = counter;
    }

    /**
     * Constructor creates a new group with a specific id (for constructiion from db)
     * @param person1
     * @throws IllegalArgumentException if id already in use
     */
    public FamilyGroup(int id) throws IllegalArgumentException {
        if (id < counter) {
            throw new IllegalArgumentException("repeat_id");
        }
        familyMembers = new Person[] {};
        this.id = id;
        if(id > counter) {
            counter = id;
        }
    }

    /**
     * Constructor creates a new group with a specific id (for constructiion from db)
     * @param person1
     * @param person2
     * @throws IllegalArgumentException if id already in use or people the same
     */
    public FamilyGroup(int id, Person person1, Person person2) throws IllegalArgumentException {
        if (id < counter) {
            throw new IllegalArgumentException("repeat_id");
        }
        if (person1 == person2) {
            throw new IllegalArgumentException("same_person");
        }
        familyMembers = new Person[] {person1, person2};
        person1.setFamilyGroup(this);
        person2.setFamilyGroup(this);
        this.id = id;
        if(id > counter) {
            counter = id;
        }
    }

    /**
     * Returns the id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Adds a person to the family group
     * @param familyMember
     * @throws IllegalArgumentException if person already has a family group
     */
    public void addFamilyMember(Person familyMember) throws IllegalArgumentException {
        if(familyMember.getFamilyGroup() == this) {
            for(Person person : familyMembers) {
                if(person.equals(familyMember)) {
                    return;
                }
            }
        }
        else if (familyMember.getFamilyGroup() != null) {
            throw new IllegalArgumentException("person_in_family");
        }
        familyMembers = Arrays.copyOf(familyMembers, familyMembers.length + 1);
        familyMembers[familyMembers.length - 1] = familyMember;
        familyMember.setFamilyGroup(this);
    }

    /**
     * Sets the family members
     * @param familyMembers
     */
    public void setFamilyMembers(Person[] familyMembers) {
        this.familyMembers = familyMembers;
    }

    /**
     * Returns the family members
     * @return familyMembers
     */
    public Person[] getFamilyMembers() {
        return familyMembers;
    }

    /**
     * Removes a person from the family group
     * @param familyMember
     */
    public void removeFamilyMember(Person familyMember) {
        if (familyMember.getFamilyGroup() != this) {
            return;
        }        

        Person[] newFamilyMembers = new Person[familyMembers.length - 1];
        int index = 0;
        for (Person person : familyMembers) {
            if (!person.equals(familyMember)) {
                newFamilyMembers[index] = person;
                index++;
            }
        }
        this.familyMembers = newFamilyMembers;
    }

}
