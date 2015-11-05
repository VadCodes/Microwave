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
    private Position m_position;
    public Emplacement(Boolean estSurTroncon, double pourcentageParcouru, Troncon troncon, Intersection intersection){
 
        if(!intersection.getListTroncons().contains(troncon)){
            throw new Error("Mauvais troncon ou intersection");
        }
        m_estSurTroncon = estSurTroncon;
        m_pourcentageParcouru = pourcentageParcouru;
        m_troncon = troncon;
        m_intersection = intersection;
    }
    public Position calculPosition(){
        double positionDepartX = m_troncon.getIntersectionDestination().getPosition().getPositionX();
        double positionDepartY = m_troncon.getIntersectionDestination().getPosition().getPositionY();
        double positionFinX = m_troncon.getIntersectionDestination().getPosition().getPositionX();
        double positionFinY = m_troncon.getIntersectionDestination().getPosition().getPositionY();
        double X = positionDepartX + Math.abs(positionFinX - positionDepartX )*m_pourcentageParcouru;
        double Y = positionDepartX + Math.abs(positionFinY - positionDepartY )*m_pourcentageParcouru;
        return new Position(X, Y); // Criss un new;
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
    public   void setPourcentageParcouru(double pourcentageParcouru){
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
                m_position.equals(autreEmpl.m_intersection));
    }
}
