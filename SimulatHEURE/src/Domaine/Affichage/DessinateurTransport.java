/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Troncon;
import Domaine.ReseauTransport.Arret;
import Domaine.ReseauTransport.Circuit;
import Domaine.ReseauTransport.PaireArretTrajet;
import Domaine.ReseauTransport.ReseauTransport;
import Domaine.ReseauTransport.SourceAutobus;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author Nathaniel
 */
public class DessinateurTransport {
    
     private final Dimension m_dimensionInitiale;
    
    private final ReseauTransport m_reseau;
    public DessinateurTransport(ReseauTransport p_reseau, Dimension p_dimensionInitiale){
        m_reseau = p_reseau;
        m_dimensionInitiale = p_dimensionInitiale;
    }
    
    public void dessiner(Graphics2D p_g)
    {
        float echelle = (float)p_g.getTransform().getScaleX();
        if (echelle > 1){
            dessinerCircuit(p_g, echelle);
            dessinerArrets(p_g, echelle);
            dessinerSourceAutobus(p_g, echelle);
        }
        else
        {
            dessinerCircuit(p_g, 1);
            dessinerArrets(p_g, 1);
            dessinerSourceAutobus(p_g, 1);
        }
    }
    
    private void dessinerCircuit(Graphics2D p_g, float p_echelle)
    {
        Stroke dashed = new BasicStroke(Troncon.LARGEUR / p_echelle, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10 / p_echelle}, 0);
        p_g.setStroke(dashed);
        
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
                        float ajX = 0;
                        float ajY = 0;

                        if (troncon.estDoubleSens())
                        {
                            float d = (float)p2.distance(p1);
                            float dx = p2.x - p1.x;
                            float dy = p2.y - p1.y;

                            float n = 3.5f;
                            ajX = (n * -dy / d) / p_echelle;
                            ajY = (n * dx / d) / p_echelle;
                        }
                        if (itTroncon.previousIndex() != 0 || !paire.getArret().getEmplacement().estSurTroncon())
                            chemin.moveTo(p1.x + ajX, p1.y + ajY);
                        
                        if (itTroncon.hasNext())
                            chemin.lineTo(p2.x + ajX, p2.y + ajY);

                        else
                        {
                            Emplacement emplacementProchainArret = paire.getTrajet().getEmplacementFinal();
                            Point2D.Float positionProchainArret = emplacementProchainArret.calculPosition(p_echelle);
                            if (emplacementProchainArret.estSurTroncon())
                            {
                                chemin.lineTo(positionProchainArret.x, positionProchainArret.y);
                            }
                            else
                            {
                                chemin.lineTo(positionProchainArret.x + ajX, positionProchainArret.y + ajY);
                            }
                        }
                    }
                }
            }
            p_g.draw(chemin); 
        }

    }
      private void dessinerSourceAutobus(Graphics2D p_g, float p_echelle)
    {
          for (ListIterator<Circuit> circuits = m_reseau.getListeCircuits().listIterator() ; circuits.hasNext() ; ){
            Circuit circuit = circuits.next();
                for (ListIterator<SourceAutobus> sources =circuit.getListeSourceAutobus().listIterator(); sources.hasNext() ; ){
                    SourceAutobus source  = sources.next();
                    if (!source.estSelectionne())
                    {
                        p_g.setColor(Color.CYAN);
                    }
                    else 
                    {
                        p_g.setColor(Color.BLUE);
                    }
                Emplacement em = source.getEmplacement();
                
                Point2D.Float position = em.calculPosition(p_echelle);
                float x = position.x - source.LARGUEUR / 2 / p_echelle;
                float y = position.y - source.LARGUEUR / 2 / p_echelle;
                float largeur = source.LARGUEUR / p_echelle;
                p_g.fill(new RoundRectangle2D.Float(x,y, largeur, largeur, largeur / 2, largeur / 2));
            }
        }
    }
      private void dessinerArrets(Graphics2D p_g, float p_echelle)
    {
         LinkedList<Arret> arrets = m_reseau.getListArrets();
        for (Arret arret :arrets){
                if (!arret.estSelectionne())
                {
                    p_g.setColor(Color.GREEN);
                }
                else 
                {
                    p_g.setColor(Color.BLUE);
                }
                Emplacement em = arret.getEmplacement();
                Point2D.Float position = em.calculPosition(p_echelle);
                
                if(em.estSurTroncon()){
                    Troncon troncon = em.getTroncon();

                    float p1x = troncon.getOrigine().getPosition().x;
                    float p1y = troncon.getOrigine().getPosition().y;
                    float p2x = troncon.getDestination().getPosition().x;
                    float p2y = troncon.getDestination().getPosition().y;
                    
                    float n = 3.5f; //aww yeah c'est hardcodé à souhait
                    if (troncon.estDoubleSens()){
                        if(p2y-p1y>0){
                            p1x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                            p2x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                            p1y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                            p2y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        }
                        else{
                            p1x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                            p2x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;   
                            p1y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                            p2y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                        }
                        float X = p1x +(p2x - p1x)*em.getPourcentageParcouru();
                        float Y = p1y +(p2y - p1y)*em.getPourcentageParcouru();
                        position = new Point2D.Float(X, Y);
                    }
                }
                
                float x = position.x -   arret.RAYON / p_echelle;
                float y = position.y -   arret.RAYON / p_echelle;
                float diametre = 2 *   arret.RAYON / p_echelle;

                p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
            }
        }
    }


