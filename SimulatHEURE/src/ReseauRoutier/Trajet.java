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
import java.util.Iterator;
import java.util.LinkedList;

public class Trajet {
    private Emplacement m_emplacementInitial;
    private Emplacement m_emplacementFinal;
    private LinkedList<Troncon> m_listTroncons;
    public Trajet(Emplacement emplacementInitial, Emplacement emplacementFinal, LinkedList<Troncon> listTroncons){
        m_emplacementInitial = emplacementInitial;
        m_emplacementFinal = emplacementFinal;
        m_listTroncons = listTroncons;
    }
    
    /*
    public Boolean estValid(){
        Boolean tmp1;
        Boolean tmp2;
        tmp1 = m_listTroncons.getFirst().equals(m_emplacementInitial.getTroncon());
        tmp2 = m_listTroncons.getLast().equals(m_emplacementFinal.getTroncon());
        Iterator<Troncon> itr = m_listTroncons.iterator();
        if(itr.hasNext()){
            Troncon theLastOne = itr.next();
            while(itr.hasNext()){
                 Troncon tmp = itr.next();
                 if(theLastOne.estAdjacent(tmp)){
                     return false;
                 }
                 theLastOne = tmp;
             }
        }
        return true;
    }*/
}
