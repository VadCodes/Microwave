/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Utilitaire;

import java.awt.geom.Point2D;

/**
 *
 * @author louis
 */
public class PaireFloats {
    private Float m_f1;
    private Float m_f2;
    
    public PaireFloats(Float f1, Float f2){
            m_f1 = f1;
            m_f2 = f2;
    }
    
    public Float getFloat1(){
        return m_f1;
    }

    public Float getFloat2(){
        return m_f2;
    }
    
    public void setFloat1(Float f1){
        m_f1 = f1;
    }
    
    public void setFloat2(Float f2){
        m_f2 = f2;
    }
}
