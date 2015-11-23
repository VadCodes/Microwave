/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauTransport.*;

//import java.awt.Dimension;
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Point2D;
import java.awt.Font;

import java.util.LinkedList;

//import java.awt.Image;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
/**
 *
 * @author Nathaniel
 */
public class DessinateurSimulation {
    //private Dimension m_dimensionInitiale;

    private ReseauTransport m_reseau;

    public DessinateurSimulation(ReseauTransport p_reseau) {//, Dimension p_dimensionInitiale){
        m_reseau = p_reseau;
        //m_dimensionInitiale = p_dimensionInitiale;
    }

    public void dessiner(Graphics2D p_g) {
        float echelle = (float) p_g.getTransform().getScaleX();
        if (echelle > 1) {
            dessinerAutobus(p_g, echelle);
        } else {
            dessinerAutobus(p_g, 1);
        }
    }

    private void dessinerAutobus(Graphics2D p_g, float p_echelle) {
        LinkedList<Circuit> circuits = m_reseau.getListeCircuits();
        for (Circuit circuit : circuits) {
            String noCorcuit = circuit.getNom();
            LinkedList<Autobus> listeAutobus = circuit.getListeAutobus();
            for (Autobus autobus : listeAutobus) {
                p_g.setColor(Color.ORANGE);
                Emplacement em = autobus.getEmplacement();
                Point2D.Float position = em.calculPosition(p_echelle);
                
                float x = position.x - Autobus.LARGEUR / p_echelle;
                float y = position.y - Autobus.LARGEUR / p_echelle;
                float largeur = 2 * Autobus.LARGEUR / p_echelle;
                
//                int int1 = (int) x;
//                int int2 = (int) y;
//                dessinerBus(p_g, int1, int2, p_echelle);
                p_g.fill(new RoundRectangle2D.Float(x, y, largeur, largeur, largeur / 2, largeur / 2));
                p_g.setColor(Color.BLACK);
                p_g.setFont(new Font("Monospaced", Font.PLAIN, (int)(30 / Math.pow(p_echelle, 0.60) )));
                p_g.drawString(noCorcuit, x, y + largeur);
            }
        }
    }
//    private void dessinerBus(Graphics2D p_g, int p_x, int p_y, float p_echelle){
//        try{
//                Image image = ImageIO.read(new File("src/bus.gif"));
//                float hauteurDefault = 30/p_echelle;
//                float x = p_x -hauteurDefault;
//                float y = p_y -hauteurDefault/2;
//                Graphics2D ii =(Graphics2D )image.getGraphics();
//               Image im =  image.getScaledInstance((int)(2*hauteurDefault),(int)hauteurDefault, Image.SCALE_SMOOTH);
//                p_g.drawImage(im, (int)x,(int) y, null);
//                
//        }
//        catch(IOException ex){
//                    System.out.println("nop");
//        }
//    }
}
