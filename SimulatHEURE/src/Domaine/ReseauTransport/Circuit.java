/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;


/**
 *
 * @author louis
 */
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Intersection;
import java.util.LinkedList;
import java.util.ListIterator;
import Domaine.ReseauRoutier.Position;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Trajet;
import java.awt.geom.Point2D;

public class Circuit {
    private String m_nom = "";
    private LinkedList<SourceAutobus> m_listeSources = new LinkedList();
    private LinkedList<Autobus> m_listeAutobus = new LinkedList();
    private LinkedList<PaireArretTrajet> m_listeArretTrajet;
    private ReseauRoutier m_reseauRoutier;
    
    public Circuit(String nom, LinkedList<PaireArretTrajet> listeArrTraj, ReseauRoutier resRoutier){
        //assert listeArrTraj doit avoir les 2 premiers
        m_nom = nom;
        m_listeArretTrajet = listeArrTraj;
        m_reseauRoutier = resRoutier;
    }
    
    public void ajouterSource(SourceAutobus source){
        m_listeSources.add(source);
    }
    
    public void ajouterAutobus(Autobus autobus){
        m_listeAutobus.add(autobus);
    }
    
    public void ajouterArret(Arret nouvArr){
        
        Emplacement precEmpl = m_listeArretTrajet.getLast().getArret().getEmplacement();
        Emplacement nouvEmpl = nouvArr.getEmplacement();
        
        Trajet nouvTrajet = dijkstra(precEmpl, nouvEmpl);
        
        //faire une paire
        //ajouterPaire()
    }
    
    private void ajouterPaire(PaireArretTrajet paire){
        m_listeArretTrajet.add(paire);
    }
    
    private Trajet dijkstra(Emplacement origine, Emplacement Destination){
        LinkedList<Intersection> listeSommets = new LinkedList();
        ListIterator<Intersection> interItr = m_reseauRoutier.getIntersections().listIterator();
        while (interItr.hasNext()) {
            listeSommets.addLast(interItr.next());
        }
        
        Boolean nouvSommetOrig = !origine.getEstSurTroncon();
        Boolean nouvSommetDest = !Destination.getEstSurTroncon();
        if (nouvSommetOrig){
            Intersection inter = new Intersection(origine.calculPosition());
            
            listeSommets.addFirst(Intersection);
        }
        
        int nb_sommets = listeSommets.size();

        if (nouvSommetOrig){
            nb_sommets++;
        }
        if (nouvSommetDest){
            nb_sommets++;
        }
        
        
    }
    
    public void calculCirculationGlobal(){
        
        //on vide toutes les files d'arrets
        ListIterator<PaireArretTrajet> arretTrajetItr = m_listeArretTrajet.listIterator();
        while (arretTrajetItr.hasNext()) {
            arretTrajetItr.next().getArret().viderFile();
        }
        
        //pour chaque autobus on calcule la circulation
        ListIterator<Autobus> autobusItr = m_listeAutobus.listIterator();
        while (autobusItr.hasNext()) {
            calculCirculation(autobusItr.next());
        }
    }
    
    public void calculCirculation(Autobus bus){
        
    }
    
    public LinkedList<Point2D.Float> getPositionsDesAutobus(){
        
        LinkedList<Point2D.Float> listePositionsAutobus = new LinkedList<Point2D.Float>();
        ListIterator<Autobus> autobusItr = m_listeAutobus.listIterator();
        while (autobusItr.hasNext()) {
            listePositionsAutobus.add(autobusItr.next().getPosition());
        }
        
        return listePositionsAutobus;
    }
    
    public LinkedList<PaireArretTrajet> getListeArretTrajet(){
        return m_listeArretTrajet;
    }
    
    public void incrementerIterateurAutobus(Autobus bus){
        bus.incrementerIterateur();
    }
    
    public void assignerTrajetAutobus(Autobus bus){
        bus.assignerTrajet(m_listeArretTrajet);
    }
    
    public void initCircuit(){
        ListIterator<SourceAutobus> sourceAutobusItr = m_listeSources.listIterator();
        while (sourceAutobusItr.hasNext()) {
            sourceAutobusItr.next().initSourceAutobus();
        }
    }
}
