/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

import Utilitaire.Distribution;
import Utilitaire.Temps;
import java.util.LinkedList;

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
       private ReseauRoutierFactory(){}
       
       
       public Troncon creerTroncon(Distribution distribution,
           Intersection intersectionDestination, Temps tempsTransitPieton){
           Troncon troncon = new Troncon(distribution, intersectionDestination, tempsTransitPieton);
           //m_listTroncons.add(troncon);
           return troncon;
       }
       
       public Intersection creerIntersection(Position position){
           Intersection intersection = new Intersection(position);
          // m_listIntersections .add(intersection);
           return intersection;
       }
       
       public Emplacement creerEmplacement(Boolean estSurTroncon, double pourcentageParcouru, Troncon troncon, Intersection intersection){
           Emplacement emplacement = new Emplacement(estSurTroncon, pourcentageParcouru, troncon, intersection);
          // m_listEmplacement .add(emplacement);
           return emplacement;
       }
       
       public Trajet creerTrajet(Emplacement emplacementInitial, Emplacement emplacementFinal, LinkedList<Troncon> listTroncons){
           Trajet trajet = new Trajet(emplacementInitial, emplacementFinal, listTroncons);
          // m_listTrajet.add(trajet);
           return trajet;
       }
       
       public Position creerPosition(double x, double y){
           Position position = new Position(x,y);
          // m_listPosition.add(position);
           return position;
       }
}
