/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BesoinsTransport;
import ReseauRoutier.Emplacement;
import Utilitaire.Distribution;
import Utilitaire.Temps;

/**
 *
 * @author vadimcote
 */
public class SourceIndividus {
    private Temps m_tempsInitial;
    private Distribution m_distributionFrequence;
    private Emplacement m_emplacementDeLaSource;
    private Temps m_frequence;
    
    
    
    public SourceIndividus(Temps p_tempsInitial, Distribution p_distributionFrequence, Emplacement p_emplacementDeLaSource){
        m_tempsInitial = p_tempsInitial;
        m_distributionFrequence = p_distributionFrequence;
        m_emplacementDeLaSource = p_emplacementDeLaSource;
    }

    public void initSourceIndividu(){
        m_frequence = m_distributionFrequence.pigerTemps();

    }
    
}
