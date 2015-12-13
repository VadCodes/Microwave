/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;

/**
 *
 * @author vadimcote
 */
import Domaine.ReseauTransport.Trajet;
//import java.util.ListIterator;
import java.util.LinkedList;
import Domaine.Utilitaire.Temps;
import Domaine.Statistiques.StatistiqueBesoin;
import Domaine.Utilitaire.AlternateurCouleurs;
import java.awt.Color;

public class Itineraire extends ElementBesoins {
    private LinkedList<PaireParcours> m_listPaireParcours;
    private SourceIndividus m_source;
    
    private Color m_couleur = null;
    
    private LinkedList<Individu> m_listIndividu = new LinkedList<>();
    private StatistiqueBesoin m_stat;
        
    public Itineraire(LinkedList<PaireParcours> listPaireParcours){
        m_listPaireParcours = listPaireParcours;
    }
    
    public Itineraire(PaireParcours paireParcours){
        m_listPaireParcours = new LinkedList<>();
        m_listPaireParcours.add(paireParcours);
    }
    
    public LinkedList<PaireParcours> getListPaireParcours(){
        return m_listPaireParcours;
    }
    public void setStat(StatistiqueBesoin p_stat){
        m_stat = p_stat;
    }
   public void setListPaireParcours(LinkedList<PaireParcours> p_list){
       m_listPaireParcours = p_list;
   }
   
   public LinkedList<Individu> getListIndividu(){
       return m_listIndividu;
   }
   
   public SourceIndividus getSourceIndividu(){
       return m_source;
   }
   
    public void updateSourceIndividus(Temps deltatT){
        m_source.miseAJourTempsRestant(deltatT);
        m_source.genererIndividus(deltatT);
        
    }
    
    public void asignerSource(SourceIndividus p_source){
        m_source = p_source;
    }
    
    public void ajouterIndividu(Individu p_individu){
        m_listIndividu.add(p_individu);
    }

    public void ajouterPaire(ParcoursBus pb, Trajet traj){
        
        m_listPaireParcours.add(new PaireParcours(traj, pb));
    }
    
    public void assignerItineraire(Individu individu){
      //   individu.assignerTrajet(m_listPaireParcours);
    }

    public void initItineraire(){
        m_source.initSourceIndividu();
    }

    public String getNom(){
        return m_stat.getNameItineraire();
    }
    public void setNom(String p_nom){
        m_stat.setNameItineraire(p_nom);
    }
    public Color getCouleur(){
        if(m_couleur==null){
            setCouleur(AlternateurCouleurs.getCouleurItineraire());
        }
        return m_couleur;
    }
    
    public void setCouleur(Color couleur){
        m_couleur = couleur;
    }
}
  