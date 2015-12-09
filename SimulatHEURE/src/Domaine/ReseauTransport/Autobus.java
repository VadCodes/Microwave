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
    private Circuit m_circuit;
    private double tempsDeVie = 0;
    private String m_id;
    private Temps m_tempsApparition;
    private LinkedList<PaireArretTrajet> m_list;
    private ListIterator<PaireArretTrajet> m_iterateur; //jsais pas comment l'initialiser à NULL
    private double temp_iteration = 0;
    private PaireArretTrajet m_paireActuelle;
    private boolean m_asTerminer = false;
    private Boolean m_boucle = false;

    public final static float LARGEUR = 48;
    public final static float HAUTEUR = 16;

    public Autobus(Emplacement emplacementActuel, int capaciteMax, String id,
            Temps tempsApparition, Boolean estSurArret) {

        m_emplacementActuel = emplacementActuel;
        m_capaciteMax = capaciteMax;
        m_id = id;
        m_tempsApparition = tempsApparition;
    }

    public boolean asTerminer() {
        return m_asTerminer;
    }

    public void miseAJourEmplacement(Temps deltatT) {
        /*
         * On calcul l'avancement en pourcentage sur un troncon.
         */
        float pourcentageInitiale = 0;
        Temps tempsTransit;
        if (m_emplacementActuel.estSurTroncon() && m_paireActuelle.getArret().getEmplacement().estSurTroncon()) {
            if (m_emplacementActuel.getTroncon().equals(m_paireActuelle.getArret().getEmplacement().getTroncon())) {
                if (m_emplacementActuel.getPourcentageParcouru() == m_paireActuelle.getArret().getEmplacement().getPourcentageParcouru()) {
                    m_paireActuelle.getArret().ajouterAutobus(new Temps(0), this);
                }
            }
        }
        if (!m_emplacementActuel.estSurTroncon() && !m_paireActuelle.getArret().getEmplacement().estSurTroncon()) {
            if (m_emplacementActuel.getIntersection().equals(m_paireActuelle.getArret().getEmplacement().getIntersection())) {
                m_paireActuelle.getArret().ajouterAutobus(new Temps(0), this);
            }
        }
        if (m_emplacementActuel.estSurTroncon()) {
            tempsTransit = m_emplacementActuel.getTroncon().getTempsTransitAutobus();
            pourcentageInitiale = m_emplacementActuel.getPourcentageParcouru();
        } else {
            pourcentageInitiale = 0;
            Troncon troncon = m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel);
            m_emplacementActuel.setTroncon(troncon);
            m_emplacementActuel.setEstSurTroncon(true);
            m_emplacementActuel.setPourcentageParcouru(pourcentageInitiale);
            miseAJourEmplacement(deltatT);
            return;
        }
        float pourcentage = (float) (pourcentageInitiale + deltatT.getTemps() / tempsTransit.getTemps());
        m_emplacementActuel.setPourcentageParcouru(pourcentage);
        /*
         * Si l'autobus est sur le troncon final du trajet 
         * Et que son pourcentage parcouru est plus grand
         *
         * On augmente l'itérateur.
         */
        boolean changementiterator = false;
        boolean changementPaireArretTrajetAVerifier;
        if (!m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()) {
            changementPaireArretTrajetAVerifier = m_emplacementActuel.getTroncon().getDestination().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getIntersection());
        } else {
            changementPaireArretTrajetAVerifier = m_emplacementActuel.getTroncon().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getTroncon());
        }
        if (changementPaireArretTrajetAVerifier) {
            changementiterator = changerPaireArretTrajet(pourcentageInitiale, deltatT);
        }

        if (pourcentage > 1 && !changementiterator) {
            pourcentage = 1;
            m_emplacementActuel.setPourcentageParcouru(pourcentage);
            float tempsParcourirResteTroncon = (float) ((1 - pourcentageInitiale) * tempsTransit.getTemps());
            if (m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel) != null) {
                Troncon troncon = m_paireActuelle.getTrajet().getNextTroncon(m_emplacementActuel);
                m_emplacementActuel.setTroncon(troncon);
                pourcentage = 0;
                m_emplacementActuel.setPourcentageParcouru(pourcentage);
            } else {

            }
            Temps tmp = new Temps(deltatT.getTemps() - tempsParcourirResteTroncon);
            miseAJourEmplacement(tmp);
        }
    }

    private boolean changerPaireArretTrajet(double p_pourcentageInitiale, Temps p_deltatT) {
        float pourcentageFinal;
        if ((m_paireActuelle.getTrajet().getListeTroncons().getFirst().equals(m_paireActuelle.getTrajet().getListeTroncons().getLast())
                && m_paireActuelle.getTrajet().getListeTroncons().size() > 1)
                && (m_emplacementActuel.getPourcentageParcouru() < m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()
                || m_emplacementActuel.getPourcentageParcouru() >= m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru())) {
            return false;
        }
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
            m_paireActuelle = m_iterateur.next();
            /*
             * Si le prochain trajet est null ça veut dire que le circuit est terminer et que l'autobus a finis son travail. 
             */
            if (m_paireActuelle.getTrajet() == null) {
                if (this.getBoucle()) {
                    m_iterateur = m_list.listIterator();
                    m_paireActuelle = m_iterateur.next();
                    float tempsParcourDeTrop = (float)((m_emplacementActuel.getPourcentageParcouru() - pourcentageFinal )* m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                    float tempsParcourirResteTroncon = (float) ((pourcentageFinal - p_pourcentageInitiale) * m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                    Temps tmp = new Temps(p_deltatT.getTemps() - tempsParcourirResteTroncon);
                    if (!m_paireActuelle.getTrajet().getEmplacementInitial().estSurTroncon()) {
                        m_emplacementActuel.setPourcentageParcouru(0.0f);
                        m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getListeTroncons().getFirst());
                    } else {
                        m_emplacementActuel.setPourcentageParcouru(m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru());
                        m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getEmplacementInitial().getTroncon());
                    }
                    m_paireActuelle.getArret().ajouterAutobus(new Temps(tempsParcourDeTrop), this);
                    miseAJourEmplacement(tmp);
                    return true;
                } else {
                    m_asTerminer = true;
                    float tempsParcourDeTrop = (float)((m_emplacementActuel.getPourcentageParcouru() - pourcentageFinal )* m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                    float tempsParcourirResteTroncon = (float) ((pourcentageFinal - p_pourcentageInitiale) * m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                    m_emplacementActuel.copy(m_paireActuelle.getArret().getEmplacement());
                    m_paireActuelle.getArret().ajouterAutobus(new Temps(tempsParcourDeTrop), this);
                    return true;
                }
            } else {
                float tempsParcourDeTrop = (float)((m_emplacementActuel.getPourcentageParcouru() - pourcentageFinal )* m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                float tempsParcourirResteTroncon = (float) ((pourcentageFinal - p_pourcentageInitiale) * m_emplacementActuel.getTroncon().getTempsTransitAutobus().getTemps());
                m_paireActuelle.getArret().ajouterAutobus(new Temps(tempsParcourDeTrop), this);
                if (m_paireActuelle.getTrajet().getEmplacementInitial().estSurTroncon()) {
                    m_emplacementActuel.setPourcentageParcouru(m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru());
                    m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getEmplacementInitial().getTroncon());
                } else {
                    m_emplacementActuel.setPourcentageParcouru(0.0f);
                    m_emplacementActuel.setTroncon(m_paireActuelle.getTrajet().getListeTroncons().getFirst());
                }
                Temps tmp = new Temps(p_deltatT.getTemps() - tempsParcourirResteTroncon);
                miseAJourEmplacement(tmp);
                return true;
            }
        }
        return false;
    }

    public Circuit getCircuit(){
        return m_circuit;
    }
    public Point2D.Float getPosition(Float p_echelle) {
        return m_emplacementActuel.calculPosition(p_echelle);
    }

    public ListIterator<PaireArretTrajet> getIterateur() {
        return m_iterateur;
    }

    public void incrementerIterateur() {
        m_paireActuelle = m_iterateur.next();
    }

    public void assignerTrajet(LinkedList<PaireArretTrajet> listeArretTrajet, Boolean boucle, Circuit circuit) {
        m_circuit = circuit;
        //Assigne l'iterateur a la premiere paire du trajet du circuit
        m_list = listeArretTrajet;
        m_boucle = boucle;
        m_iterateur = listeArretTrajet.listIterator();
        for (ListIterator<PaireArretTrajet> paires = listeArretTrajet.listIterator(); paires.hasNext();) {
            m_paireActuelle = paires.next();
            m_iterateur.next();
            //Est sur arret
            if (m_emplacementActuel.estSurTroncon() && m_paireActuelle.getArret().getEmplacement().estSurTroncon()) {
                if (m_emplacementActuel.getTroncon().equals(m_paireActuelle.getArret().getEmplacement().getTroncon())) {
                    if (m_emplacementActuel.getPourcentageParcouru() == m_paireActuelle.getArret().getEmplacement().getPourcentageParcouru()) {
                        return;
                    }
                }
            }
            if (!m_emplacementActuel.estSurTroncon() && !m_paireActuelle.getArret().getEmplacement().estSurTroncon()) {
                if (m_emplacementActuel.getIntersection().equals(m_paireActuelle.getArret().getEmplacement().getIntersection())) {
                          return;
                }
            }
            if(!m_emplacementActuel.estSurTroncon() && !m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()){
                 if (m_emplacementActuel.getIntersection().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getIntersection())) {
                     if(paires.hasNext()){
                         m_paireActuelle = paires.next();
                         m_iterateur.next();
                         if(paires.hasNext()){
                             return;
                         }
                         else{
                              m_asTerminer = true;
                         return;
                         }
                         
                     }
                }
            }
              if(m_emplacementActuel.estSurTroncon() && m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()){
                 if (m_emplacementActuel.getTroncon().equals(m_paireActuelle.getTrajet().getEmplacementFinal().getTroncon())) {
                         if(paires.hasNext()){
                             return;
                         }
                         else{
                              m_asTerminer = true;
                         return;
                         }
                }
            }
            if (m_emplacementActuel.estSurTroncon()) {
                if (m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon() && m_paireActuelle.getTrajet().getEmplacementInitial().estSurTroncon()) {
                    boolean a = m_paireActuelle.getTrajet().getEmplacementFinal().getTroncon().equals(m_emplacementActuel.getTroncon());
                    boolean b = m_paireActuelle.getTrajet().getEmplacementInitial().getTroncon().equals(m_emplacementActuel.getTroncon());
                    if (!a && !b) {
                        for (ListIterator<Troncon> troncons = m_paireActuelle.getTrajet().getListeTroncons().listIterator(); troncons.hasNext();) {
                            Troncon troncon = troncons.next();
                            if (m_emplacementActuel.estSurTroncon()) {
                                if (troncon.equals(m_emplacementActuel.getTroncon())) {
                                    return;
                                }
                            }
                        }
                    } else if (a && b) {
                        if (m_emplacementActuel.getPourcentageParcouru() < m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()
                                && (m_emplacementActuel.getPourcentageParcouru() >= m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru())) {
                            if (m_paireActuelle.getTrajet().getListeTroncons().size() == 1) {
                                return;
                            }
                        } else {
                           // TODO source sur intersection
                        }
                        if (a) {
                            if (m_emplacementActuel.getPourcentageParcouru() < m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()) {
                                return;
                            }
                        }
                        if (b) {
                            if (m_emplacementActuel.getPourcentageParcouru() >= m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru()) {
                                return;
                            }
                        }
                    } else if (a) {
                        if (m_emplacementActuel.getPourcentageParcouru() < m_paireActuelle.getTrajet().getEmplacementFinal().getPourcentageParcouru()) {
                            return;
                        }
                    } else if (b) {
                        if (m_emplacementActuel.getPourcentageParcouru() >= m_paireActuelle.getTrajet().getEmplacementInitial().getPourcentageParcouru()) {
                            return;
                        }
                    }
                } else {
                    for (ListIterator<Troncon> troncons = m_paireActuelle.getTrajet().getListeTroncons().listIterator(); troncons.hasNext();) {
                        Troncon troncon = troncons.next();
                        if (m_emplacementActuel.estSurTroncon()) {
                            if (troncon.equals(m_emplacementActuel.getTroncon())) {
                                return;
                            }
                        } else {

                        }
                    }
                }
            } else {
                //est Sur intersection
                Intersection interMax1 = null;
                if (!m_paireActuelle.getTrajet().getEmplacementInitial().estSurTroncon()) {
                    interMax1 = m_paireActuelle.getTrajet().getEmplacementInitial().getIntersection();
                }
                Intersection interMax2 = null;
                if (!m_paireActuelle.getTrajet().getEmplacementFinal().estSurTroncon()) {
                    interMax2 = m_paireActuelle.getTrajet().getEmplacementFinal().getIntersection();
                }
                if (interMax1 != null) {
                    if (m_emplacementActuel.getIntersection().equals(interMax1)) {
                        return;
                    }
                }
                if (interMax2 != null) {
                    if (m_emplacementActuel.getIntersection().equals(interMax2)) {
                        return;
                    }
                }
                for (ListIterator<Troncon> troncons = m_paireActuelle.getTrajet().getListeTroncons().listIterator(); troncons.hasNext();) {
                    Troncon troncon = troncons.next();
                    if (m_emplacementActuel.getIntersection().equals(troncon.getDestination())) {
                        if (troncons.hasNext()) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public Emplacement getEmplacement() {
        return m_emplacementActuel;
    }

    public void setID(String id) {
        m_id = id;
    }

    public String getID() {
        return m_id;
    }

    private Boolean getBoucle() {
        return m_boucle;
    }

    public void miseAJourAutobus(Temps deltatT) {
        Temps tmp = new Temps(m_tempsApparition.getTemps() - deltatT.getTemps());
        if (tmp.getTemps() < 0) {
            tempsDeVie += deltatT.getTemps();
            //System.out.println("Bus:");
            //System.out.println(temp_iteration);
            temp_iteration = deltatT.getTemps();
            Temps nouveauDeltatT = new Temps(deltatT.getTemps() - m_tempsApparition.getTemps());
            miseAJourEmplacement(nouveauDeltatT);
            m_tempsApparition = new Temps(0);
        } else {
            m_tempsApparition = tmp;
        }
    }

    public void setPlusUnIndividu(){
        m_nbPassagers++;
    }
    public void setmoinsUnIndividu(){
        m_nbPassagers--;
    }
       public int getnbPassager(){
        return m_nbPassagers;
    }
    public int getCapaciteMax(){
        return m_capaciteMax;
    }
}
