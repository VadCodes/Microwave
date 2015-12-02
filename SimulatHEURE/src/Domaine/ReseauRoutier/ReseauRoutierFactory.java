/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

import Domaine.ReseauTransport.Trajet;
import Domaine.Utilitaire.*;

import java.util.LinkedList;
import java.awt.geom.Point2D;
import java.util.ListIterator;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutierFactory {
    
       public ReseauRoutierFactory(){}       
       
      public Intersection copyIntersection(Intersection p_intersection){
          if(p_intersection != null){
            Point2D.Float p1 = new Point2D.Float(p_intersection.getPosition().x, p_intersection.getPosition().y);
            return intersection(p1);
          }
          else{
              return null;
          }
      }
       
       public Troncon copyTroncon(Troncon p_troncon){
           if(p_troncon != null){
            Intersection intersectionOrigin = copyIntersection(p_troncon.getOrigine());
            Intersection intersectionDestination = copyIntersection(p_troncon.getDestination());
            return troncon(intersectionOrigin,intersectionDestination);
           }
           else{
               return null;
           }
       }
       
       public Emplacement copyEmplacement(Emplacement p_emplacement){
           if(p_emplacement != null){
                Boolean estSurTroncon = p_emplacement.estSurTroncon();
                float  pourcentageParcouru = p_emplacement.getPourcentageParcouru();
                Troncon troncon = copyTroncon(p_emplacement.getTroncon());
                Intersection intersection = copyIntersection(p_emplacement.getIntersection());
                return creerEmplacement(estSurTroncon, pourcentageParcouru, troncon, intersection);
           }
           else{
               return null;
           }
       }
       
       public Trajet copyTrajet(Trajet p_trajet){
           if(p_trajet != null){
               Emplacement emplacementInitial = copyEmplacement(p_trajet.getEmplacementInitial());
               Emplacement emplacementFinal = copyEmplacement(p_trajet.getEmplacementFinal());
               LinkedList<Troncon> troncons = new LinkedList<>();
               for (ListIterator<Troncon> tronc = p_trajet.getListeTroncons().listIterator() ;tronc.hasNext() ; ){
                Troncon tr = copyTroncon(tronc.next());
                troncons.add(tr);
               }     
           return creerTrajet(emplacementInitial, emplacementFinal, troncons);
           }
           return null;
       }
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
