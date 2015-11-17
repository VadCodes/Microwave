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
        SELECTIONNER, INTERSECTION, TRONCON, ARRET, SOURCE
    }
    
    private ReseauRoutier m_reseauRoutier = new ReseauRoutier();
    private LinkedList<Intersection> m_parametresTroncon = new LinkedList();
    private Intersection m_intersectionOriginArret;
    private Troncon m_tronconArret;
    private ReseauTransport m_reseauTransport = new ReseauTransport() ;
    private Temps m_deltaT;
    private LinkedList<BesoinTransport> m_listBesoins = new LinkedList();
    
    private Circuit m_circuit_temp;
    
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
    
    public ElementRoutier selectionnerElementRoutier(Integer p_x, Integer p_y, Float p_echelle)
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
            
            return m_reseauRoutier.selectionnerTroncon(xReel, yReel, largeurSelection, p_echelle);
        }
        else{
            return intersection;
        }
    }
    
    public void deselectionnerRoutier()
    {
        m_parametresTroncon.clear();
        m_reseauRoutier.deselectionnerTout();
    }
    public void deselectionnerTransport(){
        //A faire
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
        
        Arret arret = m_reseauTransport.selectionnerArret(xReel, yReel, largeurSelection);
        return arret;
    }
    public void ajouterSource(Integer p_x, Integer p_y, Float p_echelle){
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;  
        for (ListIterator<Circuit> circuits =m_reseauTransport.getListeCircuits().listIterator() ; circuits.hasNext() ; ){
            Circuit circuit = circuits.next();
            if(circuit.estSelectionne()){
                for (ListIterator<PaireArretTrajet> paires =circuit.getListeArretTrajet().listIterator() ; paires.hasNext() ; ){
                    PaireArretTrajet paire = paires.next();
                   for (ListIterator<Troncon> troncons =paire.getTrajet().getListeTroncon().listIterator() ; troncons.hasNext() ; ){
                       Troncon troncon = troncons.next();
                       if(troncon.estSelectionne()){
                           Point2D.Float p1 = new Point2D.Float(xReel,yReel);
                            double distance1 = troncon.getIntersectionOrigin().getPosition().distance(p1);
                            double distance2 = troncon.longueurTroncon();
                            float pourcentage = (float) (distance1/distance2);
                             Emplacement emplacement = new Emplacement(true, pourcentage,troncon,troncon.getIntersectionOrigin());
                             Distribution distributionDefault = new Distribution();
                             m_reseauTransport.ajoutSource(emplacement, circuit, null, distributionDefault, new Temps(0));
                       }
                   }
                }
            }
        }
    }
    public void ajouterArret(Integer p_x, Integer p_y, Float p_echelle){
        float xReel = p_x / p_echelle;
        float yReel = p_y / p_echelle;  
         for (ListIterator<Intersection> intersection =m_reseauRoutier.getIntersections().listIterator() ; intersection.hasNext() ; ){
             Intersection intersectionOrigin = intersection.next();
            for (ListIterator<Troncon> troncons = intersectionOrigin.getTroncons().listIterator() ; troncons.hasNext() ; ){
                Troncon troncon = troncons.next();
                if(troncon.estSelectionne()){
                    Point2D.Float p1 = new Point2D.Float(xReel,yReel);
                    double distance1 = intersectionOrigin.getPosition().distance(p1);
                    double distance2 = troncon.longueurTroncon();
                    float pourcentage = (float) (distance1/distance2);
                     Emplacement emplacement = new Emplacement(true, pourcentage,troncon,intersectionOrigin);
                     m_reseauTransport.ajouterArret(new Arret(emplacement, ""));
                }
            }
         }
    }

    private void ajouterCircuit(Integer p_x, Integer p_y, Float p_echelle){
        //while veut construire arret
        Boolean auMoinsUnArret = (m_circuit_temp.getListeArretTrajet().getFirst() != null);
        if(auMoinsUnArret){
            //selectionner un arret
            ElementTransport nouvET = selectionnerElementTransport(p_x, p_y, p_echelle);
            if (nouvET.getClass() != Arret.class){
                return;
            }
            Arret nouvArret = (Arret) nouvET;
            
            if (nouvArret == m_circuit_temp.getListeArretTrajet().getFirst().getArret()){
                
            }
            //si arret est le meme que le premier
                //proposer boucle
                //si boucle
                    //mettre l'attribut du circuit en consequence
        
            //mettre en couleur le reste du troncon A apres

            //selectionner un deuxieme arret

            //mettre en couleur le reste du troncon Z avant

            //attendre la sélection d'un troncon contigu au troncon A, empecher tout le reste

            //meme chose pour B C D...

            //attendre la sélection d'un troncon contigu au troncon Z
        }
    }
        
    private void construireTrajet(LinkedList<Troncon> p_listTroncon, LinkedList<Troncon> p_listAConstruire){
        for (ListIterator<Troncon> troncons2 = p_listTroncon.listIterator() ; troncons2.hasNext() ; ){
                 Troncon troncon = troncons2.next();
                      if(troncon.estSelectionne()){
                               p_listAConstruire.add(troncon);
                               construireTrajet(troncon.getDestination().getTroncons(), p_listAConstruire);
                               return;
                      }
            }
    }
    public void construirePaireArretTrajet(){
        

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
    
}
