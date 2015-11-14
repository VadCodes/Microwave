package Domaine.ReseauRoutier;

import Domaine.Utilitaire.*;

import java.util.LinkedList;
//import java.util.ListIterator;

import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutier {
    private LinkedList<Intersection> m_listeIntersections = new LinkedList();
    private final ReseauRoutierFactory m_factory = new ReseauRoutierFactory(); // why pubblic !?
    
    
    
    public ReseauRoutier(){}
    
    public LinkedList<Intersection> getIntersections(){
        return m_listeIntersections;
    }
    
    public void ajouterIntersection(float p_x, float p_y)
    {
        m_listeIntersections.add(m_factory.intersection(new Point2D.Float(p_x, p_y)));
    }
    
    public Troncon ajouterTroncon(Intersection intersectionOrigin, Intersection intersectionDestination, Distribution distribution){
        double distance = intersectionOrigin.getPosition().distance(intersectionDestination.getPosition());
        Temps tempsTransit = new Temps(distance*1/4);
        Troncon troncon = m_factory.creerTroncon(distribution, intersectionDestination, tempsTransit);
        intersectionOrigin.ajouterTroncons(troncon);
        return troncon;
    }
    
    /*
    public void  validerNom(String p_nom){
        ListIterator<Intersection> intersection_it= m_listIntersections.listIterator();
        while (intersection_it.hasNext()) {
            ListIterator<Troncon> troncon_it = intersection_it.next().getListTroncons().listIterator();
             while (troncon_it.hasNext()) {
                if(troncon_it.next().getNom().equals(p_nom)){
                    throw new Error("Le nom existe déjà");
                }
             }
          }
    }
    */
    public void setNameTroncon(Troncon p_troncon, String p_nom){
        p_troncon.setNom(p_nom);
    }
    
    public Boolean selectionnerIntersection(Float p_x, Float p_y, Float p_diametre)
    {
        Ellipse2D.Float zoneSelection = new Ellipse2D.Float(p_x, p_y, p_diametre, p_diametre);

        for (Intersection intersection: m_listeIntersections)
        {
            if (zoneSelection.contains(intersection.getPosition()))
            {
                intersection.changerStatutSelection();                
                return true;                                    
            }
        }
        
        return false;
    }
    
    public void selectionnerTroncon(Float p_x, Float p_y, Float p_largeur)
    {
        Rectangle2D.Float zoneApproximative = new Rectangle2D.Float(p_x, p_y, p_largeur, p_largeur);
        
        for (Intersection intersection: m_listeIntersections)
        {
            Point2D.Float p1 = intersection.getPosition();
            
            for (Troncon troncon: intersection.getListeTroncons())
            {   
                Point2D.Float p2 = troncon.getDestination().getPosition();
                Line2D.Float segment = new Line2D.Float(p1, p2);
                
                if (segment.intersects(zoneApproximative))
                {
                    troncon.changerStatutSelection();
                    return;
                }
            }
        }
    }
    
    public void deselectionnerTout()
    {
        for (Intersection intersection: m_listeIntersections)
        {
            if (intersection.estSelectionee())
            {
                intersection.changerStatutSelection();
            }
            
            for (Troncon troncon: intersection.getListeTroncons())
            {   
                if (troncon.estSelectione())
                {
                    troncon.changerStatutSelection();
                }
            }
        }
        
        //m_fenetrePrincipale.m_controleur.m_reseauRoutier.setIntersectionsSelectionnees(0);
    }
    
    public void initReseauRoutier(){
        for (Intersection intersection : m_listeIntersections)
        {
            for (Troncon troncon : intersection.getListeTroncons())
            {
                troncon.initTroncon();
            }
        }
    }
}