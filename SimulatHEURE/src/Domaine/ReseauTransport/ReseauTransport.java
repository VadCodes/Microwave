/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Troncon;
import Domaine.Utilitaire.Distribution;
import java.util.LinkedList;
import java.util.ListIterator;
import Domaine.Utilitaire.Temps;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *
 * @author louis
 */
public class ReseauTransport {
    
    private LinkedList<Circuit> m_listeCircuits = new LinkedList<>();
    private LinkedList<Arret> m_listeArrets = new LinkedList<>();
    private int m_conteurArrets = 1;
    private int m_conteurCircuits = 1;
    private int m_conteurSources = 1;
    public ReseauTransport(){}
    
    public LinkedList<Circuit> getListeCircuits(){
        return m_listeCircuits;
    }
    public LinkedList<Arret> getListArrets (){
        return m_listeArrets;
    }
    public void ajouterArret(Arret p_arret){
        p_arret.setNom("Arret" + Integer.toString(m_conteurArrets));
        m_conteurArrets++;
        m_listeArrets.add(p_arret);
    }
    public void setListeCircuits(LinkedList<Circuit> listeCircuits){
        m_listeCircuits = listeCircuits;
    }
    public void ajouterCircuit(Circuit circ){
        circ.setNom("Circuit"+ Integer.toString(m_conteurCircuits));
        m_conteurCircuits++;
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
   public Arret selectionnerArret(Float p_x, Float p_y, Float p_diametre, Float p_echelle){
       
       Ellipse2D.Float zoneSelection = new Ellipse2D.Float(p_x, p_y, p_diametre, p_diametre);

        for (ListIterator<Arret> arrets = m_listeArrets.listIterator() ; arrets.hasNext() ; ){
            Emplacement em = arrets.next().getEmplacement();
            Point2D.Float p = em.calculPosition(p_echelle);
            if(em.estSurTroncon()){
                Troncon troncon = em.getTroncon();

                float p1x = troncon.getOrigine().getPosition().x;
                float p1y = troncon.getOrigine().getPosition().y;
                float p2x = troncon.getDestination().getPosition().x;
                float p2y = troncon.getDestination().getPosition().y;

                float n = 3.5f; //aww yeah c'est hardcodé à souhait
                if (troncon.estDoubleSens()){
                    if(p2y-p1y>0){
                        p1x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p2x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p1y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p2y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                    }
                    else{
                        p1x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p2x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;   
                        p1y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        p2y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                    }
                    float X = p1x +(p2x - p1x)*em.getPourcentageParcouru();
                    float Y = p1y +(p2y - p1y)*em.getPourcentageParcouru();
                    p = new Point2D.Float(X, Y);
                }
            }
            
            if (zoneSelection.contains(p))
            {
                arrets.previous().changerStatutSelection();
                return arrets.next();
            }
        }
        return null;
    }
   public SourceAutobus ajoutSource(Emplacement p_emplacement, Circuit p_circuit, String p_nomSource, Distribution p_distribution,  Temps p_tempsAttenteinitial){
       SourceAutobus src = new SourceAutobus(p_emplacement, p_circuit,p_nomSource,p_distribution,p_tempsAttenteinitial);
       src.setNom("Source" + Integer.toString(m_conteurSources));
       m_conteurSources++;
       p_circuit.ajouterSource(src);
       return src;
   }
   public Arret creerArret(Emplacement emplacement, String nom){
       return new Arret(emplacement, nom);
   }
   
   public void deselectionnerTout(){
       for(Arret arr : m_listeArrets){
           if (arr.estSelectionne())
            {
                arr.changerStatutSelection();
            }
       }
       for(Circuit circ : m_listeCircuits){
           if (circ.estSelectionne()){
                circ.changerStatutSelection();
           }
       }
       for(Circuit circ : m_listeCircuits){
                for(SourceAutobus sa : circ.getListeSourceAutobus()){
                    if(sa.estSelectionne()){
                        sa.changerStatutSelection();
                    }
                }
           }
   }
   
    public LinkedList<ElementTransport> getElementsSelectionnes(){
        
        LinkedList<ElementTransport> listeRetour = new LinkedList<>();
                
        for (Circuit circ: m_listeCircuits)
        {
            if (circ.estSelectionne())
            {
                listeRetour.add(circ);
            }
            
            for (SourceAutobus src: circ.getListeSourceAutobus())
            {   
                if (src.estSelectionne())
                {
                    listeRetour.add(src);
                }
            }
        }
        for (Arret arr : m_listeArrets){
            if (arr.estSelectionne()){
                listeRetour.add(arr);
            }
        }
        return listeRetour;
    }
    
    public Boolean supprimerSelection()
    {
        for (ListIterator<Arret> arrIt = m_listeArrets.listIterator() ; arrIt.hasNext() ; )
        {
            Arret arr = arrIt.next();
            if (arr.estSelectionne())
            {
                for (Circuit circ : m_listeCircuits) {
                    for (PaireArretTrajet pat : circ.getListeArretTrajet()){
                        if (pat.getArret() == arr){
                            return false;
                        }
                    }
                }
                arrIt.remove();
            }
        }
        
        for (ListIterator<Circuit> circ = m_listeCircuits.listIterator() ; circ.hasNext(); )
        {
            Circuit circuit = circ.next();
            if (circuit.estSelectionne())
            {
                circuit.getListeSourceAutobus().clear();
                circ.remove();
            }
            else
            {
                for (ListIterator<SourceAutobus> src = circuit.getListeSourceAutobus().listIterator() ; src.hasNext() ; )
                {
                    if (src.next().estSelectionne())
                    {
                        src.remove();
                    }
                    else
                    {
                        src.next();
                    }
                }
            }
        }
        
        return true;
    }
}
