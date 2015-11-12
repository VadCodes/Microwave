/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;


import Domaine.ReseauTransport.SourceAutobus;
import Domaine.ReseauTransport.Circuit;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Trajet;
import Domaine.ReseauRoutier.Troncon;
import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;
import java.util.LinkedList;
import junit.framework.TestCase;

/**
 *
 * @author Nathaniel
 */
public class SourceAutobusTest extends TestCase {
    private LinkedList<Intersection> m_intersections = new LinkedList();
    private LinkedList<Troncon> m_troncons = new LinkedList();
    private Circuit m_circuit;
    public SourceAutobusTest(String testName) {
        super(testName);
        ReseauRoutier reseauRoutier = new ReseauRoutier();
        // Ajout des intersections
        Intersection intersection1 = reseauRoutier.ajouterIntersection(0.0f, 0.0f);
        Intersection intersection2 = reseauRoutier.ajouterIntersection(0.0f, 10.0f);
        Intersection intersection3 = reseauRoutier.ajouterIntersection(10.0f, 10.0f);
        Intersection intersection4 = reseauRoutier.ajouterIntersection(20.0f, 5.0f);
        //Ajout des Troncons Et deux distributions
        Distribution distribution1 =new Distribution(new Temps(10), new Temps(15), new Temps(20));
        Distribution distribution2 =new Distribution(new Temps(5), new Temps(10), new Temps(15));
        Troncon troncon1 = reseauRoutier.ajouterTroncon(intersection1, intersection4, distribution1, new Temps(4));
        Troncon troncon2 = reseauRoutier.ajouterTroncon(intersection1, intersection2, distribution2, new Temps(4));
        Troncon troncon3 = reseauRoutier.ajouterTroncon(intersection2, intersection3, distribution2, new Temps(4));
        Troncon troncon4 = reseauRoutier.ajouterTroncon(intersection3, intersection1, distribution2, new Temps(4));
        Troncon troncon5 = reseauRoutier.ajouterTroncon(intersection3, intersection4, distribution2, new Temps(4));
        //push dans les lists
        m_intersections.add(intersection1);
        m_intersections.add(intersection2);
        m_intersections.add(intersection3);
        m_intersections.add(intersection4);
        m_troncons.add(troncon1);
        m_troncons.add(troncon2);
        m_troncons.add(troncon3);
        m_troncons.add(troncon4);
        m_troncons.add(troncon5);
        reseauRoutier.initReseauRoutier();
        Emplacement emplArr = new Emplacement(true, 0.8f, troncon2, intersection1);
        Arret arr = new Arret(emplArr, "cristre");
        LinkedList<PaireArretTrajet> listePaires = new LinkedList();
        Emplacement emplaFifi = new Emplacement(true, 0.66f,troncon3, intersection2);
        LinkedList<Troncon> listeTroncon = new LinkedList();
        listeTroncon.add(troncon2);
        listeTroncon.add(troncon3);
        Trajet trajet = new Trajet(emplArr, emplaFifi, listeTroncon);
        listePaires.add(new PaireArretTrajet(arr, trajet));
        Arret arr2d2 = new Arret(emplaFifi, "crissequatre");
        Trajet pasbon = new Trajet(emplArr, emplaFifi, new LinkedList());
        listePaires.add(new PaireArretTrajet(arr2d2, pasbon));
        m_circuit = new Circuit("bitches", listePaires, new ReseauRoutier());
    }
        

    /**
     * Test of miseAjoutTempsRestant method, of class SourceAutobus.
     */
    public void testMiseAJourTempsRestant() {
        /*
        Emplacement emplacement = new Emplacement(true, 0.5, m_troncons.getFirst(),m_troncons.getFirst().getDestination());
       Distribution distribution = new Distribution(new Temps(5),new Temps(5),new Temps(5));
       SourceAutobus source = new  SourceAutobus(emplacement, null, "test", distribution, new Temps(10));
       source.miseAjoutTempsRestant(new Temps(5));
       assertEquals(source.getNbAutobus(), 0);
       source.miseAjoutTempsRestant(new Temps(6));
       assertEquals(source.getNbAutobus(), 1);
       source.miseAjoutTempsRestant(new Temps(20));
       assertEquals(source.getNbAutobus(), 5);«*/
    }

    /**
     * Test of genererAutobus method, of class SourceAutobus.
     */
    public void testGenererAutobus() {
       
    }


    /**
     * Test of estSurArret method, of class SourceAutobus.
     */
    public void testEstSurArretTrue() {
        Emplacement emplSrc = new Emplacement(true, 0.8f, m_troncons.getFirst(), m_intersections.getFirst());
        Distribution dist = new Distribution(new Temps(5), new Temps(10), new Temps(15));
        SourceAutobus srcAutobus = new SourceAutobus(emplSrc, m_circuit, "TestSource", dist, new Temps(0));
        
        Emplacement emplArr = new Emplacement(true, 0.800000001f, m_troncons.getFirst(), m_intersections.getFirst());
        Arret arr = new Arret(emplArr, "cristre");
        m_circuit.ajouterPaire(arr, m_circuit.getListeArretTrajet().getFirst().getTrajet());
        
        assertTrue(srcAutobus.estSurArret());
    }
    
    public void testEstSurArretFalse() {
        Emplacement emplSrc = new Emplacement(true, 0.8f, m_troncons.getFirst(), m_intersections.getFirst());
        Distribution dist = new Distribution(new Temps(5), new Temps(10), new Temps(15));
        SourceAutobus srcAutobus = new SourceAutobus(emplSrc, m_circuit, "TestSource", dist, new Temps(0));
        
        Emplacement emplArr = new Emplacement(true, 0.81f, m_troncons.getFirst(), m_intersections.getFirst());
        Arret arr = new Arret(emplArr, "cristre");
        m_circuit.ajouterPaire(arr, m_circuit.getListeArretTrajet().getFirst().getTrajet());
        
        assertFalse(srcAutobus.estSurArret());
    }

    /**
     * Test of setCapaciteMax method, of class SourceAutobus.
     */
    public void testSetCapaciteMax() {
        
    }

    
}
