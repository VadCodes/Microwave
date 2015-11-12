/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;

/**
 *
 * @author louis
 */
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Intersection;
import Domaine.Utilitaire.Temps;
import java.awt.geom.Point2D;
import java.util.ListIterator;
import java.util.LinkedList;
import Domaine.ReseauRoutier.Troncon;

public class Autobus {
   
    /*la source a un attribut mutable capaciteMax defaut a 50
     *elle cree tous ces autobus avec cette capacite et on ne 
     *peut plus la changer pour l'autobus
     */
     private Emplacement m_emplacementActuel;
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
    
    public void miseAJourEmplacement(Temps deltatT){
         float pourcentage = m_emplacementActuel.getPourcentageParcouru();
        Temps tempsTransit = m_emplacementActuel.getTroncon().getTempsTransitAutobus();
        pourcentage += deltatT.getTemps()/tempsTransit.getTemps();
        m_emplacementActuel.setPourcentageParcouru(pourcentage);
        if(pourcentage > 1){
            pourcentage = 1;
            m_emplacementActuel.setPourcentageParcouru(pourcentage);
            float pourcentage1 = m_emplacementActuel.getPourcentageParcouru();
            float tempsParcourirResteTroncon = (float)((1 - pourcentage1)*tempsTransit.getTemps());
            if(m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel) != null){
                Troncon troncon = m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel);
                m_emplacementActuel.setTroncon(troncon);
                pourcentage = 0;
                m_emplacementActuel.setPourcentageParcouru(pourcentage);
            }
            else{
                m_paireActuelle = m_iterateur.next();
                m_paireActuelle.getArret().ajouterAutobus(new Temps(tempsParcourirResteTroncon) , this);
                m_emplacementActuel = m_paireActuelle.getTrajet().getEmplacementInitial();
            }
            Temps tmp = new  Temps(deltatT.getTemps() - tempsParcourirResteTroncon);
            miseAJourEmplacement(tmp);
        }
    }
    
    
    public Point2D.Float getPosition(){
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