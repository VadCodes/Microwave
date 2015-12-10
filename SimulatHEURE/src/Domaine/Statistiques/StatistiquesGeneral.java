/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Statistiques;

import java.util.LinkedList;

/**
 *
 * @author louis
 */
public class StatistiquesGeneral implements java.io.Serializable {
    private LinkedList<StatistiqueBesoin> m_statistiques = new LinkedList<>();
    
    public StatistiquesGeneral() {}
    
    public StatistiquesGeneral(StatistiquesGeneral p_stat){
        for (StatistiqueBesoin stat : p_stat.m_statistiques)
           m_statistiques.add(new StatistiqueBesoin(stat));
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
            
}
