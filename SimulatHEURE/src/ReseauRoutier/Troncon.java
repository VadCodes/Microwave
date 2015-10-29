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
       public Troncon(Temps tempsTransitAutobus,Distribution distribution,
            Temps tempsTransitPieton,
            Intersection intersectionOirgin,
           Intersection intersectionDestination){
           m_tempsTransitAutobus =  tempsTransitAutobus;
           m_distribution = distribution;
           m_tempsTransitPieton = tempsTransitPieton;
           m_intersectionOirgin = intersectionOirgin;
           m_intersectionDestination =  intersectionDestination;
       }
       
}
