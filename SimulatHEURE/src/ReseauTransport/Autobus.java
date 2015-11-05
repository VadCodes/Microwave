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
import ReseauRoutier.Emplacement;
import ReseauRoutier.Position;
import Utilitaire.Temps;
import java.util.ListIterator;
import java.util.LinkedList;

public class Autobus {
    private Emplacement m_emplacementActuel;
    private int m_capaciteMax;
    private int m_nbPassagers;
    private String m_id;
    private Temps m_tempsApparition;
    private ListIterator<PaireArretTrajet> m_iterateur;
    private Boolean m_estSurArret;
    private PaireArretTrajet m_paireActuelle;
    
    public Autobus(){
        //TODO
    }
    
    public Position getPosition(){
        return m_emplacementActuel.getPosition();
    }
    
    public ListIterator<PaireArretTrajet> getIterateur(){
        return m_iterateur;
    }
    
    public void incrementerIterateur(){
        m_paireActuelle = m_iterateur.next();            
    }

    public void assignerTrajet(LinkedList<PaireArretTrajet> listeArretTrajet){
        //Assigne l'iterateur a la premiere paire du trajet du circuit
        m_iterateur = listeArretTrajet.listIterator(0);
    }
    
}