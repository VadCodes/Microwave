/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

/**
 *
 * @author Nathaniel
 */
public class Emplacement {
    private Boolean m_estSurTroncon;
    private double m_pourcentageParcouru;
    private Troncon m_troncon;
    private Intersection m_intersection;
    public Emplacement(Boolean estSurTroncon, double pourcentageParcouru, Troncon troncon, Intersection intersection){
        m_estSurTroncon = estSurTroncon;
        m_pourcentageParcouru = pourcentageParcouru;
        m_troncon = troncon;
        m_intersection = intersection;
    }
    public Position calculPosition(){
        double positionDepartX = m_troncon.getIntersectionDistination().getPosition().getPositionX();
        double positionDepartY = m_troncon.getIntersectionDistination().getPosition().getPositionY();
        double positionFinX = m_troncon.getIntersectionDistination().getPosition().getPositionX();
        double positionFinY = m_troncon.getIntersectionDistination().getPosition().getPositionY();
        double X = positionDepartX + Math.abs(positionFinX - positionDepartX )*m_pourcentageParcouru;
        double Y = positionDepartX + Math.abs(positionFinY - positionDepartY )*m_pourcentageParcouru;
        return new Position(X, Y);
    }
    public Boolean getEstSurTroncon(){
        return m_estSurTroncon;
    }
    public  double getPourcentageParcouru(){
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
    public   void getPourcentageParcouru(double pourcentageParcouru){
        m_pourcentageParcouru = pourcentageParcouru;
    }
    public  void  getTroncon(Troncon troncon){
        m_troncon = troncon;
    }
    public  void  getIntersection(Intersection intersection){
        m_intersection = intersection;
    }
}
