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
       private String m_nom = "";
       private Temps m_tempsTransitAutobus;
       private Distribution m_distribution;
       private Temps m_tempsTransitPieton;
       private final  Intersection m_intersectionDestination;
       public Troncon(Distribution distribution,
           Intersection intersectionDestination,
           Temps tempsTransitPieton){
           m_distribution = distribution;
           m_intersectionDestination =  intersectionDestination;
           m_tempsTransitPieton = tempsTransitPieton;
       }
       public void setTempsTransit(){
           m_tempsTransitAutobus = m_distribution.pigerTemps();
           
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
       public Intersection getIntersectionDestination(){
           return m_intersectionDestination;
       }
       public String getNom(){
           return m_nom;
       }
       public void serNom(String p_nom){
           m_nom = p_nom;
       }
       public void initTroncon(){
           m_tempsTransitPieton = m_distribution.pigerTemps();
       }
       
       /*
       public double longueurTroncon(){
        return m_intersectionOrigin.getPosition().distanceEntrePositions(m_intersectionDestination.getPosition());
    }
       */
}
