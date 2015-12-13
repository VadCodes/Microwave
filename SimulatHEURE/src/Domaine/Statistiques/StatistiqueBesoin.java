/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Statistiques;

import Domaine.BesoinsTransport.Itineraire;
import Domaine.Utilitaire.Temps;

/**
 *
 * @author ns222
 */
public class StatistiqueBesoin implements java.io.Serializable {
    private double m_precisionGlobalTotal = 0;
    private double m_sommeDesTemps = 0;
    private double m_maxTempsDeplacement =0;
    private double m_minTempsDeplacement = Double.MAX_VALUE;
    private int m_nombreEchantion = 0;
    private String m_nomItineraire;
    public StatistiqueBesoin(String p_itineraire){
        m_nomItineraire = p_itineraire;
    }
    public StatistiqueBesoin(StatistiqueBesoin p_stat){
        m_sommeDesTemps = p_stat.m_sommeDesTemps;
        m_precisionGlobalTotal = p_stat.m_precisionGlobalTotal;
        m_maxTempsDeplacement = p_stat.m_maxTempsDeplacement;
        m_minTempsDeplacement = p_stat.m_minTempsDeplacement;
        m_nombreEchantion = p_stat.m_nombreEchantion;
        m_nomItineraire = p_stat.m_nomItineraire;
    }
    public String getNameItineraire(){
        return m_nomItineraire;
    }
    
    public void setNameItineraire(String p_nom){
        m_nomItineraire = p_nom;
    }
        
    public double getprecisionGlobal(){
        double precisionAbs = (m_precisionGlobalTotal/m_nombreEchantion)/60;
        double moyenne = (m_sommeDesTemps/m_nombreEchantion)/60;
        double pourcentage = 100*precisionAbs/moyenne;
        return (double)Math.round(100*(pourcentage)/60)/100;
    }
     public double getmaxTempsDeplacement(){
        return (double)Math.round(100*m_maxTempsDeplacement/60)/100;
    }
      public double getminTempsDeplacement(){
        return (double)Math.round(100*m_minTempsDeplacement/60)/100;
    }
       public double getMoyenne(){
           
        return (double)Math.round(100*(m_sommeDesTemps/m_nombreEchantion)/60)/100;
    }

            
            
    public void miseAJourStat(Temps p_temps){
        double moyenneActuel = m_sommeDesTemps/m_nombreEchantion;
        m_nombreEchantion++;
        m_sommeDesTemps += p_temps.getTemps();
        double newMoyenne = m_sommeDesTemps/m_nombreEchantion;
        if(moyenneActuel > 0){
            m_precisionGlobalTotal += Math.abs(moyenneActuel - newMoyenne);
        }
        if(p_temps.getTemps() >m_maxTempsDeplacement){
            m_maxTempsDeplacement = p_temps.getTemps();
        }
        if(p_temps.getTemps() < m_minTempsDeplacement){
            m_minTempsDeplacement =p_temps.getTemps(); 
        }
    }

    void setDefault() {
       m_precisionGlobalTotal = 0;
        m_sommeDesTemps = 0;
        m_maxTempsDeplacement =0;
        m_minTempsDeplacement = Double.MAX_VALUE;
        m_nombreEchantion = 0;
    }

    public int getNbIteration() {
        return m_nombreEchantion;
    }
}
