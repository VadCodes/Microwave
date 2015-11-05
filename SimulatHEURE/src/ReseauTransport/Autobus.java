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
    /*la source a un attribut mutable capaciteMax defaut a 50
     *elle cree tous ces autobus avec cette capacite et on ne 
     *peut plus la changer pour l'autobus
     */
    private final int m_capaciteMax;
    private int m_nbPassagers = 0;
    private String m_id;
    private Temps m_tempsApparition;
    private ListIterator<PaireArretTrajet> m_iterateur; //jsais pas comment l'initialiser à NULL
    private Boolean m_estSurArret;
    private PaireArretTrajet m_paireActuelle;
    
    public Autobus(Emplacement emplacementActuel, int capaciteMax, String id,
                    Temps tempsApparition, Boolean estSurArret){
        
        m_emplacementActuel = emplacementActuel;
        m_capaciteMax = capaciteMax; 
        m_id = id;
        m_tempsApparition = tempsApparition;
        m_estSurArret = estSurArret;
    }
    
    public Position getPosition(){
        return m_emplacementActuel.calculPosition();
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
        m_paireActuelle = listeArretTrajet.getFirst();
    }
    
    public void setID(String id){
        m_id = id;
    }
    
    public String getID(){
        return m_id;
    }

    
}