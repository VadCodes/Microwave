package Domaine.Affichage;

import Domaine.Simulatheure;

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
public class DessinateurChantierRoutier
{
    private final Dimension m_dimensionInitiale;
    
    private final Simulatheure m_controleur;

    public DessinateurChantierRoutier(Simulatheure p_controleur, Dimension p_dimensionInitiale)
    {
        this.m_controleur = p_controleur;
        this.m_dimensionInitiale = p_dimensionInitiale;
    }

    public void dessiner(Graphics2D p_g)
    {
        float echelle = (float)p_g.getTransform().getScaleX();
        if (echelle > 1)
        {
            dessinerTronconEnConstruction(p_g, echelle);
        }
        else
        {
            dessinerTronconEnConstruction(p_g, 1);
        }
    }

    private void dessinerTronconEnConstruction(Graphics2D p_g, float p_echelle)
    {
        p_g.setColor(Color.ORANGE);
        p_g.setStroke(new BasicStroke(Troncon.LARGEUR / p_echelle));

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
