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
public class StatistiquesGeneral {
    private LinkedList<StatistiqueBesoin> m_statistiques;
    public StatistiquesGeneral(){
         m_statistiques = new LinkedList<>();
    }
    public StatistiquesGeneral(StatistiquesGeneral p_stat){
        m_statistiques = new LinkedList<>();
        for (StatistiqueBesoin stat : p_stat.m_statistiques){
           StatistiqueBesoin newStat = new StatistiqueBesoin(stat);
           m_statistiques.add(newStat);
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
            
}
