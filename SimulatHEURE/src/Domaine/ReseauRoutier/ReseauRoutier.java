package Domaine.ReseauRoutier;

import java.util.LinkedList;
import java.util.ListIterator;

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
    public final ReseauRoutierFactory m_factory = new ReseauRoutierFactory();
    
    public final static double VITESSE_PIETON = 4;
    
    public ReseauRoutier(){}
    
    public LinkedList<Intersection> getIntersections()
    {
        return m_listeIntersections;
    }
    
    public void ajouterIntersection(float p_x, float p_y)
    {
        m_listeIntersections.add(m_factory.intersection(new Point2D.Float(p_x, p_y)));
    }
    
    public void setNameTroncon(Troncon p_troncon, String p_nom){
        p_troncon.setNom(p_nom);
    }
    
    public Intersection selectionnerIntersection(Float p_x, Float p_y, Float p_diametre)
    {
        Ellipse2D.Float zoneSelection = new Ellipse2D.Float(p_x, p_y, p_diametre, p_diametre);

        for (ListIterator<Intersection> intersection = m_listeIntersections.listIterator() ; intersection.hasNext() ; )
        {
            if (zoneSelection.contains(intersection.next().getPosition()))
            {
                intersection.previous().changerStatutSelection();
                return intersection.next();
            }
        }
        
        return null;
    }
    
    public Troncon selectionnerTroncon(Float p_x, Float p_y, Float p_largeur)
    {
        Rectangle2D.Float zoneApproximative = new Rectangle2D.Float(p_x, p_y, p_largeur, p_largeur);
        
        for (Intersection intersection: m_listeIntersections)
        {
            Point2D.Float p1 = intersection.getPosition();
            
            for (Troncon troncon: intersection.getTroncons())
            {   
                Point2D.Float p2 = troncon.getDestination().getPosition();
                Line2D.Float segment = new Line2D.Float(p1, p2);
                
                if (segment.intersects(zoneApproximative))
                {
                    troncon.changerStatutSelection();
                    return troncon;
                }
            }
        }
        return null;
    }
    
    public void deselectionnerTout()
    {
        for (Intersection intersection: m_listeIntersections)
        {
            if (intersection.estSelectionne())
            {
                intersection.changerStatutSelection();
            }
            
            for (Troncon troncon: intersection.getTroncons())
            {   
                if (troncon.estSelectionne())
                {
                    troncon.changerStatutSelection();
                }
            }
        }
    }
    
    public LinkedList<ElementRoutier> getElementsSelectionnes(){
        
        LinkedList<ElementRoutier> listeRetour = new LinkedList();
        
        for (Intersection intersection: m_listeIntersections)
        {
            if (intersection.estSelectionne())
            {
                listeRetour.add(intersection);
            }
            
            for (Troncon troncon: intersection.getListeTroncons())
            {   
                if (troncon.estSelectionne())
                {
                    listeRetour.add(troncon);
                }
            }
        }
        
        return listeRetour;
    }
    
    public void ajouterTroncon(Intersection p_origine, Intersection p_destination)
    {        
        p_origine.ajouterTroncon(m_factory.creerTroncon(p_origine, p_destination));
    }
    
    public Boolean supprimerSelection()
    {
        for (Intersection intersection: m_listeIntersections)
        {
            if (intersection.estSelectionne())
            {
                intersection.getTroncons().clear();
            }
            else
            {
                for (ListIterator<Troncon> troncon = intersection.getTroncons().listIterator() ; troncon.hasNext() ; )
                {
                    if (troncon.next().estSelectionne() || troncon.previous().getDestination().estSelectionne())
                    {
                        troncon.remove();
                    }
                    else
                    {
                        troncon.next();
                    }
                }
            }
        }

        boolean intersectionSupprimee = false;
        for (ListIterator<Intersection> intersection = m_listeIntersections.listIterator() ; intersection.hasNext() ; )
        {
            if (intersection.next().estSelectionne())
            {
                intersection.remove();
                intersectionSupprimee = true;
            }
        }
        
        return intersectionSupprimee;
    }
    
    public void initReseauRoutier(){
        for (Intersection intersection : m_listeIntersections)
        {
            for (Troncon troncon : intersection.getTroncons())
            {
                troncon.initTroncon();
            }
        }
    }
}
