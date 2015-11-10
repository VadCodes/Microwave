package ReseauRoutier;

import java.util.LinkedList;
import java.awt.geom.Point2D;

/**
 *
 * @author Nathaniel
 */
public class Intersection {
    private String m_name = "";
    private Point2D.Float m_position;
    private LinkedList<Troncon> m_listeTroncons = new LinkedList();
    
    public Intersection(Point2D.Float position){
        m_position = position;        
    }
    
    public void  setName(String name){
        m_name = name;
    }
    
    public String getName(){
        return m_name;
    }
    
     public Point2D.Float getPosition(){
        return m_position;
    }
     
     public  LinkedList<Troncon> getListeTroncons(){
         return m_listeTroncons;
    }
     
     public void ajouterTroncons(Troncon troncon ){
         m_listeTroncons.add(troncon);
    }
}
