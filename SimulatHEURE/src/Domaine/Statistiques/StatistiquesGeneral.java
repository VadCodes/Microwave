/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Statistiques;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author louis
 */
public class StatistiquesGeneral implements java.io.Serializable {
    private LinkedList<StatistiqueBesoin> m_statistiques = new LinkedList<>();
    
    public StatistiquesGeneral() {}
    private int m_nbJours = 1;
    public StatistiquesGeneral(StatistiquesGeneral p_stat){
        for (StatistiqueBesoin stat : p_stat.m_statistiques)
           m_statistiques.add(new StatistiqueBesoin(stat));
    }
    public void miseAjourApresFin(){
        for (ListIterator<StatistiqueBesoin> stats = m_statistiques.listIterator(); stats.hasNext();) {
            StatistiqueBesoin stat = stats.next();
            if(stat.getmaxTempsDeplacement() == 0){
                stats.remove();
            }
        }
    }
    public LinkedList<StatistiqueBesoin> getListeStatistiqueBesoin(){
        return m_statistiques;
    }
    public void addStatistiqueBesoins(StatistiqueBesoin p_stat){
        m_statistiques.add(p_stat);
    }

    public StatistiqueBesoin creatStatBesoin(String p_name) {
        StatistiqueBesoin st = new StatistiqueBesoin(p_name);
        m_statistiques.add(st);
        return st;
    }

    public void setDefault() {
       for (StatistiqueBesoin stat : m_statistiques)
           stat.setDefault();
    }

    public void ajoutUnJour(StatistiquesGeneral p_stat) {
        m_nbJours = p_stat.getNbJours() +1;
        for (ListIterator<StatistiqueBesoin> stat1s = m_statistiques.listIterator(); stat1s.hasNext();) {
            StatistiqueBesoin stat1 = stat1s.next();
            for (ListIterator<StatistiqueBesoin> stat2s = p_stat.getListeStatistiqueBesoin().listIterator(); stat2s.hasNext();) {
                StatistiqueBesoin stat2 = stat2s.next();
                if(stat1.getNameItineraire().equals(stat2.getNameItineraire())){
                    stat1.merge(stat2);
                }
            }
        }
       
    }

    public int getNbJours() {
        return  m_nbJours;
    }
            
}
