/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;

import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Trajet;
import Domaine.ReseauRoutier.Troncon;
import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;
import Domaine.Utilitaire.Temps;
import java.util.LinkedList;
import java.util.ListIterator;
import junit.framework.TestCase;

/**
 *
 * @author Nathaniel
 */
public class AutobusTest extends TestCase {
    private Intersection  m_intersection1;
    private Intersection  m_intersection2;
    private Intersection  m_intersection3;
    private Intersection  m_intersection4;
    private Troncon m_troncon1;
    private Troncon m_troncon2;
    private Troncon m_troncon3;
    private Troncon m_troncon4;
    private Troncon m_troncon5;
    private Autobus m_bus;
    public AutobusTest(String testName) {
        super(testName);
        ReseauRoutier reseauRoutier = new ReseauRoutier();
        // Ajout des intersections
        reseauRoutier.ajouterIntersection(0.0f, 0.0f);
        Intersection intersection1 = reseauRoutier.getIntersections().getLast();
        reseauRoutier.ajouterIntersection(0.0f, 10.0f);
        Intersection intersection2 = reseauRoutier.getIntersections().getLast();
        reseauRoutier.ajouterIntersection(10.0f, 10.0f);
        Intersection intersection3 = reseauRoutier.getIntersections().getLast();
        reseauRoutier.ajouterIntersection(20.0f, 5.0f);
        Intersection intersection4 = reseauRoutier.getIntersections().getLast();
        //Ajout des Troncons Et deux distributions
        Distribution distribution1 =new Distribution();
        distribution1.setDistribution(new Temps(15), new Temps(15), new Temps(15));
        Distribution distribution2 =new Distribution();
        distribution2.setDistribution(new Temps(10), new Temps(10), new Temps(10));
        reseauRoutier.ajouterTroncon(intersection1, intersection4);
        Troncon troncon1 = reseauRoutier.getIntersections().get(0).getTroncons().getLast();
        troncon1.setDistribution(distribution1);
        reseauRoutier.ajouterTroncon(intersection1, intersection2);
        Troncon troncon2 = reseauRoutier.getIntersections().get(0).getTroncons().getLast();
        troncon2.setDistribution(distribution2);
        reseauRoutier.ajouterTroncon(intersection2, intersection3);
        Troncon troncon3 = reseauRoutier.getIntersections().get(1).getTroncons().getLast();
        troncon3.setDistribution(distribution2);
        reseauRoutier.ajouterTroncon(intersection3, intersection1);
        Troncon troncon4 = reseauRoutier.getIntersections().get(2).getTroncons().getLast();
        troncon4.setDistribution(distribution2);
        reseauRoutier.ajouterTroncon(intersection3, intersection4);
        Troncon troncon5 = reseauRoutier.getIntersections().get(2).getTroncons().getLast();
        troncon5.setDistribution(distribution2);
         reseauRoutier.initReseauRoutier();
        //push dans les lists
        m_intersection1 = intersection1;
        m_intersection2 = intersection2;
        m_intersection3 = intersection3;
        m_intersection4 = intersection4;
        m_troncon1 = troncon1;
        m_troncon2 = troncon2;
        m_troncon3 = troncon3;
        m_troncon4 = troncon4;
        m_troncon5 = troncon5;
        
        //Initialisation  Bus
        LinkedList<Troncon> listTroncon1 = new LinkedList();
         LinkedList<Troncon> listTroncon2 = new LinkedList();
         LinkedList<PaireArretTrajet> listTrajet= new LinkedList();
         listTroncon1.add(m_troncon2);
         listTroncon1.add(m_troncon3);
         listTroncon2.add(m_troncon3);
         listTroncon2.add(m_troncon5);
         
        //System.out.println(m_troncon1.getTempsTransitAutobus().getTemps());
         
       
         Emplacement emplacementInitiale = new Emplacement(true, 0.25f, m_troncon2, m_intersection1);
         Emplacement emplacementMilieu = new Emplacement(true, 0.5f, m_troncon3, m_intersection2);
         Emplacement emplacementFinal = new Emplacement(true, 0.75f, m_troncon5, m_intersection3);
         
        Trajet trajet1 = new Trajet(emplacementInitiale , emplacementMilieu, listTroncon1);
        Trajet trajet2 = new Trajet(emplacementMilieu, emplacementFinal, listTroncon2);
        String id = "800";
        Arret arret1 = new Arret(emplacementInitiale, "yoyoda");
         Arret arret2 = new Arret(emplacementMilieu, "yoyo2");
         PaireArretTrajet pa1 = new PaireArretTrajet(arret1, trajet1); 
          PaireArretTrajet pa2 = new PaireArretTrajet(arret2, trajet2); 
          listTrajet.add(pa1);
          listTrajet.add(pa2);
        m_bus = new Autobus(emplacementInitiale,  50, id ,new    Temps(0), false);
        m_bus.assignerTrajet(listTrajet);
        
    }

 

    /**
     * Test of getIterateur method, of class Autobus.
     */
    public void testAutobusMemetronconPourcentageAvance() {
        Temps deltatT = new Temps(5);
        m_bus.miseAJourEmplacement(deltatT);
        assertEquals(m_bus.getEmplacement().getPourcentageParcouru(), 0.75f);
    }

   
  
    public void testAutobusChangeTroncon() {
        Temps deltatT = new Temps(10);
        m_bus.miseAJourEmplacement(deltatT);
        assertEquals(m_bus.getEmplacement().getTroncon(), m_troncon3);
        assertEquals(m_bus.getEmplacement().getPourcentageParcouru(), 0.25f);
    }

    
       public void testAutobusChangeArretTroncon() {
        Temps deltatT = new Temps(15);
        m_bus.miseAJourEmplacement(deltatT);
        assertEquals(m_bus.getEmplacement().getTroncon(), m_troncon3);
        assertEquals(m_bus.getEmplacement().getPourcentageParcouru(), 0.75f);
    }
       
          public void testAutobusChangeIterator() {
        Temps deltatT = new Temps(20);
        m_bus.miseAJourEmplacement(deltatT);
        assertEquals(m_bus.getEmplacement().getTroncon(), m_troncon5);
        assertEquals(m_bus.getEmplacement().getPourcentageParcouru(), 0.25f);
    }
    /**
     * Test of assignerTrajet method, of class Autobus.
     */
    public void testAssignerTrajet() {
        
    }

    /**
     * Test of setID method, of class Autobus.
     */
    public void testSetID() {
        
    }

    /**
     * Test of getID method, of class Autobus.
     */
    public void testGetID() {
        
    }
    
}
