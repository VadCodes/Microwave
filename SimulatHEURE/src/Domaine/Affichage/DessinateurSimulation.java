/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauTransport.Arret;
import Domaine.ReseauTransport.Autobus;
import Domaine.ReseauTransport.Circuit;
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
public class DessinateurSimulation {
    private ReseauTransport m_reseau;
    private Dimension m_dimensionInitiale;
    public DessinateurSimulation(ReseauTransport p_reseau, Dimension p_dimensionInitiale){
        m_reseau = p_reseau;
        m_dimensionInitiale = p_dimensionInitiale;
    }
    public void dessiner(Graphics2D p_g)
    {
        float echelle = (float)p_g.getTransform().getScaleX();
        if (echelle > 1){
            dessinerAutobus(p_g, echelle);
        }
        else
        {
            dessinerAutobus(p_g, 1);
        }
    }
    
    private void dessinerAutobus(Graphics2D p_g, float p_echelle)
    {
         LinkedList<Circuit> circuits = m_reseau.getListeCircuits();
        for (Circuit circuit :circuits){
            LinkedList<Autobus> autobuss = circuit.getListeAutobus();
            for (Autobus autobus :autobuss){
                 p_g.setColor(Color.PINK);
                Emplacement em = autobus.getEmplacement();
                Point2D.Float position = em.calculPosition(p_echelle);
                System.out.println("Pourcentage");
                System.out.println(em.getPourcentageParcouru());
                float x = position.x -   5 / p_echelle;
                float y = position.y -   5 / p_echelle;
                float diametre = 2 *   5 / p_echelle;
                p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
            }
        }
        }
}




  