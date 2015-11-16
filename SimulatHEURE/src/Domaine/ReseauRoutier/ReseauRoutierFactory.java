/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

import Domaine.Utilitaire.*;

import java.util.LinkedList;
import java.awt.geom.Point2D;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutierFactory {
      // private LinkedList<Troncon> m_listTroncons = new LinkedList();
      // private LinkedList<Intersection> m_listIntersections = new LinkedList();
      // private LinkedList<Emplacement> m_listEmplacement = new LinkedList();
      // private LinkedList<Trajet> m_listTrajet = new LinkedList();
      // private LinkedList<Position> m_listPosition = new LinkedList();
       public ReseauRoutierFactory(){}
       
       
       public Troncon creerTroncon(Intersection p_origine, Intersection p_destination)
       {
           double longueur = p_origine.getPosition().distance(p_destination.getPosition());
           Temps tempsTransitPieton = new Temps(longueur / ReseauRoutier.VITESSE_PIETON);
           Troncon troncon = new Troncon(p_destination, new Distribution(), tempsTransitPieton);
           troncon.setLongueur((float)longueur);
           return troncon;
       }
       
       public Intersection intersection(Point2D.Float pt)
       {
           return new Intersection(pt);
       }
       
       public Emplacement creerEmplacement(Boolean estSurTroncon, float  pourcentageParcouru, Troncon troncon, Intersection intersection){
           Emplacement emplacement = new Emplacement(estSurTroncon, pourcentageParcouru, troncon, intersection);
          // m_listEmplacement .add(emplacement);
           return emplacement;
       }
       
       public Trajet creerTrajet(Emplacement emplacementInitial, Emplacement emplacementFinal, LinkedList<Troncon> listTroncons){
           Trajet trajet = new Trajet(emplacementInitial, emplacementFinal, listTroncons);
          // m_listTrajet.add(trajet);
           return trajet;
       }
}
