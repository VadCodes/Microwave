/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.BesoinsTransport.BesoinTransport;
import Domaine.BesoinsTransport.Itineraire;
import Domaine.BesoinsTransport.PaireParcours;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Troncon;
import Domaine.ReseauTransport.Circuit;
import Domaine.ReseauTransport.PaireArretTrajet;
import Domaine.ReseauTransport.ReseauTransport;
import Domaine.ReseauTransport.SourceAutobus;
import Domaine.Utilitaire.PaireFloats;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ListIterator;

/**
 *
 * @author louis
 */
public class DessinateurBesoins {
    private BesoinTransport m_reseau;
    public DessinateurBesoins(BesoinTransport p_reseau){
        m_reseau = p_reseau;
        //m_dimensionInitiale = p_dimensionInitiale;
    }
    public void dessinerCircuit(Graphics2D p_g, float p_echelle)
    {  
        Graphics2D select_g = (Graphics2D) p_g.create();
        select_g.setColor(new Color(50,200,255 , 200));
        select_g.setStroke(new BasicStroke(Troncon.LARGEUR*1.35f/p_echelle));
        for (Itineraire itineraire: m_reseau.getItineraire()){
            //p_g.setColor(itineraire.getCouleur());
            p_g.setStroke(new BasicStroke(Troncon.LARGEUR / p_echelle, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10 / p_echelle}, 0));
            Path2D.Float chemin = new Path2D.Float();
           
            
            for (ListIterator<PaireParcours> itPaire = itineraire.getListPaireParcours().listIterator() ; itPaire.hasNext() ; )
            {
                PaireParcours paire = itPaire.next();
                ListIterator<Troncon> itTroncon = paire.getParcoursBus().getTroncons().listIterator();
                while (itTroncon.hasNext())
                {
                    Troncon troncon = itTroncon.next();
                    Point2D.Float p1 = troncon.getOrigine().getPosition();
                    Point2D.Float p2 = troncon.getDestination().getPosition();

                    PaireFloats pAj = troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
                    Float ajX = pAj.getFloat1();
                    Float ajY = pAj.getFloat2();

                    if (itTroncon.hasNext())
                        chemin.lineTo(p2.x + ajX, p2.y + ajY);

                }
                
                if (paire.getTrajet().getEmplacementInitial().estSurTroncon())
                {
                    Point2D.Float positionInitial = paire.getTrajet().getEmplacementInitial().calculPosition(p_echelle);
                    chemin.moveTo(positionInitial.x, positionInitial.y);
                }
                
                if (paire.getTrajet() != null)
                {
                    ListIterator<Troncon> itTroncon2 = paire.getTrajet().getListeTroncons().listIterator();
                    while (itTroncon2.hasNext())
                    {
                        Troncon troncon = itTroncon2.next();
                        Point2D.Float p1 = troncon.getOrigine().getPosition();
                        Point2D.Float p2 = troncon.getDestination().getPosition();
                        
                        PaireFloats pAj = troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
                        Float ajX = pAj.getFloat1();
                        Float ajY = pAj.getFloat2();
                        
                        if (itTroncon2.hasNext())
                            chemin.lineTo(p2.x + ajX, p2.y + ajY);

                    }
                }
            }
            if(m_reseau.getPileSelection().contient(itineraire))
                select_g.draw(chemin);
            p_g.draw(chemin); 
        }
    }
}
