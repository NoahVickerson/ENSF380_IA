package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersonTest {
    private Person person;

    @Before
    public void setUp() {
        person = new Person("John", "Doe", "2025-02-10", "m", "123-456-7890");
    }

    @Test
    public void testCtor() {
        assertNotNull(person);
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", person.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", person.getLastName());
    }

    @Test
    public void testGetType() {
        assertEquals("inquirer", person.getType());
    }

    @Test
    public void testGetDateOfBirth() {
        assertEquals("2025-02-10", person.getDateOfBirth());
    }

    @Test
    public void testGetPhoneNumber() {
        assertEquals("123-456-7890", person.getPhoneNumber());
    }

    @Test
    public void testGetGender() {
        assertEquals("Gender should be m", "m", person.getGender());
    }

    @Test
    public void testSetFirstName() {
        person.setFirstName("James");
        assertEquals("First name should have been updated", "James", person.getFirstName());
    }

    @Test
    public void testSetLastName() {
        person.setLastName("Smith");
        assertEquals("Last name should have been updated", "Smith", person.getLastName());
    }

    @Test
    public void testSetDateOfBirth() {
        person.setDateOfBirth("2025-03-11");
        assertEquals("Person date of birth should have been updated", "2025-03-11", person.getDateOfBirth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDateOfBirthWithInvalidFormat() {
        person.setDateOfBirth("2025/02/10");
    }

    @Test
    public void testSetPhoneNumber() {
        person.setPhoneNumber("234-999-8888");
        assertEquals("Phone number should have been updated", "234-999-8888", person.getPhoneNumber());
    }

    @Test
    public void testSetGender() {
        person.setGender("non_binary");
        assertEquals("Person gender should have been updated", "non_binary", person.getGender());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGenderWithInvalidGender() {
        person.setGender("x");
    }

    @Test 
    public void testSetFamilyGroup() {
        FamilyGroup familyGroup = new FamilyGroup(new Person("Jane", "Doe", "2025-02-10", "f", "123-456-7890"), new Person("Bill", "Doe", "2025-02-10", "m", "123-456-7890"));
        person.setFamilyGroup(familyGroup); // should be redundant because the FamilyGroup ctor will do this, but still use for decoupling
        assertEquals("Person should have been added to family group", familyGroup, person.getFamilyGroup());
    }

    @Test
    public void testAddFamilyMember()  {
        Person familyMember = new Person("Jane", "Doe", "2025-02-10", "f", "123-456-7890");
        FamilyGroup familyGroup = new FamilyGroup(familyMember, new Person("Bill", "Doe", "2025-02-10", "m", "123-456-7890")); // family member should now have this family group
        

        person.setFamilyGroup(null);
        
        System.out.println(familyMember == null);
        person.addFamilyMember(familyMember);
        assertEquals("Person should have been added to preexisting family group", familyGroup, person.getFamilyGroup());
    }

    @Test 
    public void testSetFamilyMemberRemovesPerson() {
        FamilyGroup familyGroup = new FamilyGroup(new Person("Jane", "Doe", "2025-02-10", "f", "123-456-7890"), new Person("Bill", "Doe", "2025-02-10", "m", "123-456-7890"));
        person.setFamilyGroup(familyGroup);
        person.setFamilyGroup(null);

        boolean found = false;
        for(Person person : familyGroup.getFamilyMembers()) {
            if(person.equals(this.person)) {
                found = true;
                break;
            }
        }
        assertFalse("Person should have been removed from family group", found);
    }
}
