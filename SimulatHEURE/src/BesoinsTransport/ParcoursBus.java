/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BesoinsTransport;
import ReseauTransport.Circuit;
import ReseauTransport.Arret;

/**
 *
 * @author vadimcote
 */
public class ParcoursBus {
    private Arret m_arretdepart;
    private Arret m_arretfinal;
    private Circuit m_circuit;
    public ParcoursBus(Circuit p_circuit, Arret p_arretdepart, Arret p_arretfinal){
        m_arretdepart = p_arretdepart;
        m_arretfinal = p_arretfinal;
        m_circuit = p_circuit;
    }
    public Arret getArretDepart(){
        return m_arretdepart;
    }
    public Arret getArretFinal(){
        return m_arretfinal;
    }
    public Circuit getCircuit(){
        return m_circuit;
    }
    public void setCircuit(Circuit circuit){
        m_circuit = circuit;
    }
    public void setArretFinal(Arret arretfinal){
        m_arretfinal = arretfinal;
    }
    public void setArretDepart(Arret arretdepart){
        m_arretdepart = arretdepart;
    }
    
}