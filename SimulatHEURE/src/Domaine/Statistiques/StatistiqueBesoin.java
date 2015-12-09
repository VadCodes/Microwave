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
public class StatistiqueBesoin {
    private double m_precisionGlobal;
    private double m_sommeDesTemps;
    private double m_maxTempsDeplacement =0;
    private double m_minTempsDeplacement = Double.MAX_VALUE;
    private int m_nombreEchantion = 0;
    private String m_itineraire;
    public StatistiqueBesoin(String p_itineraire){
        m_itineraire = p_itineraire;
    }
    public StatistiqueBesoin(StatistiqueBesoin p_stat){
        m_sommeDesTemps = p_stat.m_sommeDesTemps;
        m_precisionGlobal = p_stat.m_precisionGlobal;
        m_maxTempsDeplacement = p_stat.m_maxTempsDeplacement;
        m_minTempsDeplacement = p_stat.m_minTempsDeplacement;
        m_nombreEchantion = p_stat.m_nombreEchantion;
        m_itineraire = p_stat.m_itineraire;
    }
    public String getNameItineraire(){
        return m_itineraire;
    }
    public double getprecisionGlobal(){
        return (double)Math.round(100*m_precisionGlobal/60)/100;
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
        m_precisionGlobal = Math.abs(moyenneActuel - newMoyenne);
        if(p_temps.getTemps() >m_maxTempsDeplacement){
            m_maxTempsDeplacement = p_temps.getTemps();
        }
        if(p_temps.getTemps() < m_minTempsDeplacement){
            m_minTempsDeplacement =p_temps.getTemps(); 
        }
    }
}
