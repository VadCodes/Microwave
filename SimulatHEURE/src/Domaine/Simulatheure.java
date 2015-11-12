package Domaine;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauTransport.ReseauTransport;
import Domaine.BesoinsTransport.BesoinTransport;
import Domaine.ReseauTransport.PaireArretTrajet;

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
}
