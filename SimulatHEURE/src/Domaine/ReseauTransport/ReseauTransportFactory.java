/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;

import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Trajet;
import Domaine.ReseauRoutier.Troncon;
import java.util.LinkedList;

/**
 *
 * @author Nathaniel
 */
public class ReseauTransportFactory {
    
    ReseauTransportFactory(){}
    
    public Arret creerArret(Emplacement emplacement, String nom){
        return new Arret(emplacement, nom);
    } 
    
    public Circuit creerCircuit(String nom, LinkedList<PaireArretTrajet> listeArrTraj){
        return new Circuit(nom, listeArrTraj);
    } 
    
    public PaireArretTrajet creerPaireArretTrajet(Arret arret, Trajet trajet){
        return new PaireArretTrajet (arret, trajet);
    } 
    
    public Trajet creerTrajet(Emplacement emplacementInitial, Emplacement emplacementFinal, LinkedList<Troncon> listTroncons){
        return new Trajet (emplacementInitial, emplacementFinal, listTroncons);
    } 
    
    
    
}
