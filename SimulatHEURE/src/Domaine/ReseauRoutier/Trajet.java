/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

/**
 *
 * @author Nathaniel
 */
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Trajet {
    private Emplacement m_emplacementInitial;
    private Emplacement m_emplacementFinal;
    private LinkedList<Troncon> m_listTroncons;
    public Trajet(Emplacement emplacementInitial, Emplacement emplacementFinal, LinkedList<Troncon> listTroncons){
        m_emplacementInitial = emplacementInitial;
        m_emplacementFinal = emplacementFinal;
        m_listTroncons = listTroncons;
    }
    
    public Trajet(){
        m_listTroncons = new LinkedList<>();
    }
    
    public Emplacement getEmplacementInitial(){
        return m_emplacementInitial;
    }
    
    public Emplacement getEmplacementFinal(){
        return m_emplacementFinal;
    }
    
    public void setEmplacementInitial(Emplacement empl){
        m_emplacementInitial = empl;
    }
    
    public void setEmplacementFinal(Emplacement empl){
        m_emplacementFinal = empl;
    }
    
    public Troncon getNextTroncon(Emplacement emplacement){
        boolean trg= false;
         ListIterator<Troncon> troncon_it = m_listTroncons.listIterator();
             while (troncon_it.hasNext()) {
                 
                 Troncon tr = troncon_it.next();
                 if (trg){
                     return tr;
                 }
                 if(tr.equals(emplacement.getTroncon())){
                     trg = true;
                 }
             }
             return null;
    }
    public LinkedList<Troncon> getListeTroncon(){
        return m_listTroncons;
    }
}
