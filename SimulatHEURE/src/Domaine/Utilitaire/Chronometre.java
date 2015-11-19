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
    long m_tempsInitial;
    Date m_timer = new Date();
    public Chronometre(){
        
    }
    public void  start(){
        m_tempsInitial = System.currentTimeMillis();
        m_tempsPrec = m_tempsInitial;
    }
        
    public double getDeltatT(){
        long timeNow = System.currentTimeMillis();
        double tmp =  timeNow - m_tempsPrec ;
        m_tempsPrec = timeNow;
        return tmp/1000;
    }    
    
    public long getTempsDebut(){
        return m_timer.getTime()-m_tempsInitial;
    }
}
