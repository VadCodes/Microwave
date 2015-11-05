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
     * Test of setDistribution method, of class Troncon.
     */
    public void testSetDistribution() {
        Distribution distribution = new Distribution(new Temps(5), new Temps(8), new Temps(10));
        Intersection intersectionOrigin = new Intersection(new Position(5,10));
        Troncon troncon = new Troncon(distribution,  intersectionOrigin, new Temps(4));
        troncon.setTempsTransit();
        Temps time1 =  troncon.getTempsTransitAutobus();
        Temps time2 =troncon.getTempsTransitPieton();
        Boolean db1 = time1.getTemps() >= 5 && time1.getTemps() <= 10 ;
        Boolean db2 = time2.getTemps() != 0;
        assertTrue(db1);
        assertTrue(db2);
    }
}
