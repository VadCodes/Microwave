/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

import Utilitaire.Distribution;
import Utilitaire.Temps;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author NASIM80
 */
public class ReseauRoutier {
    private LinkedList<Intersection> m_listIntersections= new LinkedList();
    private ReseauRoutierFactory m_factory = new ReseauRoutierFactory();
    public ReseauRoutier(){}
    
    public void  ajouterIntersection(Double x, Double y){
        Position position = m_factory.creerPosition(x, y);
        Intersection intersection = m_factory.creerIntersection(position);
        m_listIntersections.add(intersection);
    }
    
    public void ajouterTroncon(Intersection intersectionOrigin, Intersection intersectionDestination, Distribution distribution,
              Temps tempsTransitPieton){
        Troncon troncon = m_factory.creerTroncon(distribution, intersectionDestination, tempsTransitPieton);
        intersectionOrigin.ajouterTroncons(troncon);
    }
    
    /*
    public void  validerNom(String p_nom){
        ListIterator<Intersection> intersection_it= m_listIntersections.listIterator();
        while (intersection_it.hasNext()) {
            ListIterator<Troncon> troncon_it = intersection_it.next().getListTroncons().listIterator();
             while (troncon_it.hasNext()) {
                if(troncon_it.next().getNom().equals(p_nom)){
                    throw new Error("Le nom existe déjà");
                }
             }
          }
    }
    */
    public void setNameTroncon(Troncon p_troncon, String p_nom){
        p_troncon.serNom(p_nom);
    }
    
    public void initReseauRoutier(){
        ListIterator<Intersection> intersection_it= m_listIntersections.listIterator();
        while (intersection_it.hasNext()) {
            ListIterator<Troncon> troncon_it = intersection_it.next().getListTroncons().listIterator();
            while (troncon_it.hasNext()) {
                troncon_it.next().initTroncon();
            }
          }
    }
}

