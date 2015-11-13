package Domaine.ReseauRoutier;

/**
 *
 * @author Nathaniel
 */
import Domaine.Utilitaire.Temps;
import Domaine.Utilitaire.Distribution;

public class Troncon {
        private String m_nom = "";
        private Temps m_tempsTransitAutobus;
        private Distribution m_distribution;
        private Temps m_tempsTransitPieton;
        private final Intersection m_intersectionDestination;
        public final static float LARGEUR = 5;
        public final static float GROSSEUR_FLECHE = 30;
       
       
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
       public Intersection getDestination(){
           return m_intersectionDestination;
       }
       public String getNom(){
           return m_nom;
       }
       public void serNom(String p_nom){
           m_nom = p_nom;
       }
       public void initTroncon(){
           m_tempsTransitAutobus = m_distribution.pigerTemps(); //nope !
       }
       
       
       /*
       public double longueurTroncon(){
        return m_intersectionOrigin.getPosition().distanceEntrePositions(m_intersectionDestination.getPosition());
    }
       */
}
