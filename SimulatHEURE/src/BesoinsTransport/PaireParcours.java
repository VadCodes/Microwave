/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BesoinsTransport;

import java.util.LinkedList;

/**
 *
 * @author vadimcote
 */
public class PaireParcours {
    private ParcoursPieton m_parcoursPieton;
    private ParcoursBus m_parcoursBus;
    public PaireParcours(ParcoursPieton m_parcoursPieton, ParcoursBus m_parcoursBus){
        this.m_parcoursPieton = m_parcoursPieton;
        this.m_parcoursBus = m_parcoursBus;
    }
    public ParcoursPieton getParcoursPieton(){
        return m_parcoursPieton;
    }
    public ParcoursBus getParcoursBus(){
        return m_parcoursBus;
    }
    public void setParcoursPieton(ParcoursPieton parcoursPieton){
        m_parcoursPieton = parcoursPieton;
    }
    public void setParcoursBus(ParcoursBus parcoursBus){
        m_parcoursBus = parcoursBus;
    }
}