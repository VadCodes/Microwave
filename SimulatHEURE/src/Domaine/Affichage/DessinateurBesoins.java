/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Affichage;

import Domaine.BesoinsTransport.Itineraire;
import Domaine.BesoinsTransport.PaireParcours;
import Domaine.BesoinsTransport.ReseauBesoins;
import Domaine.BesoinsTransport.SourceIndividus;
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
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author louis
 */
public class DessinateurBesoins {
    private ReseauBesoins m_reseau;
    private Boolean m_transparence;
    public DessinateurBesoins(ReseauBesoins p_reseau, Boolean transparence){
        m_reseau = p_reseau;
        m_transparence = transparence;
    }
    public void dessinerItineraire(Graphics2D p_g, float p_echelle)
    {  
        Graphics2D select_g = (Graphics2D) p_g.create();
        dessinerItineraireSelectionne(select_g, p_echelle);        
        
        for (Itineraire itineraire: m_reseau.getListItineraire()){
           
            Path2D.Float chemin1 = new Path2D.Float();
            Path2D.Float chemin2 = new Path2D.Float();
           
            Point2D.Float origine;
            if(itineraire.getListPaireParcours().getFirst().getTrajet()!=null){
                origine = itineraire.getListPaireParcours().getFirst().getTrajet().getEmplacementInitial().calculPosition(p_echelle);
                chemin1.moveTo(origine.x, origine.y);
            }
            else{
                origine = itineraire.getListPaireParcours().getFirst().getParcoursBus().getArretDepart().getEmplacement().calculPosition(p_echelle);
                chemin2.moveTo(origine.x, origine.y);
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
                        
                        //if (itTroncon.previousIndex() != 0 || !paire.getTrajet().getEmplacementInitial().estSurTroncon())
                        chemin1 = new Path2D.Float();
                        chemin1.moveTo(p1.x, p1.y);

                        chemin1.lineTo(p2.x, p2.y);

                        p_g.setStroke(new BasicStroke((Troncon.LARGEUR*2/3) / p_echelle, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new   float[]{(Troncon.LARGEUR) / p_echelle}, 0));
                        p_g.setColor(new Color(0x50FFFFFF & itineraire.getCouleur().getRGB(), m_transparence));
                        p_g.draw(chemin1); 
                        
                    }
                    
                }
                              
                if(paire.getParcoursBus()!=null){
                    
                    
                    PaireParcours paireSuivante;
                    if(itPaire.hasNext()){
                        paireSuivante = itPaire.next();
                        itPaire.previous();
                    }
                    else
                        paireSuivante = null;
                    chemin2 = new Path2D.Float();
                    Boolean premier = true;
                    
                    LinkedList<Troncon> listeTroncons = paire.getParcoursBus().getTroncons();
                    Troncon premierTroncon = listeTroncons.getFirst();
                    
                    if (listeTroncons.size()>1){
                        if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().estSurTroncon()){
                            if(paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().estSurTroncon()){
                                if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().getTroncon()==
                                        paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().getTroncon()){
                                    if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().getPourcentageParcouru()>
                                            paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().getPourcentageParcouru()){
                                        listeTroncons.addLast(premierTroncon);
                                    }
                                }
                            }
                        }
                    }
            
                    ListIterator<Troncon> itTroncon = listeTroncons.listIterator();
                    
                    while (itTroncon.hasNext())
                    {
                        Troncon troncon = itTroncon.next();
                        
                        Point2D.Float p1;
                        Point2D.Float p2;
                        Boolean p1Aj = false;
                        Boolean p2Aj = false;
                        
                        if(listeTroncons.size()==1){
                            p1 = paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().calculPosition(p_echelle);
                            p2 = paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().calculPosition(p_echelle);
                            if(!paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().estSurTroncon()){
                                p1Aj = true;
                            }
                            if(!paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().estSurTroncon()){
                                p2Aj = true;
                            }
                        }
                        else{
                            if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().estSurTroncon()){
                                if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().getTroncon()==troncon && itTroncon.hasNext()){
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
                            if(paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().estSurTroncon()){
                                if(paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().getTroncon()==troncon && (troncon!=premierTroncon || !itTroncon.hasNext())){
                                    p2 = paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().calculPosition(p_echelle);
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
                        
                        if(premier){
                            chemin2.moveTo(p1.x, p1.y);
                            premier = false;
                        }     
                        
                        chemin2.lineTo(p1.x, p1.y);
                        
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
                                chemin2.lineTo(positionProchainArret.x, positionProchainArret.y);
                            }
                            else
                            {
                                positionProchainArret = new Point2D.Float(positionProchainArret.x + ajX, positionProchainArret.y + ajY);
                                chemin2.lineTo(positionProchainArret.x, positionProchainArret.y);
                            }
                        }
                        else{
                            
                            chemin2.lineTo(p2.x, p2.y);
                        }
                    }
                    p_g.setStroke(new BasicStroke(Troncon.LARGEUR / p_echelle, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    if(m_transparence){
                        p_g.setColor(new Color(0x40FFFFFF & itineraire.getCouleur().getRGB(), true));
                    }
                    else{
                        p_g.setColor(new Color(0xA0FFFFFF & itineraire.getCouleur().getRGB(), true));
                    }
                    p_g.draw(chemin2); 
                    
                }
            }
        }
    }
    
    private Path2D.Float dessinerLosange(Graphics2D los_g, Point2D.Float position, Float p_echelle, 
                                Color couleur, float largeur){
        Path2D.Float losange = new Path2D.Float();
        los_g.setColor(couleur);
        float y1 = position.y - largeur / 2 / p_echelle;
        float x1 = position.x + largeur / 2 / p_echelle;
        float y2 = y1 + largeur / p_echelle;
        float x2 = x1 - largeur / p_echelle;
        losange.moveTo(position.x, y1);
        losange.lineTo(x1, position.y);
        losange.lineTo(position.x, y2);
        losange.lineTo(x2, position.y);
        losange.closePath();
        return losange;
    }
    
    public void dessinerSourceIndividus(Graphics2D p_g, float p_echelle){
        Emplacement emplSourceArtificielle = m_reseau.getEmplacementSourceTemp();
        if(emplSourceArtificielle!=null){
            Point2D.Float posSourceArtificielle = emplSourceArtificielle.calculPosition(p_echelle);
            Path2D.Float losange = dessinerLosange(p_g, posSourceArtificielle, p_echelle, new Color(55, 55, 55), SourceIndividus.LARGEUR);
            p_g.setStroke(new BasicStroke(1/p_echelle));
            p_g.draw(losange);
        }
        for (Itineraire itineraire: m_reseau.getListItineraire()){
            Point2D.Float origine;
            if(itineraire.getListPaireParcours().getFirst().getTrajet()!=null){
                origine = itineraire.getListPaireParcours().getFirst().getTrajet().getEmplacementInitial().calculPosition(p_echelle);
            }
            else{
                origine = itineraire.getListPaireParcours().getFirst().getParcoursBus().getArretDepart().getEmplacement().calculPosition(p_echelle);
            }
            float grossissement = 1.4f;
            if(m_reseau.getPileSelection().contient(itineraire)){
                
                Path2D.Float losange = dessinerLosange(p_g, origine, p_echelle, 
                    new Color(50,200,255,200), grossissement*SourceIndividus.LARGEUR);
                p_g.fill(losange);
            }
            else if(m_reseau.getElementCurseur()==itineraire){
                Path2D.Float losange = dessinerLosange(p_g, origine, p_echelle, 
                    new Color(255,200,0,130), grossissement*SourceIndividus.LARGEUR);
                p_g.fill(losange);
            }
            Path2D.Float los = dessinerLosange(p_g, origine, p_echelle, new Color(0x50FFFFFF & itineraire.getCouleur().getRGB(), m_transparence), SourceIndividus.LARGEUR);
            p_g.fill(los);
        }
                
    }

    private void dessinerItineraireSelectionne(Graphics2D p_g, float p_echelle) {
        
        p_g.setStroke(new BasicStroke(Troncon.LARGEUR*1.35f/p_echelle, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (Itineraire itineraire: m_reseau.getListItineraire()){
           
            if(m_reseau.getPileSelection().contient(itineraire) || m_reseau.getElementCurseur()==itineraire){
                if(m_reseau.getPileSelection().contient(itineraire)){
                    p_g.setColor(new Color(50,200,255 , 140));
                }
                else if(m_reseau.getElementCurseur()==itineraire){
                    p_g.setColor(new Color(255,200,0 , 130));
                }
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

                            //if (itTroncon.previousIndex() != 0 || !paire.getTrajet().getEmplacementInitial().estSurTroncon())
                            chemin.moveTo(p1.x, p1.y);

                            chemin.lineTo(p2.x, p2.y);                        
                        }

                    }

                    if(paire.getParcoursBus()!=null){


                        PaireParcours paireSuivante;
                        if(itPaire.hasNext()){
                            paireSuivante = itPaire.next();
                            itPaire.previous();
                        }
                        else
                            paireSuivante = null;
                        
                        Boolean premier = true;

                        LinkedList<Troncon> listeTroncons = paire.getParcoursBus().getTroncons();
                        Troncon premierTroncon = listeTroncons.getFirst();

                        if (listeTroncons.size()>1){
                            if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().estSurTroncon()){
                                if(paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().estSurTroncon()){
                                    if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().getTroncon()==
                                            paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().getTroncon()){
                                        if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().getPourcentageParcouru()>
                                                paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().getPourcentageParcouru()){
                                            listeTroncons.addLast(premierTroncon);
                                        }
                                    }
                                }
                            }
                        }

                        ListIterator<Troncon> itTroncon = listeTroncons.listIterator();

                        while (itTroncon.hasNext())
                        {
                            Troncon troncon = itTroncon.next();

                            Point2D.Float p1;
                            Point2D.Float p2;
                            Boolean p1Aj = false;
                            Boolean p2Aj = false;

                            if(listeTroncons.size()==1){
                                p1 = paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().calculPosition(p_echelle);
                                p2 = paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().calculPosition(p_echelle);
                                if(!paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().estSurTroncon()){
                                    p1Aj = true;
                                }
                                if(!paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().estSurTroncon()){
                                    p2Aj = true;
                                }
                            }
                            else{
                                if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().estSurTroncon()){
                                    if(paire.getParcoursBus().getPaireArretDepart().getArret().getEmplacement().getTroncon()==troncon && itTroncon.hasNext()){
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
                                if(paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().estSurTroncon()){
                                    if(paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().getTroncon()==troncon && (troncon!=premierTroncon || !itTroncon.hasNext())){
                                        p2 = paire.getParcoursBus().getPaireArretFinal().getArret().getEmplacement().calculPosition(p_echelle);
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

                            if(premier){
                                chemin.moveTo(p1.x, p1.y);
                                premier = false;
                            }     

                            chemin.lineTo(p1.x, p1.y);

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
                                    positionProchainArret = new Point2D.Float(positionProchainArret.x + ajX, positionProchainArret.y + ajY);
                                    chemin.lineTo(positionProchainArret.x, positionProchainArret.y);
                                }
                            }
                            else{

                                chemin.lineTo(p2.x, p2.y);
                            }
                        }
                    }
                     
                }   
                p_g.draw(chemin);
            }
            
        }
    }
}
