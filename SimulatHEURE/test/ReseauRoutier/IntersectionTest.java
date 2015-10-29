/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

import junit.framework.TestCase;

/**
 *
 * @author Nathaniel
 */
public class IntersectionTest extends TestCase {
    
    public IntersectionTest(String testName) {
        super(testName);
    }

    /**
     * Test of setName method, of class Intersection.
     */
    public void testSetAndGetName() {
        String name = "Louise";
        Position position = new Position(4,6);
        Intersection intersection = new Intersection(position);
        intersection.setName(name);
        String nameExpected = intersection.getName();
        assertEquals(name, nameExpected);
    }
    /**
     * Test of getPosition method, of class Intersection.
     */
    public void testGetPosition() {
        Position position = new Position(4,6);
        Intersection intersection = new Intersection(position);
        Position positionExpected = intersection.getPosition();
        assertEquals(position, positionExpected);
    }
}
