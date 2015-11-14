package GUI;

import Domaine.Affichage.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import javax.swing.JPanel;
//import javax.swing.border.BevelBorder;

import java.util.LinkedList;
import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauRoutier.Troncon;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class AfficheurReseau extends JPanel implements Serializable {
    
    public Dimension m_dimension;
    private MainWindow m_fenetrePrincipale;
    
    private float m_echelle = 1;
    public int m_xMax;  // Coordonnée x de l'intersection la plus loin
    public int m_yMax;  // Coordonnée y de l'intersection la plus loin
    
    public AfficheurReseau(){
    }
    
    public AfficheurReseau(MainWindow mainWindow) {
        this.m_fenetrePrincipale = mainWindow;
//        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        
        m_dimension = new Dimension();
        m_xMax = 1600;
        m_yMax = 900;
        setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics p_g)
    {
        if (m_fenetrePrincipale != null){
            
            Graphics2D graphic2D = (Graphics2D)p_g;
            
            super.paintComponent(graphic2D);
            
            graphic2D.scale(m_echelle, m_echelle);
            
            DessinateurRoutier dessinateurRoutier = new DessinateurRoutier(m_fenetrePrincipale.m_controleur.m_reseauRoutier, m_dimension);
            dessinateurRoutier.dessiner(graphic2D);
        }
    }
    
    public MainWindow getFenetrePrincipale(){
        return m_fenetrePrincipale;
    }
    
    public void setFenetrePrincipale(MainWindow p_fenetrePrincipale){
        this.m_fenetrePrincipale = p_fenetrePrincipale;
    }
    
    public Dimension getInitialDimension(){
        return m_dimension;
    }
    
    public void setInitialDimension(){
        
    }
    
    public float getEchelle(){
        return (m_echelle);
    }
    
    public void setEchelle(float p_valeur){
        
        m_echelle *= (1 - p_valeur / 16);
        //max : 300 000 (glitches)
        
        boolean ajoutIntersection = false;
        setDimension(ajoutIntersection);
    }
    
    public final void setDimension(boolean p_nouvelleIntersection){

        int l = m_fenetrePrincipale.getDefilementAfficheur().getWidth();
        int h = m_fenetrePrincipale.getDefilementAfficheur().getHeight();

        if (p_nouvelleIntersection)
        {
            m_xMax = 1600;
            m_yMax = 900;
            LinkedList<Intersection> intersections = m_fenetrePrincipale.m_controleur.m_reseauRoutier.getIntersections();
            for (Intersection intersection: intersections)
            {
                m_xMax = java.lang.Math.max(m_xMax, (int)intersection.getPosition().getX());
                m_yMax = java.lang.Math.max(m_yMax, (int)intersection.getPosition().getY());
            }
        }
        l = (int)(1.065 * m_echelle * m_xMax + 0.3 * l);
        h = (int)(1.065 * m_echelle * m_yMax + 0.4 * h);
        m_dimension.setSize(l, h);
        setPreferredSize(m_dimension);
    }
    
    public Boolean selectionnerIntersection(Integer p_x, Integer p_y)
    {
        float xReel;
        float yReel;        
        float diametre;
        
        if (m_echelle > 1)
        {
            xReel = (p_x - Intersection.RAYON) / m_echelle;
            yReel = (p_y - Intersection.RAYON) / m_echelle;
            diametre = 2 * Intersection.RAYON / m_echelle;
        }
        else
        {
            xReel = p_x / m_echelle - Intersection.RAYON;
            yReel = p_y / m_echelle - Intersection.RAYON;
            diametre = 2 * Intersection.RAYON;
        }

        Ellipse2D.Float zoneSelection = new Ellipse2D.Float(xReel, yReel, diametre, diametre);

        LinkedList<Intersection> intersections = m_fenetrePrincipale.m_controleur.m_reseauRoutier.getIntersections();
        for (Intersection intersection: intersections)
        {
            if (zoneSelection.contains(intersection.getPosition()))
            {
                intersection.changerStatutSelection();
                if (intersection.estSelectionee())
                {
                    m_fenetrePrincipale.m_controleur.m_reseauRoutier.incrementerIntersectionsSelectionnees();
                }
                else
                {
                    m_fenetrePrincipale.m_controleur.m_reseauRoutier.decrementerIntersectionsSelectionnees();
                }
                
                return true;                                    
            }
        }
        
        return false;
    }
    
    public void selectionnerTroncon(Integer p_x, Integer p_y)
    {
        float xReel;
        float yReel;        
        float longueur;
        
        if (m_echelle > 1)
        {
            xReel = (p_x - Troncon.LARGEUR / 2) / m_echelle;
            yReel = (p_y - Troncon.LARGEUR / 2) / m_echelle;
            longueur = Troncon.LARGEUR / m_echelle;
        }
        else
        {
            xReel = p_x / m_echelle - Troncon.LARGEUR / 2;
            yReel = p_y / m_echelle - Troncon.LARGEUR / 2;
            longueur = Troncon.LARGEUR;
        }
        
        Rectangle2D.Float zoneApproximative = new Rectangle2D.Float(xReel, yReel, longueur, longueur);
        
        LinkedList<Intersection> intersections = m_fenetrePrincipale.m_controleur.m_reseauRoutier.getIntersections();
        for (Intersection intersection: intersections)
        {
            Point2D.Float p1 = intersection.getPosition();
            
            for (Troncon troncon: intersection.getListeTroncons())
            {   
                Point2D.Float p2 = troncon.getDestination().getPosition();
                Line2D.Float segment = new Line2D.Float(p1, p2);
                
                if (segment.intersects(zoneApproximative))
                {
                    troncon.changerStatutSelection();
                    return;
                }
            }
        }
    }
    public void deselectionnerRoutier()
    {
        LinkedList<Intersection> intersections = m_fenetrePrincipale.m_controleur.m_reseauRoutier.getIntersections();
        for (Intersection intersection: intersections)
        {
            if (intersection.estSelectionee())
            {
                intersection.changerStatutSelection();
            }
            
            for (Troncon troncon: intersection.getListeTroncons())
            {   
                if (troncon.estSelectione())
                {
                    troncon.changerStatutSelection();
                }
            }
        }
        
        m_fenetrePrincipale.m_controleur.m_reseauRoutier.setIntersectionsSelectionnees(0);
    }
}