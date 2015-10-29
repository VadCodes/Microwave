/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauTransport;

/**
 *
 * @author louis
 */
import Utilitaire.Temps;

public class TempsArriveeAutobus {
    private Temps m_tempsArrivee;
    private Autobus m_autobus;
    
    public TempsArriveeAutobus(Temps tempsArrivee, Autobus autobus){
            m_tempsArrivee = tempsArrivee;
            m_autobus = autobus;
    }
    
    public Temps getTempsArrivee(){
        return m_tempsArrivee;
    }

    public Autobus getAutobus(){
        return m_autobus;
    }
    
}