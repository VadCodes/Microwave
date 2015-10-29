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
    private final ParcoursPieton m_parcoursPieton;
    private final ParcoursBus m_parcoursBus;
    public PaireParcours(ParcoursPieton m_parcoursPieton, ParcoursBus m_parcoursBus){
        this.m_parcoursPieton = m_parcoursPieton;
        this.m_parcoursBus = m_parcoursBus;
    }
}