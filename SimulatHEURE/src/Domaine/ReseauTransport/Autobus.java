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
    private boolean m_asTerminer = false;
    private Boolean m_boucle = false;
    
    public Autobus(Emplacement emplacementActuel, int capaciteMax, String id,
                    Temps tempsApparition, Boolean estSurArret){
        
        m_emplacementActuel = emplacementActuel;
        m_capaciteMax = capaciteMax; 
        m_id = id;
        m_tempsApparition = tempsApparition;
        m_estSurArret = estSurArret;
    }
    public boolean asTerminer(){
        return m_asTerminer;
    }
    public void miseAJourEmplacement(Temps deltatT){
        /*
         * On calcul l'avancement en pourcentage sur un troncon.
         */
        float pourcentageInitiale = 0;
        Temps tempsTransit;
        if(m_emplacementActuel.estSurTroncon()){
             tempsTransit= m_emplacementActuel.getTroncon().getTempsTransitAutobus();
             pourcentageInitiale = m_emplacementActuel.getPourcentageParcouru();
        }
        else{
            Troncon troncon = m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel);
            m_emplacementActuel.setTroncon(troncon);
            m_emplacementActuel.setPourcentageParcouru(pourcentageInitiale);
            tempsTransit= m_emplacementActuel.getTroncon().getTempsTransitAutobus();
        }
        float pourcentage = (float)(pourcentageInitiale  + deltatT.getTemps()/tempsTransit.getTemps());
        m_emplacementActuel.setPourcentageParcouru(pourcentage);
        /*
         * Si l'autobus est sur le troncon final du trajet 
         * Et que son pourcentage parcouru est plus grand
         *
         * On augmente l'itérateur.
         */
        boolean changementiterator = false;
        boolean changementPaireArretTrajetAVerifier;
        if(!m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()){
            changementPaireArretTrajetAVerifier = m_emplacementActuel.getTroncon().getDestination().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getIntersection());
        }
        else{
             changementPaireArretTrajetAVerifier = m_emplacementActuel.getTroncon().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getTroncon());
        }
        if(changementPaireArretTrajetAVerifier){
            changementiterator = changerPaireArretTrajet(pourcentageInitiale, deltatT);
        }
        
        if(pourcentage > 1 && !changementiterator){
            pourcentage = 1;
            m_emplacementActuel.setPourcentageParcouru(pourcentage);
            float tempsParcourirResteTroncon = (float)((1 - pourcentageInitiale)*tempsTransit.getTemps());
            if(m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel) != null){
                Troncon troncon = m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel);
                m_emplacementActuel.setTroncon(troncon);
                pourcentage = 0;
                m_emplacementActuel.setPourcentageParcouru(pourcentage);
            }
            else{

            }
            Temps tmp = new Temps(deltatT.getTemps() - tempsParcourirResteTroncon);
            miseAJourEmplacement(tmp);
        }
    }
     
    private boolean changerPaireArretTrajet(double p_pourcentageInitiale, Temps p_deltatT){
        float pourcentageFinal;
        if (m_emplacementActuel.estSurTroncon() && m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()){
             pourcentageFinal= m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru();
        }
         else{
            if(m_emplacementActuel.getTroncon().getDestination().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getIntersection())){
              pourcentageFinal = 1;
            }
            else return false;
        }     
        /*
         * Si le pourcentage parcouru est plus grand on change l'itérateur.
         */
        if(m_emplacementActuel.getPourcentageParcouru() > pourcentageFinal){
            Emplacement emplacement1 = m_paireActuelle.getTrajet().getEmplacementFinal();
            m_paireActuelle = m_iterateur.next();
            /*
             * Si le prochain trajet est null ça veut dire que le circuit est terminer et que l'autobus a finis son travail. 
             */
            if(m_paireActuelle.getTrajet() == null){
                if(this.getBoucle()){
                    m_iterateur = m_list.listIterator();
                    m_paireActuelle = m_iterateur.next();
                    float tempsParcourirResteTroncon = (float)((pourcentageFinal - p_pourcentageInitiale)*m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                     Temps tmp = new  Temps(p_deltatT.getTemps() - tempsParcourirResteTroncon);
                    if(!m_paireActuelle.getTrajet().getEmplacementInitial().estSurTroncon()){
                        m_emplacementActuel.setPourcentageParcouru(0.0f);
                        m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getListeTroncons().getFirst());
                    }
                    else{
                         m_emplacementActuel.setPourcentageParcouru(m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru());
                        m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getEmplacementInitial().getTroncon());
                    }
                    miseAJourEmplacement(tmp);
                    return true;
                }
                else{
                    m_asTerminer = true;
                    float tempsParcourirResteTroncon = (float)((pourcentageFinal - p_pourcentageInitiale)*m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                    m_emplacementActuel.copy(m_paireActuelle.getArret().getEmplacement());
                    m_paireActuelle.getArret().ajouterAutobus(new Temps(tempsParcourirResteTroncon) , this);
                    return true;
                }
            }
            else{
                float tempsParcourirResteTroncon = (float)((pourcentageFinal - p_pourcentageInitiale)*m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                m_paireActuelle.getArret().ajouterAutobus(new Temps(tempsParcourirResteTroncon) , this);
                if(m_paireActuelle.getTrajet().getEmplacementInitial().estSurTroncon()){
                    m_emplacementActuel.setPourcentageParcouru(m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru());
                    m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getEmplacementInitial().getTroncon());
                }
                else{
                    m_emplacementActuel.setPourcentageParcouru(0.0f);
                    m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getListeTroncons().getFirst());
                }
                Temps tmp = new  Temps(p_deltatT.getTemps() - tempsParcourirResteTroncon);
                miseAJourEmplacement(tmp);
                return true;
            }
        }
        return false;
    }
    
    public Point2D.Float getPosition(Float p_echelle){
        return m_emplacementActuel.calculPosition(p_echelle);
    }
    
    public ListIterator<PaireArretTrajet> getIterateur(){
        return m_iterateur;
    }
    
    public void incrementerIterateur(){
        m_paireActuelle = m_iterateur.next();            
    }

    public void assignerTrajet(LinkedList<PaireArretTrajet> listeArretTrajet, Boolean boucle){
        //Assigne l'iterateur a la premiere paire du trajet du circuit
        m_list = listeArretTrajet;
        m_boucle = boucle;
        m_iterateur = listeArretTrajet.listIterator();
      for (ListIterator<PaireArretTrajet> paires =listeArretTrajet.listIterator(); paires.hasNext();) {
          m_paireActuelle = paires.next();
          m_iterateur.next();
          if(m_paireActuelle .getTrajet()!=null){
             for (ListIterator<Troncon> troncons = m_paireActuelle .getTrajet().getListeTroncons().listIterator(); troncons.hasNext();) {
            Troncon troncon = troncons.next();
            if(m_emplacementActuel.estSurTroncon()){
                if(troncon.equals(m_emplacementActuel.getTroncon())){
                    return;
                }
            }
             }
            }
        }
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
    private Boolean getBoucle(){
        return m_boucle;
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
    