package GUI;

import Domaine.Simulatheure;
import Domaine.Simulatheure.Modes;
import Domaine.Simulatheure.Commandes;

import javax.swing.SwingUtilities;
import java.lang.Object;
import Domaine.Utilitaire.*;
import Domaine.ReseauRoutier.*;
import Domaine.ReseauTransport.ElementTransport;
import java.util.LinkedList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Vinny
 */
public class MainWindow extends javax.swing.JFrame {

    public Simulatheure m_controleur;
    public Modes m_mode_courant;
    public Commandes m_commande_courante;
    private Boolean m_booleanCTRL = false;
    
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        m_controleur = new Simulatheure();
        initComponents();
        routier.doClick();
        this.afficheurReseau.setDimension(false);
          
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupeModes = new javax.swing.ButtonGroup();
        groupeRoutier = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        mainPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        boutonModes = new javax.swing.JPanel();
        routier = new javax.swing.JToggleButton();
        transport = new javax.swing.JToggleButton();
        besoins = new javax.swing.JToggleButton();
        simulation = new javax.swing.JToggleButton();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        wtf2 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        wtf = new javax.swing.JLabel();
        afficheurCommandes = new javax.swing.JLayeredPane();
        boutonsRoutier = new javax.swing.JPanel();
        selectionRoutier = new javax.swing.JToggleButton();
        ajoutIntersection = new javax.swing.JToggleButton();
        constructionTroncon = new javax.swing.JToggleButton();
        suppressionRoutier = new javax.swing.JButton();
        boutonsTransport = new javax.swing.JPanel();
        selectionTransport = new javax.swing.JToggleButton();
        ajoutSource = new javax.swing.JToggleButton();
        ajoutArret = new javax.swing.JToggleButton();
        ajoutCircuit = new javax.swing.JToggleButton();
        suppressionTransport = new javax.swing.JButton();
        defilementAfficheur = new javax.swing.JScrollPane();
        afficheurReseau = new GUI.AfficheurReseau(this);
        jPanel4 = new javax.swing.JPanel();
        menu = new javax.swing.JMenuBar();
        fichier = new javax.swing.JMenu();
        quitter = new javax.swing.JMenuItem();

        groupeModes.add(routier);
        groupeModes.add(transport);
        groupeModes.add(besoins);
        groupeModes.add(simulation);

        groupeModes.add(selectionRoutier);
        groupeModes.add(ajoutIntersection);
        groupeModes.add(constructionTroncon);

        jPopupMenu1.setName(""); // NOI18N

        jMenuItem1.setText("Éditer...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Supprimer");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        mainPanel.setPreferredSize(new java.awt.Dimension(1600, 900));
        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(150, 400));
        jPanel1.setLayout(new java.awt.BorderLayout());

        boutonModes.setPreferredSize(new java.awt.Dimension(150, 400));
        boutonModes.setRequestFocusEnabled(false);
        boutonModes.setLayout(new java.awt.GridLayout(4, 1));

        routier.setText("Réseau routier");
        routier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routierActionPerformed(evt);
            }
        });
        boutonModes.add(routier);

        transport.setText("Réseau transport");
        transport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportActionPerformed(evt);
            }
        });
        boutonModes.add(transport);

        besoins.setText("Besoins transport");
        besoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                besoinsActionPerformed(evt);
            }
        });
        boutonModes.add(besoins);

        simulation.setText("Simulation");
        simulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationActionPerformed(evt);
            }
        });
        boutonModes.add(simulation);

        jPanel1.add(boutonModes, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.GridLayout(3, 2));

        jButton1.setText("Annuler");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1);

        jButton2.setText("Revenir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2);

        jLabel1.setText("Zoom :");
        jPanel5.add(jLabel1);

        wtf2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        wtf2.setText("100 %");
        wtf2.setRequestFocusEnabled(false);
        jPanel5.add(wtf2);

        jLabel2.setText("Position :");
        jPanel5.add(jLabel2);
        jPanel5.add(wtf);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel3.add(jPanel2, java.awt.BorderLayout.WEST);

        afficheurCommandes.setPreferredSize(new java.awt.Dimension(1300, 800));
        afficheurCommandes.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                afficheurReseauMouseMoved(evt);
            }
        });
        afficheurCommandes.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                afficheurReseauMouseWheelMoved(evt);
            }
        });
        afficheurCommandes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                afficheurReseauMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                afficheurReseauMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                afficheurReseauMousePressed(evt);
            }
        });

        boutonsRoutier.setAlignmentX(1.0F);
        boutonsRoutier.setAlignmentY(1.0F);
        boutonsRoutier.setOpaque(false);
        boutonsRoutier.setPreferredSize(new java.awt.Dimension(90, 120));
        boutonsRoutier.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        selectionRoutier.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        selectionRoutier.setText("Sélectionner");
        selectionRoutier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionRoutierActionPerformed(evt);
            }
        });
        boutonsRoutier.add(selectionRoutier);

        ajoutIntersection.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ajoutIntersection.setText("Intersection");
        ajoutIntersection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutIntersectionActionPerformed(evt);
            }
        });
        boutonsRoutier.add(ajoutIntersection);

        constructionTroncon.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        constructionTroncon.setText("Tronçon");
        constructionTroncon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                constructionTronconActionPerformed(evt);
            }
        });
        boutonsRoutier.add(constructionTroncon);

        suppressionRoutier.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        suppressionRoutier.setText("Supprimer");
        suppressionRoutier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressionRoutierActionPerformed(evt);
            }
        });
        boutonsRoutier.add(suppressionRoutier);

        boutonsTransport.setAlignmentX(1.0F);
        boutonsTransport.setAlignmentY(1.0F);
        boutonsTransport.setOpaque(false);
        boutonsTransport.setPreferredSize(new java.awt.Dimension(90, 120));
        boutonsTransport.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        selectionTransport.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        selectionTransport.setText("Sélectionner");
        selectionTransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionTransportActionPerformed(evt);
            }
        });
        boutonsTransport.add(selectionTransport);

        ajoutSource.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ajoutSource.setText("Source");
        ajoutSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutSourceActionPerformed(evt);
            }
        });
        boutonsTransport.add(ajoutSource);

        ajoutArret.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ajoutArret.setText("Arret");
        ajoutArret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutArretActionPerformed(evt);
            }
        });
        boutonsTransport.add(ajoutArret);

        ajoutCircuit.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ajoutCircuit.setText("Circuit");
        ajoutCircuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutCircuitActionPerformed(evt);
            }
        });
        boutonsTransport.add(ajoutCircuit);

        suppressionTransport.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        suppressionTransport.setText("Supprimer");
        suppressionTransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressionTransportActionPerformed(evt);
            }
        });
        boutonsTransport.add(suppressionTransport);

        defilementAfficheur.setPreferredSize(new java.awt.Dimension(1300, 800));
        defilementAfficheur.setWheelScrollingEnabled(false);

        afficheurReseau.setEnabled(false);
        afficheurReseau.setPreferredSize(new java.awt.Dimension(1600, 900));
        afficheurReseau.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                afficheurReseauMouseMoved(evt);
            }
        });
        afficheurReseau.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                afficheurReseauMouseWheelMoved(evt);
            }
        });
        afficheurReseau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                afficheurReseauMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                afficheurReseauMousePressed(evt);
            }
        });

        javax.swing.GroupLayout afficheurReseauLayout = new javax.swing.GroupLayout(afficheurReseau);
        afficheurReseau.setLayout(afficheurReseauLayout);
        afficheurReseauLayout.setHorizontalGroup(
            afficheurReseauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1600, Short.MAX_VALUE)
        );
        afficheurReseauLayout.setVerticalGroup(
            afficheurReseauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );

        defilementAfficheur.setViewportView(afficheurReseau);

        javax.swing.GroupLayout afficheurCommandesLayout = new javax.swing.GroupLayout(afficheurCommandes);
        afficheurCommandes.setLayout(afficheurCommandesLayout);
        afficheurCommandesLayout.setHorizontalGroup(
            afficheurCommandesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(defilementAfficheur, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
            .addGroup(afficheurCommandesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, afficheurCommandesLayout.createSequentialGroup()
                    .addContainerGap(411, Short.MAX_VALUE)
                    .addComponent(boutonsRoutier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(27, 27, 27)))
            .addGroup(afficheurCommandesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, afficheurCommandesLayout.createSequentialGroup()
                    .addContainerGap(412, Short.MAX_VALUE)
                    .addComponent(boutonsTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(26, 26, 26)))
        );
        afficheurCommandesLayout.setVerticalGroup(
            afficheurCommandesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(defilementAfficheur, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
            .addGroup(afficheurCommandesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(afficheurCommandesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsRoutier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(373, Short.MAX_VALUE)))
            .addGroup(afficheurCommandesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(afficheurCommandesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(343, Short.MAX_VALUE)))
        );
        afficheurCommandes.setLayer(boutonsRoutier, javax.swing.JLayeredPane.DEFAULT_LAYER);
        afficheurCommandes.setLayer(boutonsTransport, javax.swing.JLayeredPane.DEFAULT_LAYER);
        afficheurCommandes.setLayer(defilementAfficheur, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel3.add(afficheurCommandes, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("SimulatHEURE", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 678, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Statistiques", jPanel4);

        mainPanel.add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext().setAccessibleName("SimulatHeure");

        getContentPane().add(mainPanel);

        fichier.setText("Fichier");

        quitter.setText("Quitter");
        quitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitterActionPerformed(evt);
            }
        });
        fichier.add(quitter);

        menu.add(fichier);

        setJMenuBar(menu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void quitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitterActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitterActionPerformed

    private void routierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routierActionPerformed
        
        this.setMode(Modes.ROUTIER);
        boutonsRoutier.setVisible(true);
        selectionRoutier.doClick();        
    }//GEN-LAST:event_routierActionPerformed

    private void transportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportActionPerformed
        this.setMode(Modes.TRANSPORT);
        boutonsTransport.setVisible(true);
    }//GEN-LAST:event_transportActionPerformed

    private void besoinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_besoinsActionPerformed
        this.setMode(Modes.BESOINS);
        //boutonsBesoins.setVisible(true);
    }//GEN-LAST:event_besoinsActionPerformed

    private void simulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationActionPerformed
        this.setMode(Modes.SIMULATION);
        //boutonsSimulation.setVisible(true);
    }//GEN-LAST:event_simulationActionPerformed

    private void afficheurReseauMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMousePressed
        float echelle = afficheurReseau.getEchelle();
        if (SwingUtilities.isLeftMouseButton(evt))
        {
            
            switch (m_mode_courant)
            {                
                case ROUTIER:
                                        
                    switch (m_commande_courante)
                    {                        
                        case SELECTIONNER:
                            if(evt.isControlDown())
                            {
                                System.out.println("Pressed");
                                ElementRoutier plusieursEr = m_controleur.selectionnerPlusieursElementRoutier(evt.getX(), evt.getY(), echelle);
                            }
                            else{
                            ElementRoutier er = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle);
                            }
                               
                            break;
                        
                        case INTERSECTION:
                            m_controleur.ajouterIntersection(evt.getX(), evt.getY(), echelle);
                            boolean intersectionAjoutee = true;
                            afficheurReseau.setDimension(intersectionAjoutee);
                            defilementAfficheur.setViewportView(afficheurReseau);                            
                            break;
                            
                        case TRONCON:
                            m_controleur.construireTroncon(evt.getX(), evt.getY(), echelle);
                            break;
                            
                        default:
                            break;
                    }
                break;
                case TRANSPORT:                   
                    switch (m_commande_courante)
                    {                        
                        
                        case SELECTIONNER:
                            m_controleur.deselectionnerRoutier();
                            ElementTransport et = m_controleur.selectionnerElementTransport(evt.getX(), evt.getY(), echelle);
                            break;
                        
                        case CIRCUIT:
                            m_controleur.ajouterCircuit(evt.getX(), evt.getY(), echelle);
                            break;
                            
                        case SOURCE:
                            ElementRoutier elemRoutie = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle);
                            if (elemRoutie != null){
                                if (elemRoutie.getClass() == Troncon.class){
                                    m_controleur.ajouterSource(evt.getX(), evt.getY(), echelle);
                                    m_controleur.deselectionnerRoutier();
                                }
                            }                                
                            break;                  
                        case ARRET:
                            ElementRoutier elemRoutier = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle);
                            if (elemRoutier != null){
                                if (elemRoutier.getClass() == Troncon.class || elemRoutier.getClass() == Intersection.class){
                                    m_controleur.ajouterArret(evt.getX(), evt.getY(), echelle);
                                    m_controleur.deselectionnerRoutier();
                                }
                                else if (elemRoutier.getClass() == Intersection.class){
                                    m_controleur.ajouterArret(evt.getX(), evt.getY(), echelle);
                                m_controleur.deselectionnerRoutier();
                                }   
                            }
                            break;
                            
                        default:
                            break;
                    }
                    break;
                    
                    
                default:
                    break;
            }
        }
        else if (SwingUtilities.isRightMouseButton(evt))
        {
            switch (m_mode_courant)
            {                
                case ROUTIER:                  
                    m_controleur.deselectionnerRoutier();
                    ElementRoutier elemRoutier = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle);
                    if (elemRoutier!=null){
                        jPopupMenu1.show(this.afficheurReseau,evt.getX(),evt.getY());
                    }
                    break;
                    
                case TRANSPORT:
                    m_controleur.cancellerCircuit();
                    break;
            }
        }
        
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_afficheurReseauMousePressed

    private void afficheurReseauMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_afficheurReseauMouseWheelMoved
        
        float echelleInitiale = afficheurReseau.getEchelle();
        afficheurReseau.setEchelle(evt.getWheelRotation());
        float rapportEchelles = afficheurReseau.getEchelle() / echelleInitiale;
        
        int x = defilementAfficheur.getViewport().getViewPosition().x;
        x = (int)(evt.getX() * (rapportEchelles  - 1)) + x;
        
        int y = defilementAfficheur.getViewport().getViewPosition().y;
        y = (int)(evt.getY() * (rapportEchelles  - 1)) + y;
        
        defilementAfficheur.getViewport().setViewPosition(new java.awt.Point(x, y));
        wtf2.setText(Integer.toString((int)(afficheurReseau.getEchelle() * 100)) + " %");
        
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_afficheurReseauMouseWheelMoved

    private void afficheurReseauMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMouseMoved
        
        float x = evt.getX() / afficheurReseau.getEchelle();
        float y = evt.getY() / afficheurReseau.getEchelle();
        wtf.setText(Integer.toString((int)x) + "  " + Integer.toString((int)y));
    }//GEN-LAST:event_afficheurReseauMouseMoved

    private void afficheurReseauMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMouseExited
        wtf.setText("");
    }//GEN-LAST:event_afficheurReseauMouseExited

    private void selectionRoutierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionRoutierActionPerformed
        this.setCommande(Commandes.SELECTIONNER);
    }//GEN-LAST:event_selectionRoutierActionPerformed

    private void ajoutIntersectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutIntersectionActionPerformed
        
        this.setCommande(Commandes.INTERSECTION);
        m_controleur.deselectionnerRoutier();
        
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_ajoutIntersectionActionPerformed

  
    private void constructionTronconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_constructionTronconActionPerformed
        
        this.setCommande(Commandes.TRONCON);
        m_controleur.deselectionnerRoutier();
        
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_constructionTronconActionPerformed

    private void suppressionRoutierActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        
        boolean intersectionSupprimee = false;
        switch (m_commande_courante)
        {
            case SELECTIONNER:
                intersectionSupprimee = m_controleur.supprimerSelectionRoutier();
                break;
                
            default:
                break;
        }
        
        //afficheurReseau.setDimension(intersectionSupprimee);
        //defilementAfficheur.setViewportView(afficheurReseau);
        this.afficheurCommandes.repaint();
    }                                                

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        LinkedList<ElementRoutier> elementsSelectionnes = m_controleur.getElementsSelectionnesRoutier();
        assert(elementsSelectionnes.size() == 1);
        ElementRoutier elem = elementsSelectionnes.getFirst();
        
        //ouvrir une fenetre contextuelle qui agit sur elem, dependamment du type d'elem
        if(elem.getClass() == Intersection.class){
            EditerIntersection fenetre = new EditerIntersection();
            fenetre.setIntersection((Intersection) elem);
            fenetre.setResizable(false);
            fenetre.setLocationRelativeTo(null); //pour centrer
            fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fenetre.setVisible(true);
        }
        else if (elem.getClass() == Troncon.class){
            EditerTroncon fenetre = new EditerTroncon();
            fenetre.setTroncon((Troncon) elem);
            fenetre.setResizable(false);
            fenetre.setLocationRelativeTo(null); //pour centrer
            fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fenetre.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        LinkedList<ElementRoutier> elementsSelectionnes = m_controleur.getElementsSelectionnesRoutier();
        assert(elementsSelectionnes.size() == 1);
        ElementRoutier elem = elementsSelectionnes.getFirst();
        
        m_controleur.supprimerSelectionRoutier();
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void selectionTransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionTransportActionPerformed
        this.setCommande(Commandes.SELECTIONNER);
    }//GEN-LAST:event_selectionTransportActionPerformed

    private void ajoutArretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutArretActionPerformed
        this.setCommande(Commandes.ARRET);
        m_controleur.deselectionnerTransport();
        m_controleur.deselectionnerRoutier();
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_ajoutArretActionPerformed

    private void ajoutCircuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutCircuitActionPerformed
        this.setCommande(Commandes.CIRCUIT);
    }//GEN-LAST:event_ajoutCircuitActionPerformed

    private void suppressionTransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppressionTransportActionPerformed

    }//GEN-LAST:event_suppressionTransportActionPerformed

    private void ajoutSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutSourceActionPerformed
         this.setCommande(Commandes.SOURCE);
    }//GEN-LAST:event_ajoutSourceActionPerformed

    private void afficheurReseauMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_afficheurReseauMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });        
    }
    
    public void setMode(Modes p_mode) 
    {
        this.m_mode_courant = p_mode;
        boutonsRoutier.setVisible(false);
        boutonsTransport.setVisible(false);
        //boutonsBesoins.setVisible(false);
        //boutonsSimulation.setVisible(false);
    }
    
    public void setCommande(Commandes p_commande) 
    {
        this.m_commande_courante = p_commande;
    }
    
    public javax.swing.JScrollPane getDefilementAfficheur() 
    {
        return defilementAfficheur;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane afficheurCommandes;
    private GUI.AfficheurReseau afficheurReseau;
    private javax.swing.JToggleButton ajoutArret;
    private javax.swing.JToggleButton ajoutCircuit;
    private javax.swing.JToggleButton ajoutIntersection;
    private javax.swing.JToggleButton ajoutSource;
    private javax.swing.JToggleButton besoins;
    private javax.swing.JPanel boutonModes;
    private javax.swing.JPanel boutonsRoutier;
    private javax.swing.JPanel boutonsTransport;
    private javax.swing.JToggleButton constructionTroncon;
    private javax.swing.JScrollPane defilementAfficheur;
    private javax.swing.JMenu fichier;
    private javax.swing.ButtonGroup groupeModes;
    private javax.swing.ButtonGroup groupeRoutier;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenuItem quitter;
    private javax.swing.JToggleButton routier;
    private javax.swing.JToggleButton selectionRoutier;
    private javax.swing.JToggleButton selectionTransport;
    private javax.swing.JToggleButton simulation;
    private javax.swing.JButton suppressionRoutier;
    private javax.swing.JButton suppressionTransport;
    private javax.swing.JToggleButton transport;
    private javax.swing.JLabel wtf;
    private javax.swing.JLabel wtf2;
    // End of variables declaration//GEN-END:variables
}
