package GUI;

import Domaine.Affichage.DessinateurReseauRoutier;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class DrawingPanel extends JPanel implements Serializable {
    
    public Dimension m_dimensionInit;
    private float m_echelle = 1;
    private MainWindow mainWindow;
    
    public DrawingPanel(){
    }
    
    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
//        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        int width = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
//        setPreferredSize(new Dimension(width,1));
        setVisible(true);
        int height = (int)(width*0.5);
        m_dimensionInit = new Dimension(width,height);
    }
    @Override
    protected void paintComponent(Graphics p_g)
    {
        if (mainWindow != null){
            Graphics2D graphic2D = (Graphics2D)p_g;
            
            graphic2D.scale(m_echelle, m_echelle);
            
            super.paintComponent(graphic2D); 
            DessinateurReseauRoutier mainDrawer = new DessinateurReseauRoutier(mainWindow.m_controleur.m_reseauRoutier, m_dimensionInit);
            mainDrawer.dessiner(graphic2D);
        }
    }
    
    public MainWindow getMainWindow(){
        return mainWindow;
    }
    
    public void setMainWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }
    
    public Dimension getInitialDimension(){
        return m_dimensionInit;
    }
    
    public void setInitialDimension(){
        
    }
    
    public float getEchelle(){
        return m_echelle;
    }
    
    public void setEchelle(float p_valeur){
        if (m_echelle >= 1)
        {
            m_echelle -= p_valeur / 4;
        }
        else
        {
            m_echelle = m_echelle * (1 - p_valeur / 8);
        }
    }
}

