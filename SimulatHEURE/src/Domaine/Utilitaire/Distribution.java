package Domaine.Utilitaire;

/**
 *
 * @author vinny
 */
public class Distribution implements java.io.Serializable {
    private Temps m_tempsMinimum;
    private Temps m_tempsPlusFrequent;
    private Temps m_tempsMaximum;
    private Temps m_tempsMoyen;

    private double m_hauteur;
    private double m_penteGauche;
    private double m_aireGauche;
    private double m_penteDroite;
    
    public enum Type {

        TRONCON, AUTOBUS, PIETON
    }

    public Distribution(Type p_type)
    {
        if (p_type == Type.TRONCON)
            setDistribution(new Temps(5 * 60), new Temps(5 * 60), new Temps(5 * 60));
        else if (p_type == Type.AUTOBUS)
            setDistribution(new Temps(15 * 60), new Temps(15 * 60), new Temps(15 * 60));
        else
            setDistribution(new Temps(3 * 60), new Temps(3 * 60), new Temps(3 * 60));
    }
    
    public final void setDistribution(Temps p_tempsMinimum, Temps p_tempsPlusFrequent, Temps p_tempsMaximum)
    {
        double tMin = p_tempsMinimum.getTemps();
        double tFreq = p_tempsPlusFrequent.getTemps();
        double tMax = p_tempsMaximum.getTemps();
        
        if (tFreq > tMax)
        {
            throw new IllegalArgumentException("Le temps maximal doit être supérieur ou égal au temps le plus fréquent.", new Throwable("Champs invalides"));
        }        
        else if (tFreq < tMin)
        {
            throw new IllegalArgumentException("Le temps minimal doit être inférieur ou égal au temps le plus fréquent.", new Throwable("Champs invalides"));
        }        
        
        m_tempsMinimum = p_tempsMinimum;
        m_tempsPlusFrequent = p_tempsPlusFrequent;
        m_tempsMaximum = p_tempsMaximum;
        
        m_tempsMoyen = calculerTempsMoyen();
        
        m_hauteur = 2 / (tMax - tMin);
        m_penteGauche = m_hauteur / (tFreq - tMin);
        m_aireGauche = m_hauteur * (tFreq - tMin) / 2;
        m_penteDroite = m_hauteur / (tMax - tFreq);
        
        m_tempsMoyen = calculerTempsMoyen();
    }
    
    public Temps pigerTemps()
    {
        double aire = Math.random();
        Temps tempsPige;
        
        if (aire <= m_aireGauche)
        {
            tempsPige = new Temps(m_tempsMinimum.getTemps() + Math.sqrt(2 * aire / m_penteGauche));
        }
        else
        {
            tempsPige = new Temps(m_tempsMaximum.getTemps() - Math.sqrt(2 * (1 - aire) / m_penteDroite));
        }
        
        return tempsPige;
    }
    
    private Temps calculerTempsMoyen()
    {
        double aire = 0.5;
        Temps tempsMoyen;
        
        if (aire <= m_aireGauche)
        {
            tempsMoyen = new Temps(m_tempsMinimum.getTemps() + Math.sqrt(2 * aire / m_penteGauche));
        }
        else
        {
            tempsMoyen = new Temps(m_tempsMaximum.getTemps() - Math.sqrt(2 * (1 - aire) / m_penteDroite));
        }
        
        return tempsMoyen;
    }
    
    public Temps getTempsMin(){
        return m_tempsMinimum;
    }
    
    public Temps getTempsMax(){
        return m_tempsMaximum;
    }
    
    public Temps getTempsFreq(){
        return m_tempsPlusFrequent;
    }
    
    public Temps getTempsMoyen(){
        return m_tempsMoyen;
    }
}
