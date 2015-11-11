package GUI;

import Domaine.Affichage.DessinateurReseauRoutier;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import javax.swing.JPanel;
//import javax.swing.border.BevelBorder;

import Domaine.ReseauRoutier.Intersection;
import java.util.LinkedList;

public class DrawingPanel extends JPanel implements Serializable {
    
    public Dimension m_dimension;
    public int m_xMax = 0;  // Coordonnée x de l'intersection la plus loin
    public int m_yMax = 0;  // Coordonnée y de l'intersection la plus loin
    private MainWindow m_fenetrePrincipale;
    private float m_echelle = 1;
    
    public DrawingPanel(){
    }
    
    public DrawingPanel(MainWindow mainWindow) {
        this.m_fenetrePrincipale = mainWindow;
//        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        
        m_dimension = new Dimension();
        setDimension(false);
        setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics p_g)
    {
        if (m_fenetrePrincipale != null){
            
            Graphics2D graphic2D = (Graphics2D)p_g;
            
            graphic2D.scale(m_echelle, m_echelle);
            
            super.paintComponent(graphic2D);
            
            DessinateurReseauRoutier mainDrawer = new DessinateurReseauRoutier(m_fenetrePrincipale.m_controleur.m_reseauRoutier, m_dimension);
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
        return m_echelle;
    }
    
    public void setEchelle(float p_valeur){
        
        m_echelle *= (1 - p_valeur / 16);
        
        boolean changement = false;
        setDimension(changement);
    }
    
    public final void setDimension(boolean p_nouvelleIntersection){

        int l = m_fenetrePrincipale.getScrollPane().getWidth();
        int h = m_fenetrePrincipale.getScrollPane().getHeight();
        
        if (p_nouvelleIntersection)
        {
            m_xMax = 0;
            m_yMax= 0;
            LinkedList<Intersection> intersections = m_fenetrePrincipale.m_controleur.m_reseauRoutier.getIntersections();
            for (Intersection intersection: intersections)
            {
                m_xMax = java.lang.Math.max(m_xMax, (int)intersection.getPosition().getX());
                m_yMax = java.lang.Math.max(m_yMax, (int)intersection.getPosition().getY());
            }
        }

        l = (int)(m_echelle * m_xMax + 0.95 * l);
        h = (int)(m_echelle * m_yMax + 0.95 * h);
//        l = java.lang.Math.max((int)(m_echelle * xMax + 0.95 * l), (int)(1.5 * l));
//        h = java.lang.Math.max((int)(m_echelle * yMax + 0.95 * h), (int)(1.5 * h));
        m_dimension.setSize(l, h);
        setPreferredSize(m_dimension);
    }
}

