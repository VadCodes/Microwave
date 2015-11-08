/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauTransport;
import ReseauRoutier.Emplacement;
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
    private Temps m_frequence;
    private Temps m_tempsAttenteinitial;
    private Temps m_tempsAvantApparition;
    public SourceAutobus(Emplacement p_emplacement, Circuit p_circuit, String p_nomSource, Temps p_frequence, Temps p_tempsAttenteinitial){
        m_emplacement = p_emplacement;
        m_circuit = p_circuit;
        m_nomSource = p_nomSource;
        m_frequence = p_frequence;
        m_tempsAttenteinitial = p_tempsAttenteinitial;
        m_tempsAvantApparition = m_tempsAttenteinitial;
    }
    
    public void miseAjoutTempsRestant(Temps p_deltatT){
        double tmp = m_tempsAvantApparition.getTemps() - p_deltatT.getTemps();
        m_tempsAvantApparition = new Temps(tmp);
    }
    public void genererAutobus(int p_capaciteMax){
        while(m_tempsAvantApparition.getTemps() <= 0){
            m_nbAutobusGeneres++;
            Temps tempsApparition = tempsApparition();
            Autobus nouvelAutobus = new Autobus(m_emplacement, p_capaciteMax, genererBusID(),tempsApparition ,estSurArret() );
            m_circuit.assignerTrajetAutobus(nouvelAutobus);
            double tmp = m_tempsAvantApparition.getTemps() + m_frequence.getTemps();
            m_tempsAvantApparition = new Temps(tmp);
        }
    }
    
    public String genererBusID(){
        return m_nomSource + Integer.toString(m_nbAutobusGeneres);
    }
        
    public Boolean estSurArret(){
        return m_emplacement.equals(m_circuit.getListeArretTrajet().getFirst().getArret().getEmplacement()); 
    }
    
    public Temps tempsApparition(){
       double tmp =  m_tempsAttenteinitial.getTemps()+ (m_frequence.getTemps() * m_nbAutobusGeneres);
        Temps tmo = new Temps(tmp);
        return tmo;
    }
}
