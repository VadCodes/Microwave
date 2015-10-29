/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauTransport;
import java.util.LinkedList;

/**
 *
 * @author louis
 */
import java.util.LinkedList;

public class Circuit {
    String nom = "";
    Arret Origine;
    LinkedList<SourceAutobus> listeSources;
    LinkedList<Autobus> listeAutobus;
    LinkedList<PaireArretTrajet> listeArretTrajet;
    
    public void ajouterSource(SourceAutobus source){
        listeSources.add(source);
    }
    
    public void ajouterAutobus(SourceAutobus source){
        listeSources.add(source);
    }
    
    public void calculCirculationGlobal(){
        
    }
}
