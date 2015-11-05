/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

import Utilitaire.Distribution;
import java.util.LinkedList;
import Utilitaire.Temps;

/**
 *
 * @author Nathaniel
 */
public class Intersection {
    private String m_name = "";
    private Position m_position;
     private LinkedList<Troncon> m_listTroncons = new LinkedList();
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
         return m_listTroncons;
     }
     public void ajouterTroncons(Troncon troncon ){
         m_listTroncons.add( troncon);
     }
    
}
