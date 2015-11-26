/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;

/**
 *
 * @author vadimcote
 */
public class SourceIndividus {
    private Temps m_tempsInitial;
    private Distribution m_distributionFrequence;
    private Emplacement m_emplacement;
    private Temps m_frequence;
    private String m_nom;
    private Itineraire m_itineraire;
    private Temps m_tempsAvantApparition;
    private int m_nbIndividusGeneres = 0;
    
    
    
    public SourceIndividus(Temps p_tempsInitial, Temps p_tempsAvantApparition, Distribution p_distributionFrequence, Emplacement p_emplacement, String p_nom
   , Itineraire p_itineraire){
        m_tempsInitial = p_tempsInitial;
        m_distributionFrequence = p_distributionFrequence;
        m_emplacement = p_emplacement;
        m_nom = p_nom;
        m_tempsAvantApparition = p_tempsAvantApparition;
    }

    public void initSourceIndividu(){
        m_frequence = m_distributionFrequence.pigerTemps();

    }
    
    private String genererBusID(){
        m_nbIndividusGeneres++;
        String tmp = m_nom.concat("A" +Integer.toString(m_nbIndividusGeneres));
        return tmp;
    }
    
    public void setNom(String nom){
        m_nom = nom;
    }
    public void setDefault() {
        m_nbIndividusGeneres = 0;
        m_tempsAvantApparition = m_tempsInitial;
    }
    public void setTempsAttenteInitial(Temps temps){
        m_tempsInitial = temps;
         setDefault() ;
    }
    public Temps getTempsAttenteInitial(){
        return m_tempsInitial;
    }
    public void setDistribution(Distribution dist){
        m_distributionFrequence = dist;
    }
    public Distribution getDistribution(){
        return m_distributionFrequence;
    }
}