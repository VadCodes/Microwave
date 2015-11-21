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
            dessinerArrets(p_g, echelle);
            dessinerCircuit(p_g, echelle);
            dessinerSourceAutobus(p_g, echelle);
        }
        else
        {
            dessinerArrets(p_g, 1);
            dessinerCircuit(p_g, 1);
            dessinerSourceAutobus(p_g, 1);
        }
    }
    
    private void dessinerCircuit(Graphics2D p_g, float p_echelle){
        Stroke dashed = new BasicStroke(Troncon.LARGEUR / p_echelle, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        p_g.setStroke(dashed);
        for (ListIterator<Circuit> circuits = m_reseau.getListeCircuits().listIterator() ; circuits.hasNext() ; ){
            Circuit circuit = circuits.next();
            if(circuit.estSelectionne()){
                p_g.setColor(Color.BLUE);
            }
            else{
                p_g.setColor(Color.RED);
            }
            Arret arretDebut  =  circuit.getListeArretTrajet().getFirst().getArret();
            Troncon tronconFin = circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-2).getTrajet().getListeTroncon().getLast();//p-e -2 ou pas
            Arret arretFin = circuit.getListeArretTrajet().getLast().getArret();
            Point2D.Float pd = arretDebut.getEmplacement().calculPosition(p_echelle);
            Point2D.Float pf = arretFin.getEmplacement().calculPosition(p_echelle);
            Path2D.Float ligne= new Path2D.Float();  
            ligne.moveTo(pd.x, pd.y);
            for (PaireArretTrajet paire : circuit.getListeArretTrajet()){
                if (paire.getTrajet()!=null){
                    Boolean premier = true;
                    LinkedList<Troncon> listeTroncons = paire.getTrajet().getListeTroncon();
                    for(Troncon troncon : listeTroncons){
                        if(!premier && troncon == tronconFin){
                            ligne.lineTo(pf.x, pf.y);
                        }
                        else if (premier && troncon == tronconFin && listeTroncons.size()==1){
                            ligne.lineTo(pf.x, pf.y);
                        }
                        else{
                            // Des fois le troncon p-e null si c'est le dernier de tout le réseau
                            Point2D.Float p = troncon.getDestination().getPosition();
                            float p1x = p.x;
                            float p1y = p.y;
                            if(troncon.getDoubleSens()){
                                Point2D.Float p2 = troncon.getIntersectionOrigin().getPosition();
                                
                                float p2x = p2.x;
                                float p2y = p2.y;

                                float n = 3.5f;
                                if(p2y-p1y>0){
                                    p1x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                                    p1y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                                    p2x += n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                                    p2y -= n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                                }
                                else{
                                    p1x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                                    p1y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                                    p2x -= n*Math.cos(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                                    p2y += n*Math.sin(Math.atan((p2x-p1x)/(p2y-p1y))) / p_echelle;
                                }
                                ligne.lineTo(p2x, p2y);
                            }
                            ligne.lineTo(p1x,p1y);
                        }
                        premier = false;
                    }
                }
            }
            p_g.draw(ligne); 
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
                float x = position.x -   source.LONGUEUR / p_echelle;
                float y = position.y -   source.LARGUEUR/ p_echelle;
                p_g.fill(new Rectangle2D.Float(x,y, source.LONGUEUR,source.LARGUEUR ));
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
                
                if(em.getEstSurTroncon()){
                    Troncon troncon = em.getTroncon();

                    float p1x = troncon.getIntersectionOrigin().getPosition().x;
                    float p1y = troncon.getIntersectionOrigin().getPosition().y;
                    float p2x = troncon.getDestination().getPosition().x;
                    float p2y = troncon.getDestination().getPosition().y;
                    
                    float n = 3.5f; //aww yeah c'est hardcodé à souhait
                    if (troncon.getDoubleSens()){
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


