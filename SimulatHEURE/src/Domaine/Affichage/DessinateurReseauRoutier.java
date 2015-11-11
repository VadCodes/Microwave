package Domaine.Affichage;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.util.LinkedList;

import Domaine.ReseauRoutier.*;

/**
 *
 * @author Vinny
 */
public class DessinateurReseauRoutier
{
    private final Dimension m_dimensionInit;
    
    private final ReseauRoutier m_reseau;
    private final float m_rayonIntersection = 10;
    private final float m_largeurTroncon = 5;

    public DessinateurReseauRoutier(ReseauRoutier p_reseau, Dimension p_dimensionInit)
    {
        this.m_reseau = p_reseau;
        this.m_dimensionInit = p_dimensionInit;
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
        p_g.setColor(Color.CYAN);

        LinkedList<Intersection> intersections = m_reseau.getIntersections();
        for (Intersection intersection: intersections)
        {
            Point2D.Float position = intersection.getPosition();
            float x = (float)position.getX() - m_rayonIntersection / p_echelle;
            float y = (float)position.getY() - m_rayonIntersection / p_echelle;
            float diametre = 2 * m_rayonIntersection / p_echelle;
            p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
        }
    }

    private void dessinerTroncons(Graphics2D p_g, float p_echelle)
    {
        p_g.setColor(Color.PINK);
        p_g.setStroke(new BasicStroke(m_largeurTroncon / p_echelle));

        LinkedList<Intersection> intersections = m_reseau.getIntersections();
        for (Intersection intersection: intersections)
        {
            for (Troncon troncon: intersection.getListeTroncons())
            {
                Point2D.Float p1 = intersection.getPosition();
                Point2D.Float p2 = troncon.getDestination().getPosition();

                p_g.draw(new Line2D.Float(p1, p2));
            }
        }
    }
}
