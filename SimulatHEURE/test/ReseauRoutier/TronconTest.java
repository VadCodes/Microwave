/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

import Utilitaire.Distribution;
import Utilitaire.Temps;
import junit.framework.TestCase;

/**
 *
 * @author Nathaniel
 */
public class TronconTest extends TestCase {
    
    public TronconTest(String testName) {
        super(testName);
    }

    /**
     * Test of setTempsTransit method, of class Troncon.
     */
    public void testSetTempsTransit() {
        System.out.println("setTempsTransit");
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getTempsTransitAutobus method, of class Troncon.
     */
    public void testGetTempsTransitAutobus() {
        System.out.println("getTempsTransitAutobus");
        Troncon instance = null;
        Temps expResult = null;
        Temps result = instance.getTempsTransitAutobus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTempsTransitPieton method, of class Troncon.
     */
    public void testGetTempsTransitPieton() {
        System.out.println("getTempsTransitPieton");
        Troncon instance = null;
        Temps expResult = null;
        Temps result = instance.getTempsTransitPieton();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDistribution method, of class Troncon.
     */
    public void testSetDistribution() {
        System.out.println("setDistribution");
        Distribution distribution = null;
        Troncon instance = null;
        instance.setDistribution(distribution);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIntersectionOrigin method, of class Troncon.
     */
    public void testGetIntersectionOrigin() {
        System.out.println("getIntersectionOrigin");
        Troncon instance = null;
        Intersection expResult = null;
        Intersection result = instance.getIntersectionOrigin();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIntersectionDistination method, of class Troncon.
     */
    public void testGetIntersectionDistination() {
        System.out.println("getIntersectionDistination");
        Troncon instance = null;
        Intersection expResult = null;
        Intersection result = instance.getIntersectionDistination();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of longueurTroncon method, of class Troncon.
     */
    public void testLongueurTroncon() {
        System.out.println("longueurTroncon");
        Troncon instance = null;
        double expResult = 0.0;
        double result = instance.longueurTroncon();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
