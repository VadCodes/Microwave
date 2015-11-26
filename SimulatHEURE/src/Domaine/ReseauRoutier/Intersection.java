package Domaine.ReseauRoutier;

import java.util.LinkedList;
import java.awt.geom.Point2D;

/**
 *
 * @author Nathaniel
 */
public class Intersection extends ElementRoutier{
    private String m_name = "";
    private Point2D.Float m_position;
    private LinkedList<Troncon> m_listeTroncons = new LinkedList<>();
    
    public final static float RAYON = 10;
    
    public Intersection(Point2D.Float p_position){
        m_position = p_position;
    }
    
    public void  setNom(String name){
        m_name = name;
    }
    
    public String getName(){
        return m_name;
    }
    
     public Point2D.Float getPosition(){
        return m_position;
    }
     
     public  LinkedList<Troncon> getTroncons(){
         return m_listeTroncons;
    }
     
     public void ajouterTroncon(Troncon troncon ){
         m_listeTroncons.add(troncon);
    }
     
    public LinkedList<Intersection> getEnfants(){
        LinkedList<Intersection> listeRetour = new LinkedList<>();
        for (Troncon trc : m_listeTroncons){
            listeRetour.add(trc.getDestination());
        }
        return listeRetour;
    }
}
