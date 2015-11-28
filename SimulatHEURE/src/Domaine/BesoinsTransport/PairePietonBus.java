/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;

import Domaine.ReseauTransport.Autobus;

/**
 *
 * @author ns222
 */
public class PairePietonBus {
    private Individu m_pieton;
    private Autobus m_bus;
    PairePietonBus(Individu p_individu, Autobus p_bus){
        m_pieton = p_individu;
        m_bus = p_bus;
    }
    public Individu getPieton(){
        return m_pieton;
    }
    public Autobus getBus(){
        return m_bus;
    }
    
}
