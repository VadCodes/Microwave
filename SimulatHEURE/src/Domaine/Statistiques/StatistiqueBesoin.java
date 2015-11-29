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
    private Itineraire m_itineraire;
    public StatistiqueBesoin(Itineraire p_itineraire){
        m_itineraire = p_itineraire;
    }
    public void miseAJourStat(Temps p_temps){
        m_nombreEchantion++;
        m_sommeDesTemps += p_temps.getTemps();
        if(p_temps.getTemps() >m_maxTempsDeplacement){
            m_maxTempsDeplacement = p_temps.getTemps();
        }
        if(p_temps.getTemps() < m_minTempsDeplacement){
            m_minTempsDeplacement =p_temps.getTemps(); 
        }
    }
}
