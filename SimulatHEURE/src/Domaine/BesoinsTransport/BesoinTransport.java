/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author louis
 */
public class BesoinTransport {
    
    private LinkedList<Individu> m_listeIndividus;
    private LinkedList<Itineraire> m_itineraires;
    private SourceIndividus m_source;
    private PileSelectionBesoins m_pileSelection;
     
    
    public BesoinTransport(LinkedList<Individu> p_listeIndividus,  LinkedList<Itineraire> p_itineraire, SourceIndividus p_source){
        m_listeIndividus = p_listeIndividus;
        m_itineraires = p_itineraire;
        m_source = p_source;
    }
    public LinkedList<Individu> getListIndividus(){
        return m_listeIndividus;
    }
    public  LinkedList<Itineraire> getItineraire(){
        return m_itineraires;
    }
    public SourceIndividus getSourceIndividus(){
        return m_source;
    }
    public void setListIndividus(LinkedList<Individu> p_listeIndividus){
        m_listeIndividus = p_listeIndividus;
    }
    public void setItineraire( LinkedList<Itineraire> p_itineraire){
        m_itineraires = p_itineraire;
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
    
}