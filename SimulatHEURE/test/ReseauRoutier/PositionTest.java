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
public class PositionTest extends TestCase {
    
    public PositionTest(String testName) {
        super(testName);
    }

    /**
     * Test of getPositionX method, of class Position.
     */
    public void testGetPositionX() {
        System.out.println("getPositionX");
        double expResult = 4.5;
        Position instance = new Position(expResult, 6.7);
        double result = instance.getPositionX();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPositionY method, of class Position.
     */
    public void testGetPositionY() {
        System.out.println("getPositionY");
        double expResult = 4.5;
        Position instance = new Position(6.7, expResult);
        double result = instance.getPositionY();
        assertEquals(expResult, result);
    }
    
}
