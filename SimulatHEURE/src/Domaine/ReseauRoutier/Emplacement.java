/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

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
                throw new Error("Mauvais troncon ou intersection");
            }
            m_troncon = troncon;
            m_pourcentageParcouru = pourcentageParcouru;
        }
        m_estSurTroncon = estSurTroncon;
        
        m_intersection = intersection;
    }
    public Point2D.Float calculPosition(){
        if(m_estSurTroncon){
            float p1x = m_troncon.getIntersectionOrigin().getPosition().x;
            float p1y = m_troncon.getIntersectionOrigin().getPosition().y;
            float p2x = m_troncon.getDestination().getPosition().x;
            float p2y = m_troncon.getDestination().getPosition().y;
            
            float n = 3.5f;
            if(m_troncon.getDoubleSens()){
                if(p2y-p1y>0){
                    p1x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y)));
                    p2x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y)));
                    p1y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y)));
                    p2y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y)));
                }
                else{
                    p1x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y)));
                    p2x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y)));   
                    p1y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y)));
                    p2y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y)));
                }

            }
            
            float X = p1x +(p2x - p1x)*getPourcentageParcouru();
            float Y = p1y +(p2y - p1y)*getPourcentageParcouru();
            return new Point2D.Float(X, Y);
        }
        else{
            return m_intersection.getPosition();
        }
    }
    public Boolean getEstSurTroncon(){
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
        return (m_estSurTroncon.equals(autreEmpl.m_estSurTroncon) &&
                (Math.abs(m_pourcentageParcouru-autreEmpl.m_pourcentageParcouru) <= 0.000001) &&
                m_troncon.equals(autreEmpl.m_troncon) &&
                m_intersection.equals(autreEmpl.m_intersection) &&
                this.calculPosition().equals(autreEmpl.calculPosition()));
    }
}
