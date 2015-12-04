/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 *
 * @author louis
 */
public class PileSelectionTransport implements java.io.Serializable {
    private LinkedList<ElementTransport> m_pile = new LinkedList<ElementTransport>();
    
    public PileSelectionTransport(){}
    
    public void ajouter(ElementTransport et){
        if (et!=null){
            this.enlever(et);
            m_pile.addLast(et);
        }
    }
    
    public ElementTransport getDessus(){
        try{
            return m_pile.getLast();
        }
        catch(NoSuchElementException e){
            return null;
        }
    }
    
    public void vider(){
        m_pile.clear();
    }
    
    public Boolean contient(ElementTransport et){
        return m_pile.contains(et);
    }
    
    public void enlever(ElementTransport et){
        if (this.contient(et)){
            m_pile.remove(et);
        }
    }
    
    public LinkedList<ElementTransport> getListe(){
        return m_pile;
    }
    
    public ElementTransport depiler(){
        try{
            ElementTransport et = m_pile.getLast();
            m_pile.removeLast();
            return et;
        }
        catch(NoSuchElementException e){
            return null;
        }
    }
}
