package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FamilyGroupTest {
    private FamilyGroup familyGroup;
    private Person person1;
    private Person person2;

    @Before
    public void setUp() {
        person1 = new Person("Jen", "Smith", "2025-02-10", "female", "123-456-7890");
        person2 = new Person("Bill", "Smith", "2025-02-10", "male", "123-456-7890");
        familyGroup = new FamilyGroup(person1, person2);
    }

    @Test
    public void testCtor() {
        assertNotNull(familyGroup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtorSamePerson() {
        FamilyGroup newGroup = new FamilyGroup(person1, person1);
    }

    @Test
    public void testSetFamilyMembers() {
        Person[] familyMembers = {person1, person2};
        familyGroup.setFamilyMembers(familyMembers);

        assertArrayEquals(familyMembers, familyGroup.getFamilyMembers());
    }

    @Test
    public void testAddFamilyMember() {
        Person newPerson = new Person("Jane", "Doe", "2025-02-10", "female", "123-456-7890");
        familyGroup.addFamilyMember(newPerson);

        boolean found = false;
        for (Person person : familyGroup.getFamilyMembers()) {
            if (person.equals(newPerson)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testRemoveFamilyMember() {
        familyGroup.removeFamilyMember(person1);

        boolean found = false;
        for (Person person : familyGroup.getFamilyMembers()) {
            if (person.equals(person1)) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testaddFamilyMemberAlreadyExists() {
        Person otherPerson = new Person("Noah", "Vickerson", "2025-02-10", "m", "123-456-7890");
        FamilyGroup otherGroup = new FamilyGroup(otherPerson, new Person("Some", "One", "2025-02-10", "m", "123-456-7890"));
        familyGroup.addFamilyMember(otherPerson);
    }
}
