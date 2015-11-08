/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauTransport;


/**
 *
 * @author louis
 */
import java.util.LinkedList;
import java.util.ListIterator;
import ReseauRoutier.Position;

public class Circuit {
    private String nom = "";
    private Arret Origine;
    private LinkedList<SourceAutobus> listeSources;
    private LinkedList<Autobus> listeAutobus;
    private LinkedList<PaireArretTrajet> listeArretTrajet;
    
    public void ajouterSource(SourceAutobus source){
        listeSources.add(source);
    }
    
    public void ajouterAutobus(Autobus autobus){
        listeAutobus.add(autobus);
    }
    
    public void calculCirculationGlobal(){
        
        //on vide toutes les files d'arrets
        ListIterator<PaireArretTrajet> arretTrajetItr = listeArretTrajet.listIterator();
        while (arretTrajetItr.hasNext()) {
            arretTrajetItr.next().getArret().viderFile();
        }
        
        //pour chaque autobus on calcule la circulation
        ListIterator<Autobus> autobusItr = listeAutobus.listIterator();
        while (autobusItr.hasNext()) {
            calculCirculation(autobusItr.next());
        }
    }
    
    public void calculCirculation(Autobus bus){
        
    }
    
    public LinkedList<Position> getPositionsDesAutobus(){
        
        LinkedList<Position> listePositionsAutobus = new LinkedList<Position>();
        ListIterator<Autobus> autobusItr = listeAutobus.listIterator();
        while (autobusItr.hasNext()) {
            listePositionsAutobus.add(autobusItr.next().getPosition());
        }
        
        return listePositionsAutobus;
    }
    
    public LinkedList<PaireArretTrajet> getListeArretTrajet(){
        return listeArretTrajet;
    }
    
    public void incrementerIterateurAutobus(Autobus bus){
        bus.incrementerIterateur();
    }
    
    public void assignerTrajetAutobus(Autobus bus){
        bus.assignerTrajet(listeArretTrajet);
    }
    
    public void initCircuit(){
        ListIterator<SourceAutobus> sourceAutobusItr = listeSources.listIterator();
        while (sourceAutobusItr.hasNext()) {
            sourceAutobusItr.next().initSourceAutobus();
        }
    }
}
