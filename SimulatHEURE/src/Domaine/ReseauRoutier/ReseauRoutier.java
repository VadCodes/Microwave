package Domaine.ReseauRoutier;

import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;
import java.awt.geom.Point2D;
import java.util.LinkedList;
//import java.util.ListIterator;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutier {
    private LinkedList<Intersection> m_listeIntersections = new LinkedList();
    public ReseauRoutierFactory m_factory = new ReseauRoutierFactory(); // why pubblic !?
    
    private int m_nombreIntersectionsSelectionnees = 0;
    
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
    
    public Troncon ajouterTroncon(Intersection intersectionOrigin, Intersection intersectionDestination, Distribution distribution){
        double distance = intersectionOrigin.getPosition().distance(intersectionDestination.getPosition());
        Temps tempsTransit = new Temps(distance*1/4);
        Troncon troncon = m_factory.creerTroncon(distribution, intersectionDestination, tempsTransit);
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
        p_troncon.setNom(p_nom);
    }
    
    public Integer getIntersectionsSelectionnees()
    {
        return m_nombreIntersectionsSelectionnees;
    }
    
    public void setIntersectionsSelectionnees(Integer p_nombre)
    {
        m_nombreIntersectionsSelectionnees = p_nombre;
    }
    
    public void incrementerIntersectionsSelectionnees()
    {
        m_nombreIntersectionsSelectionnees += 1;
    }
    
    public void decrementerIntersectionsSelectionnees()
    {
        m_nombreIntersectionsSelectionnees -= 1;
    }
    
    public void initReseauRoutier(){
        for (Intersection intersection : m_listeIntersections)
        {
            for (Troncon troncon : intersection.getListeTroncons())
            {
                troncon.initTroncon();
            }
        }
    }
}

