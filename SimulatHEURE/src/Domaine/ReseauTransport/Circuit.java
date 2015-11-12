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
import java.util.LinkedList;
import java.util.ListIterator;
import Domaine.ReseauRoutier.Position;
import Domaine.Utilitaire.Temps;

public class Circuit {
    private String m_nom = "";
    private LinkedList<SourceAutobus> m_listeSources = new LinkedList();
    private LinkedList<Autobus> m_listeAutobus = new LinkedList();
    private LinkedList<PaireArretTrajet> m_listeArretTrajet;
    
    public Circuit(String nom, LinkedList<PaireArretTrajet> listeArrTraj){
        //assert listeArrTraj doit avoir les 2 premiers
        m_nom = nom;
        m_listeArretTrajet = listeArrTraj;
    }
    
    public void updateSourceAutobus(Temps deltatT){
         ListIterator<SourceAutobus> sourceAutobusItr = m_listeSources.listIterator();
        while (sourceAutobusItr.hasNext()) {
            SourceAutobus src = sourceAutobusItr.next();
            src.miseAjoutTempsRestant(deltatT);
            src.genererAutobus();
        }
    }
    public void ajouterSource(SourceAutobus source){
        m_listeSources.add(source);
    }
    
    public void ajouterAutobus(Autobus autobus){
        m_listeAutobus.add(autobus);
    }
    
    public void ajouterPaire(PaireArretTrajet paire){
        m_listeArretTrajet.add(paire);
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
    
    public LinkedList<Position> getPositionsDesAutobus(){
        
        LinkedList<Position> listePositionsAutobus = new LinkedList<Position>();
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
