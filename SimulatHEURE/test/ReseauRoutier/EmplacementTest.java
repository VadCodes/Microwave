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
public class EmplacementTest extends TestCase {
    
    public EmplacementTest(String testName) {
        super(testName);
    }

    /**
     * Test of calculPosition method, of class Emplacement.
     */
    public void testCalculPosition() {
        Distribution distribution = new Distribution(new Temps(5), new Temps(8), new Temps(10));
        Intersection intersectionDestination = new Intersection(new Position(5,10));
         Intersection intersectionOrigin = new Intersection(new Position(6,11));
        Troncon troncon = new Troncon(distribution,  intersectionDestination, new Temps(4));
       intersectionOrigin.ajouterTroncons(troncon);
        Emplacement emplacement = new Emplacement(true, 0.55, troncon, intersectionOrigin);
        Position posi = emplacement.calculPosition();
    }
}
