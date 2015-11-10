/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.Utilitaire.Temps;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;

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
    private Temps m_tempsAttenteinitial;
    private Temps m_tempsAvantApparition;
    
    public SourceAutobus(Emplacement p_emplacement, Circuit p_circuit, String p_nomSource, Distribution p_distribution,  Temps p_tempsAttenteinitial){
        m_emplacement = p_emplacement;
        m_circuit = p_circuit;
        m_nomSource = p_nomSource;
        m_distibutionFrequence = p_distribution;
        m_tempsAttenteinitial = p_tempsAttenteinitial;
        m_tempsAvantApparition = m_tempsAttenteinitial;
    }
    
    public void miseAjoutTempsRestant(Temps p_deltatT){
        // Le temps avant le prochain ajout d'autobus diminu selon le deltatT
        double tmp = m_tempsAvantApparition.getTemps() - p_deltatT.getTemps();
        m_tempsAvantApparition = new Temps(tmp);
    }
    public void genererAutobus(){
        //Tant que le temps est négatif ou égale a zéro on pop des autobus pour remettre le temps d'apparition > 0;
        while(m_tempsAvantApparition.getTemps() <= 0){
            miseAjourAvantAjout();
            Autobus nouvelAutobus = new Autobus(m_emplacement, m_capaciteMax,  genererBusID(),tempsApparition() ,estSurArret() );
            m_circuit.ajouterAutobus(nouvelAutobus);
            m_circuit.assignerTrajetAutobus(nouvelAutobus);
            //On update le temps avant apparition. On l'addition de la frequence.
            double tmp = m_tempsAvantApparition.getTemps() + m_frequence.getTemps();
            m_tempsAvantApparition = new Temps(tmp);
            initSourceAutobus();
        }
    }
    public int getNbAutobus(){
        return m_nbAutobusGeneres;
    }
    private String genererBusID(){
        m_nbAutobusGeneres++;
        return m_nomSource + Integer.toString(m_nbAutobusGeneres);
    }
        
    public Boolean estSurArret(){
        return m_emplacement.equals(m_circuit.getListeArretTrajet().getFirst().getArret().getEmplacement()); 
    }
    
    private Temps tempsApparition(){
       double tmp =  m_tempsAttenteinitial.getTemps()+ (m_frequence.getTemps() * m_nbAutobusGeneres);
        Temps tmo = new Temps(tmp);
        return tmo;
    }
    
    public void setCapaciteMax(int capacite){
        m_capaciteMax = capacite;
    }
    
    public void initSourceAutobus(){
        m_frequence  = m_distibutionFrequence.pigerTemps();
    }
    private void miseAjourAvantAjout(){
        //met à jour le nombre d'autobus et on pige un nouveau temps de distribution
       // m_frequence  = m_distibutionFrequence.pigerTemps();
        m_nbAutobusGeneres++;
    }
}
