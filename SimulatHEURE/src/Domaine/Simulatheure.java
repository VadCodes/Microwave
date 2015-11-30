package Domaine;

import Domaine.ReseauTransport.Trajet;
import Domaine.ReseauRoutier.*;
import Domaine.ReseauTransport.*;
import Domaine.BesoinsTransport.*;
import Domaine.Utilitaire.*;
import java.awt.geom.Point2D;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author vinny
 */
public class Simulatheure {

    public enum Modes {

        ROUTIER, TRANSPORT, BESOINS, SIMULATION
    }

    public enum Commandes {

        SELECTIONNER, INTERSECTION, TRONCON, ARRET, SOURCEAUTOBUS, AJOUTERCIRCUIT, EDITERCIRCUIT
    }
    //private Simulatheure_log m_log = new Simulatheure_log();
    //private RecullerRetablir m_reculelrRetablir = new RecullerRetablir();
    private ReseauRoutier m_reseauRoutier = new ReseauRoutier();
    private LinkedList<Intersection> m_parametresTroncon = new LinkedList<>();

    private ReseauTransport m_reseauTransport;
    private LinkedList<Arret> m_arretsNouveauTrajet = new LinkedList<>();
    private LinkedList<Troncon> m_tronconsNouveauTrajet = new LinkedList<>();
//    private Trajet m_trajet_temp = new Trajet();
//    private Boolean m_modeNouvelArret = true;
//    private Arret m_arret_temp = new Arret();
    private Boolean m_dijkstra = true;

    private LinkedList<BesoinTransport> m_listBesoins = new LinkedList<>();

    public Simulatheure() {
        m_reseauRoutier = new ReseauRoutier();
        m_reseauTransport = new ReseauTransport(m_reseauRoutier);
    }

    public void arreterSimulation() {
        for (ListIterator<Arret> arrets = m_reseauTransport.getListeArrets().listIterator(); arrets.hasNext();) {
            Arret arret = arrets.next();
            arret.viderFile();
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

    public void demarrerSimulation() {
        m_reseauRoutier.initReseauRoutier();
        m_reseauTransport.initReseauTransport();
        ListIterator<BesoinTransport> BesoinTransportItr = m_listBesoins.listIterator();
        while (BesoinTransportItr.hasNext()) {
            BesoinTransportItr.next().initBesoinTransport();
        }
    }

    public void rafraichirSimulation(Temps m_deltaT) {
        m_reseauTransport.calculEtatReseauTransport(m_deltaT);
    }

    public ElementRoutier selectionnerElementRoutier(Integer p_x, Integer p_y, Float p_echelle, Boolean p_estMultiple) {
        ElementRoutier elementRoutier = obtenirElementRoutier(p_x, p_y, p_echelle);
        if (elementRoutier != null) {
            if (!p_estMultiple) {
                deselectionnerRoutier();
            }

            m_reseauRoutier.getPileSelection().ajouter(elementRoutier);
        }
        //m_log.ajouterAction("selectionnerElementRoutier".concat("\t").concat(p_x.toString()).concat("\t").concat(p_y.toString()).concat("\t").concat(p_echelle.toString()));
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
        m_arretsNouveauTrajet.clear();
        m_tronconsNouveauTrajet.clear();
        m_reseauRoutier.desuggererTout();
//        m_trajet_temp = new Trajet();
//        m_arret_temp = new Arret();
//        m_modeNouvelArret = true;
        m_reseauTransport.deselectionnerTout();
    }

    public void deselectionnerTout() {
        deselectionnerRoutier();
        deselectionnerTransport();
    }

    public void ajouterIntersection(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;
        m_reseauRoutier.ajouterIntersection(xReel, yReel);
        //String action = "ajouterIntersection\t".concat(p_x.toString()).concat("\t").concat(p_y.toString()).concat("\t").concat(p_echelle.toString());
        //m_log.ajouterAction(action);
        //m_reculelrRetablir.ajouterAction(action);

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

        Boolean intersectionSelect = m_reseauRoutier.selectionnerIntersection(xReel, yReel, largeurSelection);

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
                Boolean circSelect = m_reseauTransport.selectionnerCircuit(xReel, yReel, largeurSelection, p_echelle);
                if (circSelect){
                    return m_reseauTransport.getPileSelection().getDessus();
                }
                else{
                    return null;
                }
            }
        }
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
            
            m_reseauTransport.ajouterArret(new Arret(emplacementDesire, ""));
            return true; 
        }
        else 
            return false;
    }

    public void cancellerCircuit() {
//        deselectionnerRoutier();  // ça "désuggère" aussi
//        m_trajet_temp = new Trajet();
//        m_arret_temp = new Arret();
//        m_modeNouvelArret = true;
    }

    public Boolean construireCircuit(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel;
        float yReel;
        float largeurSelection;
        Boolean estConstructible = false;
        
        Arret arretInitiale;
        Arret arretFinale;
        Boolean arretEstNouvelle = false;
        Boolean succes = false;

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

            Boolean arrSelect = m_reseauTransport.selectionnerArret(xReel, yReel, largeurSelection, p_echelle);
            if (!arrSelect) {
                arretEstNouvelle = ajouterArret(p_x, p_y, p_echelle);
                if (arretEstNouvelle) {
                    m_arretsNouveauTrajet.add(m_reseauTransport.getListeArrets().getLast());
                    m_reseauTransport.getPileSelection().ajouter(m_arretsNouveauTrajet.getLast());
                    succes = true;
                }
            }
            else {
                Arret arret = (Arret) m_reseauTransport.getPileSelection().getDessus();
                if (m_reseauTransport.getPileSelection().contient(arret)) {
                    m_arretsNouveauTrajet.add(arret);
                    succes = true;
                } else {
                    m_arretsNouveauTrajet.clear();
                }
            }
            
            Circuit circuitSelectionne = obtenirCircuitSelectionne();
            if (circuitSelectionne != null)
            {
                // Reste à gérer les culs de sac.. Peut-être modifier canceller circuit pour la cause
                if (circuitSelectionne.getVeutBoucler())
                {
                    m_reseauTransport.getPileSelection().enlever(m_arretsNouveauTrajet.getLast());
                    
                    m_arretsNouveauTrajet.clear();
                    
                    if (arretEstNouvelle)
                        m_reseauTransport.getListeArrets().removeLast();
                    
                    throw new IllegalArgumentException("Le circuit boucle.", new Throwable("Construction impossible"));
                }
                
                else if (succes)
                    m_arretsNouveauTrajet.addFirst(circuitSelectionne.getListeArretTrajet().getLast().getArret());
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
                //m_reseauTransport.getListeCircuits().getLast(). wtf comment on fait pour sélectionner ??
            }
            else
            {
                circuitSelectionne.getListeArretTrajet().removeLast();
                circuitSelectionne.getListeArretTrajet().add(new PaireArretTrajet(arretInitiale, trajet));
                circuitSelectionne.getListeArretTrajet().add(new PaireArretTrajet(arretFinale, null));
                if (circuitSelectionne.getListeArretTrajet().getFirst().getArret() == arretFinale)
                    circuitSelectionne.setPeutBoucler(true);
                else
                    circuitSelectionne.setPeutBoucler(false);
            }            
            
            m_arretsNouveauTrajet.clear();
            m_tronconsNouveauTrajet.clear();
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
            m_tronconsNouveauTrajet = m_reseauTransport.dijkstra(arretInitiale.getEmplacement(), arretFinale.getEmplacement());
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

    public void editerCircuit(Circuit circuit, Integer p_x, Integer p_y, Float p_echelle) {
//        Arret arretPrecedent = circuit.getListeArretTrajet().getLast().getArret();
//        Boolean arretEstNouvelle = false;
//
//        if (m_modeNouvelArret) {
//
//            if (circuit.getPeutBoucler()) {
//                return;
//            }
//            Arret nouvArret;
//            ElementTransport nouvET = obtenirElementTransport(p_x, p_y, p_echelle);
//            if (nouvET == null || nouvET.getClass() != Arret.class) {
//                arretEstNouvelle = ajouterArret(p_x, p_y, p_echelle);
//                if (arretEstNouvelle)
//                {
//                    nouvArret = m_reseauTransport.getListeArrets().getLast();
//                    m_reseauTransport.getPileSelection().ajouter(nouvArret);
//                }
//                else
//                {
//                    return;
//                }
//            }
//            else
//            {
//                nouvArret = (Arret) nouvET;
//            }
//
//            //verifier que l'arret n'est pas deja dans le circuit ou si premier boucler
//            Boolean premier = true;
//            for (PaireArretTrajet pat : circuit.getListeArretTrajet()) {
//                if (pat.getArret() == nouvArret && premier) {
//                    circuit.setPeutBoucler(true);
//                } 
//                else if (pat.getArret() == nouvArret) {
//                    return;
//                }
//                 premier = false;
//            }
//
//            Emplacement emplPrec = arretPrecedent.getEmplacement();
//            Emplacement emplNouv = nouvArret.getEmplacement();
//            Boolean precSurTrc = emplPrec.estSurTroncon();
//            Boolean nouvSurTrc = emplNouv.estSurTroncon();
//
//            Boolean memeTronconBonSens = precSurTrc && nouvSurTrc && emplPrec.getTroncon() == emplNouv.getTroncon() && emplPrec.getPourcentageParcouru() < emplNouv.getPourcentageParcouru();
//            Boolean trcVersInterDestImmediate = precSurTrc && !nouvSurTrc && emplPrec.getTroncon().getDestination() == emplNouv.getIntersection();
//            Boolean interOrigVersTrcImmediat = !precSurTrc && nouvSurTrc && emplPrec.getIntersection() == emplNouv.getTroncon().getOrigine();
//            Boolean trcVersTrcSuivant = precSurTrc && nouvSurTrc && emplPrec.getTroncon().getDestination() == emplNouv.getTroncon().getOrigine();
//
//            if (memeTronconBonSens || trcVersInterDestImmediate || interOrigVersTrcImmediat || trcVersTrcSuivant) {
//                LinkedList<Troncon> listetmp = new LinkedList<>();
//                if (!interOrigVersTrcImmediat) {
//                    listetmp.add(emplPrec.getTroncon());
//                }
//                if (trcVersTrcSuivant || interOrigVersTrcImmediat) {
//                    listetmp.add(emplNouv.getTroncon());
//                }
//                Trajet trj = new Trajet(emplPrec, emplNouv, listetmp);
//                circuit.ajouterPaire(nouvArret, null);
//                circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 2).setTrajet(trj);
//                cancellerCircuit();
//                return;
//            }
//
//            if(!m_reseauTransport.arretsSontConnectables(arretPrecedent, nouvArret)){
//                cancellerCircuit();
//                m_reseauTransport.getPileSelection().enlever(nouvArret);
//                if (arretEstNouvelle)
//                    m_reseauTransport.getListeArrets().removeLast();
//                throw new IllegalArgumentException("L'arrêt n'est pas atteignable.", new Throwable("Construction impossible"));
//            }
//            
//            //mettre en couleur le troncon partiel apres l'arret precedent
////            if (circuit.getListeArretTrajet().getLast().getArret().getEmplacement().estSurTroncon()) {
////                Troncon trc = circuit.getListeArretTrajet().getLast().getArret().getEmplacement().getTroncon();
////                trc.changerStatutSelection();
////            }
//
//            //mettre en couleur le troncon partiel avant le nouvel arret
////            if (nouvArret.getEmplacement().estSurTroncon()) {
////                Troncon trc = nouvArret.getEmplacement().getTroncon();
////                trc.changerStatutSelection();
////            }
//            
//            m_modeNouvelArret = false;
//
//            m_arret_temp = nouvArret;
//            if (emplPrec.estSurTroncon()) {
//                m_trajet_temp.getListeTroncons().add(emplPrec.getTroncon());
//            }
//            m_trajet_temp.setEmplacementInitial(emplPrec);
//
//            //pour les suggestions
//            Intersection interSugg;
//            if (precSurTrc) {
//                interSugg = emplPrec.getTroncon().getDestination();
//            } else {
//                interSugg = emplPrec.getIntersection();
//            }
//            for (Intersection intrsct : m_reseauRoutier.getIntersections()) {
//                for (Troncon trc : intrsct.getTroncons()) {
//                    if (interSugg.getTroncons().contains(trc)) {
//                        trc.setEstSuggere(true);
//                    } else {
//                        trc.setEstSuggere(false);
//                    }
//                }
//            }
//            if (m_dijkstra) {
//                editerCircuit(circuit, p_x, p_y, p_echelle);
//            }
//        } else { //mode trajet     
//            if (m_dijkstra) {     
//                m_trajet_temp.setListeTroncons(m_reseauTransport.dijkstra(arretPrecedent.getEmplacement(), m_arret_temp.getEmplacement()));
//                m_trajet_temp.setEmplacementFinal(m_arret_temp.getEmplacement());
//                circuit.ajouterPaire(m_arret_temp, null);
//                circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 2).setTrajet(m_trajet_temp);
//                cancellerCircuit();
//                return;
//            }
//
//            ElementRoutier nouvER = obtenirElementRoutier(p_x, p_y, p_echelle);
//            if (nouvER == null || nouvER.getClass() != Troncon.class) {
//                return;
//            }
//            Troncon nouvTroncon = (Troncon) nouvER;
//
//            if (m_trajet_temp.getListeTroncons().isEmpty()) { //trajet pas encore créé
//                if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 1)
//                        .getArret().getEmplacement().estSurTroncon()) { //il faut vérifier que c'est après le dernier arret
//                    if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 1)
//                            .getArret().getEmplacement().getTroncon().getDestination() != nouvTroncon.getOrigine()) {
//                        return;
//                    }
//                } else if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 1)
//                        .getArret().getEmplacement().getIntersection() != nouvTroncon.getOrigine()) {
//                    return;
//                }
//            } else if (!nouvTroncon.getOrigine().equals(m_trajet_temp.getListeTroncons().getLast().getDestination())) {
//                //il faut que ça soit contigu
//                return;
//            }
//
//            m_reseauRoutier.getPileSelection().ajouter(nouvTroncon);
//            m_trajet_temp.getListeTroncons().add(nouvTroncon);
//
//            for (Intersection intrsct : m_reseauRoutier.getIntersections()) {
//                for (Troncon trc : intrsct.getTroncons()) {
//                    if (nouvTroncon.getDestination().getTroncons().contains(trc)) {
//                        trc.setEstSuggere(true);
//                    } else {
//                        trc.setEstSuggere(false);
//                    }
//                }
//            }
//
//            //si dernier troncon avant l'arret on push le trajet
//            if (m_arret_temp.getEmplacement().estSurTroncon()) {
//                if (nouvTroncon.getDestination() == m_arret_temp.getEmplacement().getTroncon().getOrigine()) {
//                    m_trajet_temp.getListeTroncons().addLast(m_arret_temp.getEmplacement().getTroncon());
//                    m_trajet_temp.setEmplacementFinal(m_arret_temp.getEmplacement());
//                    circuit.ajouterPaire(m_arret_temp, null);
//                    circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 2).setTrajet(m_trajet_temp);
//
//                    cancellerCircuit();
//                }
//            } else //arret sur intersection
//            if (nouvTroncon.getDestination() == m_arret_temp.getEmplacement().getIntersection()) {
//                m_trajet_temp.setEmplacementFinal(m_arret_temp.getEmplacement());
//                circuit.ajouterPaire(m_arret_temp, null);
//                circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 2).setTrajet(m_trajet_temp);
//
//                cancellerCircuit();
//            }
//        }
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
                Distribution distributionDefault = new Distribution();
                distributionDefault.setDistribution(new Temps(15 * 60), new Temps(15 * 60), new Temps(15 * 60));
                for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
                    Circuit circuit = circuits.next();
                    if (m_reseauTransport.getPileSelection().contient(circuit)) {
                        m_reseauTransport.ajoutSource(emplacement, circuit, "Source", distributionDefault, new Temps(0));
                        return;
                    }
                }
            }
        } else if (elementRoutier != null) {
            if (elementRoutier.getClass() == Intersection.class) {
                Intersection intersection = (Intersection) elementRoutier;
                Emplacement emplacement = new Emplacement(false, 0, null, intersection);
                Distribution distributionDefault = new Distribution();
                distributionDefault.setDistribution(new Temps(15 * 60), new Temps(15 * 60), new Temps(15 * 60));
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
                                    m_reseauTransport.ajoutSource(emplacement, circuit, "Source", distributionDefault, new Temps(0));
                                        return;
                                }
                            }
                            if(interMax2 != null){
                                if(intersection.equals(interMax2)){
                                    m_reseauTransport.ajoutSource(emplacement, circuit, "Source", distributionDefault, new Temps(0));
                                        return;
                                }
                            }
                            for (ListIterator<Troncon> troncons = paire.getTrajet().getListeTroncons().listIterator(); troncons.hasNext();) {
                                Troncon troncon = troncons.next();
                                if(intersection.equals(troncon.getDestination())){
                                    if(troncons.hasNext()){
                                        m_reseauTransport.ajoutSource(emplacement, circuit, "Source", distributionDefault, new Temps(0));
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
                                                if (!circuit.getPeutBoucler()) {
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
                                            Distribution distributionDefault = new Distribution();
                                            distributionDefault.setDistribution(new Temps(15 * 60), new Temps(15 * 60), new Temps(15 * 60));
                                            m_reseauTransport.ajoutSource(emplacement, circuit, "Source", distributionDefault, new Temps(0));
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

        m_reseauRoutier.supprimerSelection();
        ajusterDoubleSens();
        return true;
    }

    public Boolean supprimerSelectionTransport() {
        Boolean supprimee = m_reseauTransport.supprimerSelection();
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
            m_reseauTransport.optimiserCircuitsAffectes(circuitsAffectes, p_tronconModifie);
    }
    
    public Circuit obtenirCircuitDeSource(SourceAutobus src) {
        for (Circuit circ : m_reseauTransport.getListeCircuits()) {
            if (circ.getListeSources().contains(src)) {
                return circ;
            }
        }
        return null;
    }
    
//    public void annulerDerniereAction() {
//        String str = m_reculelrRetablir.getLastAction();
//        if (str != null) {
//            String[] parts = str.split(Pattern.quote("\t"));
//            if (parts.length == 4) {
//                String action = parts[0];
//                int x = Integer.parseInt(parts[1]);
//                int y = Integer.parseInt(parts[2]);
//                float echelle = Float.parseFloat(parts[3]);
//                switch (action) {
//                    case "ajouterIntersection":
//                        deselectionnerRoutier();
//                        selectionnerElementRoutier(x, y, echelle, false);
//                        supprimerSelectionRoutier();
//                        break;
//                    case "construireTroncon":
//                        deselectionnerRoutier();
//                        selectionnerElementRoutier(x, y, echelle, false);
//                        for (ListIterator<Intersection> intersections = m_reseauRoutier.getIntersections().listIterator(); intersections.hasNext();) {
//                            Intersection intersection = intersections.next();
//                            if (intersection.estSelectionne()) {
//                                intersection.getTroncons().getFirst().changerStatutSelection();
//                                intersection.changerStatutSelection();
//                                break;
//                            }
//                        }
//                        supprimerSelectionRoutier();
//                }
//            }
//        }
//    }

}
