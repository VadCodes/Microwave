/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauRoutier;

/**
 *
 * @author louis
 */
public class ElementRoutier {
    private Boolean m_estSelectionne = false;
    
    public Boolean estSelectionne(){
        return m_estSelectionne;
    }
    
    public void changerStatutSelection(){
        m_estSelectionne = !m_estSelectionne;
    }
}
