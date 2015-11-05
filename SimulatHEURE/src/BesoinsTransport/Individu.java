/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BesoinsTransport;
import ReseauRoutier.Emplacement;
import java.util.ListIterator;
import Utilitaire.Temps;

/**
 *
 * @author vadimcote
 */
public class Individu {
    private ListIterator<PaireParcours> m_iterateurItineraire;
    private Emplacement m_emplacementActuel;
    private Itineraire m_itineraire;
    private Temps m_tempsApparition;
    private Boolean estSurParcoursPieton;
    private PaireParcours m_paireActuelle;
    public Individu(Emplacement p_emplacementActuel, Itineraire p_itineraire, Temps p_temps){
        m_emplacementActuel = p_emplacementActuel;
        m_itineraire = p_itineraire;
        m_tempsApparition = p_temps;
        
        m_iterateurItineraire = m_itineraire.getListPaireParcours().listIterator(0);
        m_paireActuelle = m_itineraire.getListPaireParcours().getFirst();
    }
    public void incrementeItineraire(){
        m_paireActuelle = m_iterateurItineraire.next();
    }
    
    
    
    
            
    
}
