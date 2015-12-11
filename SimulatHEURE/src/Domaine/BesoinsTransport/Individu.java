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
    private double temp_iteration = 0;
    private StatistiqueBesoin m_stat;
    private Temps m_tempsArriverDeTrop = new Temps(0);
    public float RAYON = 3;
    private String m_nom;

    public Individu(Emplacement p_emplacementActuel, Itineraire p_itineraire, Temps p_tempsApparition, Boolean estSurArret,
            StatistiqueBesoin p_stat,String p_nom) {
        m_nom = p_nom;
        m_stat = p_stat;
        m_emplacementActuel = p_emplacementActuel;
        m_itineraire = p_itineraire;
        m_tempsApparition = p_tempsApparition;
        m_iterateurItineraire = m_itineraire.getListPaireParcours().listIterator(0);
        m_iterateurItineraire.next();
        m_paireActuelle = m_itineraire.getListPaireParcours().getFirst();
        m_estSurArret = estSurArret;
    }

    public void incrementeItineraire() {
        m_paireActuelle = m_iterateurItineraire.next();
    }

    public Emplacement getEmplacementActuel() {
        return m_emplacementActuel;
    }

    public void miseAJourIndividu(Temps deltatT) {
        Temps tmp = new Temps(m_tempsApparition.getTemps() - deltatT.getTemps());
        if (tmp.getTemps() < 0) {
            temp_iteration = deltatT.getTemps();
            Temps nouveauDeltatT = new Temps(deltatT.getTemps() - m_tempsApparition.getTemps());
            m_tempsDeVie += nouveauDeltatT.getTemps();
            miseAJourEmplacement(nouveauDeltatT);
            m_tempsApparition = new Temps(0);
        } else {
            m_tempsApparition = tmp;
        }
    }

   // public void setTempsApparition(Temps p_temps) {
        //m_tempsApparition = p_temps;
    //}

    public void miseAJourEmplacement(Temps deltatT) {
        /*
         * On calcul l'avancement en pourcentage sur un troncon.
         */
        if(!m_estSurArret && !m_estEnBus && !m_emplacementActuel.estSurTroncon()){
            if(m_paireActuelle.getTrajet() != null){
                m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getListeTroncons().getFirst());
                m_emplacementActuel.setPourcentageParcouru(0);
                m_emplacementActuel.setEstSurTroncon(true);
            }
        }
        float pourcentageInitiale = 0;
        Temps tempsTransit;
        if (m_estSurArret) {
            if (m_paireActuelle.getParcoursBus() != null) {
                if (!m_emplacementActuel.estSurTroncon() && !m_paireActuelle.getParcoursBus().getArretFinal().getEmplacement().estSurTroncon()) {
                    if (m_emplacementActuel.getIntersection().equals(m_paireActuelle.getParcoursBus().getArretFinal().getEmplacement().getIntersection())) {
                        m_estSurArret = false;
                        if (m_iterateurItineraire.hasNext()) {
                            m_paireActuelle = m_iterateurItineraire.next();
                            if (m_paireActuelle.getTrajet() != null) {
                                m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel));
                                m_emplacementActuel.setEstSurTroncon(true);
                                m_emplacementActuel.setPourcentageParcouru(0.0f);
                            } else {
                                m_estSurArret = true;
                                m_paireActuelle.getParcoursBus().getArretDepart().ajouterPieton(new Temps(Double.MAX_VALUE), this);
                                m_paireActuelle.getParcoursBus().getArretDepart().miseAJourArret();
                                return;
                            }
                        } else {
                            m_asTerminer = true;
                            m_stat.miseAJourStat(new Temps(m_tempsDeVie - m_tempsArriverDeTrop.getTemps()));
                            return;
                        }
                    }
                }
                else if (m_emplacementActuel.estSurTroncon() && m_paireActuelle.getParcoursBus().getArretFinal().getEmplacement().estSurTroncon()) {
                    if (m_emplacementActuel.getTroncon().equals(m_paireActuelle.getParcoursBus().getArretFinal().getEmplacement().getTroncon())) {
                        if (m_emplacementActuel.getPourcentageParcouru() == m_paireActuelle.getParcoursBus().getArretFinal().getEmplacement().getPourcentageParcouru()) {
                            m_estSurArret = false;
                            if (m_iterateurItineraire.hasNext()) {
                                m_paireActuelle = m_iterateurItineraire.next();
                                if (m_paireActuelle.getTrajet() != null) {
                                    m_emplacementActuel.copy(m_paireActuelle.getTrajet().getEmplacementInitial());
                                } else {
                                    m_estSurArret = true;
                                    m_paireActuelle.getParcoursBus().getArretDepart().ajouterPieton(new Temps(Double.MAX_VALUE), this);
                                    m_paireActuelle.getParcoursBus().getArretDepart().miseAJourArret();
                                    return;
                                }
                            } else {
                                m_asTerminer = true;
                                m_stat.miseAJourStat(new Temps(m_tempsDeVie - m_tempsArriverDeTrop.getTemps()));
                                return;
                            }
                        }
                    }
                }
            }
            if (m_estSurArret) {
                m_paireActuelle.getParcoursBus().getArretDepart().miseAJourArret();
                if(m_estSurArret){
                    return;
                }
            }
        }
        if (m_estEnBus) {
            m_paireActuelle.getParcoursBus().getArretFinal().miseAJourArret();
            if(m_estEnBus){
                return;
            }
            miseAJourEmplacement(m_tempsArriverDeTrop);
            return;
            
        }
        if (m_paireActuelle.getParcoursBus() != null) {
            if (m_emplacementActuel.estSurTroncon() && m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().estSurTroncon()) {
                if (m_emplacementActuel.getTroncon().equals(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getTroncon())) {
                    if (m_emplacementActuel.getPourcentageParcouru() == m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getPourcentageParcouru()) {
                        m_estSurArret = true;
                        m_paireActuelle.getParcoursBus().getArretDepart().ajouterPieton(new Temps(Double.MAX_VALUE), this);
                        m_paireActuelle.getParcoursBus().getArretDepart().miseAJourArret();
                        return;
                    }
                }
            }
            if (!m_emplacementActuel.estSurTroncon() && !m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().estSurTroncon()) {
                if (m_emplacementActuel.getIntersection().equals(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getIntersection())) {
                    m_estSurArret = true;
                    m_paireActuelle.getParcoursBus().getArretDepart().ajouterPieton(new Temps(Double.MAX_VALUE), this);
                    m_paireActuelle.getParcoursBus().getArretDepart().miseAJourArret();
                    return;
                }
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
        float pourcentage = (float) (pourcentageInitiale + (deltatT.getTemps()) / tempsTransit.getTemps());
        //m_tempsArriverDeTrop = new Temps(0);
        m_emplacementActuel.setPourcentageParcouru(pourcentage);
        /*
         * Si le pieton est sur l'arret de son circuit il entre dans l'autobus jusqu'a l'arret final
         *
         * On augmente l'itérateur.
         */
        if (m_paireActuelle.getParcoursBus() != null) {
            float pourcentageFinal;
            float pourcentageArret1 = m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getPourcentageParcouru();
            if (m_emplacementActuel.estSurTroncon() && m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()) {
                if (m_emplacementActuel.getTroncon().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getTroncon())) {
                    if (m_emplacementActuel.getPourcentageParcouru() >= m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()) {
                        pourcentageFinal = m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru();
                        m_emplacementActuel.copy(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement());
                        float tempsParcourirDeTrop = (float) ((pourcentage - m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()) * m_emplacementActuel.getTroncon().getTempsTransitPieton().getTemps());m_estSurArret = true;
                        m_paireActuelle.getParcoursBus().getArretDepart().ajouterPieton(new Temps(tempsParcourirDeTrop), this);
                        m_paireActuelle.getParcoursBus().getArretDepart().miseAJourArret();
                        return;
                    }
                }
            }
            if (m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().estSurTroncon()) {
                if (m_emplacementActuel.getTroncon().equals(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getTroncon())) {
                    if (m_emplacementActuel.getPourcentageParcouru() >= m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getPourcentageParcouru()) {
                        m_emplacementActuel.copy(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement());
                        float tempsParcourirDeTrop = (float) ((pourcentage - m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()) * m_emplacementActuel.getTroncon().getTempsTransitPieton().getTemps());
                        m_estSurArret = true;
                        m_paireActuelle.getParcoursBus().getArretDepart().ajouterPieton(new Temps(tempsParcourirDeTrop), this);
                        m_paireActuelle.getParcoursBus().getArretDepart().miseAJourArret();
                        return;
                    }
                }
            } else if (m_emplacementActuel.getTroncon().getDestination().equals(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement().getIntersection())) {
                if (m_emplacementActuel.getPourcentageParcouru() >= 1) {
                    float tempsParcourirDeTrop = (float) ((pourcentage - 1) * m_emplacementActuel.getTroncon().getTempsTransitPieton().getTemps());
                    m_emplacementActuel.copy(m_paireActuelle.getParcoursBus().getArretDepart().getEmplacement());
                    m_estSurArret = true;
                    m_paireActuelle.getParcoursBus().getArretDepart().ajouterPieton(new Temps(tempsParcourirDeTrop), this);
                    m_paireActuelle.getParcoursBus().getArretDepart().miseAJourArret();
                    return;
                }
            }
        }
        
        else{
            if (m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()) {
                if (m_emplacementActuel.getTroncon().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getTroncon())) {
                    if (pourcentage >= m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()) {
                        m_asTerminer = true;
                        float tempsParcourirDeTrop = (float) ((pourcentage - m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()) * m_emplacementActuel.getTroncon().getTempsTransitPieton().getTemps());
                        m_emplacementActuel.copy(m_paireActuelle.getTrajet().getEmplacementFinal());
                        m_stat.miseAJourStat(new Temps(m_tempsDeVie - tempsParcourirDeTrop));
                        return;
                    }
                } 

            }
            else if (m_emplacementActuel.getTroncon().getDestination().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getIntersection())) {
                    if (pourcentage >= 1) {
                        float tempsParcourirDeTrop = (float) ((pourcentage - 1) * m_emplacementActuel.getTroncon().getTempsTransitPieton().getTemps());
                        m_asTerminer = true;
                        m_emplacementActuel.copy(m_paireActuelle.getTrajet().getEmplacementFinal());
                        m_stat.miseAJourStat(new Temps(m_tempsDeVie - tempsParcourirDeTrop));
                        return;
                    }
                }
        }
        if (pourcentage > 1) {
            pourcentage = 1;
            m_emplacementActuel.setPourcentageParcouru(pourcentage);
            float tempsParcourirResteTroncon = (float) ((1 - pourcentageInitiale) * tempsTransit.getTemps());
            float tempsParcourirDeTrop = (float) ((pourcentage - 1) * m_emplacementActuel.getTroncon().getTempsTransitPieton().getTemps());
            if (m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel) != null) {
                Troncon troncon = m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel);
                m_emplacementActuel.setTroncon(troncon);
                pourcentage = 0;
                m_emplacementActuel.setPourcentageParcouru(pourcentage);
            }
            Temps tmp = new Temps(deltatT.getTemps() - tempsParcourirResteTroncon);
            miseAJourEmplacement(tmp);
        }
    }

    public Circuit getProchaineCircuit() {
        return m_paireActuelle.getParcoursBus().getCircuit();
    }

    public void setIndividuEstDansBus(boolean b, Autobus p_bus, Temps p_temps) {
        m_estEnBus = b;
        if (b) {
            PairePietonBus paire = new PairePietonBus(this, p_bus);
            m_paireActuelle.getParcoursBus().getArretFinal().addPietonAttenteDeSortirBus(paire);
            m_estSurArret = false;
        } else {
            m_estSurArret = true;
            m_emplacementActuel.copy(m_paireActuelle.getParcoursBus().getArretFinal().getEmplacement());
            m_tempsArriverDeTrop = p_temps;
        }
    }

    public boolean estEnBus() {
        return m_estEnBus;
    }
    
    public boolean estSurArret() {
        return m_estSurArret;
    }

    // public void estDansBus() {
    //   m_paireActuelle.getParcoursBus().getArretFinal().miseAJourArret();
    //}

    boolean asTerminer() {
      return m_asTerminer;
    }

    public String getNom() {
        return m_nom;
    }
}
