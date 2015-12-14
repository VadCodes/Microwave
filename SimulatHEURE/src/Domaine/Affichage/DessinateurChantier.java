package Domaine.Affichage;

import Domaine.Simulatheure;
import Domaine.ReseauRoutier.Troncon;

import java.awt.Color;
//import java.awt.Dimension;

import java.awt.BasicStroke;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import java.awt.geom.Path2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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

//    public void dessiner(Graphics2D p_g, float p_p2x, float p_p2y)
//    {
//        float echelle = (float)p_g.getTransform().getScaleX();
//        if (echelle > 1)
//            dessinerTronconEnConstruction(p_g, echelle, p_p2x, p_p2y);
//        
//        else
//            dessinerTronconEnConstruction(p_g, 1, p_p2x, p_p2y);
//    }

    public void dessinerTronconEnConstruction(Graphics2D p_g, float p_echelle, float p_p2x, float p_p2y)
    {
        p_g.setColor(Color.ORANGE);
        p_g.setStroke(new BasicStroke((Troncon.LARGEUR / 2) / p_echelle));
        Point2D.Float p1 = this.m_controleur.getParametresTroncon().getFirst().getPosition();
        Point2D.Float p2 = new Point2D.Float(p_p2x, p_p2y);

        Path2D.Float fleche = new Path2D.Float();
        
        fleche.moveTo(p1.x, p1.y);
        fleche.lineTo(p2.x, p2.y);
        p_g.draw(fleche);           
    }
    
    public void dessinerGabarit(Graphics2D p_g, float p_echelle)
    {
        try
        {
            if (this.m_controleur.getGabarit() == null)
                this.m_controleur.setGabarit(ImageIO.read(new File(this.m_controleur.getCheminGabarit())));
            
            p_g.drawImage(this.m_controleur.getGabarit(), 0, 0, null);
        }
        catch(IOException e)
        {
            this.m_controleur.setCheminGabarit("");
            System.out.println("nope");
        }
    }
}
