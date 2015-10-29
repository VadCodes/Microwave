/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nathaniel
 */
public class PositionTest {
    
    public PositionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Test of getPositionX method, of class Position.
     */
    @org.junit.Test
    public void testGetPositionX() {
        System.out.println("getPositionX");
        Position instance = null;
        double expResult = 0.0;
        double result = instance.getPositionX();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPositionY method, of class Position.
     */
    @org.junit.Test
    public void testGetPositionY() {
        System.out.println("getPositionY");
        Position instance = null;
        double expResult = 0.0;
        double result = instance.getPositionY();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
