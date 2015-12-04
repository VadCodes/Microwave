/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;

import Domaine.ReseauRoutier.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 *
 * @author louis
 */
public class PileSelectionBesoins {
    private LinkedList<ElementBesoins> m_pile = new LinkedList<ElementBesoins>();
    
    public PileSelectionBesoins(){}
    
    public void ajouter(ElementBesoins eb){
        if (eb!=null){
            this.enlever(eb);
            m_pile.addLast(eb);
        }
    }
    
    public ElementBesoins getDessus(){
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
    
    public Boolean contient(ElementBesoins eb){
        return m_pile.contains(eb);
    }
    
    public void enlever(ElementBesoins eb){
        if (this.contient(eb)){
            m_pile.remove(eb);
        }
    }
    
    public LinkedList<ElementBesoins> getListe(){
        return m_pile;
    }
    
    public ElementBesoins depiler(){
        try{
            ElementBesoins eb = m_pile.getLast();
            m_pile.removeLast();
            return eb;
        }
        catch(NoSuchElementException e){
            return null;
        }
    }
}
