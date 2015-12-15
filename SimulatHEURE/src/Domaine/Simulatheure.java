package Domaine;

import Domaine.ReseauRoutier.*;
import Domaine.ReseauTransport.*;
import Domaine.BesoinsTransport.*;
import Domaine.Statistiques.*;
import Domaine.Utilitaire.*;
import java.awt.geom.Point2D;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author vinny
 */
public class Simulatheure implements java.io.Serializable {

    public void miseAjoutStatistiqueApresArret() {
        StatistiquesGeneral st = new StatistiquesGeneral(getStatistique());
        st.miseAjourApresFin();
        m_statistiques.add(st);
    }
    
    public void mergeStatistiquePlusieurJours( StatistiquesGeneral p_stat){
        m_reseauBesoins.setStatistiquePlusUnJour(p_stat);
    }

    public LinkedList<StatistiquesGeneral> getListStatistique() {
        return m_statistiques;
    }

    
    public enum Mode {

        ROUTIER, TRANSPORT, BESOINS, SIMULATION
    }
    
    public enum Commande {

        SELECTIONNER, INTERSECTION, TRONCON, ARRET, SOURCEAUTOBUS, CIRCUIT, BESOIN
    }
    private Historique m_historique = new Historique();
    
    private ReseauRoutier m_reseauRoutier;
    private LinkedList<Intersection> m_parametresTroncon = new LinkedList<>();

    private LinkedList<StatistiquesGeneral> m_statistiques = new LinkedList<>();
    
    private ReseauTransport m_reseauTransport;
    private LinkedList<Arret> m_arretsNouveauTrajet = new LinkedList<>();
    private LinkedList<Troncon> m_tronconsNouveauTrajet = new LinkedList<>();
    private Boolean m_dijkstra = true;

    
    private ReseauBesoins m_reseauBesoins = new ReseauBesoins();
    private Boolean m_chercherArretMemeCircuit = false;
    private Arret m_arret1Besoin = null;
    private Itineraire m_itineraireEnConstruction = null;

    public Simulatheure() {
        initControleur();
    }
    public double getPrecisionMax() {
        double precision = -1;
        int nbIterationMin = Integer.MAX_VALUE;
        for (StatistiqueBesoin stat : m_reseauBesoins.getStatistique().getListeStatistiqueBesoin()){
           if(stat.getNbIteration() < nbIterationMin){
               nbIterationMin = stat.getNbIteration();
           }
       }
       for (StatistiqueBesoin stat : m_reseauBesoins.getStatistique().getListeStatistiqueBesoin()){
           if(stat.getprecisionGlobal() > precision && nbIterationMin > 10){
               precision = stat.getprecisionGlobal();
           }
       }
       if(precision == -1){
           return Double.MAX_VALUE;
       }
       else{
        return precision;
       }
    }


    public ReseauRoutier getRoutier() {
        return m_reseauRoutier;
    }

    public LinkedList<Intersection> getParametresTroncon() {
        return m_parametresTroncon;
    }

    public ReseauTransport getTransport() {
        return m_reseauTransport;
    }

    public ReseauBesoins getBesoins() {
        return m_reseauBesoins;
    }
    
    public ElementRoutier selectionnerElementRoutier(Integer p_x, Integer p_y, Float p_echelle, Boolean p_estMultiple) {
        ElementRoutier elementRoutier = obtenirElementRoutier(p_x, p_y, p_echelle);
        if (elementRoutier != null) {
            if (!p_estMultiple) {
                deselectionnerRoutier();
            }

            m_reseauRoutier.getPileSelection().ajouter(elementRoutier);
        }
        return elementRoutier;
    }

    public ElementRoutier obtenirElementRoutier(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel;
        float yReel;
        float largeurSelection;

        if (p_echelle > 1) {
            xReel = (p_x - Intersection.RAYON) / p_echelle;
            yReel = (p_y - Intersection.RAYON) / p_echelle;
            largeurSelection = 2 * Intersection.RAYON / p_echelle;
        } else {
            xReel = p_x / p_echelle - Intersection.RAYON;
            yReel = p_y / p_echelle - Intersection.RAYON;
            largeurSelection = 2 * Intersection.RAYON;
        }

        Intersection intersection = m_reseauRoutier.obtenirIntersection(xReel, yReel, largeurSelection);
        if (intersection == null) {
            if (p_echelle > 1) {
                xReel = (p_x - Troncon.LARGEUR / 2) / p_echelle;
                yReel = (p_y - Troncon.LARGEUR / 2) / p_echelle;
                largeurSelection = Troncon.LARGEUR / p_echelle;
            } else {
                xReel = p_x / p_echelle - Troncon.LARGEUR / 2;
                yReel = p_y / p_echelle - Troncon.LARGEUR / 2;
                largeurSelection = Troncon.LARGEUR;
            }

            return m_reseauRoutier.obtenirTroncon(xReel, yReel, largeurSelection, p_echelle);
        } else {
            return intersection;
        }
    }

    public void deselectionnerRoutier() {
        m_parametresTroncon.clear();
        m_reseauRoutier.deselectionnerTout();
    }

    public void deselectionnerTransport() {
        m_reseauRoutier.desuggererTout();
        m_arretsNouveauTrajet.clear();
        m_tronconsNouveauTrajet.clear();
        m_reseauTransport.deselectionnerTout();
    }
    
    public void deselectionnerBesoins(){
        cleanItineraireTemp();
        m_reseauBesoins.deselectionnerTout();
    }

    public void deselectionnerTout() {
        deselectionnerRoutier();
        deselectionnerTransport();
        deselectionnerBesoins();
    }
    
    public void resilierConstruction()
    {
        m_parametresTroncon.clear();
        m_arretsNouveauTrajet.clear();
        m_tronconsNouveauTrajet.clear();
    }

    public void ajouterIntersection(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;
        m_reseauRoutier.ajouterIntersection(xReel, yReel);
        
        m_historique.modifier();                                                // ANNULER-RÉTABLIR
    }

    public void construireTroncon(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel;
        float yReel;
        float largeurSelection;

        if (p_echelle > 1) {
            xReel = (p_x - Intersection.RAYON) / p_echelle;
            yReel = (p_y - Intersection.RAYON) / p_echelle;
            largeurSelection = 2 * Intersection.RAYON / p_echelle;
        } else {
            xReel = p_x / p_echelle - Intersection.RAYON;
            yReel = p_y / p_echelle - Intersection.RAYON;
            largeurSelection = 2 * Intersection.RAYON;
        }

        Boolean intersectionSelect = m_reseauRoutier.selectionnerIntersectionVinny(xReel, yReel, largeurSelection);

        if (!intersectionSelect) {
            ajouterIntersection(p_x, p_y, p_echelle);
            m_parametresTroncon.add(m_reseauRoutier.getIntersections().getLast());
            m_reseauRoutier.getPileSelection().ajouter(m_parametresTroncon.getLast());
        }
        else
        {
            Intersection intersection = (Intersection) m_reseauRoutier.getPileSelection().getDessus();
            if (m_reseauRoutier.getPileSelection().contient(intersection))
            {
                m_parametresTroncon.add(intersection);
            } else {
                m_parametresTroncon.removeFirst();
            }
        }
        
        if (m_parametresTroncon.size() == 2)
            {
                Intersection origine = m_parametresTroncon.getFirst();
                Intersection destination = m_parametresTroncon.getLast();
                
                m_parametresTroncon.removeFirst();
                m_reseauRoutier.getPileSelection().enlever(origine);

                m_reseauRoutier.ajouterTroncon(origine, destination);
                ajusterDoubleSens();
        
                m_historique.modifier();                                        // ANNULER-RÉTABLIR
            }
    }

    public ElementTransport selectionnerElementTransport(Integer p_x, Integer p_y, Float p_echelle, Boolean p_estMultiple){
        ElementTransport elementTransport = obtenirElementTransport(p_x, p_y, p_echelle);
        if (elementTransport != null) {
            if (!p_estMultiple) {
                deselectionnerTransport();
            }

            m_reseauTransport.getPileSelection().ajouter(elementTransport);
        }
        return elementTransport;
    }
    
    public ElementTransport obtenirElementTransport(Integer p_x, Integer p_y, Float p_echelle) {
        
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;
        float largeurSelection;
        
        if (p_echelle > 1) {
            largeurSelection = SourceAutobus.LARGEUR / p_echelle;
        } else {
            largeurSelection = SourceAutobus.LARGEUR;
        }

        Boolean srcSelect = m_reseauTransport.selectionnerSourceAutobus(xReel, yReel, largeurSelection, p_echelle);
        if (srcSelect) {
            return m_reseauTransport.getPileSelection().getDessus();
        } else {
            if (p_echelle > 1) {
                xReel = (p_x - Arret.RAYON) / p_echelle;
                yReel = (p_y - Arret.RAYON) / p_echelle;
            largeurSelection = 2 * Arret.RAYON / p_echelle;
            } else {
                xReel = p_x / p_echelle - Arret.RAYON;
                yReel = p_y / p_echelle - Arret.RAYON;
                largeurSelection = 2 * Arret.RAYON;
            }
            Boolean arrSelect = m_reseauTransport.selectionnerArret(xReel, yReel, largeurSelection, p_echelle);
            if (arrSelect){
                return m_reseauTransport.getPileSelection().getDessus();
            }
            else{
                if (p_echelle > 1) {
                    xReel = (p_x - Troncon.LARGEUR / 2) / p_echelle;
                    yReel = (p_y - Troncon.LARGEUR / 2) / p_echelle;
                    largeurSelection = Troncon.LARGEUR / p_echelle;
                } else {
                    xReel = p_x / p_echelle - Troncon.LARGEUR / 2;
                    yReel = p_y / p_echelle - Troncon.LARGEUR / 2;
                    largeurSelection = Troncon.LARGEUR;
                }
                Boolean circSelect = selectionnerCircuit(xReel, yReel, largeurSelection, p_echelle);
                if (circSelect){
                    return m_reseauTransport.getPileSelection().getDessus();
                }
                else{
                    return null;
                }
            }
        }
    }
    
    public Boolean selectionnerCircuit(Float xReel, Float yReel, Float largeurSelection, Float p_echelle)
    {
        Troncon trc = m_reseauRoutier.obtenirTroncon(xReel, yReel, largeurSelection, p_echelle);
        return m_reseauTransport.selectionnerCircuit(xReel, yReel, p_echelle, trc);
    }

    public Boolean ajouterArret(Integer p_x, Integer p_y, Float p_echelle) {
        
        ElementRoutier elementRoutier = obtenirElementRoutier(p_x, p_y, p_echelle);
        if (elementRoutier != null)
        {
            Emplacement emplacementDesire;
            if (elementRoutier.getClass() == Intersection.class)
            {
                emplacementDesire = new Emplacement(false, 0, null, (Intersection)elementRoutier);
            }
            else
            {
                Troncon tronconObtenu = (Troncon)elementRoutier;
                Point2D.Float p2 = new Point2D.Float(p_x / p_echelle, p_y / p_echelle);
                float d1 = (float)tronconObtenu.getOrigine().getPosition().distance(p2);
                float d2 = tronconObtenu.getLongueurTroncon();
                emplacementDesire = new Emplacement(true, d1 / d2, tronconObtenu, tronconObtenu.getOrigine());
            }
            
            for (Arret arret : m_reseauTransport.getListeArrets())
                if (arret.getEmplacement().equals(emplacementDesire))
                    return false;
            
            m_reseauTransport.ajouterArret(new Arret(emplacementDesire));
            
            m_historique.modifier();                                            // ANNULER-RÉTABLIR
            return true; 
        }
        else 
            return false;
    }

    public Boolean construireCircuit(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel;
        float yReel;
        float largeurSelection;
        Boolean estConstructible = false;
        
        Arret arretInitiale;
        Arret arretFinale;
        Boolean arretEstNouvelle = false;
        Boolean succesAjoutArret = false;

        if (m_arretsNouveauTrajet.size() < 2) {
            if (p_echelle > 1) {
                xReel = (p_x - Arret.RAYON) / p_echelle;
                yReel = (p_y - Arret.RAYON) / p_echelle;
                largeurSelection = 2 * Arret.RAYON / p_echelle;
            } else {
                xReel = p_x / p_echelle - Arret.RAYON;
                yReel = p_y / p_echelle - Arret.RAYON;
                largeurSelection = 2 * Arret.RAYON;
            }

            Arret arret = m_reseauTransport.selectionnerArretVinny(xReel, yReel, largeurSelection, p_echelle);
            if (arret == null) {
                arretEstNouvelle = ajouterArret(p_x, p_y, p_echelle);
                if (arretEstNouvelle) {
                    m_arretsNouveauTrajet.add(m_reseauTransport.getListeArrets().getLast());
                    m_reseauTransport.getPileSelection().ajouter(m_arretsNouveauTrajet.getLast());
                    succesAjoutArret = true;
                }
            }
            else {
                if (m_reseauTransport.getPileSelection().contient(arret)) {
                    m_arretsNouveauTrajet.add(arret);
                    succesAjoutArret = true;
                } else {
                    m_arretsNouveauTrajet.clear();
                }
            }
            
            Circuit circuitSelectionne = obtenirCircuitSelectionne();
            if (circuitSelectionne != null)
            {
                // Reste à gérer les culs de sac.. Peut-être modifier canceller circuit pour la cause
                if (circuitSelectionne.veutBoucler())
                {
                    m_reseauTransport.getPileSelection().enlever(m_arretsNouveauTrajet.getLast());
                    
                    m_arretsNouveauTrajet.clear();
                    
                    if (arretEstNouvelle)
                        m_reseauTransport.getListeArrets().removeLast();
                    
                    throw new IllegalArgumentException("Le circuit est configuré pour boucler.", new Throwable("Allongement impossible"));
                }
                
                else if (succesAjoutArret)
                    if (circuitSelectionne.getListeArretTrajet().getLast().getArret() != m_arretsNouveauTrajet.getLast())
                        m_arretsNouveauTrajet.addFirst(circuitSelectionne.getListeArretTrajet().getLast().getArret());
                    else
                    {
                        m_arretsNouveauTrajet.clear();
                        throw new IllegalArgumentException("Un trajet ne peut pas boucler sur la même arret.", new Throwable("Allongement impossible"));
                    }
            }
            
            if (m_arretsNouveauTrajet.size() == 2)
            {
                arretInitiale = m_arretsNouveauTrajet.getFirst();
                arretFinale = m_arretsNouveauTrajet.getLast();
                estConstructible = petitTrajetCircuitEstConstructible(arretInitiale, arretFinale);
                if (!estConstructible)
                {                
                    if (!m_reseauTransport.arretsSontConnectables(arretInitiale, arretFinale))
                    {
                        m_reseauRoutier.desuggererTout();
                        
                        m_reseauTransport.getPileSelection().enlever(arretFinale);
                        
                        if (circuitSelectionne != null)
                            m_arretsNouveauTrajet.clear();
                        else
                            m_arretsNouveauTrajet.removeLast();                        
                       
                        m_tronconsNouveauTrajet.clear();
                        
                        if (arretEstNouvelle)
                            m_reseauTransport.getListeArrets().removeLast();

                        throw new IllegalArgumentException("L'arrêt n'est pas atteignable.", new Throwable("Construction impossible"));
                    }
                    else if (m_dijkstra)
                        return construireCircuit(p_x, p_y, p_echelle);
                }
            }
        }
        else 
        {
            estConstructible = construireLongTrajetCircuit(p_x, p_y, p_echelle);
        }
        
        if (estConstructible)
        {
            arretInitiale = m_arretsNouveauTrajet.getFirst();
            arretFinale = m_arretsNouveauTrajet.getLast();
            Trajet trajet = new Trajet(arretInitiale.getEmplacement(), arretFinale.getEmplacement(), new LinkedList<>(m_tronconsNouveauTrajet));
            
            Circuit circuitSelectionne = obtenirCircuitSelectionne();
            if (circuitSelectionne == null)
            {
                LinkedList<PaireArretTrajet> listePaires = new LinkedList<>();
                listePaires.add(new PaireArretTrajet(arretInitiale, trajet));
                listePaires.add(new PaireArretTrajet(arretFinale, null));

                m_reseauTransport.ajouterCircuit(new Circuit(listePaires));
                m_reseauTransport.getPileSelection().ajouter(m_reseauTransport.getListeCircuits().getLast());
            }
            else
            {
                PaireArretTrajet anciennePaire = circuitSelectionne.getListeArretTrajet().getLast();
                PaireArretTrajet nouvellePaire = new PaireArretTrajet(arretInitiale, trajet);
                miseAJourPaireArretTrajetItineraire(circuitSelectionne, anciennePaire, nouvellePaire);
                circuitSelectionne.getListeArretTrajet().removeLast();
                circuitSelectionne.getListeArretTrajet().add(nouvellePaire);
                circuitSelectionne.getListeArretTrajet().add(new PaireArretTrajet(arretFinale, null));
                if (circuitSelectionne.getListeArretTrajet().getFirst().getArret() == arretFinale)
                    circuitSelectionne.setPeutBoucler(true);
                else
                    circuitSelectionne.setPeutBoucler(false);
                m_reseauTransport.getPileSelection().ajouter(circuitSelectionne);
            }            
            
            m_reseauTransport.getPileSelection().enlever(arretInitiale);
            deselectionnerRoutier();
            resilierConstruction();
            m_historique.modifier();                                            // ANNULER-RÉTABLIR
        }
        return estConstructible;
    }

    private Boolean petitTrajetCircuitEstConstructible(Arret arretInitiale, Arret arretFinale)
    {
        Boolean estConstructible = false;

        if (arretInitiale.getEmplacement().estSurTroncon()) {
            m_tronconsNouveauTrajet.add(arretInitiale.getEmplacement().getTroncon());
            for (Troncon troncon : m_tronconsNouveauTrajet.getLast().getDestination().getTroncons()) {
                troncon.setEstSuggere(true);
            }

            if (arretFinale.getEmplacement().estSurTroncon()) {
                if (arretFinale.getEmplacement().getTroncon() == m_tronconsNouveauTrajet.getLast()) {
                    if (arretInitiale.getEmplacement().getPourcentageParcouru() < arretFinale.getEmplacement().getPourcentageParcouru()) {
                        estConstructible = true;
                    } else {
                        m_tronconsNouveauTrajet.add(arretFinale.getEmplacement().getTroncon());
                    }
                } else {
                    m_tronconsNouveauTrajet.add(arretFinale.getEmplacement().getTroncon());
                    estConstructible = arretFinale.getEmplacement().getTroncon().estSuggere();
                }
            } 
            else 
            {
                estConstructible = m_tronconsNouveauTrajet.getLast().getDestination() == arretFinale.getEmplacement().getIntersection();
            }
        } 
        else 
        {
            for (Troncon troncon : arretInitiale.getEmplacement().getIntersection().getTroncons()) {
                troncon.setEstSuggere(true);
            }

            if (arretFinale.getEmplacement().estSurTroncon()) {
                m_tronconsNouveauTrajet.add(arretFinale.getEmplacement().getTroncon());
                estConstructible = arretFinale.getEmplacement().getTroncon().estSuggere();
            }
        }
        return estConstructible;
    }
        
    private Boolean construireLongTrajetCircuit(Integer p_x, Integer p_y, Float p_echelle)
    // Appeler seulement si les arrêt du nouveau trajet sont connectables.
    {
        float xReel;
        float yReel;
        float largeurSelection;
        Arret arretFinale = m_arretsNouveauTrajet.getLast();
        Boolean estConstructible = false;
        
        if(m_dijkstra)
        {
            estConstructible = true;
            Arret arretInitiale = m_arretsNouveauTrajet.getFirst();
            m_tronconsNouveauTrajet = m_reseauRoutier.dijkstra(arretInitiale.getEmplacement(), arretFinale.getEmplacement());
        }
        else
        {   
            if (p_echelle > 1) {
                xReel = (p_x - Troncon.LARGEUR / 2) / p_echelle;
                yReel = (p_y - Troncon.LARGEUR / 2) / p_echelle;
                largeurSelection = Troncon.LARGEUR / p_echelle;
            } else {
                xReel = p_x / p_echelle - Troncon.LARGEUR / 2;
                yReel = p_y / p_echelle - Troncon.LARGEUR / 2;
                largeurSelection = Troncon.LARGEUR;
            }

            Troncon tronconSelectionne = m_reseauRoutier.obtenirTroncon(xReel, yReel, largeurSelection, p_echelle);
            if (tronconSelectionne == null || !tronconSelectionne.estSuggere()) {
                return estConstructible;
            }

            if (arretFinale.getEmplacement().estSurTroncon()) {
                m_tronconsNouveauTrajet.add(m_tronconsNouveauTrajet.size() - 1, tronconSelectionne);
                estConstructible = tronconSelectionne.getDestination() == m_tronconsNouveauTrajet.getLast().getOrigine();
            } else {
                m_tronconsNouveauTrajet.add(tronconSelectionne);
                estConstructible = tronconSelectionne.getDestination() == arretFinale.getEmplacement().getIntersection();
            }

            if (!estConstructible) {
                m_reseauRoutier.desuggererTout();
                m_reseauRoutier.getPileSelection().ajouter(tronconSelectionne);
                for (Troncon troncon : tronconSelectionne.getDestination().getTroncons()) {
                    if (!m_tronconsNouveauTrajet.contains(troncon)) {
                        troncon.setEstSuggere(true);
                    }
                }
            }
        }
        return estConstructible;
    }

    public void ajouterSource(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;

        ElementRoutier elementRoutier = selectionnerElementRoutier(p_x, p_y, p_echelle, false);
        ElementTransport elementTransport = obtenirElementTransport(p_x, p_y, p_echelle);
        if (elementTransport != null && elementTransport.getClass() != Circuit.class) {
            if (elementTransport.getClass() == Arret.class) {
                Arret arret = (Arret) elementTransport;
                Emplacement emplacement = new Emplacement(false, 0, null, null);
                emplacement.copy(arret.getEmplacement());
                for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
                    Circuit circuit = circuits.next();
                    if (m_reseauTransport.getPileSelection().contient(circuit)) {
                        for ( PaireArretTrajet arretTrajet :circuit.getListeArretTrajet()){
                            Arret arretDansCircuit = arretTrajet.getArret();
                            if(arret.equals(arretDansCircuit)){
                                m_reseauTransport.ajoutSource(emplacement, circuit);
                                m_historique.modifier();                                // ANNULER-RÉTABLIR
                                return;
                            }
                        }
                        
                    }
                }
            }
        } else if (elementRoutier != null) {
            if (elementRoutier.getClass() == Intersection.class) {
                Intersection intersection = (Intersection) elementRoutier;
                Emplacement emplacement = new Emplacement(false, 0, null, intersection);
                for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
                    Circuit circuit = circuits.next();
                    if (m_reseauTransport.getPileSelection().contient(circuit)) {
                        for (ListIterator<PaireArretTrajet> paires = circuit.getListeArretTrajet().listIterator(); paires.hasNext();) {
                            PaireArretTrajet paire = paires.next();
                            if (paire.getTrajet() == null) {
                                return;
                            }
                            Intersection interMax1= null;
                            if(!paire.getTrajet().getEmplacementInitial().estSurTroncon()){
                                interMax1 = paire.getTrajet().getEmplacementInitial().getIntersection();
                            }
                            Intersection interMax2= null;
                            if(!paire.getTrajet().getEmplacementFinal().estSurTroncon()){
                                interMax2 = paire.getTrajet().getEmplacementFinal().getIntersection();
                            }
                            if(interMax1 != null){
                                if(intersection.equals(interMax1)){
                                    m_reseauTransport.ajoutSource(emplacement, circuit);
                                    m_historique.modifier();                    // ANNULER-RÉTABLIR
                                    return;
                                }
                            }
                            if(interMax2 != null){
                                if(intersection.equals(interMax2)){
                                    m_reseauTransport.ajoutSource(emplacement, circuit);
                                    m_historique.modifier();                    // ANNULER-RÉTABLIR
                                    return;
                                }
                            }
                            for (ListIterator<Troncon> troncons = paire.getTrajet().getListeTroncons().listIterator(); troncons.hasNext();) {
                                Troncon troncon = troncons.next();
                                if(intersection.equals(troncon.getDestination())){
                                    if(troncons.hasNext()){
                                        m_reseauTransport.ajoutSource(emplacement, circuit);
                                        m_historique.modifier();                // ANNULER-RÉTABLIR
                                        return;
                                    }
                                }
                                
                            }
                        }
                    }
                }
            } else if (elementRoutier.getClass() == Troncon.class) {
                for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
                    Circuit circuit = circuits.next();
                    if (m_reseauTransport.getPileSelection().contient(circuit)) {
                        Arret arret1 = circuit.getListeArretTrajet().getFirst().getArret();
                        Arret arret2 = circuit.getListeArretTrajet().getLast().getArret();
                        for (ListIterator<PaireArretTrajet> paires = circuit.getListeArretTrajet().listIterator(); paires.hasNext();) {
                            PaireArretTrajet paire = paires.next();
                            if (paire.getTrajet() == null) {
                                return;
                            }
                            for (ListIterator<Troncon> troncons = paire.getTrajet().getListeTroncons().listIterator(); troncons.hasNext();) {
                                Troncon troncon = troncons.next();
                                if (m_reseauRoutier.getPileSelection().contient(troncon)) {
                                    Point2D.Float p1 = new Point2D.Float(xReel, yReel);
                                    double distance1 = troncon.getOrigine().getPosition().distance(p1);
                                    double distance2 = troncon.getLongueurTroncon();
                                    float pourcentage = (float) (distance1 / distance2);
                                    Boolean avantArret1 = false;
                                    Boolean apresArret2 = false;
                                    Troncon trc1 = null;
                                    Troncon trc2 = null;
                                    if (arret1 != null) {
                                        boolean add =true;
                                        if (arret1.getEmplacement().estSurTroncon()) {
                                            trc1 = arret1.getEmplacement().getTroncon();
                                            if (troncon.equals(trc1)) {
                                                if (pourcentage <= arret1.getEmplacement().getPourcentageParcouru()) {
                                                    avantArret1 = true;
                                                }
                                            }
                                        }
                                        if (arret2 != null) {
                                            if (arret2.getEmplacement().estSurTroncon()) {
                                                trc2 = arret2.getEmplacement().getTroncon();
                                                if (troncon.equals(trc2)) {
                                                    if (pourcentage >= arret2.getEmplacement().getPourcentageParcouru()) {
                                                        apresArret2 = true;
                                                    }
                                                }
                                            }
                                        }
                                        if (trc1 != null && trc2 != null) {
                                            if (trc1.equals(trc2)) {
                                                if (!circuit.peutBoucler()) {
                                                    if (arret1.getEmplacement().getPourcentageParcouru() > arret2.getEmplacement().getPourcentageParcouru()) {
                                                        if (avantArret1 && apresArret2) {
                                                            return;
                                                        }
                                                    } else if (avantArret1 || apresArret2) {
                                                        return;
                                                    }
                                                }
                                            }
                                             else{
                                            if(trc1!= null){
                                                if(avantArret1){
                                                    add = false;
                                                }
                                            }
                                            if (trc2!= null){
                                                if(apresArret2){
                                                    add = false;
                                                }
                                            }
                                            }
                                        }
                                        if(add){
                                            Emplacement emplacement = new Emplacement(true, pourcentage, troncon, troncon.getOrigine());
                                            m_reseauTransport.ajoutSource(emplacement, circuit);
                                            m_historique.modifier();            // ANNULER-RÉTABLIR
                                            return;
                                        }
                                            
                                        
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public LinkedList<ElementRoutier> getElementsSelectionnesRoutier() {
        return m_reseauRoutier.getElementsSelectionnes();
    }

    public LinkedList<ElementTransport> getElementsSelectionnesTransport() {
        return m_reseauTransport.getElementsSelectionnes();
    }
    
    public LinkedList<ElementBesoins> getElementsSelectionnesBesoins() {
        return m_reseauBesoins.getElementsSelectionnes();
    }

    public Boolean supprimerSelectionRoutier() {
        PileSelectionRoutier pile = m_reseauRoutier.getPileSelection();
        for (Arret arr : m_reseauTransport.getListeArrets()) {
            if (arr.getEmplacement().estSurTroncon()) {
                Troncon trc_arr = arr.getEmplacement().getTroncon();
                if (pile.contient(trc_arr) || pile.contient(trc_arr.getDestination())
                        || pile.contient(trc_arr.getOrigine())){
                    return false;
                }
            } else if (pile.contient(arr.getEmplacement().getIntersection())) {
                return false;
            }
        }
        for (Circuit circ : m_reseauTransport.getListeCircuits()) {
            for (PaireArretTrajet pat : circ.getListeArretTrajet()) {
                Trajet traj = pat.getTrajet();
                if (traj != null) {
                    for (Troncon trc : traj.getListeTroncons()) {
                        if (pile.contient(trc) || pile.contient(trc.getOrigine())
                                || pile.contient(trc.getDestination())) {
                            return false;
                        }
                    }
                }
            }
        }
        for(Itineraire itn : m_reseauBesoins.getListItineraire()){
            for(PaireParcours pat : itn.getListPaireParcours()){
                if(pat.getTrajet()!=null){
                    for(Troncon trc : pat.getTrajet().getListeTroncons()){
                        if (pile.contient(trc) || pile.contient(trc.getOrigine())
                                    || pile.contient(trc.getDestination())) {
                            return false;
                        }
                    }
                }
                if(pat.getParcoursBus()!=null){
                    for(Troncon trc : pat.getParcoursBus().getTroncons()){
                        if (pile.contient(trc) || pile.contient(trc.getOrigine())
                                    || pile.contient(trc.getDestination())) {
                            return false;
                        }
                    }
                }
            }
        }

        m_reseauRoutier.supprimerSelection();
        ajusterDoubleSens();
        
        m_historique.modifier();                                                // ANNULER-RÉTABLIR
        return true;
    }

    public Boolean supprimerSelectionTransport() {
        PileSelectionTransport pile = m_reseauTransport.getPileSelection();
        for(Itineraire itn : m_reseauBesoins.getListItineraire()){
            for(PaireParcours pat : itn.getListPaireParcours()){
                if(pat.getParcoursBus()!=null){
                    if(pile.contient(pat.getParcoursBus().getArretDepart())
                            ||pile.contient(pat.getParcoursBus().getArretFinal())
                            ||pile.contient(pat.getParcoursBus().getCircuit())){
                        throw new IllegalArgumentException("Un élément de transport ne peut être supprimé car un itinéraire en dépend.", new Throwable("Construction impossible"));
                        
                    }
                }
            }
        }
        Boolean supprimee = m_reseauTransport.supprimerSelection();
        
        if (supprimee)
            m_historique.modifier();                                            // ANNULER-RÉTABLIR
        
        return supprimee;
    }
    
     public Boolean supprimerSelectionBesoins() {
        Boolean supprimee = m_reseauBesoins.supprimerSelection();
        
        if (supprimee)
            m_historique.modifier();                                            // ANNULER-RÉTABLIR
        
        return supprimee;
    }


    public void ajusterDoubleSens() {
        for (Intersection intrsct : m_reseauRoutier.getIntersections()) {
            for (Troncon trc : intrsct.getTroncons()) {
                trc.setDoubleSens();
            }
        }
    }

    public Circuit obtenirCircuitSelectionne() {
        for (Circuit circ : m_reseauTransport.getListeCircuits()) {
            if (m_reseauTransport.getPileSelection().contient(circ)) {
                return circ;
            }
        }
        return null;
    }
    
    public void changerStatutDijkstra() {
        m_dijkstra = !m_dijkstra;
    }
    
    public LinkedList<Circuit> obtenirCircuitsAffectes(Troncon p_tronconModifie)
    {
        return m_reseauTransport.obtenirCircuitsAffectes(p_tronconModifie);
    }
    
    public void optimiserCircuitsAffectes(LinkedList<Circuit> circuitsAffectes, Troncon p_tronconModifie)
    {
        if (m_dijkstra)
        {
            LinkedList<Trajet> trajetsAffectes = m_reseauTransport.obtenirTrajetsAffectes(circuitsAffectes, p_tronconModifie);
            for (Trajet trajet : trajetsAffectes)
            {
                trajet.setListeTroncons(m_reseauRoutier.dijkstra(trajet.getEmplacementInitial(), trajet.getEmplacementFinal()));
            }

            m_reseauTransport.supprimerSourcesOrphelines(circuitsAffectes);
        }
    }
    
    public Circuit obtenirCircuitDeSource(SourceAutobus src) {
        for (Circuit circ : m_reseauTransport.getListeCircuits()) {
            if (circ.getListeSources().contains(src)) {
                return circ;
            }
        }
        return null;
    }
    
    public void demarrerSimulation() {
        m_reseauBesoins.getStatistique().setDefault();
        m_reseauRoutier.initReseauRoutier();
        m_reseauTransport.initReseauTransport();
        m_reseauBesoins.initBesoinTransport();
//        ListIterator<Itineraire> BesoinTransportItr = m_reseauBesoins.getListItineraire().listIterator();
//        while (BesoinTransportItr.hasNext()) {
//           // BesoinTransportItr.next().initBesoinTransport();
//        }
    }

    public void rafraichirSimulation(Temps m_deltaT) {
        m_reseauTransport.calculEtatReseauTransport(m_deltaT);
        m_reseauBesoins.calculEtatReseauBesoin(m_deltaT);
    }
    
    public void arreterSimulation() {
        for (ListIterator<Arret> arrets = m_reseauTransport.getListeArrets().listIterator(); arrets.hasNext();) {
            Arret arret = arrets.next();
            arret.miseADefaut();
        }
        for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSources().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                source.setDefault();
            }
        }
        for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSources().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                source.setDefault();
            }
            circuit.getListeAutobus().clear();
            circuit.clearRepresentation();
        }
        for (ListIterator<Itineraire> itineraires = m_reseauBesoins.getListItineraire().listIterator(); itineraires.hasNext();) {
            Itineraire itineraire = itineraires.next();
            itineraire.getListIndividu().clear();
            itineraire.getSourceIndividu().miseADefaut();
        }
    }
    
    public Historique getHistorique()
    {
        return m_historique;
    }
    
    public void annuler()
    {
            m_historique.annuler();
            m_reseauRoutier = m_historique.getRoutierCourant();
            m_reseauTransport = m_historique.getTransportCourant();
            m_reseauBesoins = m_historique.getBesoinsCourant();
            resilierConstruction();
            cleanItineraireTemp();
    }
    
    public void retablir()
    {
            m_historique.retablir();
            m_reseauRoutier = m_historique.getRoutierCourant();
            m_reseauTransport = m_historique.getTransportCourant();
            m_reseauBesoins = m_historique.getBesoinsCourant();
            resilierConstruction();
            cleanItineraireTemp();
    }
    
    public final void initControleur()
    {
        if (this.m_historique.getCurseur() == null)
            m_historique.initCurseur();
        
        m_reseauRoutier = m_historique.getRoutierCourant();
        m_reseauTransport = m_historique.getTransportCourant();
        m_reseauBesoins = m_historique.getBesoinsCourant();
    }
    
    public ElementBesoins selectionnerElementBesoins(Integer p_x, Integer p_y, Float p_echelle, Boolean p_estMultiple) {
        ElementBesoins elementBesoins = obtenirElementBesoins(p_x, p_y, p_echelle);
        if (elementBesoins != null) {
            if (!p_estMultiple) {
                deselectionnerBesoins();
            }

            m_reseauBesoins.getPileSelection().ajouter(elementBesoins);
        }
        return elementBesoins;
    }

    public ElementBesoins obtenirElementBesoins(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel;
        float yReel;
        float largeurSelection;

        if (p_echelle > 1) {
            xReel = (p_x - Troncon.LARGEUR / 2) / p_echelle;
            yReel = (p_y - Troncon.LARGEUR / 2) / p_echelle;
            largeurSelection = Troncon.LARGEUR / p_echelle;
        } else {
            xReel = p_x / p_echelle - Troncon.LARGEUR / 2;
            yReel = p_y / p_echelle - Troncon.LARGEUR / 2;
            largeurSelection = Troncon.LARGEUR;
        }
        Boolean itnSelect = selectionnerItineraire(xReel, yReel, largeurSelection, p_echelle);
        if (itnSelect){
            return m_reseauBesoins.getPileSelection().getDessus();
        }
        else{
            return null;
        }
        
    }
    
    public Boolean selectionnerItineraire(Float xReel, Float yReel, Float largeurSelection, Float p_echelle)
    {
        if (m_reseauBesoins.selectionnerSourceIndividus(xReel, yReel, p_echelle)){
            return true;
        }
        Troncon trc = m_reseauRoutier.obtenirTroncon(xReel, yReel, largeurSelection, p_echelle);
        return m_reseauBesoins.selectionnerItineraire(xReel, yReel, p_echelle, trc);
    }
    
    public Boolean construireItineraire(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel;
        float yReel;
        float largeurSelection;

        if (p_echelle > 1) {
            xReel = (p_x - Arret.RAYON) / p_echelle;
            yReel = (p_y - Arret.RAYON) / p_echelle;
            largeurSelection = 2 * Arret.RAYON / p_echelle;
        } else {
            xReel = p_x / p_echelle - Arret.RAYON;
            yReel = p_y / p_echelle - Arret.RAYON;
            largeurSelection = 2 * Arret.RAYON;
        }
        Arret arret = m_reseauTransport.selectionnerArretVinny(xReel, yReel, largeurSelection, p_echelle);
        if(m_reseauBesoins.getEmplacementSourceTemp()==null){
            if (arret == null) {
                ElementRoutier elementRoutier = obtenirElementRoutier(p_x, p_y, p_echelle);
                if (elementRoutier != null)
                {
                    Emplacement emplacementDesire;
                    if (elementRoutier.getClass() == Intersection.class)
                    {
                        emplacementDesire = new Emplacement(false, 0, null, (Intersection)elementRoutier);
                    }
                    else
                    {
                        Troncon tronconObtenu = (Troncon)elementRoutier;
                        Point2D.Float p2 = new Point2D.Float(p_x / p_echelle, p_y / p_echelle);
                        float d1 = (float)tronconObtenu.getOrigine().getPosition().distance(p2);
                        float d2 = tronconObtenu.getLongueurTroncon();
                        emplacementDesire = new Emplacement(true, d1 / d2, tronconObtenu, tronconObtenu.getOrigine());
                    }

                    m_reseauBesoins.setEmplacementSourceTemp(emplacementDesire);
                    m_chercherArretMemeCircuit = false;

                }
                else{
                    return false;
                }
            }
            else {
                m_reseauBesoins.setEmplacementSourceTemp(arret.getEmplacement());
                m_arret1Besoin = arret;
                m_chercherArretMemeCircuit = true;
            }
        }
        else{
            if (arret==null){
                if(!m_chercherArretMemeCircuit && m_itineraireEnConstruction!=null){
                    ElementRoutier elementRoutier = obtenirElementRoutier(p_x, p_y, p_echelle);
                    if (elementRoutier != null)
                    {
                        Emplacement emplacementDesire;
                        if (elementRoutier.getClass() == Intersection.class)
                        {
                            emplacementDesire = new Emplacement(false, 0, null, (Intersection)elementRoutier);
                        }
                        else
                        {
                            Troncon tronconObtenu = (Troncon)elementRoutier;
                            Point2D.Float p2 = new Point2D.Float(p_x / p_echelle, p_y / p_echelle);
                            float d1 = (float)tronconObtenu.getOrigine().getPosition().distance(p2);
                            float d2 = tronconObtenu.getLongueurTroncon();
                            emplacementDesire = new Emplacement(true, d1 / d2, tronconObtenu, tronconObtenu.getOrigine());
                        }

                        if (!m_reseauTransport.emplacementsSontConnectables(m_arret1Besoin.getEmplacement(), emplacementDesire))
                            throw new IllegalArgumentException("L'emplacement n'est pas atteignable.", new Throwable("Construction impossible"));
                        
                        Trajet trajFin = new Trajet(m_arret1Besoin.getEmplacement(), emplacementDesire, 
                            m_reseauRoutier.dijkstra(m_arret1Besoin.getEmplacement(), emplacementDesire));
                        
                        PaireParcours paireParcFin = new PaireParcours(trajFin, null); 
                        
                        m_itineraireEnConstruction.getListPaireParcours().addLast(paireParcFin);
                        
                        cleanItineraireTemp();
                        
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
                
            if(m_chercherArretMemeCircuit){
                Boolean succesRecherche = false;
                Circuit circuitElu = null;
                PaireArretTrajet patAvant = null;
                PaireArretTrajet patApres = null; 
                for (Circuit circ : m_reseauTransport.getListeCircuits()){
                    Integer compteur_boucle_patIt = 0;
                    Integer compteur_max = 2*circ.getListeArretTrajet().size();
                    PaireArretTrajet patApresPrecedente = null;
                    for (ListIterator<PaireArretTrajet> patIt = circ.getListeArretTrajet().listIterator(); patIt.hasNext();) {
                        patApres = patIt.next();
                        if(patApres.getArret()==arret){
                            Integer compteur_boucle_patItInv = 0;
                            for(ListIterator<PaireArretTrajet> patItInv = circ.getListeArretTrajet().listIterator(patIt.previousIndex()); patItInv.hasPrevious();){
                                patAvant = patItInv.previous();
                                if(patAvant == patApresPrecedente){
                                    break;
                                }
                                if(patAvant.getArret()==m_arret1Besoin){
                                    circuitElu = circ;
                                    succesRecherche = true;
                                    break;
                                }
                                if(compteur_boucle_patItInv==compteur_max){
                                    break;
                                }
                                compteur_boucle_patItInv++;
                                if(circ.veutBoucler() && circ.peutBoucler() && !patItInv.hasPrevious()){
                                    patItInv = circ.getListeArretTrajet().listIterator(circ.getListeArretTrajet().size()-1);
                                }
                            }
                            patApresPrecedente = patApres;
                        }
                        if(succesRecherche || compteur_boucle_patIt==compteur_max){
                            break;
                        }
                        compteur_boucle_patIt++;
                        if(circ.veutBoucler() && circ.peutBoucler() && !patIt.hasNext()){
                            patIt = circ.getListeArretTrajet().listIterator(0);
                        }
                    }
                    if(succesRecherche) break;
                }
                if(succesRecherche){
                    ParcoursBus parcoBus = new ParcoursBus(circuitElu, patAvant, patApres);
                    
                    if(m_itineraireEnConstruction!=null){
                        if(m_itineraireEnConstruction.getListPaireParcours().getLast().getParcoursBus()==null){
                            m_itineraireEnConstruction.getListPaireParcours().getLast().setParcoursBus(parcoBus);
                        }
                        else{
                            m_itineraireEnConstruction.getListPaireParcours().addLast(new PaireParcours(null,parcoBus));
                        }
                    }
                    else{
                        PaireParcours paireParc = new PaireParcours(null, parcoBus); 
                        Itineraire itn = new Itineraire(paireParc);
                        m_reseauBesoins.ajouterItineraire(itn);
                        m_historique.modifier();                                // ANNULER-RÉTABLIR
                        m_itineraireEnConstruction = itn;
                    }
                    m_arret1Besoin = arret;
                    m_chercherArretMemeCircuit = false;
                }
                else{
                    throw new IllegalArgumentException("L'arrêt n'est pas atteignable dans ce circuit.", new Throwable("Construction impossible"));
                }
            }
            else{
                //pour skipper trajet pieton
                Boolean succesRecherche = false;
                Circuit circuitElu = null;
                PaireArretTrajet patAvant = null;
                PaireArretTrajet patApres = null;
                if(m_itineraireEnConstruction!=null){
                    for (Circuit circ : m_reseauTransport.getListeCircuits()){
                        Integer compteur_boucle_patIt = 0;
                        Integer compteur_max = 2*circ.getListeArretTrajet().size();
                        PaireArretTrajet patApresPrecedente = null;
                        for (ListIterator<PaireArretTrajet> patIt = circ.getListeArretTrajet().listIterator(); patIt.hasNext();) {
                            patApres = patIt.next();
                            if(patApres.getArret()==arret){
                                Integer compteur_boucle_patItInv = 0;
                                for(ListIterator<PaireArretTrajet> patItInv = circ.getListeArretTrajet().listIterator(patIt.previousIndex()); patItInv.hasPrevious();){
                                    patAvant = patItInv.previous();
                                    if(patAvant == patApresPrecedente){
                                        break;
                                    }
                                    if(patAvant.getArret()==m_arret1Besoin){
                                        circuitElu = circ;
                                        succesRecherche = true;
                                        break;
                                    }
                                    if(compteur_boucle_patItInv==compteur_max){
                                        break;
                                    }
                                    compteur_boucle_patItInv++;
                                    if(circ.veutBoucler() && circ.peutBoucler() && !patItInv.hasPrevious()){
                                        patItInv = circ.getListeArretTrajet().listIterator(circ.getListeArretTrajet().size()-1);
                                    }
                                }
                                patApresPrecedente = patApres;
                            }
                            if(succesRecherche || compteur_boucle_patIt==compteur_max){
                                break;
                            }
                            compteur_boucle_patIt++;
                            if(circ.veutBoucler() && circ.peutBoucler() && !patIt.hasNext()){
                                patIt = circ.getListeArretTrajet().listIterator(0);
                            }
                        }
                        if(succesRecherche) break;
                    }
                }
                if(succesRecherche){
                    ParcoursBus parcoBus = new ParcoursBus(circuitElu, patAvant, patApres);
                    
                    if(m_itineraireEnConstruction.getListPaireParcours().getLast().getParcoursBus()==null){
                        m_itineraireEnConstruction.getListPaireParcours().getLast().setParcoursBus(parcoBus);
                    }
                    else{
                        m_itineraireEnConstruction.getListPaireParcours().addLast(new PaireParcours(null,parcoBus));
                    }
                    m_arret1Besoin = arret;
                    m_chercherArretMemeCircuit = false;
                }
                else{ //trajetpieton
                    if (!m_reseauTransport.emplacementsSontConnectables(m_reseauBesoins.getEmplacementSourceTemp(), arret.getEmplacement()))
                        throw new IllegalArgumentException("L'arrêt n'est pas atteignable.", new Throwable("Construction impossible"));

                    Trajet traj; 

                    if(m_itineraireEnConstruction!=null){
                        traj = new Trajet(m_arret1Besoin.getEmplacement(), arret.getEmplacement(), 
                            m_reseauRoutier.dijkstra(m_arret1Besoin.getEmplacement(), arret.getEmplacement()));

                        m_itineraireEnConstruction.getListPaireParcours().addLast(new PaireParcours(traj,null));
                    }
                    else{
                        traj = new Trajet(m_reseauBesoins.getEmplacementSourceTemp(), arret.getEmplacement(), 
                            m_reseauRoutier.dijkstra(m_reseauBesoins.getEmplacementSourceTemp(), arret.getEmplacement()));
                        //premier trajet
                        PaireParcours paireParc = new PaireParcours(traj, null); 
                        Itineraire itn = new Itineraire(paireParc);
                        m_reseauBesoins.ajouterItineraire(itn);
                        m_historique.modifier();                                // ANNULER-RÉTABLIR
                        m_itineraireEnConstruction = itn;
                    }
                    m_arret1Besoin = arret;
                    m_chercherArretMemeCircuit = true;
                }
            }
        }
        m_reseauBesoins.getPileSelection().ajouter(m_itineraireEnConstruction);
        return true;
    }
      
    public void cleanItineraireTemp(){
        m_reseauBesoins.setEmplacementSourceTemp(null);
        m_chercherArretMemeCircuit = false;
        m_arret1Besoin = null;
        m_itineraireEnConstruction = null;
    }
    
    public StatistiquesGeneral getStatistique(){
        return m_reseauBesoins.getStatistique();
    }
    
    public void setElementCurseurRoutier(java.awt.event.MouseEvent evt, float echelle){
        ElementRoutier er = obtenirElementRoutier(evt.getX(), evt.getY(), echelle);
        if(er!=null){
            m_reseauRoutier.setElementCurseur(er);
        }
        else{
            m_reseauRoutier.setElementCurseur(null);
        }
    }
    
    public void setElementCurseurTransport(java.awt.event.MouseEvent evt, float echelle){
        LinkedList<ElementTransport> ll = new LinkedList<>();
        for(ElementTransport ett : m_reseauTransport.getPileSelection().getListe()){
            ll.add(ett);
        }
        ElementTransport et = obtenirElementTransport(evt.getX(), evt.getY(), echelle);
        if(et!=null){
            if(!ll.contains(et))
                m_reseauTransport.getPileSelection().enlever(et);
            m_reseauTransport.setElementCurseur(et);
        }
        else{
            m_reseauTransport.setElementCurseur(null);
        }
    }
    
    public void setElementCurseurBesoins(java.awt.event.MouseEvent evt, float echelle){
        LinkedList<ElementBesoins> ll = new LinkedList<>();
        for(ElementBesoins ebb : m_reseauBesoins.getPileSelection().getListe()){
            ll.add(ebb);
        }
        ElementBesoins eb = obtenirElementBesoins(evt.getX(), evt.getY(), echelle);
        if(eb!=null){
            if(!ll.contains(eb))
                m_reseauBesoins.getPileSelection().enlever(eb);
            m_reseauBesoins.setElementCurseur(eb);
        }
        else{
            m_reseauBesoins.setElementCurseur(null);
        }
    }
    
    private void miseAJourPaireArretTrajetItineraire(Circuit circ, 
                        PaireArretTrajet patAvant, PaireArretTrajet patApres){
        for(Itineraire itn : m_reseauBesoins.getListItineraire()){
            for(PaireParcours pp : itn.getListPaireParcours()){
                if(pp.getParcoursBus()!=null){
                    if(pp.getParcoursBus().getCircuit()==circ){
                        if(pp.getParcoursBus().getPaireArretDepart()==patAvant){
                            pp.getParcoursBus().setPaireArretDepart(patApres);
                        }
                        if(pp.getParcoursBus().getPaireArretFinal()==patAvant){
                            pp.getParcoursBus().setPaireArretFinal(patApres);
                        }
                    }
                }
            }
        }
    }
}
