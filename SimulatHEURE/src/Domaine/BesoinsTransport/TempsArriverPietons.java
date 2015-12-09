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
    private Temps m_tempsDeTrop;
    private Individu m_individu;
    
    public TempsArriverPietons(Temps tempsArrivee, Individu individu){
            m_tempsDeTrop = tempsArrivee;
            m_individu= individu;
    }
    
    public Temps getTempsDeTrop(){
        return m_tempsDeTrop;
    }

    public Individu getPieton(){
        return m_individu;
    }
    public void setTempsArriver(Temps p_arrive){
        m_tempsDeTrop = p_arrive;
    }
    
}