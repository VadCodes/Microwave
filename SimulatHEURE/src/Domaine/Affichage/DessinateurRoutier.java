package Domaine.Affichage;

import Domaine.Utilitaire.PaireFloats;
import Domaine.ReseauRoutier.*;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.BasicStroke;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

import java.awt.geom.Path2D;

import java.util.LinkedList;

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
                p_g.setColor(Color.BLACK);
            }
            else 
            {
                p_g.setColor(Color.BLUE);
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
        p_g.setColor(Color.LIGHT_GRAY);
        p_g.setStroke(new BasicStroke(Troncon.LARGEUR / p_echelle));

        LinkedList<Intersection> intersections = m_reseau.getIntersections();
        for (Intersection intersection: intersections)
        {
            Point2D.Float p1 = intersection.getPosition();
            
            for (Troncon troncon: intersection.getTroncons())
            {   
                if (troncon.estSuggere())
                    p_g.setColor(Color.ORANGE);          
                else if (troncon.estSelectionne())
                    p_g.setColor(Color.BLUE);
                else 
                    p_g.setColor(Color.LIGHT_GRAY);
                    
                Point2D.Float p2 = troncon.getDestination().getPosition();

                Path2D.Float fleche = new Path2D.Float();  
                
                PaireFloats pAj = troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
                Float ajX = pAj.getFloat1();
                Float ajY = pAj.getFloat2();
                
                Float p1x = p1.x + ajX;
                Float p1y = p1.y + ajY;
                Float p2x = p2.x + ajX;
                Float p2y = p2.y + ajY;
                
                fleche.moveTo(p1x, p1y);
                fleche.lineTo(p2x, p2y);
                p_g.draw(fleche);

                float d = (float)p2.distance(p1);
                float dx = p2.x - p1x;
                float dy = p2y - p1y;

                if(troncon.estDoubleSens()){
                    fleche.moveTo(p1x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE * dx / d) / p_echelle, 
                            p1y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE * dy / d) / p_echelle);
                    fleche.lineTo(p1x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE / 2 * -dy / d) / p_echelle, 
                            p1y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE / 2 * dx / d) / p_echelle);
                    fleche.lineTo(p1x + 0.5 * dx, p1y + 0.5 * dy);                    
                }
                else{
                    fleche.moveTo(p1x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE * dx / d) / p_echelle, 
                            p1y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE * dy / d) / p_echelle);
                    fleche.lineTo(p1x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE / 2 * -dy / d) / p_echelle, 
                            p1y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE / 2 * dx / d) / p_echelle);
                    fleche.lineTo(p1x + 0.5 * dx + ((Troncon.GROSSEUR_FLECHE / 2) * (dy / d)) / p_echelle, 
                            p1y + 0.5 * dy + ((Troncon.GROSSEUR_FLECHE / 2) * (-dx / d)) / p_echelle);
                }
                fleche.closePath();
                p_g.fill(fleche);              
            }
        }
    }
}
