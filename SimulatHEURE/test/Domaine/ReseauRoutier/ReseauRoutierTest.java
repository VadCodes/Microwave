/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

import Domaine.Utilitaire.*;
import java.util.LinkedList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutierTest {
    private LinkedList<Intersection> m_intersections = new LinkedList();
    private LinkedList<Troncon> m_troncons = new LinkedList();
    ReseauRoutier reseauRoutier ;
    public ReseauRoutierTest() {
        // Ajout des intersections
        reseauRoutier  = new ReseauRoutier();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Test of getIntersections method, of class ReseauRoutier.
     */
    @Test
    public void testGetIntersections() {
        reseauRoutier.ajouterIntersection(0.0f, 0.0f);
        Intersection intersection1 = reseauRoutier.getIntersections().getLast();
        assertEquals(reseauRoutier.getIntersections().getFirst(), intersection1);
    }

    /**
     * Test of ajouterIntersection method, of class ReseauRoutier.
     */
    @Test
    public void testAjouterIntersection() {
       
    }

    /**
     * Test of ajouterTroncon method, of class ReseauRoutier.
     */
    @Test
    public void testAjouterTroncon() {
        reseauRoutier.ajouterIntersection(0.0f, 0.0f);
        Intersection intersection1 = reseauRoutier.getIntersections().getLast();
        reseauRoutier.ajouterIntersection(0.0f, 10.0f);
        Intersection intersection2 = reseauRoutier.getIntersections().getLast();
        reseauRoutier.ajouterTroncon(intersection1, intersection2);
        assertTrue(reseauRoutier.getIntersections().getFirst().getTroncons().size() == 1);
    }


    /**
     * Test of initReseauRoutier method, of class ReseauRoutier.
     */
    @Test
    public void testInitReseauRoutier() {
        reseauRoutier.ajouterIntersection(0.0f, 0.0f);
        Intersection intersection1 = reseauRoutier.getIntersections().getLast();
        reseauRoutier.ajouterIntersection(0.0f, 10.0f);
        Intersection intersection2 = reseauRoutier.getIntersections().getLast();
        
        reseauRoutier.ajouterTroncon(intersection1, intersection2);
        Troncon troncon2 = reseauRoutier.getIntersections().getFirst().getTroncons().getFirst();
        Distribution distribution = new Distribution();
        distribution.setDistribution(new Temps(5), new Temps(10), new Temps(15));
        troncon2.setDistribution(distribution);
        reseauRoutier.initReseauRoutier();
        Temps time1 =  troncon2.getTempsTransitAutobus();
        Boolean db1 = time1.getTemps() >= 5 && time1.getTemps() <= 15 ;
        assertTrue(db1);
    }
    
}
