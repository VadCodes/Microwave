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
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.font.GlyphVector;
import java.io.File;
import java.io.IOException;

import java.util.LinkedList;
import javax.imageio.ImageIO;
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
        
        try {
            Image image = ImageIO.read(new File("src/bus.gif"));
            
            int hauteur = image.getHeight(null);
            int largeur = image.getWidth(null);

            for (Circuit circuit : m_reseau.getListeCircuits()) {

                GlyphVector noCircuit = circuit.getRepresentationNom();

                for (Autobus autobus : circuit.getListeAutobus()) {
                    p_g.setColor(Color.BLACK);
                    Emplacement em = autobus.getEmplacement();
                    Point2D.Float position = em.calculPosition(p_echelle);
                    
                    int xImage = (int)(position.x * p_echelle - largeur / 2);
                    int yImage = (int)(position.y * p_echelle - hauteur / 2);
                    
                    int xNoCorcuit = (int)(position.x * p_echelle + 6 - largeur / 2);
                    int yNoCircuit = (int)(position.y * p_echelle + 7 - hauteur / 2);
                    

                    
                    p_g.scale(1 / p_echelle, 1 / p_echelle);
                    p_g.drawImage(image, xImage, yImage, null);
                    p_g.drawGlyphVector(noCircuit, xNoCorcuit, yNoCircuit);
                    
                    if (autobus.getnbPassager() > 0)
                    {
                        String nbPassagers = Integer.toString(autobus.getnbPassager());                    
                        int ajX = (int)p_g.getFontMetrics().getStringBounds(nbPassagers, p_g).getWidth();
                        p_g.drawString(nbPassagers, xImage + 36 - ajX, yImage + 26);
                    }
                    
                    p_g.scale(p_echelle, p_echelle);
                }
            }
        }
        catch(IOException e) {
            System.out.println("nope");
        }
    }
    
    public void dessinerPietons(Graphics2D p_g, float p_echelle)
    {
        LinkedList<Individu> individus = m_reseauBesoins.getListIndividus();
        for (Individu individu :individus){
            if(!individu.estEnBus() && !individu.estSurArret()){//&& !individu.estSurArret()){
                Emplacement em = individu.getEmplacementActuel();
                Point2D.Float position = em.calculPosition(p_echelle);
                float x = position.x -   individu.RAYON / p_echelle;
                float y = position.y -   individu.RAYON / p_echelle;
                float diametre = 2 *   individu.RAYON / p_echelle;
                
                p_g.setColor(Color.CYAN);
                dessinerPietons(p_g,position,p_echelle);
                //p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
            }
            
        }
    }
    
    public void dessinerPietonsEnAttenteEmbarquer(Graphics2D p_g, float p_echelle)
    {
        for (Arret arret : m_reseau.getListeArrets())
        {
            
           if (arret.getPietonsEnAttenteEmbarquer() > 0)
            {
                Point2D.Float position = arret.getEmplacement().calculPosition(p_echelle);
                p_g.setColor(Color.LIGHT_GRAY);
                double ajX = p_g.getFontMetrics().getStringBounds(Integer.toString(arret.getPietonsEnAttenteEmbarquer()), p_g).getCenterX();
                double ajY = p_g.getFontMetrics().getStringBounds(Integer.toString(arret.getPietonsEnAttenteEmbarquer()), p_g).getCenterY();
                p_g.scale(1 / p_echelle, 1 / p_echelle);
                p_g.drawString(Integer.toString(arret.getPietonsEnAttenteEmbarquer()), (int)(position.x * p_echelle - ajX), (int)(position.y * p_echelle - ajY));
                p_g.scale(p_echelle, p_echelle);
            }
        }
    }
    
    private void dessinerPietons(Graphics2D p_g,Point2D.Float p_position, float p_echelle){
        try{
                Image image = ImageIO.read(new File("src/pieton.gif"));
                
                float hauteur = 33;
                float largeur = 23;
                float x = p_position.x - largeur / p_echelle;
                float y = p_position.y - hauteur / p_echelle;
                float largeurFinal = largeur / p_echelle;
                float hauteurFinal = hauteur / p_echelle;
                int xF = (int)((x + largeurFinal / 2) * p_echelle);
                int yF = (int)((y + hauteurFinal / 2) * p_echelle);
                //Image im =  image.getScaledInstance((int)(largeur),(int)hauteur, Image.SCALE_DEFAULT);
                p_g.scale(1 / p_echelle, 1 / p_echelle);
                p_g.drawImage(image, xF,yF, null);
                p_g.scale(p_echelle, p_echelle);
              
                
        }
        catch(IOException ex){
                    System.out.println("nop");
        }
    }
}
