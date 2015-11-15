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
            if (!intersection.estSelectionne())
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
                if (!troncon.estSelectionne())
                {
                    p_g.setColor(Color.PINK);
                }
                else 
                {
                    p_g.setColor(Color.BLUE);
                }   
                    
                Point2D.Float p2 = troncon.getDestination().getPosition();

                Path2D.Float fleche = new Path2D.Float();  
                
                float p1x = p1.x;
                float p1y = p1.y;
                float p2x = p2.x;
                float p2y = p2.y;
                float n = 5;
                if (troncon.getDoubleSens()){
                    if(p2y-p1y>0){
                        p1x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y)));
                        p2x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y)));
                    }
                    else{
                        p1x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y)));
                        p2x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y)));   
                    }
                    if(p2x-p1x>0){
                        p1y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y)));
                        p2y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y)));
                    }
                    else{
                        p1y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y)));
                        p2y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y)));
                    }
                }
                
                fleche.moveTo(p1x, p1y);
                fleche.lineTo(p2x, p2y);
                p_g.draw(fleche);

                float d = (float)p2.distance(p1);
                float dx = p2x - p1x;
                float dy = p2y - p1y;

                fleche.moveTo(p1x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE * dx / d) / p_echelle, 
                        p1y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE * dy / d) / p_echelle);
                fleche.lineTo(p1x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE / 2 * -dy / d) / p_echelle, 
                        p1y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE / 2 * dx / d) / p_echelle);
                fleche.lineTo(p1x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE / 2 * dy / d) / p_echelle, 
                        p1y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE / 2 * -dx / d) / p_echelle);
                fleche.closePath();
                p_g.fill(fleche);              
            }
        }
    }
}
