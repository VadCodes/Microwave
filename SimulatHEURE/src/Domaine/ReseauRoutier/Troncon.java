package Domaine.ReseauRoutier;

/**
 *
 * @author Nathaniel
 */
import Domaine.Utilitaire.*;

public class Troncon {
    private String m_nom = "";
    private Temps m_tempsTransitAutobus;
    private Distribution m_distribution;
    private Temps m_tempsTransitPieton;
    private final Intersection m_intersectionDestination;
    
    public final static float LARGEUR = 5;
    public final static float GROSSEUR_FLECHE = 30;
    private Boolean m_estSelectionne = false;


    public Troncon(Intersection p_destination, Distribution distribution, Temps p_tempsTransitPieton)
    {
        m_intersectionDestination =  p_destination;
        m_distribution = distribution;
        m_tempsTransitPieton = p_tempsTransitPieton;
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
    public void setNom(String p_nom){
        m_nom = p_nom;
    }

    public Boolean estSelectione(){
        return m_estSelectionne;
    }

    public void changerStatutSelection(){
     m_estSelectionne = !m_estSelectionne;
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
