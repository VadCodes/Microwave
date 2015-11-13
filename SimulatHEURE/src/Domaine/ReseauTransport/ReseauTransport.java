/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import Domaine.Utilitaire.Temps;

/**
 *
 * @author louis
 */
public class ReseauTransport {
    
    private LinkedList<Circuit> m_listeCircuits = new LinkedList();
    private Temps tempsDebut;
    public ReseauTransport(){
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
    public void calculEtatReseauTransport(Temps deltaT){
        ListIterator<Circuit> circuitItr = m_listeCircuits.listIterator();
        while (circuitItr.hasNext()) {
            Circuit  crc = circuitItr.next();
            crc.updateSourceAutobus(deltaT);
            crc.calculCirculationGlobal(deltaT);
        }
    }
}
