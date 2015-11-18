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
        }
        else
        {
            dessinerArrets(p_g, 1);
        }
    }
    
    private void dessinerCircuit(Graphics2D p_g, float p_echelle){
        p_g.setColor(Color.PINK);
        p_g.setStroke(new BasicStroke(Troncon.LARGEUR / p_echelle));
        for (ListIterator<Circuit> circuits = m_reseau.getListeCircuits().listIterator() ; circuits.hasNext() ; ){
            Circuit circuit = circuits.next();
            Arret arretDebut  =  circuit.getListeArretTrajet().getFirst().getArret();
            Troncon tronconFin = circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-2).getTrajet().getListeTroncon().getLast();//p-e -2 ou pas
            Arret arretFin = circuit.getListeArretTrajet().get(circuit.getListeArretTrajet().size()-2).getArret();
            Point2D.Float pd = arretDebut.getEmplacement().calculPosition();
            Point2D.Float pf = arretFin.getEmplacement().calculPosition();
            Path2D.Float ligne= new Path2D.Float();  
            ligne.moveTo(pd.x, pd.y);
            for (ListIterator<PaireArretTrajet> paires =circuit.getListeArretTrajet().listIterator(); paires.hasNext() ; ){
                PaireArretTrajet paire = paires.next();
                for (ListIterator<Troncon> troncons =paire.getTrajet().getListeTroncon().listIterator(); troncons.hasNext() ; ){
                    Troncon troncon = troncons.next();
                  
                    if(!circuit.estSelectionne()){
                         p_g.setColor(Color.MAGENTA);
                    }
                    else{
                        p_g.setColor(Color.BLUE);
                    }
                    if(troncon == tronconFin){
                        ligne.lineTo(pf.x, pf.y);
                    }
                    else{
                        Point2D.Float p = troncon.getDestination().getPosition();
                        ligne.lineTo(p.x,p.y);
                    }
                }
            }
            p_g.fill(ligne);   
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
                
                Point2D.Float position = em.calculPosition();
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
                
                Point2D.Float position = em.calculPosition();
                System.out.println("Pourcentage");
                System.out.println(em.getPourcentageParcouru());
                float x = position.x -   arret.RAYON / p_echelle;
                float y = position.y -   arret.RAYON / p_echelle;
                float diametre = 2 *   arret.RAYON / p_echelle;

                p_g.fill(new Ellipse2D.Float(x, y, diametre, diametre));
            }
        }
    }


