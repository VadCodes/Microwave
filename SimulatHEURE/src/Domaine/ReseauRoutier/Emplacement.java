/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

import Domaine.Utilitaire.PaireFloats;
import java.awt.geom.Point2D;

/**
 *
 * @author Nathaniel
 */
public class Emplacement {
    private Boolean m_estSurTroncon;
    private float m_pourcentageParcouru;
    private Troncon m_troncon;
    private Intersection m_intersection;
    
    public Emplacement(Boolean estSurTroncon, float pourcentageParcouru, Troncon troncon, Intersection intersection){
 
        if(estSurTroncon){
            if(!intersection.getTroncons().contains(troncon)){
               // throw new Error("Mauvais troncon ou intersection");
            }
            m_troncon = troncon;
            m_pourcentageParcouru = pourcentageParcouru;
        }
        m_estSurTroncon = estSurTroncon;
        
        m_intersection = intersection;
    }
    public void copy(Emplacement p_emplacement){
        m_estSurTroncon = p_emplacement.estSurTroncon();
        m_pourcentageParcouru = p_emplacement.getPourcentageParcouru();
        m_troncon = p_emplacement.getTroncon();
        m_intersection = p_emplacement.getIntersection();
    }
    public Point2D.Float calculPosition(Float p_echelle){
        if(m_estSurTroncon){
            Point2D.Float p1 = m_troncon.getOrigine().getPosition();
            Point2D.Float p2 = m_troncon.getDestination().getPosition();
            
            PaireFloats pAj = m_troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
            
            float X = p1.x +(p2.x - p1.x)*getPourcentageParcouru() + pAj.getFloat1();
            float Y = p1.y +(p2.y - p1.y)*getPourcentageParcouru() + pAj.getFloat2();
            return new Point2D.Float(X, Y);
        }
        else{
            return m_intersection.getPosition();
        }
    }
    public Boolean estSurTroncon(){
        return m_estSurTroncon;
    }
    public  float getPourcentageParcouru(){
        return m_pourcentageParcouru;
    }
    public  Troncon getTroncon(){
        return m_troncon;
    }
    public  Intersection getIntersection(){
        return m_intersection;
    }
     public  void setEstSurTroncon(Boolean estSurTroncon){
        m_estSurTroncon = estSurTroncon;
    }
    public   void setPourcentageParcouru(float pourcentageParcouru){
        m_pourcentageParcouru = pourcentageParcouru;
    }
    public  void  setTroncon(Troncon troncon){
        m_troncon = troncon;
    }
    public  void  setIntersection(Intersection intersection){
        m_intersection = intersection;
    }
    public Boolean equals(Emplacement autreEmpl){
        if (m_estSurTroncon && autreEmpl.estSurTroncon()){
            return ((Math.abs(m_pourcentageParcouru-autreEmpl.m_pourcentageParcouru) <= 0.000001) &&
                    m_troncon.equals(autreEmpl.m_troncon));
        }
        else if (!m_estSurTroncon && !autreEmpl.estSurTroncon()){
            return (m_intersection == autreEmpl.getIntersection());
        }
        else return false;
    }
}
