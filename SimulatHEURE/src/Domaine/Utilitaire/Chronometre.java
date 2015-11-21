/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Utilitaire;
import java.util.Date;


/**
 *
 * @author Nathaniel
 */
public class Chronometre {
    long m_tempsPrec;
    long m_tempsInitial = -1;
    long m_tempsPauseDebut;
    boolean m_pause = false;
    Date m_timer = new Date();
    public Chronometre(){ }
    public void  start(){
        if(m_tempsInitial == -1){
            m_tempsInitial = System.currentTimeMillis();
            m_tempsPrec = m_tempsInitial;
        }
        else{
            m_tempsPrec =  System.currentTimeMillis();
            m_pause = false;
        }
    }
        
    
    public double getDeltatT(){
        if(!m_pause){
           long timeNow = System.currentTimeMillis();
           double tmp =  timeNow - m_tempsPrec ;
           m_tempsPrec = timeNow;
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
        return m_timer.getTime()-m_tempsInitial;
    }
}
