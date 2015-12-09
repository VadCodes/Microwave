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
    private Temps m_tempsInitial;
    private Distribution m_distributionFrequence;
    private Emplacement m_emplacement;
    private Temps m_frequence;
    private String m_nom;
    private int m_nbMaxIndividus = Integer.MAX_VALUE;
    private int m_nbIndividusGeneres = 0;
    private Itineraire m_itineraire;
    private Temps m_tempsAvantApparition;
    private StatistiqueBesoin m_stat;
    
    public final static float LARGEUR = 20;
    
    public SourceIndividus(Temps p_tempsInitial, Distribution p_distributionFrequence, Emplacement p_emplacement, String p_nom
   , Itineraire p_itineraire, StatistiqueBesoin p_stat){
        m_stat = p_stat;
        m_tempsInitial = p_tempsInitial;
        m_distributionFrequence = p_distributionFrequence;
        m_emplacement = p_emplacement;
        m_nom = p_nom;
        m_tempsAvantApparition = m_tempsInitial;
        m_itineraire = p_itineraire;
    }
    
    public void miseAJourTempsRestant(Temps p_deltatT){
        // Le temps avant le prochain ajout d'autobus diminu selon le deltatT
        double tmp = m_tempsAvantApparition.getTemps() - p_deltatT.getTemps();
        m_tempsAvantApparition = new Temps(tmp);       
    }

    public void initSourceIndividu(){
        m_frequence = m_distributionFrequence.pigerTemps();

    }
    
    public void setNom(String nom){
        m_nom = nom;
    }
    public void setEmplacement(Emplacement emplacement){
        m_emplacement = emplacement;
    }
    public void setDefault() {
        m_tempsAvantApparition = m_tempsInitial;
    }
    public void setTempsAttenteInitial(Temps temps){
        m_tempsInitial = temps;
         setDefault() ;
    }
    public Temps getTempsAttenteInitial(){
        return m_tempsInitial;
    }
    public void setDistribution(Distribution dist){
        m_distributionFrequence = dist;
    }
    public Distribution getDistribution(){
        return m_distributionFrequence;
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
       m_tempsAvantApparition = m_tempsInitial;
    }
}