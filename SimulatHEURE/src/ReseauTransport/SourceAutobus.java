/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauTransport;

import ReseauRoutier.Emplacement;
import Utilitaire.Distribution;
import Utilitaire.Temps;

/**
 *
 * @author louis
 */
public class SourceAutobus {
    private String m_nomSource;
    private int m_nbAutobusGeneres = 0;
    private Circuit m_circuit;
    private Emplacement m_emplacement;
    private int m_capaciteMax = 50;
    private Distribution m_distibutionFrequence;
    private Temps m_frequence;
    
    public void genererAutobus(){
        Autobus nouvelAutobus = new Autobus(m_emplacement, m_capaciteMax, 
                                            genererBusID(), calculerTempsApparition(), 
                                            estSurArret());
        m_circuit.assignerTrajetAutobus(nouvelAutobus);
        m_circuit.ajouterAutobus(nouvelAutobus);
    }
    
    public String genererBusID(){
        m_nbAutobusGeneres++;
        return m_nomSource + Integer.toString(m_nbAutobusGeneres);
    }
        
    public Boolean estSurArret(){
        return m_emplacement.equals(m_circuit.getListeArretTrajet().getFirst().getArret().getEmplacement()); 
    }
    
    public Temps calculerTempsApparition(){
        //TODO
    }
    
    public void setCapaciteMax(int capacite){
        m_capaciteMax = capacite;
    }
    
    public void initSourceAutobus(){
        m_frequence = m_distibutionFrequence.pigerTemps();
    }
}
