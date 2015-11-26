package Domaine.Affichage;

import Domaine.Simulatheure;
import Domaine.ReseauRoutier.Troncon;

import java.awt.Color;
//import java.awt.Dimension;

import java.awt.BasicStroke;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import java.awt.geom.Path2D;

/**
 *
 * @author Vinny
 */
public class DessinateurChantier
{
    //private final Dimension m_dimensionInitiale;
    
    private final Simulatheure m_controleur;

    public DessinateurChantier(Simulatheure p_controleur) //, Dimension p_dimensionInitiale)
    {
        this.m_controleur = p_controleur;
        //this.m_dimensionInitiale = p_dimensionInitiale;
    }

    public void dessiner(Graphics2D p_g, float p_p2x, float p_p2y)
    {
        float echelle = (float)p_g.getTransform().getScaleX();
        if (echelle > 1)
            dessinerTronconEnConstruction(p_g, echelle, p_p2x, p_p2y);
        
        else
            dessinerTronconEnConstruction(p_g, 1, p_p2x, p_p2y);
    }

    private void dessinerTronconEnConstruction(Graphics2D p_g, float p_echelle, float p_p2x, float p_p2y)
    {
        p_g.setColor(Color.ORANGE);
        p_g.setStroke(new BasicStroke((Troncon.LARGEUR / 2) / p_echelle));
        Point2D.Float p1 = this.m_controleur.getParametresTroncon().getFirst().getPosition();
        Point2D.Float p2 = new Point2D.Float(p_p2x, p_p2y);

        Path2D.Float fleche = new Path2D.Float();
        
        fleche.moveTo(p1.x, p1.y);
        fleche.lineTo(p2.x, p2.y);
        p_g.draw(fleche);

//        float d = (float)p2.distance(p1);
//        float dx = p2.x - p1.x;
//        float dy = p2.y - p1.y;
//        
//        float grosseurFleche = Troncon.GROSSEUR_FLECHE / 2;
//
//        fleche.moveTo(p1.x + 0.5 * dx + (grosseurFleche * dx / d) / p_echelle, 
//                p1.y + 0.5 * dy + (grosseurFleche * dy / d) / p_echelle);
//        fleche.lineTo(p1.x + 0.5 * dx + (grosseurFleche / 2 * -dy / d) / p_echelle, 
//                p1.y + 0.5 * dy + (grosseurFleche / 2 * dx / d) / p_echelle);
//        fleche.lineTo(p1.x + 0.5 * dx + ((grosseurFleche / 2) * (dy / d)) / p_echelle, 
//                p1.y + 0.5 * dy + ((grosseurFleche / 2) * (-dx / d)) / p_echelle);
//            
//        fleche.closePath();
//        p_g.fill(fleche);              
    }
}
