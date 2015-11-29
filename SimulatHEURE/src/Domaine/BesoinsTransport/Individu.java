/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;
import Domaine.ReseauRoutier.Emplacement;
import java.util.ListIterator;
import Domaine.Utilitaire.Temps;
import Domaine.ReseauRoutier.Troncon;
import Domaine.ReseauTransport.Autobus;
import Domaine.ReseauTransport.Circuit;
import Domaine.Statistiques.StatistiqueBesoin;

/**
 *
 * @author vadimcote
 */
public class Individu {
    private ListIterator<PaireParcours> m_iterateurItineraire;
    private Emplacement m_emplacementActuel;
    private Itineraire m_itineraire;
    private Temps m_tempsApparition;
    private boolean m_asTerminer = false;
    private Boolean m_estEnBus = false;
    private PaireParcours m_paireActuelle;
    private Boolean m_estSurArret = false;
    private double m_tempsDeVie = 0;
    private StatistiqueBesoin m_stat;
    public Individu(Emplacement p_emplacementActuel, Itineraire p_itineraire, Temps p_tempsApparition, Boolean estSurArret,
            StatistiqueBesoin p_stat){
        m_stat = p_stat;
        m_emplacementActuel = p_emplacementActuel;
        m_itineraire = p_itineraire;
        m_tempsApparition = p_tempsApparition;        
        m_iterateurItineraire = m_itineraire.getListPaireParcours().listIterator(0);
        m_paireActuelle = m_itineraire.getListPaireParcours().getFirst();
        m_estSurArret = estSurArret;
    }
    
    public void incrementeItineraire(){
        m_paireActuelle = m_iterateurItineraire.next();
    }
    
    public Emplacement getEmplacementActuel(){
        return m_emplacementActuel;
    }
    public void miseAJourIndividu(Temps deltatT) {
        Temps tmp = new Temps(m_tempsApparition.getTemps() - deltatT.getTemps());
        if (tmp.getTemps() < 0) {
            Temps nouveauDeltatT = new Temps(deltatT.getTemps() - m_tempsApparition.getTemps());
            miseAJourEmplacement(nouveauDeltatT);
            m_tempsApparition = new Temps(0);
        } else {
            m_tempsApparition = tmp;
        }
    }  
    public void setTempsApparition(Temps p_temps){
        m_tempsApparition = p_temps;
    }
            
            
            
    
    public void miseAJourEmplacement(Temps deltatT) {
        /*
         * On calcul l'avancement en pourcentage sur un troncon.
         */
        float pourcentageInitiale = 0;
        Temps tempsTransit;
         if(m_estEnBus){
             estDansBus();
             if(!m_estEnBus){
                 miseAJourIndividu(deltatT);
                 return;
             }
         }
        if (m_emplacementActuel.estSurTroncon()) {
            tempsTransit = m_emplacementActuel.getTroncon().getTempsTransitPieton();
            pourcentageInitiale = m_emplacementActuel.getPourcentageParcouru();
        } else {
            Troncon troncon = m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel);
            m_emplacementActuel.setTroncon(troncon);
            m_emplacementActuel.setPourcentageParcouru(pourcentageInitiale);
            tempsTransit = m_emplacementActuel.getTroncon().getTempsTransitPieton();
        }
        float pourcentage = (float) (pourcentageInitiale + deltatT.getTemps() / tempsTransit.getTemps());
        m_emplacementActuel.setPourcentageParcouru(pourcentage);
        /*
         * Si le pieton est sur l'arret de son circuit il entre dans l'autobus jusqu'a l'arret final
         *
         * On augmente l'itérateur.
         */
        boolean changementiterator = false;
        boolean changementPaireParcoursAVerifier;
        changementPaireParcoursAVerifier = m_emplacementActuel.equals(m_paireActuelle.getParcoursBus().getArretFinal().getEmplacement());
        
        if (changementPaireParcoursAVerifier) {
            changementiterator = changerPaireParcoursBusTrajet(pourcentageInitiale, deltatT);
        }
        if (m_estSurArret ){
                pourcentage = m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getPourcentageParcouru();
                m_emplacementActuel.setPourcentageParcouru(pourcentage);
                m_paireActuelle.getParcoursBus().getArretDepart().incrementerNbreIndividu();
                m_paireActuelle.getParcoursBus().getArretDepart();
                
        }
    }
         private boolean changerPaireParcoursBusTrajet(double p_pourcentageInitiale, Temps p_deltatT) {
        float pourcentageFinal;
        if (m_emplacementActuel.estSurTroncon() && m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()) {
            pourcentageFinal = m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru();
        } else {
            if (m_emplacementActuel.getTroncon().getDestination().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getIntersection())) {
                pourcentageFinal = 1;
            } else {
                return false;
            }
        }
        /*
         * Si le pourcentage parcouru est plus grand on change l'itérateur.
         */
        if (m_emplacementActuel.getPourcentageParcouru() > pourcentageFinal) {
            Emplacement emplacement1 = m_paireActuelle.getTrajet().getEmplacementFinal();
            m_paireActuelle = m_iterateurItineraire.next();
            /*
             * Si le prochain trajet est null ça veut dire que le circuit est terminer et que l'autobus a finis son travail. 
             */
            if (m_paireActuelle.getTrajet() == null) {
                    m_asTerminer = true;
                    float tempsParcourirResteTroncon = (float) ((pourcentageFinal - p_pourcentageInitiale) * m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                     m_stat.miseAJourStat(new Temps(m_tempsDeVie - p_deltatT.getTemps() +tempsParcourirResteTroncon));
                    return true;

            } else {
                float tempsParcourirResteTroncon = (float) ((pourcentageFinal - p_pourcentageInitiale) * m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                if (m_estEnBus){
                    m_emplacementActuel.copy(m_paireActuelle.getParcoursBus().getArretFinal().getEmplacement());
                    m_estEnBus = false;
                    //calcule de mise a jour d'emplacement
                }
                else{
                     m_emplacementActuel.copy(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement());
                     Temps tempsArriveArret = new Temps( tempsParcourirResteTroncon);
                    m_estSurArret  = true;
                    m_paireActuelle.getParcoursBus().getArretDepart().ajouterPieton(tempsArriveArret, this);
                }
                Temps tmp = new Temps(p_deltatT.getTemps() - tempsParcourirResteTroncon);
                miseAJourEmplacement(tmp);
                return true;
            }
        }
        return false;
    }
       public Circuit getProchaineCircuit(){
           return m_paireActuelle.getParcoursBus().getCircuit();
       }
       public void setIndividuEstDansBus(boolean b, Autobus p_bus){
           m_estEnBus = b;
           if(b){
           PairePietonBus paire = new PairePietonBus(this,p_bus );
           m_paireActuelle.getParcoursBus().getArretFinal().addPietonAttenteDeSortirBus(paire);
           m_estSurArret = false;
           }
       }
       private void estDansBus(){
           m_paireActuelle.getParcoursBus().getArretFinal().miseAJourArret();
       }
            
}
