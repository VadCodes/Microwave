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
    StatistiquesGeneral(){
         m_statistiques = new LinkedList<>();
    }
    StatistiquesGeneral(StatistiquesGeneral p_stat){
        m_statistiques = new LinkedList<>();
        for (StatistiqueBesoin stat : p_stat.m_statistiques){
           StatistiqueBesoin newStat = new StatistiqueBesoin(stat);
           m_statistiques.add(newStat);
        }
    }
    public LinkedList<StatistiqueBesoin> getListeStatistiqueBesoin(){
        return m_statistiques;
    }
            
}
