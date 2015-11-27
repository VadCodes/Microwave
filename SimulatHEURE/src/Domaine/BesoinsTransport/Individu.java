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
    private Boolean m_estSurArret = false;
    public Individu(Emplacement p_emplacementActuel, Itineraire p_itineraire, Temps p_tempsApparition, Boolean estSurArret){
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
    
    public void miseAJourEmplacement(Temps deltatT) {
        /*
         * On calcul l'avancement en pourcentage sur un troncon.
         */
        float pourcentageInitiale = 0;
        Temps tempsTransit;
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
            //changementiterator = changerPaireArretTrajet(pourcentageInitiale, deltatT);
        }
        if (m_paireActuelle.getTrajet().getEmplacementFinal().equals(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement())){
                   
            if (pourcentage > m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getPourcentageParcouru() && !changementiterator) {
                pourcentage = m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getPourcentageParcouru();
                m_emplacementActuel.setPourcentageParcouru(pourcentage);
                m_paireActuelle.getParcoursBus().getArretDepart().incrementerNbreIndividu();
                m_estSurArret = true;
                Temps tempsArriveArret = new Temps(deltatT.getTemps());
        // TODO si le pieton arrive avant bus rentrer...
/*                if(tempsArriveArret)
                float tempsParcourirResteTroncon = (float) ((1 - pourcentageInitiale) * tempsTransit.getTemps());
       
               } else {

            }
            Temps tmp = new Temps(deltatT.getTemps() - tempsParcourirResteTroncon);
            miseAJourEmplacement(tmp);
            }
 */       }
    }
    }   
    
}
