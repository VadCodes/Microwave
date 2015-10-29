/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;


/**
 *
 * @author Nathaniel
 */
import Utilitaire.Temps;
import Utilitaire.Distribution;

public class Troncon {
       private Temps m_tempsTransitAutobus;
       private Distribution m_distribution;
       private Temps m_tempsTransitPieton;
       private Intersection m_intersectionOirgin;
       private Intersection m_intersectionDestination;
       public Troncon(Distribution distribution,
            Temps tempsTransitPieton,
            Intersection intersectionOirgin,
           Intersection intersectionDestination){
           m_distribution = distribution;
           m_tempsTransitPieton = tempsTransitPieton;
           m_intersectionOirgin = intersectionOirgin;
           m_intersectionDestination =  intersectionDestination;
       }
       public void setTempsTransit(){
           m_tempsTransitAutobus = m_distribution.pigerTemps();
           m_tempsTransitPieton = new Temps(longueurTroncon()/4);
       }
       public Temps getTempsTransitAutobus(){
           return m_tempsTransitAutobus;
       }
       public Temps getTempsTransitPieton(){
           return m_tempsTransitPieton;
       }
       public void setDistribution(Distribution distribution){
           m_distribution = distribution;
       }
       public Intersection getIntersectionOrigin(){
           return m_intersectionOirgin;
       }
       public Intersection getIntersectionDistination(){
           return m_intersectionDestination;
       }
       public double longueurTroncon(){
        return m_intersectionOirgin.getPosition().distanceEntrePositions(m_intersectionDestination.getPosition());
    }
}
