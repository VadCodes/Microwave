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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
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


