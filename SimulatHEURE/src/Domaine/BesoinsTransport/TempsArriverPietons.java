/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;

/**
 *
 * @author louis
 */
import Domaine.Utilitaire.Temps;

public class TempsArriverPietons {
    private Temps m_tempsArrivee;
    private Individu m_individu;
    
    public TempsArriverPietons(Temps tempsArrivee, Individu individu){
            m_tempsArrivee = tempsArrivee;
            m_individu= individu;
    }
    
    public Temps getTempsArrivee(){
        return m_tempsArrivee;
    }

    public Individu getPieton(){
        return m_individu;
    }
    public void setTempsArriver(Temps p_arrive){
        m_tempsArrivee = p_arrive;
    }
    
}