/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilitaire;

/**
 *
 * @author vinny
 */
public class Distribution {
    private Temps m_tempsMinimum;
    private Temps m_tempsPlusFrequent;
    private Temps m_tempsMaximum;

    private double m_hauteur;
    private double m_penteGauche;
    private double m_aireGauche;
    private double m_penteDroite;
    private double m_aireDroite;
    
    private Temps m_tempsPige = new Temps(0);

    Distribution(Temps p_tempsMinimum, Temps p_tempsPlusFrequent, Temps p_tempsMaximum)
    {
        double tMin = p_tempsMinimum.getTemps();
        double tFreq = p_tempsPlusFrequent.getTemps();
        double tMax = p_tempsMaximum.getTemps();
        
        if (tMax < tMin)
        {
            // throw tabarnak
        }        
        else if (tFreq < tMin || tFreq > tMax)
        {
            // throw tabarnak
        }        
        
        m_tempsMinimum = p_tempsMinimum;
        m_tempsPlusFrequent = p_tempsPlusFrequent;
        m_tempsMaximum = p_tempsMaximum;
        
        m_hauteur = 2 / (tMax - tMin);
        m_penteGauche = m_hauteur / (tFreq - tMin);
        m_aireGauche = m_hauteur * (tFreq - tMin) / 2;
        m_penteDroite = m_hauteur / (tMax - tFreq);
        m_aireDroite = m_hauteur * (tMax - tFreq) / 2;
    }
    
    publicTemps pigerTemps(double p_nombre)
    {
        if (p_nombre < 0 || p_nombre > 1)
        {
            //throw tabarnak
        }
        else if (p_nombre < m_aireGauche)
        {
            
        }
        
    }
}
