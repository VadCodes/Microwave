package Domaine.ReseauRoutier;

import Domaine.Utilitaire.PaireFloats;
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
    
    public void copier(ReseauRoutier p_reseauRoutier){
        p_reseauRoutier.m_conteurIntersection = this.m_conteurIntersection;
        p_reseauRoutier.m_conteurTroncon = this.m_conteurTroncon;
        p_reseauRoutier.m_listeIntersections  = new LinkedList<>();
        for (ListIterator<Intersection> intersections = this.m_listeIntersections.listIterator() ;intersections.hasNext() ; ){
           // Intersection inter = p_reseauRoutier.intersections.next();
        }
               // Intersection intersection = p_reseauRoutier.copyIntersection(intersections.next());
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
                
                PaireFloats pAj = troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
                Float ajX = pAj.getFloat1();
                Float ajY = pAj.getFloat2();

                Line2D.Float segment = new Line2D.Float(new Point2D.Float(p1.x + ajX, p1.y + ajY), new Point2D.Float(p2.x + ajX, p2.y + ajY));
                
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
        Boolean suppressionOK = true;
        
        for (ListIterator<Intersection> intersectionIt = m_listeIntersections.listIterator() ; intersectionIt.hasNext() ; )
        {
            Intersection intersection = intersectionIt.next();
            
            if (intersection.estSelectionne())
            {
                intersection.getTroncons().clear();
                intersectionIt.remove();
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
        
        return suppressionOK;
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
