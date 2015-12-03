/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.BesoinsTransport;
import Domaine.ReseauRoutier.Troncon;
import Domaine.ReseauTransport.Circuit;
import Domaine.ReseauTransport.Arret;
import Domaine.ReseauTransport.PaireArretTrajet;
import java.util.LinkedList;
import java.util.ListIterator;

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

    public LinkedList<Troncon> getTroncons(){
        boolean premierArret = false;
        LinkedList<Troncon> mesTroncons = new LinkedList<>();
         for (ListIterator<PaireArretTrajet>  paires = m_circuit.getListeArretTrajet().listIterator(); paires.hasNext();) {
            PaireArretTrajet paire = paires.next();
            if(m_arretdepart.equals(paire.getArret())){
                premierArret = true;
            }
            else if(m_arretfinal.equals(paire.getArret())){
                premierArret = false;
            }
            if(premierArret = true){
                for (ListIterator<Troncon>  troncons = paire.getTrajet().getListeTroncons().listIterator();troncons.hasNext();){ 
                    Troncon troncon = troncons.next();
                    mesTroncons.add(troncon);
                }
        }
    }
        return mesTroncons;
    }
}
       