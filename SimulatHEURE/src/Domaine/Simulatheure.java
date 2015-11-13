package Domaine;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauTransport.ReseauTransport;
import Domaine.BesoinsTransport.BesoinTransport;
import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauTransport.PaireArretTrajet;
import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;
import java.awt.geom.Point2D;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author vinny
 */
public class Simulatheure {
    public enum Modes {
        ROUTIER, TRANSPORT, BESOINS, SIMULATION
    }
    
    public enum Commandes {
        SELECTIONNER, INTERSECTION, TRONCON, SUPPRIMER
    }
    
    public ReseauRoutier m_reseauRoutier ;
    private ReseauTransport m_reseauTransport ;
    private Temps m_deltaT;
    private LinkedList<BesoinTransport> m_listBesoins = new LinkedList();
    public Simulatheure() {
        m_reseauRoutier = new ReseauRoutier();
        m_reseauTransport = new ReseauTransport();
        
    }
    
    public void demarrerSimulation(){
        m_reseauRoutier.initReseauRoutier();
        m_reseauTransport.initReseauTransport();
        ListIterator<BesoinTransport> BesoinTransportItr = m_listBesoins.listIterator();
        while (BesoinTransportItr.hasNext()) {
            BesoinTransportItr.next().initBesoinTransport();
        }
    }
    public void rafraichirSimulation(Temps m_deltaT){
        m_reseauTransport.calculEtatReseauTransport(m_deltaT);
    }
    public void ajouterIntersection(Point2D.Float poin){
        m_reseauRoutier.ajouterIntersection(poin.x, poin.y);
    }
    
    public void ajoutITroncon(Intersection intersectionOrigin, Intersection intersectionDestination, Distribution distribution){
        m_reseauRoutier.ajouterTroncon(intersectionOrigin, intersectionDestination, distribution);
    }
}
