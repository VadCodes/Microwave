/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

import Domaine.ReseauTransport.Trajet;
//import Domaine.Utilitaire.*;

import java.util.LinkedList;
import java.awt.geom.Point2D;
//import java.util.ListIterator;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutierFactory implements java.io.Serializable {
    
       public ReseauRoutierFactory(){}       
              
       public Troncon troncon(Intersection p_origine, Intersection p_destination)
       {
           return new Troncon(p_origine, p_destination);
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
