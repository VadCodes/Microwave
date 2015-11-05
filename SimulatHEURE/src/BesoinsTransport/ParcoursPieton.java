/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BesoinsTransport;
import ReseauRoutier.Trajet;
import ReseauRoutier.Emplacement;
import ReseauRoutier.Troncon;
import java.util.LinkedList;

/**
 *Le ParcoursPieton est un trajet
 * @author vadimcote
 */
public class ParcoursPieton {
    private Trajet m_Trajet;
    public ParcoursPieton(Trajet p_Trajet){
        m_Trajet = p_Trajet;
    }
    public Trajet getTrajet(){
        return m_Trajet;
    }
    public void setTrajet(Trajet trajet){
        m_Trajet = trajet;
    }
    
}

