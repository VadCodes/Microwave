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
<<<<<<< HEAD:SimulatHEURE/src/BesoinsTransport/PaireParcours.java
public class PaireParcours {
    private final ParcoursPieton m_parcoursPieton;
    private final ParcoursBus m_parcoursBus;
    public PaireParcours(ParcoursPieton m_parcoursPieton, ParcoursBus m_parcoursBus){
=======
public class paireParcours {
    private ParcoursPieton m_parcoursPieton;
    private ParcoursBus m_parcoursBus;
    public paireParcours(ParcoursPieton m_parcoursPieton, ParcoursBus m_parcoursBus){
>>>>>>> Vadim:SimulatHEURE/src/BesoinsTransport/paireParcours.java
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