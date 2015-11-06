/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauTransport;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author louis
 */
public class ReseauTransport {
    
    private LinkedList<Circuit> m_listeCircuits = new LinkedList();
    
    public ReseauTransport(){
    }
    
    public ReseauTransport(LinkedList<Circuit> listeCircuits){
        m_listeCircuits = listeCircuits;
    }
    public LinkedList<Circuit> getListeCircuits(){
        return m_listeCircuits;
    }
    public void setListeCircuits(LinkedList<Circuit> listeCircuits){
        m_listeCircuits = listeCircuits;
    }
            
    public void initReseauTransport(){
        ListIterator<Circuit> circuitItr = m_listeCircuits.listIterator();
        while (circuitItr.hasNext()) {
            circuitItr.next().initCircuit();
        }
    };
}