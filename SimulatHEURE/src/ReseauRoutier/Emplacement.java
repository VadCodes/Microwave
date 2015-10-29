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
}
