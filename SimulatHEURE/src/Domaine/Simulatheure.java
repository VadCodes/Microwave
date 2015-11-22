package Domaine;

import Domaine.ReseauTransport.ElementTransport;
import Domaine.ReseauRoutier.*;
import Domaine.ReseauTransport.*;
import Domaine.BesoinsTransport.*;
import Domaine.Utilitaire.*;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.util.Iterator;

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
        SELECTIONNER, INTERSECTION, TRONCON, ARRET, SOURCE, AJOUTERCIRCUIT, EDITERCIRCUIT
    }
    
    private ReseauRoutier m_reseauRoutier = new ReseauRoutier();
    private LinkedList<Intersection> m_parametresTroncon = new LinkedList<>();
    //private Intersection m_intersectionOriginArret;
    private LinkedList<ElementRoutier> m_parametresElementRoutier = new LinkedList<>();
    //private Troncon m_tronconArret;
    private ReseauTransport m_reseauTransport = new ReseauTransport() ;
    //private Temps m_deltaT;
    private LinkedList<BesoinTransport> m_listBesoins = new LinkedList<>();
    
    private LinkedList<Arret> m_arretsNouveauCircuit = new LinkedList<>();
    private LinkedList<Troncon> m_tronconsNouveauTrajet = new LinkedList<>();
    
//    private Circuit m_circuit_temp = new Circuit();
    private Trajet m_trajet_temp = new Trajet();
    private Boolean m_modeNouvelArret = true;
    private Arret m_arret_temp = new Arret();
    
    public Simulatheure() {}  
    
    public ReseauRoutier getRoutier()
    {
        return m_reseauRoutier;
    }
    
    public ReseauTransport getTransport(){
        return m_reseauTransport;
    }
    
    public void demarrerSimulation(){
        m_reseauRoutier.initReseauRoutier();
        m_reseauTransport.initReseauTransport();
        ListIterator<BesoinTransport> BesoinTransportItr = m_listBesoins.listIterator();
        while (BesoinTransportItr.hasNext()) {
            BesoinTransportItr.next().initBesoinTransport();
        }
    }
    public void rafraichirSimulation(Temps m_deltaT){
        m_reseauTransport.calculEtatReseauTransport(m_deltaT);
    }
    
    public ElementRoutier selectionnerElementRoutier(Integer p_x, Integer p_y, Float p_echelle){
        ElementRoutier er = obtenirElementRoutier(p_x, p_y, p_echelle);
        if (er != null){
            er.changerStatutSelection(); 
            m_parametresElementRoutier.add(er); 
            if (m_parametresElementRoutier.size() == 2)
            {
                ElementRoutier premierElement = m_parametresElementRoutier.getFirst();
                ElementRoutier deuxiemeElement = m_parametresElementRoutier.getLast();
                deselectionnerRoutier();
                selectionnerElementRoutier(p_x, p_y, p_echelle);
            }
        }
        
        return er;
    }
    public ElementRoutier selectionnerPlusieursElementRoutier(Integer p_x, Integer p_y, Float p_echelle){
        ElementRoutier plusieursEr = obtenirElementRoutier(p_x, p_y, p_echelle);
        if (plusieursEr != null){
            plusieursEr.changerStatutSelection(); 
                   
        }
        return plusieursEr;
    }
    
    
    public ElementRoutier obtenirElementRoutier(Integer p_x, Integer p_y, Float p_echelle)
    {
        float xReel;
        float yReel;        
        float largeurSelection;        
        
        if (p_echelle > 1)
        {
            xReel = (p_x - Intersection.RAYON) / p_echelle;
            yReel = (p_y - Intersection.RAYON) / p_echelle;
            largeurSelection = 2 * Intersection.RAYON / p_echelle;
        }
        else
        {
            xReel = p_x / p_echelle - Intersection.RAYON;
            yReel = p_y / p_echelle - Intersection.RAYON;
            largeurSelection = 2 * Intersection.RAYON;
        }        
        
        Intersection intersection = m_reseauRoutier.obtenirIntersection(xReel, yReel, largeurSelection);
        if (intersection == null)
        {
            if (p_echelle > 1)
            {
                xReel = (p_x - Troncon.LARGEUR / 2) / p_echelle;
                yReel = (p_y - Troncon.LARGEUR / 2) / p_echelle;
                largeurSelection = Troncon.LARGEUR / p_echelle;
            }
            else
            {
                xReel = p_x / p_echelle - Troncon.LARGEUR / 2;
                yReel = p_y / p_echelle - Troncon.LARGEUR / 2;
                largeurSelection = Troncon.LARGEUR;
            }
            
            return m_reseauRoutier.obtenirTroncon(xReel, yReel, largeurSelection, p_echelle);
        }
        else{
            return intersection;
        }
    }
    
    public void deselectionnerTout(){
        deselectionnerRoutier();
        deselectionnerTransport();
    }
    
    public void deselectionnerRoutier()
    {
        m_parametresTroncon.clear();
        m_parametresElementRoutier.clear();
        m_reseauRoutier.deselectionnerTout();
    }
        
    public void deselectionnerTransport()
    {
        m_arretsNouveauCircuit.clear();
        m_tronconsNouveauTrajet.clear();
        m_reseauTransport.deselectionnerTout();
    }
    
    public void ajouterIntersection(Integer p_x, Integer p_y, Float p_echelle)
    {
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;  
        m_reseauRoutier.ajouterIntersection(xReel, yReel);
    }
    
    public void construireTroncon(Integer p_x, Integer p_y, Float p_echelle)
    {
        float xReel;
        float yReel;        
        float largeurSelection;        
        
        if (p_echelle > 1)
        {
            xReel = (p_x - Intersection.RAYON) / p_echelle;
            yReel = (p_y - Intersection.RAYON) / p_echelle;
            largeurSelection = 2 * Intersection.RAYON / p_echelle;
        }
        else
        {
            xReel = p_x / p_echelle - Intersection.RAYON;
            yReel = p_y / p_echelle - Intersection.RAYON;
            largeurSelection = 2 * Intersection.RAYON;
        }
        
        Intersection intersection = m_reseauRoutier.selectionnerIntersection(xReel, yReel, largeurSelection);
        if (intersection != null)
        {
            m_parametresTroncon.add(intersection);
            if (m_parametresTroncon.size() == 2)
            {
                Intersection origine = m_parametresTroncon.getFirst();
                Intersection destination = m_parametresTroncon.getLast();
                
                m_reseauRoutier.ajouterTroncon(origine, destination);
                ajusterDoubleSens();
                deselectionnerRoutier();
            }
        }
    }
    
    public ElementTransport selectionnerElementTransport(Integer p_x, Integer p_y, Float p_echelle){
        float xReel;
        float yReel;        
        float largeurSelection;         
          if (p_echelle > 1)
        {
            xReel = (p_x - Arret.RAYON) / p_echelle;
            yReel = (p_y - Arret.RAYON) / p_echelle;
            largeurSelection = 2 * Arret.RAYON / p_echelle;
        }
        else
        {
            xReel = p_x / p_echelle - Arret.RAYON;
            yReel = p_y / p_echelle - Arret.RAYON;
            largeurSelection = 2 * Arret.RAYON;
        }        
        
        return m_reseauTransport.selectionnerArret(xReel, yReel, largeurSelection, p_echelle);
    }
    
    public void ajouterArret(Integer p_x, Integer p_y, Float p_echelle){
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;  
        for (ListIterator<Intersection> intersection =m_reseauRoutier.getIntersections().listIterator() ; intersection.hasNext() ; ){
            Intersection intersectionOrigin = intersection.next();
                if(intersectionOrigin.estSelectionne()){
                    for(Arret arret : m_reseauTransport.getListArrets()){
                        if(!arret.getEmplacement().estSurTroncon()){
                        if(arret.getEmplacement().getIntersection() == intersectionOrigin){
                            return; 
                        }
                        }
                    }
                Point2D.Float p2 = new Point2D.Float(xReel,yReel);
                Emplacement arretSurIntersection = new Emplacement(false, 0, null,intersectionOrigin);
                m_reseauTransport.ajouterArret(new Arret(arretSurIntersection, ""));  
                return;
                }                
             
    
            for (ListIterator<Troncon> troncons = intersectionOrigin.getTroncons().listIterator() ; troncons.hasNext() ; ){
                Troncon troncon = troncons.next();
                if(troncon.estSelectionne()){
                    Point2D.Float p1 = new Point2D.Float(xReel,yReel);
                    double distance1 = intersectionOrigin.getPosition().distance(p1);
                    double distance2 = troncon.getLongueurTroncon();
                    float pourcentage = (float) (distance1/distance2);
                     Emplacement emplacement = new Emplacement(true, pourcentage,troncon,intersectionOrigin);
                     m_reseauTransport.ajouterArret(new Arret(emplacement, ""));
                     return;
                }
            }
        }
    }
    
    public void cancellerCircuit(){
        deselectionnerRoutier();
//        m_circuit_temp = new Circuit();
        m_trajet_temp = new Trajet();
        m_arret_temp = new Arret();
        m_modeNouvelArret = true;
    }

    public boolean construireCircuit(Integer p_x, Integer p_y, Float p_echelle)
    {
        float xReel;
        float yReel;        
        float largeurSelection;
        Boolean estConstructible = false;
        
        if (m_arretsNouveauCircuit.size() < 2)
        {
            if (p_echelle > 1)
            {
                xReel = (p_x - Arret.RAYON) / p_echelle;
                yReel = (p_y - Arret.RAYON) / p_echelle;
                largeurSelection = 2 * Arret.RAYON / p_echelle;
            }
            else
            {
                xReel = p_x / p_echelle - Arret.RAYON;
                yReel = p_y / p_echelle - Arret.RAYON;
                largeurSelection = 2 * Arret.RAYON;
            }
            
            Arret arretSelectionnee = this.m_reseauTransport.selectionnerArret(xReel, yReel, largeurSelection, p_echelle);
            if (arretSelectionnee != null && m_arretsNouveauCircuit.isEmpty())
            {
                m_arretsNouveauCircuit.add(arretSelectionnee);
            }
            else if (arretSelectionnee != null && m_arretsNouveauCircuit.getFirst() != arretSelectionnee)
            {
                m_arretsNouveauCircuit.add(arretSelectionnee);
            }
            
            if (m_arretsNouveauCircuit.size() == 2)
            {
                Arret arretInitiale = m_arretsNouveauCircuit.getFirst();
                Arret arretFinale = m_arretsNouveauCircuit.getLast();

                LinkedList<PaireArretTrajet> listePaires = new LinkedList<>();
                listePaires.add(new PaireArretTrajet(arretFinale, null));

                if (arretInitiale.getEmplacement().estSurTroncon())
                {
                    m_tronconsNouveauTrajet.add(arretInitiale.getEmplacement().getTroncon());

                    if (arretFinale.getEmplacement().estSurTroncon())
                    {
                        m_tronconsNouveauTrajet.add(arretFinale.getEmplacement().getTroncon());

                        if (m_tronconsNouveauTrajet.getFirst() == m_tronconsNouveauTrajet.getLast())
                        {
                            m_tronconsNouveauTrajet.removeLast();
                            estConstructible = true;
                        }
                        else if (m_tronconsNouveauTrajet.getFirst().getDestination() == m_tronconsNouveauTrajet.getLast().getOrigine())
                        {
                            estConstructible = true;
                        }
                    }
                    else if (m_tronconsNouveauTrajet.getFirst().getDestination() == arretFinale.getEmplacement().getIntersection())
                    {
                        estConstructible = true;
                    }
                }
                else if (arretFinale.getEmplacement().estSurTroncon())
                {
                    m_tronconsNouveauTrajet.add(arretFinale.getEmplacement().getTroncon());

                    if (m_tronconsNouveauTrajet.getFirst().getOrigine() == arretInitiale.getEmplacement().getIntersection())
                    {
                        estConstructible = true;
                    }
                }
                else
                {
                    for (Troncon troncon: arretInitiale.getEmplacement().getIntersection().getTroncons())
                    {
                        if (troncon.getDestination() == arretFinale.getEmplacement().getIntersection())
                        {
                            m_tronconsNouveauTrajet.add(troncon);
                            estConstructible = true;
                            break;
                        }
                    }
                }

                if (estConstructible)
                {
                    Trajet trajet = new Trajet(arretInitiale.getEmplacement(), arretFinale.getEmplacement(), new LinkedList<>(m_tronconsNouveauTrajet));
                    listePaires.addFirst(new PaireArretTrajet(arretInitiale, trajet));

                    this.m_reseauTransport.ajouterCircuit(new Circuit(listePaires, m_reseauRoutier));
                    return estConstructible;
                }
                else
                {
                    for (Troncon troncon: m_tronconsNouveauTrajet)
                    {
                        troncon.changerStatutSelection();
                    }
                }
            }
        }
        else
        {
            if (p_echelle > 1)
            {
                xReel = (p_x - Troncon.LARGEUR / 2) / p_echelle;
                yReel = (p_y - Troncon.LARGEUR / 2) / p_echelle;
                largeurSelection = Troncon.LARGEUR / p_echelle;
            }
            else
            {
                xReel = p_x / p_echelle - Troncon.LARGEUR / 2;
                yReel = p_y / p_echelle - Troncon.LARGEUR / 2;
                largeurSelection = Troncon.LARGEUR;
            }
            
            Troncon tronconSelectionne = m_reseauRoutier.obtenirTroncon(xReel, yReel, largeurSelection, p_echelle);
            if (tronconSelectionne != null)
            {
                
            }
            LinkedList<Troncon> troncons = new LinkedList<>();
            if (m_arretsNouveauCircuit.getFirst().getEmplacement().estSurTroncon())
            {
                m_arretsNouveauCircuit.getFirst().getEmplacement().getTroncon().changerStatutSelection();
                m_tronconsNouveauTrajet.add(m_arretsNouveauCircuit.getFirst().getEmplacement().getTroncon());
                
                if (m_arretsNouveauCircuit.getLast().getEmplacement().estSurTroncon())
                {
                    if (m_arretsNouveauCircuit.getFirst().getEmplacement().getTroncon() == m_arretsNouveauCircuit.getLast().getEmplacement().getTroncon())
                    {
                        //Trajet
                    }
                }
            }
        }
        return estConstructible;
        
        
    }
    
    public void editerCircuit(Circuit circuit, Integer p_x, Integer p_y, Float p_echelle){
        Arret arretPrecedent = circuit.getListeArretTrajet().getLast().getArret();
        
        if (m_modeNouvelArret){

            ElementTransport nouvET = selectionnerElementTransport(p_x, p_y, p_echelle);
            if (nouvET == null || nouvET.getClass() != Arret.class){
                return;
            }
            Arret nouvArret = (Arret) nouvET;

            //verifier que l'arret n'est pas deja dans le circuit ou si premier boucler
            Boolean premier = true;
            for(PaireArretTrajet pat : circuit.getListeArretTrajet()){
                if (premier){
                    circuit.setBoucle(true);
                    premier = false;
                }
                else if (pat.getArret() == nouvArret){
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

            if (memeTronconBonSens || trcVersInterDestImmediate || interOrigVersTrcImmediat || trcVersTrcSuivant){
                LinkedList<Troncon> listetmp = new LinkedList<>();
                listetmp.add(emplPrec.getTroncon());
                if (trcVersTrcSuivant){
                    listetmp.add(emplNouv.getTroncon());
                }
                Trajet trj = new Trajet(emplPrec, emplNouv, listetmp);
                circuit.ajouterPaire(nouvArret, null);
                circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-2).setTrajet(trj);
                cancellerCircuit();
                return;
            }
            
            //mettre en couleur le troncon partiel apres l'arret precedent
            if(circuit.getListeArretTrajet().getLast().getArret().getEmplacement().estSurTroncon()){
                Troncon trc = circuit.getListeArretTrajet().getLast().getArret().getEmplacement().getTroncon();
                trc.changerStatutSelection();
            }

            //mettre en couleur le troncon partiel avant le nouvel arret
            if(nouvArret.getEmplacement().estSurTroncon()){
                Troncon trc = nouvArret.getEmplacement().getTroncon();
                trc.changerStatutSelection();
            }
            m_modeNouvelArret = false;

            m_arret_temp = nouvArret;
        }
        else{ //mode trajet           
            ElementRoutier nouvER = obtenirElementRoutier(p_x, p_y, p_echelle);
            if (nouvER == null || nouvER.getClass() != Troncon.class){
                return;
            }
            Troncon nouvTroncon = (Troncon) nouvER;
            
            if (m_trajet_temp.getListeTroncons().isEmpty()) { //trajet pas encore créé
                if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-1)
                        .getArret().getEmplacement().estSurTroncon()) { //il faut vérifier que c'est après le dernier arret
                    if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-1)
                            .getArret().getEmplacement().getTroncon().getDestination() != nouvTroncon.getOrigine()) { 
                        return;
                    }
                }
                else{
                    if (circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-1)
                            .getArret().getEmplacement().getIntersection() != nouvTroncon.getOrigine()){
                        return;
                    }
                }
            }
            else{
                if (!nouvTroncon.getOrigine().equals(m_trajet_temp.getListeTroncons().getLast().getDestination())) {
                    //il faut que ça soit contigu
                    return;
                }
            }
            
            nouvTroncon.changerStatutSelection();
            m_trajet_temp.getListeTroncons().add(nouvTroncon);
            
            //si dernier troncon avant l'arret on push le trajet
            if(m_arret_temp.getEmplacement().estSurTroncon()){
                if (nouvTroncon.getDestination() == m_arret_temp.getEmplacement().getTroncon().getOrigine()) {
                    m_trajet_temp.getListeTroncons().addLast(m_arret_temp.getEmplacement().getTroncon());
                    circuit.ajouterPaire(m_arret_temp, null);
                    circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-2).setTrajet(m_trajet_temp);
                    
                    cancellerCircuit();
                }
            }
            else{ //arret sur intersection
                if (nouvTroncon.getDestination() == m_arret_temp.getEmplacement().getIntersection()) {
                    circuit.ajouterPaire(m_arret_temp, null);
                    circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-2).setTrajet(m_trajet_temp);

                    cancellerCircuit();
                }
            }
        }
    }
    
    public void ajouterSource(Integer p_x, Integer p_y, Float p_echelle){
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;  
        for (ListIterator<Circuit> circuits =m_reseauTransport.getListeCircuits().listIterator() ; circuits.hasNext() ; ){
            Circuit circuit = circuits.next();
            if(circuit.estSelectionne()){
                Arret arret1 = circuit.getListeArretTrajet().getFirst().getArret();
                Arret arret2 = circuit.getListeArretTrajet().getLast().getArret();
                for (ListIterator<PaireArretTrajet> paires =circuit.getListeArretTrajet().listIterator() ; paires.hasNext() ; ){
                    PaireArretTrajet paire = paires.next();
                   for (ListIterator<Troncon> troncons =paire.getTrajet().getListeTroncons().listIterator() ; troncons.hasNext() ; ){
                       Troncon troncon = troncons.next();
                       if(troncon.estSelectionne()){
                           Point2D.Float p1 = new Point2D.Float(xReel,yReel);
                            double distance1 = troncon.getOrigine().getPosition().distance(p1);
                            double distance2 = troncon.getLongueurTroncon();
                            float pourcentage = (float) (distance1/distance2);
                           if(arret1 != null){
                            if(arret1.getEmplacement().getTroncon() != null){
                                if(troncon.equals(arret1.getEmplacement().getTroncon())){
                                    if(pourcentage <= arret1.getEmplacement().getPourcentageParcouru()){
                                        return;
                                    }
                            }
                            }
                            if(arret2 != null){
                               if(arret2.getEmplacement().getTroncon() != null){
                                 if(troncon.equals(arret2.getEmplacement().getTroncon())){
                                    if(pourcentage >= arret2.getEmplacement().getPourcentageParcouru()){
                                        return;
                                    }
                                 }
                            }
                            }
                             Emplacement emplacement = new Emplacement(true, pourcentage,troncon,troncon.getOrigine());
                             Distribution distributionDefault = new Distribution();
                             m_reseauTransport.ajoutSource(emplacement, circuit, "Source", distributionDefault, new Temps(0));
                             return;
                       }
                       }
                   }
                }
            }
        }
    }
    
    public LinkedList<ElementRoutier> getElementsSelectionnesRoutier(){
        return m_reseauRoutier.getElementsSelectionnes();
    }
    
    public Boolean supprimerSelectionRoutier()
    {
        Boolean supprimee = m_reseauRoutier.supprimerSelection();
        ajusterDoubleSens();
        return supprimee;
    }
    
    public void ajusterDoubleSens(){
        for (Intersection intrsct : m_reseauRoutier.getIntersections()){
            for (Troncon trc : intrsct.getTroncons()){
                trc.setDoubleSens();
            }
        }
    }
    
    public Circuit obtenirCircuitSelectionne(){
        for (Circuit circ : m_reseauTransport.getListeCircuits()){
            if (circ.estSelectionne()){
                return circ;
            }
        }
        return null;
    }
    
}
