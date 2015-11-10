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
import java.util.List;
import java.util.LinkedList;
public class Itineraire {
    private LinkedList<PaireParcours> m_list;
    
    public Itineraire(){
        m_list = new LinkedList<PaireParcours>();
    }
    public void ajouterPaireParcours(PaireParcours paireParcours){
        m_list.add(paireParcours);
    }
    public LinkedList<PaireParcours> getListPaireParcours(){
        return m_list;
    }
   public void setListPaireParcours(LinkedList<PaireParcours> p_list){
       m_list = p_list;
   }

}
  