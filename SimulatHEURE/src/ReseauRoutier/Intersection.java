package ReseauRoutier;

import java.util.LinkedList;

/**
 *
 * @author Nathaniel
 */
public class Intersection {
    private String m_name = "";
    private Position m_position;
    private LinkedList<Troncon> m_listeTroncons = new LinkedList();
    
    public Intersection(Position position){
        m_position = position;        
    }
    
    public void  setName(String name){
        m_name = name;
    }
    
    public String getName(){
        return m_name;
    }
    
     public Position getPosition(){
        return m_position;
    }
     
     public  LinkedList<Troncon> getListTroncons(){
         return m_listeTroncons;
    }
     
     public void ajouterTroncons(Troncon troncon ){
         m_listeTroncons.add(troncon);
    }
}
