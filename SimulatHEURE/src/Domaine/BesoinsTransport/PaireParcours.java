/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;

import java.util.LinkedList;

/**
 *
 * @author vadimcote
 */
public class PaireParcours {
    private ParcoursPieton m_parcoursPieton;
    private ParcoursBus m_parcoursBus;
    public PaireParcours(ParcoursPieton p_parcoursPieton, ParcoursBus p_parcoursBus){
        m_parcoursPieton = p_parcoursPieton;
        m_parcoursBus = p_parcoursBus;
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