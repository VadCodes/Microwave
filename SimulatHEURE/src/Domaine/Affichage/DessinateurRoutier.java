package Domaine.Affichage;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

import java.awt.geom.Path2D;

import java.util.LinkedList;
import Domaine.ReseauRoutier.*;

/**
 *
 * @author Vinny
 */
public class DessinateurRoutier
{
    private final Dimension m_dimensionInitiale;
    
    private final ReseauRoutier m_reseau;

    public DessinateurRoutier(ReseauRoutier p_reseau, Dimension p_dimensionInitiale)
    {
        this.m_reseau = p_reseau;
        this.m_dimensionInitiale = p_dimensionInitiale;
    }

    public void dessiner(Graphics2D p_g)
    {
        float echelle = (float)p_g.getTransform().getScaleX();
        if (echelle > 1){
            dessinerTroncons(p_g, echelle);
            dessinerIntersections(p_g, echelle);
        }
        else
        {
            dessinerTroncons(p_g, 1);
            dessinerIntersections(p_g, 1);
        }
    }

    private void dessinerIntersections(Graphics2D p_g, float p_echelle)
    {
        LinkedList<Intersection> intersections = m_reseau.getIntersections();
        for (Intersection intersection: intersections)
        {
            if (!intersection.estSelectionee())
            {
                p_g.setColor(Color.CYAN);
            }
            else 
            {
                p_g.setColor(Color.RED);
            }
            
            Point2D.Float position = intersection.getPosition();
            float x = position.x - Intersection.RAYON / p_echelle;
            float y = position.y - Intersection.RAYON / p_echelle;
            float diametre = 2 * Intersection.RAYON / p_echelle;
            
            p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
        }
    }

    private void dessinerTroncons(Graphics2D p_g, float p_echelle)
    {
        p_g.setColor(Color.PINK);
        p_g.setStroke(new BasicStroke(Troncon.LARGEUR / p_echelle));

        LinkedList<Intersection> intersections = m_reseau.getIntersections();
        for (Intersection intersection: intersections)
        {
            Point2D.Float p1 = intersection.getPosition();
            
            for (Troncon troncon: intersection.getTroncons())
            {   
                if (!troncon.estSelectione())
                {
                    p_g.setColor(Color.PINK);
                }
                else 
                {
                    p_g.setColor(Color.BLUE);
                }
                
                Point2D.Float p2 = troncon.getDestination().getPosition();

                Path2D.Float fleche = new Path2D.Float();                
                fleche.moveTo(p1.x, p1.y);
                fleche.lineTo(p2.x, p2.y);
                p_g.draw(fleche);
                
                float d = (float)p2.distance(p1);
                float dx = p2.x - p1.x;
                float dy = p2.y - p1.y;
                
                fleche.moveTo(p1.x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE * dx / d) / p_echelle, 
                        p1.y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE * dy / d) / p_echelle);
                fleche.lineTo(p1.x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE / 2 * -dy / d) / p_echelle, 
                        p1.y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE / 2 * dx / d) / p_echelle);
                fleche.lineTo(p1.x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE / 2 * dy / d) / p_echelle, 
                        p1.y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE / 2 * -dx / d) / p_echelle);
                fleche.closePath();
                p_g.fill(fleche);
            }
        }
    }
}