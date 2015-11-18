/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.Utilitaire.Distribution;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import Domaine.Utilitaire.Temps;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author louis
 */
public class ReseauTransport {
    
    private LinkedList<Circuit> m_listeCircuits = new LinkedList();
    private LinkedList<Arret> m_listeArrets = new LinkedList();
    private Temps tempsDebut;
    public ReseauTransport(){}
    
    public LinkedList<Circuit> getListeCircuits(){
        return m_listeCircuits;
    }
    public LinkedList<Arret> getListArrets (){
        return m_listeArrets;
    }
    public void ajouterArret(Arret p_arret){
        m_listeArrets.add(p_arret);
    }
    public void setListeCircuits(LinkedList<Circuit> listeCircuits){
        m_listeCircuits = listeCircuits;
    }
    public void ajouterCircuit(Circuit circ){
        m_listeCircuits.add(circ);
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
   public Arret selectionnerArret(Float p_x, Float p_y, Float p_diametre){
       Ellipse2D.Float zoneSelection = new Ellipse2D.Float(p_x, p_y, p_diametre, p_diametre);

       for (ListIterator<Arret> arrets = m_listeArrets.listIterator() ; arrets.hasNext() ; ){
            if (zoneSelection.contains(arrets.next().getEmplacement().calculPosition()))
            {
                arrets.previous().changerStatutSelection();
                return arrets.next();
            }
        }
        return null;
    }
   public SourceAutobus ajoutSource(Emplacement p_emplacement, Circuit p_circuit, String p_nomSource, Distribution p_distribution,  Temps p_tempsAttenteinitial){
       SourceAutobus src = new SourceAutobus(p_emplacement, p_circuit,p_nomSource,p_distribution,p_tempsAttenteinitial);
       p_circuit.ajouterSource(src);
       return src;
   }
   public Arret creerArret(Emplacement emplacement, String nom){
       return new Arret(emplacement, nom);
   }
}
