/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.BesoinsTransport.Individu;
import Domaine.BesoinsTransport.ReseauBesoins;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauTransport.*;

//import java.awt.Dimension;

import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;

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
    private ReseauBesoins m_reseauBesoins;

    public DessinateurSimulation(ReseauTransport p_reseau, ReseauBesoins p_reseauBesoins) {//, Dimension p_dimensionInitiale){
        m_reseau = p_reseau;
        m_reseauBesoins = p_reseauBesoins;
        //m_dimensionInitiale = p_dimensionInitiale;
    }

//    public void dessiner(Graphics2D p_g) {
//        float echelle = (float) p_g.getTransform().getScaleX();
//        if (echelle > 1)
//            dessinerAutobus(p_g, echelle);
//            
//        else
//            dessinerAutobus(p_g, 1);
//    }

    public void dessinerAutobus(Graphics2D p_g, float p_echelle) {
        LinkedList<Circuit> circuits = m_reseau.getListeCircuits();
        for (Circuit circuit : circuits) {
            GlyphVector noCircuit = circuit.getRepresentationNom();
            LinkedList<Autobus> listeAutobus = circuit.getListeAutobus();
            for (Autobus autobus : listeAutobus) {
                p_g.setColor(Color.DARK_GRAY);
                Emplacement em = autobus.getEmplacement();
                Point2D.Float position = em.calculPosition(p_echelle);
                
                float x = position.x - Autobus.LARGEUR / 2 / p_echelle;
                float y = position.y - Autobus.HAUTEUR / 2 / p_echelle;
                float largeur = Autobus.LARGEUR / p_echelle;
                float hauteur = Autobus.HAUTEUR / p_echelle;
                
//                int int1 = (int) x;
//                int int2 = (int) y;
//                dessinerBus(p_g, int1, int2, p_echelle);
                p_g.fill(new RoundRectangle2D.Float(x, y, largeur, hauteur, hauteur / 2, hauteur / 2));
                p_g.setColor(Color.WHITE);
                p_g.scale(1 / p_echelle, 1 / p_echelle);
                p_g.drawGlyphVector(noCircuit, (x + largeur / 2) * p_echelle, (y + hauteur / 2) * p_echelle);
                p_g.scale(p_echelle, p_echelle);
            }
        }
    }
    public void dessinerPietons(Graphics2D p_g, float p_echelle)
    {
        LinkedList<Individu> individus = m_reseauBesoins.getListIndividus();
        for (Individu individu :individus){
            if(!individu.estEnBus()){
                Emplacement em = individu.getEmplacementActuel();
                Point2D.Float position = em.calculPosition(p_echelle);
                float x = position.x -   individu.RAYON / p_echelle;
                float y = position.y -   individu.RAYON / p_echelle;
                float diametre = 2 *   individu.RAYON / p_echelle;
                p_g.setColor(Color.CYAN);
                p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
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
