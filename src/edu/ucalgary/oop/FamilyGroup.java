package edu.ucalgary.oop;

import java.util.Arrays;

public class FamilyGroup {
    private Person[] familyMembers;
    private final int id;
    private static int counter = 0;

    public FamilyGroup() {
        familyMembers = new Person[] {};
        id = counter++;
    }

    public FamilyGroup(Person person1, Person person2) throws IllegalArgumentException {
        if (person1 == person2) {
            throw new IllegalArgumentException("Persons cannot be the same");
        }
        familyMembers = new Person[] {person1, person2};
        person1.setFamilyGroup(this);
        person2.setFamilyGroup(this);
        id = counter++;
    }

    public int getId() {
        return id;
    }

    public void addFamilyMember(Person familyMember) throws IllegalArgumentException {
        if(familyMember.getFamilyGroup() == this) {
            for(Person person : familyMembers) {
                if(person.equals(familyMember)) {
                    return;
                }
            }
        }
        else if (familyMember.getFamilyGroup() != null) {
            throw new IllegalArgumentException("Person already has a family group");
        }
        familyMembers = Arrays.copyOf(familyMembers, familyMembers.length + 1);
        familyMembers[familyMembers.length - 1] = familyMember;
    }

    public void setFamilyMembers(Person[] familyMembers) {
        this.familyMembers = familyMembers;
    }

    public Person[] getFamilyMembers() {
        return familyMembers;
    }

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
