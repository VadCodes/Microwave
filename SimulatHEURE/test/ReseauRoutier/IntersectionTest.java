/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauRoutier.Position;
import Domaine.ReseauRoutier.Troncon;
import Domaine.Utilitaire.Temps;
import Domaine.Utilitaire.Distribution;
import junit.framework.TestCase;

import java.awt.geom.Point2D;

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
        Point2D.Float position = new Point2D.Float(4,6);
        Intersection intersection = new Intersection(position);
        intersection.setName(name);
        String nameExpected = intersection.getName();
        assertEquals(name, nameExpected);
    }
    /**
     * Test of getPosition method, of class Intersection.
     */
    public void testGetPosition() {
        Point2D.Float position = new Point2D.Float(4,6);
        Intersection intersection = new Intersection(position);
        Point2D.Float positionExpected = intersection.getPosition();
        assertEquals(position, positionExpected);
    }
    public void testAjouterTroncon(){
        Point2D.Float posi= new Point2D.Float(4,5);
        Point2D.Float posi2 = new Point2D.Float(6,8);
        Intersection intersec = new Intersection(posi);
        Intersection intersec2 = new Intersection(posi2);
        Temps t1 = new  Temps(3);
        Temps t2 = new  Temps(4);
        Temps t3= new  Temps(5);
        Distribution distribution = new Distribution(t1,t2,t3);
        Troncon troncon = new Troncon(distribution, intersec2,  new Temps(4));
        intersec.ajouterTroncons(troncon);
        assertEquals(intersec.getListeTroncons().getFirst().getDestination().getPosition(), intersec2.getPosition());
    }
}
