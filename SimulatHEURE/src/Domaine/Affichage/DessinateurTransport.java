/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.ReseauTransport.Arret;
import Domaine.ReseauTransport.Circuit;
import Domaine.ReseauTransport.PaireArretTrajet;
import Domaine.ReseauTransport.ReseauTransport;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 *
 * @author Nathaniel
 */
public class DessinateurTransport {
    
     private final Dimension m_dimensionInitiale;
    
    private final ReseauTransport m_reseau;
    public DessinateurTransport(ReseauTransport p_reseau, Dimension p_dimensionInitiale){
        m_reseau = p_reseau;
        m_dimensionInitiale = p_dimensionInitiale;
    }
      private void dessinerIArrets(Graphics2D p_g, float p_echelle)
    {
        LinkedList<Circuit> circuits = m_reseau.getListeCircuits();
        for (Circuit circuit : circuits){
            LinkedList<PaireArretTrajet> paires = circuit.getListeArretTrajet();
            for (PaireArretTrajet paire:paires)
            {
                if (!paire.getArret().estSelectionne())
                {
                    p_g.setColor(Color.GREEN);
                }
                else 
                {
                    p_g.setColor(Color.ORANGE);
                }

                Point2D.Float position = paire.getArret().getEmplacement().calculPosition();
                float x = position.x -  paire.getArret().RAYON / p_echelle;
                float y = position.y -  paire.getArret().RAYON / p_echelle;
                float diametre = 2 *  paire.getArret().RAYON / p_echelle;

                p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
            }
        }
    }
}
