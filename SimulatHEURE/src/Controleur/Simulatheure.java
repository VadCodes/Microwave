/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;
import ReseauRoutier.ReseauRoutier;
import ReseauTransport.ReseauTransport;
import BesoinsTransport.BesoinTransport;
import ReseauTransport.PaireArretTrajet;

import java.util.LinkedList;
import java.util.ListIterator;


/**
 *
 * @author vinny
 */
public class Simulatheure {
    private ReseauRoutier m_reseauRoutier;
    private ReseauTransport m_reseauTransport;
    private LinkedList<BesoinTransport> m_listBesoins;
    
    public Simulatheure() {
        
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
