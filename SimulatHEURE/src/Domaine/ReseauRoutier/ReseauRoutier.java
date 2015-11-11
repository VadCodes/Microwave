package Domaine.ReseauRoutier;

import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.ListIterator;
//import java.util.ListIterator;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutier {
    private LinkedList<Intersection> m_listeIntersections = new LinkedList();
    public ReseauRoutierFactory m_factory = new ReseauRoutierFactory();
    public ReseauRoutier(){}
    
    public LinkedList<Intersection> getIntersections(){
        return m_listeIntersections;
    }
    
    public Intersection  ajouterIntersection(float x, float y){
        Point2D.Float position = m_factory.creerPosition(x, y);
        Intersection intersection = m_factory.creerIntersection(position);
        m_listeIntersections.add(intersection);
        return intersection;
    }
    
    public Troncon ajouterTroncon(Intersection intersectionOrigin, Intersection intersectionDestination, Distribution distribution,
              Temps tempsTransitPieton){
        Troncon troncon = m_factory.creerTroncon(distribution, intersectionDestination, tempsTransitPieton);
        intersectionOrigin.ajouterTroncons(troncon);
        return troncon;
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
         ListIterator<Intersection> intersection_it= m_listeIntersections.listIterator();
        while (intersection_it.hasNext()) {
            ListIterator<Troncon> troncon_it = intersection_it.next().getListeTroncons().listIterator();
             while (troncon_it.hasNext()) {
               Troncon tr =  troncon_it.next();
               tr.initTroncon();
             }
          }
        /*
        for (Intersection intersection : m_listeIntersections)
        {
            for (Troncon troncon : intersection.getListeTroncons())
            {
                troncon.initTroncon();
            }
        }*/
    }
}

