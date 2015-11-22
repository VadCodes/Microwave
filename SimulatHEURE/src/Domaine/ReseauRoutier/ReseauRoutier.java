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
    private LinkedList<Intersection> m_listeIntersections = new LinkedList<>();
    public final ReseauRoutierFactory m_factory = new ReseauRoutierFactory();
    private int m_conteurTroncon = 1;
    private int m_conteurIntersection = 1;
    
    public final static double VITESSE_PIETON = 4;
    
    public ReseauRoutier(){}
    
    public LinkedList<Intersection> getIntersections()
    {
        return m_listeIntersections;
    }
    
    public void ajouterIntersection(float p_x, float p_y)
    {
        Intersection inter = m_factory.intersection(new Point2D.Float(p_x, p_y));
        inter.setNom("Intersection" + Integer.toString(m_conteurIntersection));
        m_conteurIntersection++;
        m_listeIntersections.add(inter);
    }
    
    public void setNameTroncon(Troncon p_troncon, String p_nom){
        p_troncon.setNom(p_nom);
    }
    
    public Intersection selectionnerIntersection(Float p_x, Float p_y, Float p_diametre){
        Intersection inter = obtenirIntersection(p_x, p_y, p_diametre);
        if (inter != null){
            inter.changerStatutSelection();
        }
        return inter;
    }
    
    public Intersection obtenirIntersection(Float p_x, Float p_y, Float p_diametre)
    {
        Ellipse2D.Float zoneSelection = new Ellipse2D.Float(p_x, p_y, p_diametre, p_diametre);

        for (Intersection inter : m_listeIntersections){
            if (zoneSelection.contains(inter.getPosition())){
                return inter;
            }
        }
        return null;
    }
    
//    public Troncon selectionnerTroncon(Float p_x, Float p_y, Float p_largeur, Float p_echelle){
//        Troncon trc = obtenirTroncon(p_x, p_y, p_largeur, p_echelle); //sans selection
//        if (trc != null){
//            trc.changerStatutSelection();
//        }
//        return trc;
//    }
    
    public Troncon obtenirTroncon(Float p_x, Float p_y, Float p_largeur, Float p_echelle)
    {
        Rectangle2D.Float zoneApproximative = new Rectangle2D.Float(p_x, p_y, p_largeur, p_largeur);
        
        for (Intersection intersection: m_listeIntersections)
        {
            Point2D.Float p1 = intersection.getPosition();
            
            for (Troncon troncon: intersection.getTroncons())
            {   
                Point2D.Float p2 = troncon.getDestination().getPosition();
                float p1x = p1.x;
                float p1y = p1.y;
                float p2x = p2.x;
                float p2y = p2.y;
                float n = 3.5f;
                if (troncon.estDoubleSens()){
                    if(p2y-p1y>0){
                        p1x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p2x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p1y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p2y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                    }
                    else{
                        p1x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p2x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;   
                        p1y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p2y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                    }
                }
                Line2D.Float segment = new Line2D.Float(new Point2D.Float(p1x, p1y), new Point2D.Float(p2x, p2y));
                
                if (segment.intersects(zoneApproximative))
                {
                    return troncon;
                }
            }
        }
        return null;
    }
    
    public void deselectionnerTout()
    {
        desuggererTout();
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
    
    public void desuggererTout()
    {
        for (Intersection intersection: m_listeIntersections)
        {
            intersection.setEstSuggere(false);
            
            for (Troncon troncon: intersection.getTroncons())
            {   
                troncon.setEstSuggere(false);
            }
        }
    }
    
    public LinkedList<ElementRoutier> getElementsSelectionnes(){
        
        LinkedList<ElementRoutier> listeRetour = new LinkedList<>();
        
        for (Intersection intersection: m_listeIntersections)
        {
            if (intersection.estSelectionne())
            {
                listeRetour.add(intersection);
            }
            
            for (Troncon troncon: intersection.getTroncons())
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
        if (p_origine == p_destination)
        {
            return;
        }
        
        //on s'assure qu'il n'y a pas deux tronçons de origine à destination
        for(Troncon trc : p_origine.getTroncons()){
            if (trc.getDestination()==p_destination){
                return; 
            }
        }
        Troncon tr = m_factory.creerTroncon(p_origine, p_destination);
        tr.setNom( "Troncon" +Integer.toString(m_conteurTroncon));
        m_conteurTroncon++;
        p_origine.ajouterTroncon(tr);
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
