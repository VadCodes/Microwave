/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauTransport;

/**
 *
 * @author louis
 */
import ReseauRoutier.Emplacement;
import Utilitaire.Temps;
import java.util.Queue;

public class Arret {
    private Emplacement m_emplacement;
    private String m_nom = "";
    private Queue<TempsArriveeAutobus> m_fileAutobus;
    
    public Arret(Emplacement emplacement, String nom){
        m_emplacement = emplacement;
        m_nom = nom;
    }
    
    public void viderFile(){
        m_fileAutobus.clear();
    }
    
    public void ajouterAutobus(Temps tempsArrivee, Autobus autobus)
    {
        m_fileAutobus.offer(new TempsArriveeAutobus(tempsArrivee, autobus));
    }
    
    public int nbAutobus()
    {
        return m_fileAutobus.size();
    }
    
    public String getNom(){
        return m_nom;
    }
    
    public void setNom(String nom){
        m_nom = nom;
    }
    
    public Emplacement getEmplacement(){
        return m_emplacement;
    }
    
    public void setEmplacement(Emplacement empl){
        m_emplacement = empl;
    }
}