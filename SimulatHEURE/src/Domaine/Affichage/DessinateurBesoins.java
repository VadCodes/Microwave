/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.BesoinsTransport.Itineraire;
import Domaine.BesoinsTransport.PaireParcours;
import Domaine.BesoinsTransport.ReseauBesoins;
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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ListIterator;

/**
 *
 * @author louis
 */
public class DessinateurBesoins {
    private ReseauBesoins m_reseau;
    public DessinateurBesoins(ReseauBesoins p_reseau){
        m_reseau = p_reseau;
        //m_dimensionInitiale = p_dimensionInitiale;
    }
    public void dessinerItineraire(Graphics2D p_g, float p_echelle)
    {  
        Graphics2D select_g = (Graphics2D) p_g.create();
        select_g.setColor(new Color(50,200,255 , 200));
        select_g.setStroke(new BasicStroke(Troncon.LARGEUR*1.35f/p_echelle));
        
        Graphics2D src_g = (Graphics2D) p_g.create();
        Graphics2D transit_g = (Graphics2D) p_g.create();
        
        Emplacement emplSourceArtificielle = m_reseau.getEmplacementSourceTemp();
        if(emplSourceArtificielle!=null){
            Point2D.Float posSourceArtificielle = emplSourceArtificielle.calculPosition(p_echelle);
            Path2D.Float losange = new Path2D.Float();
            src_g.setColor(new Color(0,200,0));
            float y1 = posSourceArtificielle.y - SourceAutobus.LARGEUR / 2 / p_echelle;
            float x1 = posSourceArtificielle.x + SourceAutobus.LARGEUR / 2 / p_echelle;
            float y2 = y1 + SourceAutobus.LARGEUR / p_echelle;
            float x2 = x1 - SourceAutobus.LARGEUR / p_echelle;
            losange.moveTo(posSourceArtificielle.x, y1);
            losange.lineTo(x1, posSourceArtificielle.y);
            losange.lineTo(posSourceArtificielle.x, y2);
            losange.lineTo(x2, posSourceArtificielle.y);
            losange.closePath();
            src_g.fill(losange);
        }
        
        
        for (Itineraire itineraire: m_reseau.getListItineraire()){
            p_g.setStroke(new BasicStroke(Troncon.LARGEUR*1.35f / p_echelle));
            p_g.setColor(new Color(0,200,0 , 150));
            Path2D.Float chemin = new Path2D.Float();
           
            Point2D.Float origine;
            if(itineraire.getListPaireParcours().getFirst().getTrajet()!=null){
                origine = itineraire.getListPaireParcours().getFirst().getTrajet().getEmplacementInitial().calculPosition(p_echelle);
                chemin.moveTo(origine.x, origine.y);
            }
            else{
                origine = itineraire.getListPaireParcours().getFirst().getParcoursBus().getArretDepart().getEmplacement().calculPosition(p_echelle);
                chemin.moveTo(origine.x, origine.y);
            }
            
            for (ListIterator<PaireParcours> itPaire = itineraire.getListPaireParcours().listIterator() ; itPaire.hasNext() ; )
            {
                PaireParcours paire = itPaire.next();
                
                if (paire.getTrajet() != null)
                {                    
                    ListIterator<Troncon> itTroncon = paire.getTrajet().getListeTroncons().listIterator();
                    while (itTroncon.hasNext())
                    {
                        Troncon troncon = itTroncon.next();
                        
                        Point2D.Float p1;
                        Point2D.Float p2;
                        Boolean p1Aj = false;
                        Boolean p2Aj = false;
                        if(paire.getTrajet().getEmplacementInitial().estSurTroncon()){
                            if(paire.getTrajet().getEmplacementInitial().getTroncon()==troncon){
                                p1 = paire.getTrajet().getEmplacementInitial().calculPosition(p_echelle);
                            }
                            else{
                                p1 = troncon.getOrigine().getPosition();
                                p1Aj = true;
                            }
                        }
                        else{
                            p1 = troncon.getOrigine().getPosition();
                            p1Aj = true;
                        }
                        if(paire.getTrajet().getEmplacementFinal().estSurTroncon()){
                            if(paire.getTrajet().getEmplacementFinal().getTroncon()==troncon){
                                p2 = paire.getTrajet().getEmplacementFinal().calculPosition(p_echelle);
                            }
                            else{
                                p2 = troncon.getDestination().getPosition();
                                p2Aj = true;
                            }
                        }
                        else{
                            p2 = troncon.getDestination().getPosition();
                            p2Aj = true;
                        }
                        
                        PaireFloats pAj = troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
                        Float ajX = pAj.getFloat1();
                        Float ajY = pAj.getFloat2();
                        
                        if(p1Aj){
                            p1 = new Point2D.Float(p1.x + ajX, p1.y + ajY);
                        }
                        if(p2Aj){
                            p2 = new Point2D.Float(p2.x + ajX, p2.y + ajY);
                        }
                        
                        if (itTroncon.previousIndex() != 0 || !paire.getTrajet().getEmplacementInitial().estSurTroncon())
                            chemin.moveTo(p1.x, p1.y);

                        chemin.lineTo(p2.x, p2.y);
                    }
                }
                              
                if(paire.getParcoursBus()!=null){
                    ListIterator<Troncon> itTroncon = paire.getParcoursBus().getTroncons().listIterator();
                    
                    PaireParcours paireSuivante;
                    if(itPaire.hasNext()){
                        paireSuivante = itPaire.next();
                        itPaire.previous();
                    }
                    else
                        paireSuivante = null;
                    
                    while (itTroncon.hasNext())
                    {
                        Troncon troncon = itTroncon.next();
                        Point2D.Float p1;
                        Point2D.Float p2;
                        Boolean p1Aj = false;
                        Boolean p2Aj = false;
                        if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().estSurTroncon()){
                            if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().getTroncon()==troncon){
                                p1 = paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().calculPosition(p_echelle);
                            }
                            else{
                                p1 = troncon.getOrigine().getPosition();
                                p1Aj = true;
                            }
                        }
                        else{
                            p1 = troncon.getOrigine().getPosition();
                            p1Aj = true;
                        }
                        if(paire.getParcoursBus().getPaireArretDepart().getTrajet().getEmplacementFinal().estSurTroncon()){
                            if(paire.getParcoursBus().getPaireArretDepart().getTrajet().getEmplacementFinal().getTroncon()==troncon){
                                p2 = paire.getParcoursBus().getPaireArretDepart().getTrajet().getEmplacementFinal().calculPosition(p_echelle);
                            }
                            else{
                                p2 = troncon.getDestination().getPosition();
                                p2Aj = true;
                            }
                        }
                        else{
                            p2 = troncon.getDestination().getPosition();
                            p2Aj = true;
                        }
                        
                        PaireFloats pAj = troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
                        Float ajX = pAj.getFloat1();
                        Float ajY = pAj.getFloat2();
                        
                        if(p1Aj){
                            p1 = new Point2D.Float(p1.x + ajX, p1.y + ajY);
                        }
                        if(p2Aj){
                            p2 = new Point2D.Float(p2.x + ajX, p2.y + ajY);
                        }
                        
                        if (itTroncon.previousIndex() != 0 || (paire.getTrajet()!=null && !paire.getTrajet().getEmplacementInitial().estSurTroncon()))
                            chemin.moveTo(p1.x, p1.y);
                        
                        if (paireSuivante!=null && !itTroncon.hasNext())
                        {
                            Emplacement emplacementProchainArret;
                            if(paireSuivante.getTrajet()!=null){
                                emplacementProchainArret = paireSuivante.getTrajet().getEmplacementInitial();
                            }
                            else{
                                emplacementProchainArret = paireSuivante.getParcoursBus().getArretDepart().getEmplacement();
                            }
                            
                            Point2D.Float positionProchainArret = emplacementProchainArret.calculPosition(p_echelle);
                            if (emplacementProchainArret.estSurTroncon())
                            {
                                chemin.lineTo(positionProchainArret.x, positionProchainArret.y);
                            }
                            else
                            {
                                chemin.lineTo(positionProchainArret.x, positionProchainArret.y);
                            }
                        }
                        else
                            chemin.lineTo(p2.x, p2.y);
                    }
                }
            }
            if(m_reseau.getPileSelection().contient(itineraire))
                select_g.draw(chemin);
            p_g.draw(chemin); 
            
            Path2D.Float losange = new Path2D.Float();
            src_g.setColor(new Color(0,200,0));
            float y1 = origine.y - SourceAutobus.LARGEUR / 2 / p_echelle;
            float x1 = origine.x + SourceAutobus.LARGEUR / 2 / p_echelle;
            float y2 = y1 + SourceAutobus.LARGEUR / p_echelle;
            float x2 = x1 - SourceAutobus.LARGEUR / p_echelle;
            losange.moveTo(origine.x, y1);
            losange.lineTo(x1, origine.y);
            losange.lineTo(origine.x, y2);
            losange.lineTo(x2, origine.y);
            losange.closePath();
            src_g.fill(losange);
        }
    }
}
