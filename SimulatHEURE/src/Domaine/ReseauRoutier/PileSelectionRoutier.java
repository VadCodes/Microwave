/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 *
 * @author louis
 */
public class PileSelectionRoutier implements java.io.Serializable {
    private LinkedList<ElementRoutier> m_pile = new LinkedList<ElementRoutier>();
    
    public PileSelectionRoutier(){}
    
    public void ajouter(ElementRoutier er){
        if (er!=null){
            this.enlever(er);
            m_pile.addLast(er);
        }
    }
    
    public ElementRoutier getDessus(){
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
    
    public Boolean contient(ElementRoutier er){
        return m_pile.contains(er);
    }
    
    public void enlever(ElementRoutier er){
        if (this.contient(er)){
            m_pile.remove(er);
        }
    }
    
    public LinkedList<ElementRoutier> getListe(){
        return m_pile;
    }
    
    public ElementRoutier depiler(){
        try{
            ElementRoutier er = m_pile.getLast();
            m_pile.removeLast();
            return er;
        }
        catch(NoSuchElementException e){
            return null;
        }
    }
}
