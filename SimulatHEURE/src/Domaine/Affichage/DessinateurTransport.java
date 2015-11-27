/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.Utilitaire.PaireFloats;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Troncon;
import Domaine.ReseauTransport.*;

//import java.awt.Dimension;

import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author Nathaniel
 */
public class DessinateurTransport {
    
    //private final Dimension m_dimensionInitiale;
    
    private final ReseauTransport m_reseau;
    
    public DessinateurTransport(ReseauTransport p_reseau){//, Dimension p_dimensionInitiale){
        m_reseau = p_reseau;
        //m_dimensionInitiale = p_dimensionInitiale;
    }
    
//    public void dessiner(Graphics2D p_g)
//    {
//        float echelle = (float)p_g.getTransform().getScaleX();
//        if (echelle > 1){
//            dessinerCircuit(p_g, echelle);
//            dessinerArrets(p_g, echelle);
//            dessinerSourceAutobus(p_g, echelle);
//        }
//        else
//        {
//            dessinerCircuit(p_g, 1);
//            dessinerArrets(p_g, 1);
//            dessinerSourceAutobus(p_g, 1);
//        }
//    }
    
    public void dessinerArrets(Graphics2D p_g, float p_echelle)
    {
        LinkedList<Arret> arrets = m_reseau.getListeArrets();
        for (Arret arret :arrets){
            if (!arret.estSelectionne())
                p_g.setColor(Color.GREEN);
            else 
                p_g.setColor(Color.BLUE);

            Emplacement em = arret.getEmplacement();

            Point2D.Float position = em.calculPosition(p_echelle);
            float x = position.x -   arret.RAYON / p_echelle;
            float y = position.y -   arret.RAYON / p_echelle;
            float diametre = 2 *   arret.RAYON / p_echelle;
            p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
        }
    }
    
    public void dessinerCircuit(Graphics2D p_g, float p_echelle)
    {
        p_g.setStroke(new BasicStroke(Troncon.LARGEUR / p_echelle, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10 / p_echelle}, 0));
        
        for (Circuit circuit: m_reseau.getListeCircuits())
        {
            if(circuit.estSelectionne())
            {
                p_g.setColor(Color.BLUE);
            }
            else
            {
                p_g.setColor(Color.RED);
            }
            
            Path2D.Float chemin = new Path2D.Float();
            for (ListIterator<PaireArretTrajet> itPaire = circuit.getListeArretTrajet().listIterator() ; itPaire.hasNext() ; )
            {
                PaireArretTrajet paire = itPaire.next();
                if (paire.getArret().getEmplacement().estSurTroncon())
                {
                    Point2D.Float positionArret = paire.getArret().getEmplacement().calculPosition(p_echelle);
                    chemin.moveTo(positionArret.x, positionArret.y);
                }
                
                if (paire.getTrajet() != null)
                {
                    ListIterator<Troncon> itTroncon = paire.getTrajet().getListeTroncons().listIterator();
                    while (itTroncon.hasNext())
                    {
                        Troncon troncon = itTroncon.next();
                        Point2D.Float p1 = troncon.getOrigine().getPosition();
                        Point2D.Float p2 = troncon.getDestination().getPosition();
                        
                        PaireFloats pAj = troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
                        Float ajX = pAj.getFloat1();
                        Float ajY = pAj.getFloat2();
                        
                        if (itTroncon.previousIndex() != 0 || !paire.getArret().getEmplacement().estSurTroncon())
                            chemin.moveTo(p1.x + ajX, p1.y + ajY);
                        
                        if (itTroncon.hasNext())
                            chemin.lineTo(p2.x + ajX, p2.y + ajY);

                        else if (itPaire.hasNext())
                        {
                            Emplacement emplacementProchainArret = itPaire.next().getArret().getEmplacement();
                            Point2D.Float positionProchainArret = emplacementProchainArret.calculPosition(p_echelle);
                            if (emplacementProchainArret.estSurTroncon())
                            {
                                chemin.lineTo(positionProchainArret.x, positionProchainArret.y);
                            }
                            else
                            {
                                chemin.lineTo(positionProchainArret.x + ajX, positionProchainArret.y + ajY);
                            }
                            itPaire.previous();
                        }
                    }
                }
            }
            p_g.draw(chemin); 
        }

    }
      public void dessinerSourceAutobus(Graphics2D p_g, float p_echelle)
    {
          for (ListIterator<Circuit> circuits = m_reseau.getListeCircuits().listIterator() ; circuits.hasNext() ; ){
            Circuit circuit = circuits.next();
                for (ListIterator<SourceAutobus> sources =circuit.getListeSourceAutobus().listIterator(); sources.hasNext() ; ){
                    SourceAutobus source  = sources.next();
                    if (!source.estSelectionne())
                        p_g.setColor(Color.MAGENTA);
                    else 
                        p_g.setColor(Color.BLUE);
                    
                Path2D.Float losange = new Path2D.Float();  

                Emplacement em = source.getEmplacement();                
                Point2D.Float position = em.calculPosition(p_echelle);
 
                float y1 = position.y - SourceAutobus.LARGEUR / 2 / p_echelle;
                float x1 = position.x + SourceAutobus.LARGEUR / 2 / p_echelle;
                float y2 = y1 + SourceAutobus.LARGEUR / p_echelle;
                float x2 = x1 - SourceAutobus.LARGEUR / p_echelle;
                
                losange.moveTo(position.x, y1);
                losange.lineTo(x1, position.y);
                losange.lineTo(position.x, y2);
                losange.lineTo(x2, position.y);
                losange.closePath();
                
                p_g.fill(losange);
            }
        }
    }
    }


