/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;

import Domaine.ReseauTransport.Trajet;
import java.util.LinkedList;
import Domaine.ReseauRoutier.*;
import Domaine.ReseauTransport.PaireArretTrajet;
import java.util.ListIterator;

/**
 *
 * @author vadimcote
 */
public class PaireParcours {
    private ParcoursBus m_parcoursBus;
    private Trajet m_trajet;
    public PaireParcours( Trajet p_trajet, ParcoursBus p_parcoursBus){
        m_trajet = p_trajet;
        m_parcoursBus = p_parcoursBus;
    }
    public Trajet getTrajet(){
        return m_trajet;
    }
    public ParcoursBus getParcoursBus(){
        return m_parcoursBus;
    }
    public void setTrajet(Trajet p_trajet){
        m_trajet = p_trajet;
    }
    public void setParcoursBus(ParcoursBus parcoursBus){
        m_parcoursBus = parcoursBus;
    }
    public LinkedList<Troncon> getTroncons(){
        return m_parcoursBus.getTroncons();
    }
}
    
