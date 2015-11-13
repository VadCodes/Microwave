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
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class PanneauAfficheur extends JPanel implements Serializable {
    
    public Dimension m_dimension;
    private MainWindow m_fenetrePrincipale;
    
    private float m_echelle = 1;
    public int m_xMax;  // Coordonnée x de l'intersection la plus loin
    public int m_yMax;  // Coordonnée y de l'intersection la plus loin
    
    public PanneauAfficheur(){
    }
    
    public PanneauAfficheur(MainWindow mainWindow) {
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
        
        if (m_echelle < 1)
        {
            xReel = p_x / m_echelle - Intersection.RAYON;
            yReel = p_y / m_echelle - Intersection.RAYON;
            diametre = 2 * Intersection.RAYON;
        }
        else
        {
            xReel = (p_x - Intersection.RAYON) / m_echelle;
            yReel = (p_y - Intersection.RAYON) / m_echelle;
            diametre = 2 * Intersection.RAYON / m_echelle;
        }

        Ellipse2D.Float zoneSelection = new Ellipse2D.Float(xReel, yReel, diametre, diametre);

        LinkedList<Intersection> intersections = m_fenetrePrincipale.m_controleur.m_reseauRoutier.getIntersections();
        for (Intersection intersection: intersections)
        {
            if (zoneSelection.contains(intersection.getPosition()))
            {
                intersection.changerStatutSelection();
                return true;                                    
            }
        }
        
        return false;
    }
    
    public void selectionnerTroncon(Integer p_x, Integer p_y)
    {
//        LinkedList<Intersection> intersections = m_reseau.getIntersections();
//        for (Intersection intersection: intersections)
//        {
//            Point2D.Float p1 = intersection.getPosition();
//            
//            for (Troncon troncon: intersection.getListeTroncons())
//            {   
//                Point2D.Float p2 = troncon.getDestination().getPosition();
//
//                Path2D.Float fleche = new Path2D.Float();                
//                fleche.moveTo(p1.x, p1.y);
//                fleche.lineTo(p2.x, p2.y);
//                p_g.draw(fleche);
//                
//                float d = (float)p2.distance(p1);
//                float dx = p2.x - p1.x;
//                float dy = p2.y - p1.y;
//                
//                fleche.moveTo(p1.x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE * dx / d) / m_echelle, 
//                        p1.y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE * dy / d) / m_echelle);
//                fleche.lineTo(p1.x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE / 2 * -dy / d) / m_echelle, 
//                        p1.y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE / 2 * dx / d) / m_echelle);
//                fleche.lineTo(p1.x + 0.5 * dx + (Troncon.GROSSEUR_FLECHE / 2 * dy / d) / m_echelle, 
//                        p1.y + 0.5 * dy + (Troncon.GROSSEUR_FLECHE / 2 * -dx / d) / m_echelle);
//                fleche.closePath();
//                p_g.fill(fleche);
//            }
//        }
    }
}