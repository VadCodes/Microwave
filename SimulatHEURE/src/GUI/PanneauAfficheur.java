package GUI;

import Domaine.Affichage.AfficheurReseauRoutier;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import javax.swing.JLayeredPane;
//import javax.swing.border.BevelBorder;

import java.util.LinkedList;
import Domaine.ReseauRoutier.Intersection;

public class PanneauAfficheur extends JLayeredPane implements Serializable {
    
    public Dimension m_dimension;
    public int m_xMax;  // Coordonnée x de l'intersection la plus loin
    public int m_yMax;  // Coordonnée y de l'intersection la plus loin
    private MainWindow m_fenetrePrincipale;
    private float m_echelle = 1;
    
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
            
            AfficheurReseauRoutier mainDrawer = new AfficheurReseauRoutier(m_fenetrePrincipale.m_controleur.m_reseauRoutier, m_dimension);
            mainDrawer.dessiner(graphic2D);
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
        
        boolean changement = false;
        setDimension(changement);
    }
    
    public final void setDimension(boolean p_nouvelleIntersection){

        int l = m_fenetrePrincipale.getScrollPane().getWidth();
        int h = m_fenetrePrincipale.getScrollPane().getHeight();

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
}

