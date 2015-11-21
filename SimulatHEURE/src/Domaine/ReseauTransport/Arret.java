/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;

/**
 *
 * @author louis
 */
import Domaine.ReseauRoutier.Emplacement;
import Domaine.Utilitaire.Temps;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Arret extends ElementTransport{
    private Emplacement m_emplacement;
    private String m_nom = "";
    private LinkedList<TempsArriveeAutobus> m_fileAutobus = new LinkedList();
    public final static float RAYON = 8;
    
    public Arret(Emplacement emplacement, String nom){
        m_emplacement = emplacement;
        m_nom = nom;
    }
    
    public Arret(){
        
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