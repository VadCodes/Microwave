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
    private LinkedList<PaireArretTrajet> m_list;
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
        /*
        * On calcul l'avancement en pourcentage sur un troncon.
        */
         float pourcentageInitiale = m_emplacementActuel.getPourcentageParcouru();
        Temps tempsTransit = m_emplacementActuel.getTroncon().getTempsTransitAutobus();
        float pourcentage = (float)(pourcentageInitiale  + deltatT.getTemps()/tempsTransit.getTemps());
        m_emplacementActuel.setPourcentageParcouru(pourcentage);
        System.out.println("PourcentageInitial :");
         System.out.println(pourcentageInitiale);
         System.out.println("Pourcentage après calcul :");
         System.out.println(pourcentage);
        /*
        *Si l'autobus est sur le troncon final du trajet 
        * Et que son pourcentage parcouru est plus grand
        *
        * On augmente l'itérateur.
         */
        boolean changementiterator = false;
        if(m_emplacementActuel.getTroncon().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getTroncon())){
            float pourcentageFinal = m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru();
            if(pourcentage > pourcentageFinal){
                changementiterator = true;
                System.out.println("On devrait changer la paire !!");
                Emplacement emplacement1 = m_paireActuelle.getTrajet().getEmplacementFinal();
                m_paireActuelle = m_iterateur.next();
                Emplacement emplacement2 = m_paireActuelle.getTrajet().getEmplacementFinal();
                boolean bo = emplacement1.equals(emplacement2);
                System.out.println(bo);
                 float tempsParcourirResteTroncon = (float)((pourcentageFinal - pourcentageInitiale)*tempsTransit.getTemps());
                 System.out.println("tempsParcourirResteTroncon :");
                 System.out.println(tempsParcourirResteTroncon);
                m_paireActuelle.getArret().ajouterAutobus(new Temps(tempsParcourirResteTroncon) , this);
                m_emplacementActuel.setPourcentageParcouru(m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru());
                m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getEmplacementInitial().getTroncon());
                Temps tmp = new  Temps(deltatT.getTemps() - tempsParcourirResteTroncon);
                miseAJourEmplacement(tmp);
            }
        }
        if(pourcentage > 1 && !changementiterator){
            pourcentage = 1;
            m_emplacementActuel.setPourcentageParcouru(pourcentage);
            float tempsParcourirResteTroncon = (float)((1 - pourcentageInitiale)*tempsTransit.getTemps());
            System.out.println("On change de troncon seulement");
            System.out.println("tempsParcourirResteTroncon :");
                 System.out.println(tempsParcourirResteTroncon);
            if(m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel) != null){
                Troncon troncon = m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel);
                m_emplacementActuel.setTroncon(troncon);
                pourcentage = 0;
                m_emplacementActuel.setPourcentageParcouru(pourcentage);
            }
            else{
                
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
        m_list = listeArretTrajet;
        m_iterateur = listeArretTrajet.listIterator();
        m_paireActuelle = m_iterateur.next();
    }
    
    public Emplacement getEmplacement(){
        return m_emplacementActuel;
    }
    public void setID(String id){
        m_id = id;
    }
    
    public String getID(){
        return m_id;
    }
    private boolean boucleOuPas(){
        if(m_list.getFirst().getArret().equals(m_list.getLast().getArret())){
      
        }
          return true;
    }
    public void miseAJourAutobus (Temps deltatT){
        Temps tmp = new Temps(m_tempsApparition.getTemps() - deltatT.getTemps());
         if (tmp.getTemps() < 0){
             Temps nouveauDeltatT = new Temps(deltatT.getTemps() - m_tempsApparition.getTemps());
             miseAJourEmplacement(nouveauDeltatT);
             m_tempsApparition = new Temps(0);
         }
         else{
             m_tempsApparition = tmp;
         }
    }
            
            
}
    