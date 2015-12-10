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
    private PaireArretTrajet m_paireArretTrajetDepart;
    private PaireArretTrajet m_paireArretTrajetFinal;
    private Circuit m_circuit;
    
    public ParcoursBus(Circuit p_circuit, PaireArretTrajet p_pairearretdepart, PaireArretTrajet p_pairearretfinal){
        m_paireArretTrajetDepart = p_pairearretdepart;
        m_paireArretTrajetFinal = p_pairearretfinal;
        m_circuit = p_circuit;
    }
    public Arret getArretDepart(){
        return m_paireArretTrajetDepart.getArret();
    }
    public Arret getArretFinal(){
        return m_paireArretTrajetFinal.getArret();
    }
    public PaireArretTrajet getPaireArretDepart(){
        return m_paireArretTrajetDepart;
    }
    public PaireArretTrajet getPaireArretFinal(){
        return m_paireArretTrajetFinal;
    }
    public Circuit getCircuit(){
        return m_circuit;
    }
    public void setCircuit(Circuit circuit){
        m_circuit = circuit;
    }
    public void setPaireArretFinal(PaireArretTrajet p_pairearretfinal){
        m_paireArretTrajetFinal = p_pairearretfinal;
    }
    public void setPaireArretDepart(PaireArretTrajet p_pairearretfinal){
        m_paireArretTrajetDepart = p_pairearretfinal;
    }

    public LinkedList<Troncon> getTroncons(){
        Boolean premierArret = false;
        LinkedList<Troncon> mesTroncons = new LinkedList<>();
        for(PaireArretTrajet pat : m_circuit.getListeArretTrajet()){
            if(pat==m_paireArretTrajetDepart){
                premierArret = true;
            }
            else if(pat==m_paireArretTrajetFinal){
                premierArret = false;
                break;
            }
            
            if (premierArret){
                if(pat.getTrajet()!=null){
                    for (ListIterator<Troncon>  troncons = pat.getTrajet().getListeTroncons().listIterator();troncons.hasNext();){ 
                        Troncon troncon = troncons.next();
                        if(!mesTroncons.contains(troncon))
                            mesTroncons.add(troncon);
                    }
                }
            }
        } 
        return mesTroncons;
    }
}
       