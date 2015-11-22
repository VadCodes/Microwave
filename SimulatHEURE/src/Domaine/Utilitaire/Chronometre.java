/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Utilitaire;


/**
 *
 * @author Nathaniel
 */
public class Chronometre {
    long m_tempsPrec;
    long m_tempsDepuisDebut= 0;
    long m_tempsPauseDebut;
    double m_facteurVitesse = 1;
    boolean m_pause = false;
    public Chronometre(){ }
    public void  start(){
           m_pause = false;
           m_tempsPrec =  System.currentTimeMillis();
    }
        
    public double getFacteurVitesse(){
        return m_facteurVitesse;
    }
    public void avancerX2(){
        m_facteurVitesse *=2;
    }
    
    public void ralentirX2(){
        m_facteurVitesse /=2;
    }
    public boolean estEnPause(){
        return m_pause;
    }
    public double getDeltatT(){
        if(!m_pause){
           long timeNow = System.currentTimeMillis();
           double tmp =  m_facteurVitesse*(timeNow - m_tempsPrec );
           m_tempsPrec = timeNow;
           m_tempsDepuisDebut += tmp;
            return tmp/1000;
        }
        return 0;
    }    
    public void pause(){
        if(!m_pause){
            m_pause = true;
            m_tempsPauseDebut = System.currentTimeMillis();
        }
    }
    
    public long getTempsDebut(){
        return m_tempsDepuisDebut/1000;
    }
    
}
