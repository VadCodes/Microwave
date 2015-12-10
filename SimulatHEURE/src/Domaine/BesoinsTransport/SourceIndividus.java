/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.Statistiques.StatistiqueBesoin;
import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;

/**
 *
 * @author vadimcote
 */
public class SourceIndividus {
    private Emplacement m_emplacement;
    private Itineraire m_itineraire;
    private StatistiqueBesoin m_stat;
    private Distribution m_distribution = new Distribution(Distribution.Type.PIETON);
    private Temps m_tempsAttenteInitial = new Temps(0);
    private Temps m_tempsAvantApparition;
    private int m_nbMaxIndividus = Integer.MAX_VALUE;
    
    private Temps m_frequence;
    
    private int m_nbIndividusGeneres = 0;
    
    public final static float LARGEUR = 20;
    
    public SourceIndividus(Emplacement p_emplacement, Itineraire p_itineraire, StatistiqueBesoin p_stat){
        m_stat = p_stat;
        m_emplacement = p_emplacement;
        m_tempsAvantApparition = m_tempsAttenteInitial;
        m_itineraire = p_itineraire;
    }
    
    public Emplacement getEmplacement() {
        return m_emplacement;
    }
    
    public void miseAJourTempsRestant(Temps p_deltatT){
        // Le temps avant le prochain ajout d'autobus diminu selon le deltatT
        double tmp = m_tempsAvantApparition.getTemps() - p_deltatT.getTemps();
        m_tempsAvantApparition = new Temps(tmp);       
    }

    public void initSourceIndividu() {
        m_frequence = m_distribution.pigerTemps();
    }
    
    public void setDefault() {
        m_tempsAvantApparition = m_tempsAttenteInitial;
    }
    public void setTempsAttenteInitial(Temps temps){
        m_tempsAttenteInitial.setTemps(temps.getTemps());
        setDefault() ;
    }
    public Temps getTempsAttenteInitial(){
        return m_tempsAttenteInitial;
    }
    public void setDistribution(Distribution p_distribution){
        m_distribution.setDistribution(p_distribution.getTempsMin(), p_distribution.getTempsFreq(), p_distribution.getTempsMax());
    }
    public Distribution getDistribution(){
        return m_distribution;
    }
    
    public void setNbMaxIndividus(int p_nbMaxIndividus){
        m_nbMaxIndividus = p_nbMaxIndividus;
    }
    public int getNbMaxIndividus(){
        return m_nbMaxIndividus;
    }
    
    
    public void genererIndividus(Temps p_deltatT){
        while(m_tempsAvantApparition.getTemps() <= 0 && (m_nbMaxIndividus > m_nbIndividusGeneres )){
            m_nbIndividusGeneres++;
            Emplacement em = new Emplacement(m_emplacement.estSurTroncon(), m_emplacement.getPourcentageParcouru(),m_emplacement.getTroncon(), m_emplacement.getIntersection());
            Temps tempsAvantApparition = new Temps ( p_deltatT.getTemps() + m_tempsAvantApparition.getTemps());
            Individu nouvelIndividu = new Individu(em, m_itineraire, tempsAvantApparition, false, m_stat);
            m_itineraire.ajouterIndividu(nouvelIndividu);
            m_itineraire.assignerItineraire(nouvelIndividu);
            //On update le temps avant apparition. On l'addition de la frequence.
            double tmp = m_tempsAvantApparition.getTemps() + m_frequence.getTemps();
            m_tempsAvantApparition = new Temps(tmp);
            initSourceIndividu();
        }
    }
   // public Boolean estSurArret(){
       // return m_emplacement.equals(m_itineraire.getListeParcoursBusTrajet().getFirst().getArret().getEmplacement()); 
   // }

    public void miseADefaut() {
        m_nbIndividusGeneres = 0;
       m_tempsAvantApparition = m_tempsAttenteInitial;
    }
}