package Domaine;

import Domaine.ReseauRoutier.*;
import Domaine.ReseauTransport.*;
import Domaine.BesoinsTransport.*;
import Domaine.Utilitaire.*;
import java.awt.geom.Point2D;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Pattern;

/**
 *
 * @author vinny
 */
public class Simulatheure {

    public enum Modes {

        ROUTIER, TRANSPORT, BESOINS, SIMULATION
    }

    public enum Commandes {

        SELECTIONNER, INTERSECTION, TRONCON, ARRET, SOURCE, AJOUTERCIRCUIT, EDITERCIRCUIT
    }
    private Simulatheure_log m_log = new Simulatheure_log();
    private RecullerRetablir m_reculelrRetablir = new RecullerRetablir();
    private ReseauRoutier m_reseauRoutier = new ReseauRoutier();
    private LinkedList<Intersection> m_parametresTroncon = new LinkedList<>();

    private ReseauTransport m_reseauTransport;
    private LinkedList<Arret> m_arretsNouveauCircuit = new LinkedList<>();
    private LinkedList<Troncon> m_tronconsNouveauTrajet = new LinkedList<>();
    private Trajet m_trajet_temp = new Trajet();
    private Boolean m_modeNouvelArret = true;
    private Arret m_arret_temp = new Arret();
    private Boolean m_dijkstra = false;

    private LinkedList<BesoinTransport> m_listBesoins = new LinkedList<>();

    public Simulatheure() {
        m_reseauRoutier = new ReseauRoutier();
        m_reseauTransport = new ReseauTransport(m_reseauRoutier);
    }

    public void arreterSimulation() {
        for (ListIterator<Arret> arrets = m_reseauTransport.getListArrets().listIterator(); arrets.hasNext();) {
            Arret arret = arrets.next();
            arret.viderFile();
        }
        for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSourceAutobus().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                source.setDefault();
            }
        }
        for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSourceAutobus().listIterator(); sources.hasNext();) {
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

            elementRoutier.changerStatutSelection();
        }
        m_log.ajouterAction("selectionnerElementRoutier".concat("\t").concat(p_x.toString()).concat("\t").concat(p_y.toString()).concat("\t").concat(p_echelle.toString()));
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
        m_arretsNouveauCircuit.clear();
        m_tronconsNouveauTrajet.clear();
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
        String action = "ajouterIntersection\t".concat(p_x.toString()).concat("\t").concat(p_y.toString()).concat("\t").concat(p_echelle.toString());
        m_log.ajouterAction(action);
        m_reculelrRetablir.ajouterAction(action);

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

        Intersection intersection = m_reseauRoutier.selectionnerIntersection(xReel, yReel, largeurSelection);
        if (intersection == null) {
            ajouterIntersection(p_x, p_y, p_echelle);
            m_parametresTroncon.add(m_reseauRoutier.getIntersections().getLast());
            m_parametresTroncon.getLast().changerStatutSelection();
        }
        else
        {
            if (intersection.estSelectionne())
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
                origine.changerStatutSelection();

                m_reseauRoutier.ajouterTroncon(origine, destination);
                ajusterDoubleSens();
            }
    }

    public ElementTransport selectionnerElementTransport(Integer p_x, Integer p_y, Float p_echelle) {
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

        SourceAutobus src = m_reseauTransport.selectionnerSourceAutobus(xReel, yReel, largeurSelection, p_echelle);
        if (src != null) {
            return src;
        } else {
            return m_reseauTransport.selectionnerArret(xReel, yReel, largeurSelection, p_echelle);
        }
    }

    public void ajouterArret(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;
        for (ListIterator<Intersection> intersection = m_reseauRoutier.getIntersections().listIterator(); intersection.hasNext();) {
            Intersection intersectionOrigin = intersection.next();
            if (intersectionOrigin.estSelectionne()) {
                for (Arret arret : m_reseauTransport.getListArrets()) {
                    if (!arret.getEmplacement().estSurTroncon()) {
                        if (arret.getEmplacement().getIntersection() == intersectionOrigin) {
                            return;
                        }
                    }
                }
                Point2D.Float p2 = new Point2D.Float(xReel, yReel);
                Emplacement arretSurIntersection = new Emplacement(false, 0, null, intersectionOrigin);
                m_reseauTransport.ajouterArret(new Arret(arretSurIntersection, ""));
                return;
            }

            for (ListIterator<Troncon> troncons = intersectionOrigin.getTroncons().listIterator(); troncons.hasNext();) {
                Troncon troncon = troncons.next();
                if (troncon.estSelectionne()) {
                    Point2D.Float p1 = new Point2D.Float(xReel, yReel);
                    double distance1 = intersectionOrigin.getPosition().distance(p1);
                    double distance2 = troncon.getLongueurTroncon();
                    float pourcentage = (float) (distance1 / distance2);
                    Emplacement emplacement = new Emplacement(true, pourcentage, troncon, intersectionOrigin);
                    m_reseauTransport.ajouterArret(new Arret(emplacement, ""));
                    return;
                }
            }
        }
    }

    public void cancellerCircuit() {
        deselectionnerRoutier();
        for (Intersection intrsct : m_reseauRoutier.getIntersections()) {
            for (Troncon trc : intrsct.getTroncons()) {
                if (trc.estSuggere()) {
                    trc.setEstSuggere(false);
                }
            }
        }
        m_trajet_temp = new Trajet();
        m_arret_temp = new Arret();
        m_modeNouvelArret = true;
    }

    public boolean construireCircuit(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel;
        float yReel;
        float largeurSelection;
        Boolean estConstructible = false;
        Arret arretInitiale;
        Arret arretFinale;

        if (m_arretsNouveauCircuit.size() < 2) {
            if (p_echelle > 1) {
                xReel = (p_x - Arret.RAYON) / p_echelle;
                yReel = (p_y - Arret.RAYON) / p_echelle;
                largeurSelection = 2 * Arret.RAYON / p_echelle;
            } else {
                xReel = p_x / p_echelle - Arret.RAYON;
                yReel = p_y / p_echelle - Arret.RAYON;
                largeurSelection = 2 * Arret.RAYON;
            }

            Arret arret = m_reseauTransport.selectionnerArret(xReel, yReel, largeurSelection, p_echelle);
            if (arret != null) {
                if (arret.estSelectionne()) {
                    m_arretsNouveauCircuit.add(arret);
                } else {
                    m_arretsNouveauCircuit.clear();
                }
            }

            if (m_arretsNouveauCircuit.size() == 2) {
                arretInitiale = m_arretsNouveauCircuit.getFirst();
                arretFinale = m_arretsNouveauCircuit.getLast();
                
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
                    } else {
                        estConstructible = m_tronconsNouveauTrajet.getLast().getDestination() == arretFinale.getEmplacement().getIntersection();
                    }
                } else {
                    for (Troncon troncon : arretInitiale.getEmplacement().getIntersection().getTroncons()) {
                        troncon.setEstSuggere(true);
                    }

                    if (arretFinale.getEmplacement().estSurTroncon()) {
                        m_tronconsNouveauTrajet.add(arretFinale.getEmplacement().getTroncon());
                        estConstructible = arretFinale.getEmplacement().getTroncon().estSuggere();
                    }
                }
                if (!estConstructible)
                {                
                    if (!m_reseauTransport.arretSontConnectables(arretInitiale, arretFinale))
                    {
                        m_arretsNouveauCircuit.removeLast();
                        arretFinale.changerStatutSelection();
                        if (arretFinale.getEmplacement().estSurTroncon())
                            m_tronconsNouveauTrajet.removeLast();
                        m_reseauRoutier.desuggererTout();
                        throw new RuntimeException("L'arrêt n'est pas atteignable", new Throwable("Construction impossible"));
                    }
                    else if(m_dijkstra)
                    {
                        m_reseauRoutier.desuggererTout();
                        estConstructible = construireCircuit(p_x, p_y, p_echelle);
                        return estConstructible;
                    }
                }
            }

        } else {
            arretInitiale = m_arretsNouveauCircuit.getFirst();
            arretFinale = m_arretsNouveauCircuit.getLast();
            
            if(m_dijkstra){
                estConstructible = true;
                LinkedList<Troncon> dijk = new LinkedList<>();
                if (arretInitiale.getEmplacement().estSurTroncon()) {
                    if (arretFinale.getEmplacement().estSurTroncon()) {
                        dijk = m_reseauTransport.dijkstra(arretInitiale.getEmplacement().getTroncon().getDestination(), arretFinale.getEmplacement().getTroncon().getOrigine());
                        dijk.addFirst(arretInitiale.getEmplacement().getTroncon());
                        dijk.addLast(arretFinale.getEmplacement().getTroncon());
                    } else {
                        dijk = m_reseauTransport.dijkstra(arretInitiale.getEmplacement().getTroncon().getDestination(), arretFinale.getEmplacement().getIntersection());
                        dijk.addFirst(arretInitiale.getEmplacement().getTroncon());
                    }
                } else {
                    if (arretFinale.getEmplacement().estSurTroncon()) {
                        dijk = m_reseauTransport.dijkstra(arretInitiale.getEmplacement().getIntersection(), arretFinale.getEmplacement().getTroncon().getOrigine());
                        dijk.addLast(arretFinale.getEmplacement().getTroncon());
                    } else {
                        dijk = m_reseauTransport.dijkstra(arretInitiale.getEmplacement().getIntersection(), arretFinale.getEmplacement().getIntersection());
                    }
                }

                m_tronconsNouveauTrajet = dijk;
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
                    tronconSelectionne.changerStatutSelection();
                    for (Troncon troncon : tronconSelectionne.getDestination().getTroncons()) {
                        if (!m_tronconsNouveauTrajet.contains(troncon)) {
                            troncon.setEstSuggere(true);
                        }
                    }
                }
            }
        }

        if (estConstructible) {
            arretInitiale = m_arretsNouveauCircuit.getFirst();
            arretFinale = m_arretsNouveauCircuit.getLast();
            Trajet trajet = new Trajet(arretInitiale.getEmplacement(), arretFinale.getEmplacement(), new LinkedList<>(m_tronconsNouveauTrajet));

            LinkedList<PaireArretTrajet> listePaires = new LinkedList<>();
            listePaires.add(new PaireArretTrajet(arretInitiale, trajet));
            listePaires.add(new PaireArretTrajet(arretFinale, null));

            this.m_reseauTransport.ajouterCircuit(new Circuit(listePaires));
        }
        return estConstructible;
    }

    public void editerCircuit(Circuit circuit, Integer p_x, Integer p_y, Float p_echelle) {
        Arret arretPrecedent = circuit.getListeArretTrajet().getLast().getArret();

        if (m_modeNouvelArret) {

            if (circuit.getBoucle()) {
                return;
            }

            ElementTransport nouvET = selectionnerElementTransport(p_x, p_y, p_echelle);
            if (nouvET == null || nouvET.getClass() != Arret.class) {
                return;
            }
            Arret nouvArret = (Arret) nouvET;

            m_reseauTransport.arretSontConnectables(arretPrecedent, nouvArret);
            
            //verifier que l'arret n'est pas deja dans le circuit ou si premier boucler
            Boolean premier = true;
            for (PaireArretTrajet pat : circuit.getListeArretTrajet()) {
                if (pat.getArret() == nouvArret && premier) {
                    circuit.setBoucle(true);
                    premier = false;
                } else if (pat.getArret() == nouvArret) {
                    return;
                }
            }

            Emplacement emplPrec = arretPrecedent.getEmplacement();
            Emplacement emplNouv = nouvArret.getEmplacement();
            Boolean precSurTrc = emplPrec.estSurTroncon();
            Boolean nouvSurTrc = emplNouv.estSurTroncon();

            Boolean memeTronconBonSens = precSurTrc && nouvSurTrc && emplPrec.getTroncon() == emplNouv.getTroncon() && emplPrec.getPourcentageParcouru() < emplNouv.getPourcentageParcouru();
            Boolean trcVersInterDestImmediate = precSurTrc && !nouvSurTrc && emplPrec.getTroncon().getDestination() == emplNouv.getIntersection();
            Boolean interOrigVersTrcImmediat = !precSurTrc && nouvSurTrc && emplPrec.getIntersection() == emplNouv.getTroncon().getOrigine();
            Boolean trcVersTrcSuivant = precSurTrc && nouvSurTrc && emplPrec.getTroncon().getDestination() == emplNouv.getTroncon().getOrigine();

            if (memeTronconBonSens || trcVersInterDestImmediate || interOrigVersTrcImmediat || trcVersTrcSuivant) {
                LinkedList<Troncon> listetmp = new LinkedList<>();
                if (!interOrigVersTrcImmediat) {
                    listetmp.add(emplPrec.getTroncon());
                }
                if (trcVersTrcSuivant || interOrigVersTrcImmediat) {
                    listetmp.add(emplNouv.getTroncon());
                }
                Trajet trj = new Trajet(emplPrec, emplNouv, listetmp);
                circuit.ajouterPaire(nouvArret, null);
                circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 2).setTrajet(trj);
                cancellerCircuit();
                return;
            }

            //mettre en couleur le troncon partiel apres l'arret precedent
            if (circuit.getListeArretTrajet().getLast().getArret().getEmplacement().estSurTroncon()) {
                Troncon trc = circuit.getListeArretTrajet().getLast().getArret().getEmplacement().getTroncon();
                trc.changerStatutSelection();
            }

            //mettre en couleur le troncon partiel avant le nouvel arret
            if (nouvArret.getEmplacement().estSurTroncon()) {
                Troncon trc = nouvArret.getEmplacement().getTroncon();
                trc.changerStatutSelection();
            }
            m_modeNouvelArret = false;

            m_arret_temp = nouvArret;
            if (emplPrec.estSurTroncon()) {
                m_trajet_temp.getListeTroncons().add(emplPrec.getTroncon());
            }
            m_trajet_temp.setEmplacementInitial(emplPrec);

            //pour les suggestions
            Intersection interSugg;
            if (precSurTrc) {
                interSugg = emplPrec.getTroncon().getDestination();
            } else {
                interSugg = emplPrec.getIntersection();
            }
            for (Intersection intrsct : m_reseauRoutier.getIntersections()) {
                for (Troncon trc : intrsct.getTroncons()) {
                    if (interSugg.getTroncons().contains(trc)) {
                        trc.setEstSuggere(true);
                    } else {
                        trc.setEstSuggere(false);
                    }
                }
            }
            if (m_dijkstra) {
                editerCircuit(circuit, p_x, p_y, p_echelle);
            }
        } else { //mode trajet     
            if (m_dijkstra) {
                LinkedList<Troncon> dijk = new LinkedList<>();
                if (arretPrecedent.getEmplacement().estSurTroncon()) {
                    if (m_arret_temp.getEmplacement().estSurTroncon()) {
                        dijk = m_reseauTransport.dijkstra(arretPrecedent.getEmplacement().getTroncon().getDestination(), m_arret_temp.getEmplacement().getTroncon().getOrigine());
                        dijk.addFirst(arretPrecedent.getEmplacement().getTroncon());
                        dijk.addLast(m_arret_temp.getEmplacement().getTroncon());
                    } else {
                        dijk = m_reseauTransport.dijkstra(arretPrecedent.getEmplacement().getTroncon().getDestination(), m_arret_temp.getEmplacement().getIntersection());
                        dijk.addFirst(arretPrecedent.getEmplacement().getTroncon());
                    }
                } else {
                    if (m_arret_temp.getEmplacement().estSurTroncon()) {
                        dijk = m_reseauTransport.dijkstra(arretPrecedent.getEmplacement().getIntersection(), m_arret_temp.getEmplacement().getTroncon().getOrigine());
                        dijk.addLast(m_arret_temp.getEmplacement().getTroncon());
                    } else {
                        dijk = m_reseauTransport.dijkstra(arretPrecedent.getEmplacement().getIntersection(), m_arret_temp.getEmplacement().getIntersection());
                    }
                }
                m_trajet_temp.setListeTroncons(dijk);
                m_trajet_temp.setEmplacementFinal(m_arret_temp.getEmplacement());
                circuit.ajouterPaire(m_arret_temp, null);
                circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 2).setTrajet(m_trajet_temp);
                cancellerCircuit();
                return;
            }

            ElementRoutier nouvER = obtenirElementRoutier(p_x, p_y, p_echelle);
            if (nouvER == null || nouvER.getClass() != Troncon.class) {
                return;
            }
            Troncon nouvTroncon = (Troncon) nouvER;

            if (m_trajet_temp.getListeTroncons().isEmpty()) { //trajet pas encore créé
                if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 1)
                        .getArret().getEmplacement().estSurTroncon()) { //il faut vérifier que c'est après le dernier arret
                    if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 1)
                            .getArret().getEmplacement().getTroncon().getDestination() != nouvTroncon.getOrigine()) {
                        return;
                    }
                } else if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 1)
                        .getArret().getEmplacement().getIntersection() != nouvTroncon.getOrigine()) {
                    return;
                }
            } else if (!nouvTroncon.getOrigine().equals(m_trajet_temp.getListeTroncons().getLast().getDestination())) {
                //il faut que ça soit contigu
                return;
            }

            nouvTroncon.changerStatutSelection();
            m_trajet_temp.getListeTroncons().add(nouvTroncon);

            for (Intersection intrsct : m_reseauRoutier.getIntersections()) {
                for (Troncon trc : intrsct.getTroncons()) {
                    if (nouvTroncon.getDestination().getTroncons().contains(trc)) {
                        trc.setEstSuggere(true);
                    } else {
                        trc.setEstSuggere(false);
                    }
                }
            }

            //si dernier troncon avant l'arret on push le trajet
            if (m_arret_temp.getEmplacement().estSurTroncon()) {
                if (nouvTroncon.getDestination() == m_arret_temp.getEmplacement().getTroncon().getOrigine()) {
                    m_trajet_temp.getListeTroncons().addLast(m_arret_temp.getEmplacement().getTroncon());
                    m_trajet_temp.setEmplacementFinal(m_arret_temp.getEmplacement());
                    circuit.ajouterPaire(m_arret_temp, null);
                    circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 2).setTrajet(m_trajet_temp);

                    cancellerCircuit();
                }
            } else //arret sur intersection
            if (nouvTroncon.getDestination() == m_arret_temp.getEmplacement().getIntersection()) {
                m_trajet_temp.setEmplacementFinal(m_arret_temp.getEmplacement());
                circuit.ajouterPaire(m_arret_temp, null);
                circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size() - 2).setTrajet(m_trajet_temp);

                cancellerCircuit();
            }
        }
    }

    public void ajouterSource(Integer p_x, Integer p_y, Float p_echelle) {
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;

        ElementRoutier elementRoutier = selectionnerElementRoutier(p_x, p_y, p_echelle, false);
        ElementTransport elementTransport = selectionnerElementTransport(p_x, p_y, p_echelle);
        if (elementTransport != null) {
            if (elementTransport.getClass() == Arret.class) {
                Arret arret = (Arret) elementTransport;
                Emplacement emplacement = new Emplacement(false, 0, null, null);
                emplacement.copy(arret.getEmplacement());
                Distribution distributionDefault = new Distribution();
                distributionDefault.setDistribution(new Temps(15 * 60), new Temps(15 * 60), new Temps(15 * 60));
                for (ListIterator<Circuit> circuits = m_reseauTransport.getListeCircuits().listIterator(); circuits.hasNext();) {
                    Circuit circuit = circuits.next();
                    if (circuit.estSelectionne()) {
                        m_reseauTransport.ajoutSource(emplacement, circuit, "Source", distributionDefault, new Temps(0));
                    }
                    return;

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
                    if (circuit.estSelectionne()) {
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
                    if (circuit.estSelectionne()) {
                        Arret arret1 = circuit.getListeArretTrajet().getFirst().getArret();
                        Arret arret2 = circuit.getListeArretTrajet().getLast().getArret();
                        for (ListIterator<PaireArretTrajet> paires = circuit.getListeArretTrajet().listIterator(); paires.hasNext();) {
                            PaireArretTrajet paire = paires.next();
                            if (paire.getTrajet() == null) {
                                return;
                            }
                            for (ListIterator<Troncon> troncons = paire.getTrajet().getListeTroncons().listIterator(); troncons.hasNext();) {
                                Troncon troncon = troncons.next();
                                if (troncon.estSelectionne()) {
                                    Point2D.Float p1 = new Point2D.Float(xReel, yReel);
                                    double distance1 = troncon.getOrigine().getPosition().distance(p1);
                                    double distance2 = troncon.getLongueurTroncon();
                                    float pourcentage = (float) (distance1 / distance2);
                                    Boolean avantArret1 = false;
                                    Boolean apresArret2 = false;
                                    Troncon trc1 = null;
                                    Troncon trc2 = null;
                                    if (arret1 != null) {
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
                                                if (!circuit.getBoucle()) {
                                                    if (arret1.getEmplacement().getPourcentageParcouru() > arret2.getEmplacement().getPourcentageParcouru()) {
                                                        if (avantArret1 && apresArret2) {
                                                            return;
                                                        }
                                                    } else if (avantArret1 || apresArret2) {
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                        if (avantArret1 || apresArret2) {
                                            return;
                                        } else {
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
        for (Arret arr : m_reseauTransport.getListArrets()) {
            if (arr.getEmplacement().estSurTroncon()) {
                Troncon trc_arr = arr.getEmplacement().getTroncon();
                if (trc_arr.estSelectionne() || trc_arr.getDestination().estSelectionne()
                        || trc_arr.getOrigine().estSelectionne()) {
                    return false;
                }
            } else if (arr.getEmplacement().getIntersection().estSelectionne()) {
                return false;
            }
        }
        for (Circuit circ : m_reseauTransport.getListeCircuits()) {
            for (PaireArretTrajet pat : circ.getListeArretTrajet()) {
                Trajet traj = pat.getTrajet();
                if (traj != null) {
                    for (Troncon trc : traj.getListeTroncons()) {
                        if (trc.estSelectionne() || trc.getOrigine().estSelectionne()
                                || trc.getDestination().estSelectionne()) {
                            return false;
                        }
                    }
                }
            }
        }

        Boolean supprimee = m_reseauRoutier.supprimerSelection();
        ajusterDoubleSens();
        return supprimee;
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
            if (circ.estSelectionne()) {
                return circ;
            }
        }
        return null;
    }

    public void annulerDerniereAction() {
        String str = m_reculelrRetablir.getLastAction();
        if (str != null) {
            String[] parts = str.split(Pattern.quote("\t"));
            if (parts.length == 4) {
                String action = parts[0];
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                float echelle = Float.parseFloat(parts[3]);
                switch (action) {
                    case "ajouterIntersection":
                        deselectionnerRoutier();
                        selectionnerElementRoutier(x, y, echelle, false);
                        supprimerSelectionRoutier();
                        break;
                    case "construireTroncon":
                        deselectionnerRoutier();
                        selectionnerElementRoutier(x, y, echelle, false);
                        for (ListIterator<Intersection> intersections = m_reseauRoutier.getIntersections().listIterator(); intersections.hasNext();) {
                            Intersection intersection = intersections.next();
                            if (intersection.estSelectionne()) {
                                intersection.getTroncons().getFirst().changerStatutSelection();
                                intersection.changerStatutSelection();
                                break;
                            }
                        }
                        supprimerSelectionRoutier();
                }
            }
        }
    }

}
