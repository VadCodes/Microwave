/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;
import Domaine.Reseau;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Troncon;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author louis
 */
public class ReseauBesoins extends Reseau {
    
    private LinkedList<Individu> m_listeIndividus;
    private LinkedList<Itineraire> m_listeItineraires;
    private SourceIndividus m_source;
    private PileSelectionBesoins m_pileSelection;
    
    public ReseauBesoins(LinkedList<Individu> p_listeIndividus, LinkedList<Itineraire> p_itineraire, SourceIndividus p_source){
        m_listeIndividus = p_listeIndividus;
        m_listeItineraires = p_itineraire;
        m_source = p_source;
    }
    public LinkedList<Individu> getListIndividus(){
        return m_listeIndividus;
    }
    public LinkedList<Itineraire> getListItineraire(){
        return m_listeItineraires;
    }
    public SourceIndividus getSourceIndividus(){
        return m_source;
    }
    public void setListIndividus(LinkedList<Individu> p_listeIndividus){
        m_listeIndividus = p_listeIndividus;
    }
    public void setListItineraire(LinkedList<Itineraire> p_itineraire){
        m_listeItineraires = p_itineraire;
    }
    public void setSourceIndividus(SourceIndividus p_source){
        m_source = p_source; 
    }
    public void initBesoinTransport(){
        m_source.initSourceIndividu();      
    }
            
    public PileSelectionBesoins getPileSelection(){
        return m_pileSelection;
    }
    
    public void deselectionnerTout(){
        m_pileSelection.vider();
    }
    
    public Boolean selectionnerItineraire(Float xReel, Float yReel, Float p_echelle, Troncon trc){
        if (trc!=null){
            Float pourcentageClic = trc.getPourcentageClic(xReel, yReel, p_echelle);
            Emplacement empl_deb = null;
            Emplacement empl_fin = null;
            Boolean itineraireSelectionne = false;
            for (Itineraire itn : m_listeItineraires){
                for (PaireParcours paireParc : itn.getListPaireParcours()){
                    if (paireParc.getTrajet() != null){
                        for (Troncon trc2 : paireParc.getTrajet().getListeTroncons()){
                            if (trc2.equals(trc)){
                                if (paireParc.getTrajet().getEmplacementInitial().estSurTroncon())
                                    empl_deb = paireParc.getTrajet().getEmplacementInitial();
                                if (paireParc.getTrajet().getEmplacementFinal().estSurTroncon())
                                    empl_fin = paireParc.getTrajet().getEmplacementFinal();
                                
                                if (empl_deb != null && empl_fin != null){
                                    if (empl_deb.getTroncon().equals(trc) && empl_fin.getTroncon().equals(trc)){
                                        if (empl_deb.getPourcentageParcouru() < empl_fin.getPourcentageParcouru()){
                                            //si entre
                                            if (pourcentageClic > empl_deb.getPourcentageParcouru() && 
                                                    pourcentageClic < empl_fin.getPourcentageParcouru()){
                                                itineraireSelectionne = true;
                                            }
                                        }
                                        else{
                                            //si avant ou apres
                                            if (pourcentageClic > empl_deb.getPourcentageParcouru() ||
                                                    pourcentageClic < empl_fin.getPourcentageParcouru()){
                                                itineraireSelectionne = true;
                                            }
                                        }
                                    }
                                    else if(empl_deb.getTroncon().equals(trc)){
                                        // si apres
                                        if (pourcentageClic > empl_deb.getPourcentageParcouru()){
                                            itineraireSelectionne = true;
                                        }
                                    }
                                    else if(empl_fin.getTroncon().equals(trc)){
                                        // si avant
                                        if (pourcentageClic < empl_fin.getPourcentageParcouru()){
                                            itineraireSelectionne = true;
                                        }
                                    }
                                    else{
                                        itineraireSelectionne = true;
                                    }
                                }
                                else if(empl_deb != null){
                                    if (empl_deb.getTroncon().equals(trc)){
                                        if (pourcentageClic > empl_deb.getPourcentageParcouru()){
                                            itineraireSelectionne = true;
                                        }
                                    }
                                    else{
                                        itineraireSelectionne = true;
                                    }
                                }
                                else if(empl_fin != null){
                                    if(empl_fin.getTroncon().equals(trc)){
                                        if (pourcentageClic < empl_fin.getPourcentageParcouru()){
                                            itineraireSelectionne = true;
                                        }
                                    }
                                    else{
                                        itineraireSelectionne = true;
                                    }
                                }
                                else{
                                    itineraireSelectionne = true;
                                }

                                if (itineraireSelectionne){
                                    m_pileSelection.ajouter(itn);
                                    return true;
                                }

                                empl_deb = null;
                                empl_fin = null;
                            }
                        }
                    }
                    else if (paireParc.getParcoursBus() != null){
                        ParcoursBus parcoBus = paireParc.getParcoursBus();
                        LinkedList<Troncon> parcoBusTroncons = parcoBus.getTroncons();
                        for (Troncon trc2 : parcoBusTroncons){
                            if (trc2.equals(trc)){
                                if (parcoBus.getArretDepart().getEmplacement().estSurTroncon())
                                    empl_deb = parcoBus.getArretDepart().getEmplacement();
                                if (parcoBus.getArretFinal().getEmplacement().estSurTroncon())
                                    empl_fin = parcoBus.getArretFinal().getEmplacement();
                                
                                if (empl_deb != null && empl_fin != null){
                                    if (empl_deb.getTroncon().equals(trc) && empl_fin.getTroncon().equals(trc)){
                                        if (empl_deb.getPourcentageParcouru() < empl_fin.getPourcentageParcouru()){
                                            //si entre
                                            if (pourcentageClic > empl_deb.getPourcentageParcouru() && 
                                                    pourcentageClic < empl_fin.getPourcentageParcouru()){
                                                itineraireSelectionne = true;
                                            }
                                        }
                                        else{
                                            //si avant ou apres
                                            if (pourcentageClic > empl_deb.getPourcentageParcouru() ||
                                                    pourcentageClic < empl_fin.getPourcentageParcouru()){
                                                itineraireSelectionne = true;
                                            }
                                        }
                                    }
                                    else if(empl_deb.getTroncon().equals(trc)){
                                        // si apres
                                        if (pourcentageClic > empl_deb.getPourcentageParcouru()){
                                            itineraireSelectionne = true;
                                        }
                                    }
                                    else if(empl_fin.getTroncon().equals(trc)){
                                        // si avant
                                        if (pourcentageClic < empl_fin.getPourcentageParcouru()){
                                            itineraireSelectionne = true;
                                        }
                                    }
                                    else{
                                        itineraireSelectionne = true;
                                    }
                                }
                                else if(empl_deb != null){
                                    if (empl_deb.getTroncon().equals(trc)){
                                        if (pourcentageClic > empl_deb.getPourcentageParcouru()){
                                            itineraireSelectionne = true;
                                        }
                                    }
                                    else{
                                        itineraireSelectionne = true;
                                    }
                                }
                                else if(empl_fin != null){
                                    if(empl_fin.getTroncon().equals(trc)){
                                        if (pourcentageClic < empl_fin.getPourcentageParcouru()){
                                            itineraireSelectionne = true;
                                        }
                                    }
                                    else{
                                        itineraireSelectionne = true;
                                    }
                                }
                                else{
                                    itineraireSelectionne = true;
                                }

                                if (itineraireSelectionne){
                                    m_pileSelection.ajouter(itn);
                                    return true;
                                }

                                empl_deb = null;
                                empl_fin = null;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
}
