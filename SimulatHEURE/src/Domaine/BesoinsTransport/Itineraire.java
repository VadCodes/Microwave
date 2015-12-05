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
import java.util.ListIterator;
import java.util.LinkedList;
import Domaine.Utilitaire.Temps;
import Domaine.ReseauRoutier.*;

public class Itineraire extends ElementBesoins {
    private LinkedList<PaireParcours> m_listPaireParcours;
    private LinkedList<SourceIndividus> m_listSources = new LinkedList<>();
    private LinkedList<Individu> m_listIndividu = new LinkedList<>();
    
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
    
   public void setListPaireParcours(LinkedList<PaireParcours> p_list){
       m_listPaireParcours = p_list;
   }
   
   public LinkedList<Individu> getListIndividu(){
       return m_listIndividu;
   }
   
   public LinkedList<SourceIndividus> getListSourceIndividu(){
       return m_listSources;
   }
   
    public void updateSourceIndividus(Temps deltatT){
         ListIterator<SourceIndividus> sourceIndividusItr = m_listSources.listIterator();
        while (sourceIndividusItr.hasNext()) {
            SourceIndividus src = sourceIndividusItr.next();
            src.miseAJourTempsRestant(deltatT);
            src.genererIndividus(deltatT);
        }
    }
    
    public void ajouterSource(SourceIndividus p_source){
        m_listSources.add(p_source);
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
        ListIterator<SourceIndividus> sourceIndividuItr = m_listSources.listIterator();
        while (sourceIndividuItr.hasNext()) {
            sourceIndividuItr.next().initSourceIndividu();
        }
    }

}
  