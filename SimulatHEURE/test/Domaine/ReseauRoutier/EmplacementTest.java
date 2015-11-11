/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauRoutier.Position;
import Domaine.ReseauRoutier.Troncon;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;
import junit.framework.TestCase;

import java.awt.geom.Point2D;

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
        Intersection intersectionDestination = new Intersection(new Point2D.Float(5,10));
         Intersection intersectionOrigin = new Intersection(new Point2D.Float(6,11));
        Troncon troncon = new Troncon(distribution,  intersectionDestination, new Temps(4));
       intersectionOrigin.ajouterTroncons(troncon);
        Emplacement emplacement = new Emplacement(true, 0.55, troncon, intersectionOrigin);
        Position posi = emplacement.calculPosition();
    }
}
