package Domaine.ReseauRoutier;

/**
 *
 * @author Nathaniel
 */
import Domaine.Utilitaire.*;

public class Troncon extends ElementRoutier{
    private String m_nom = "";
    private Temps m_tempsTransitAutobus;
    private Distribution m_distribution;
    private Temps m_tempsTransitPieton;
    private final Intersection m_intersectionDestination;
    private Boolean m_doubleSens = false;
    private float m_longeur;
    
    public final static float LARGEUR = 5;
    public final static float GROSSEUR_FLECHE = 30;


    public Troncon(Intersection p_destination, Distribution distribution, Temps p_tempsTransitPieton)
    {
        m_intersectionDestination =  p_destination;
        m_distribution = distribution;
        m_tempsTransitPieton = p_tempsTransitPieton;
       
    }
    public void setLongueur(float p_longueur){
         m_longeur = p_longueur;
    }
    public void setTempsTransit(){
        m_tempsTransitAutobus = m_distribution.pigerTemps();
    }
    public void setDoubleSens(){
        Boolean doubleSens = false;
        for (Troncon trcDest: this.getDestination().getTroncons()){
            for (Troncon trcOrig: trcDest.getDestination().getTroncons()){
                if (trcOrig == this){
                    doubleSens = true;
                    break;
                }
            }
        }       
        m_doubleSens = doubleSens;
    }
    public Boolean getDoubleSens(){
        return m_doubleSens;
    }
    
    public Temps getTempsTransitAutobus(){
        return m_tempsTransitAutobus;
    }
    public Temps getTempsTransitPieton(){
        return m_tempsTransitPieton;
    }
    public void setDistribution(Distribution p_distribution){
        m_distribution = p_distribution;
    }
    public Distribution getDistribution(){
        return m_distribution;
    }
    public Intersection getDestination(){
        return m_intersectionDestination;
    }
    public String getNom(){
        return m_nom;
    }
    public void setNom(String p_nom){
        m_nom = p_nom;
    }

    public void initTroncon(){
        m_tempsTransitAutobus = m_distribution.pigerTemps(); //nope !
    }


    public float longueurTroncon(){
     return m_longeur;
 }
}
