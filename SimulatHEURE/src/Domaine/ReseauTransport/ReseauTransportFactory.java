/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;

import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Troncon;
import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;
import java.util.LinkedList;

/**
 *
 * @author Nathaniel
 */
public class ReseauTransportFactory {
    ReseauTransportFactory(){}

    public Arret creerArret(Emplacement emplacement){
        return new Arret(emplacement);
    } 
    
    public Circuit creerCircuit(LinkedList<PaireArretTrajet> listeArrTraj){
        return new Circuit(listeArrTraj);
    } 
    
    public PaireArretTrajet creerPaireArretTrajet(Arret arret, Trajet trajet){
        return new PaireArretTrajet (arret, trajet);
    } 
    
    public Trajet creerTrajet(Emplacement emplacementInitial, Emplacement emplacementFinal, LinkedList<Troncon> listTroncons){
        return new Trajet (emplacementInitial, emplacementFinal, listTroncons);
    } 
    public SourceAutobus creerSource(Emplacement p_emplacement, Circuit p_circuit, Distribution p_distribution){
        return new SourceAutobus(p_emplacement, p_circuit, p_distribution);
    }
    
    
}
