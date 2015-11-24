package GUI;

import Domaine.Simulatheure;
import Domaine.Simulatheure.Modes;
import Domaine.Simulatheure.Commandes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import Domaine.Utilitaire.*;
import Domaine.ReseauRoutier.*;
import Domaine.ReseauTransport.*;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import javax.swing.JFrame;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinny
 */
public class MainWindow extends javax.swing.JFrame {

    public double m_tempsDebutSimulation;
    public double m_tempsFinSimulation;
    public Simulatheure m_controleur;
    public Simulatheure m_controleurSimulation;
    public Simulatheure m_contoleurReseau;
    public Modes m_mode_courant;
    public Commandes m_commande_courante;
    private Boolean m_booleanCTRL = false;
    private Timer m_timer;
    private Chronometre m_crono;
    private MainWindow m_this = this; //l33t
    private boolean m_simulationEstLancer = false;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        changeLookAndFeel();
        m_controleur = new Simulatheure();
        initComponents();
        routier.doClick();
        this.afficheurReseau.setDimension(false);

//        int x1 = 100;   int y1 = 100;
//        int x2 = 500;   int y2 = 500;
//        int x3 = 300;
//                        int y3 = 200;
//        int x4 = 200;
//        int x5 = 400;
//        int x6 = 278;   int y4 = 437;
//        
//        java.awt.event.MouseEvent evtInter1 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x1, y1, 1, false, 1);
//        java.awt.event.MouseEvent evtInter2 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x2, y1, 1, false, 1);
//        java.awt.event.MouseEvent evtInter3 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x2, y2, 1, false, 1);
//        java.awt.event.MouseEvent evtInter4 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x3, y2, 1, false, 1);
//        java.awt.event.MouseEvent evtInter5 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x4, y3, 1, false, 1);
//        
//        java.awt.event.MouseEvent evtArret2 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x2, y3, 1, false, 1);
//        
//        java.awt.event.MouseEvent evtTrajet1 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x3, y1, 1, false, 1);
//        
//        java.awt.event.MouseEvent evtCircuit1 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x5, y2, 1, false, 1);
//        java.awt.event.MouseEvent evtCircuit2 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x6, y4, 1, false, 1);
//        java.awt.event.MouseEvent evtSource1 = new java.awt.event.MouseEvent(defilementAfficheur, 0, 0, 0, x4, y1, 1, false, 1);
//        
//        afficheurReseauMousePressed(evtInter1);
//        afficheurReseauMousePressed(evtInter2);
//        afficheurReseauMousePressed(evtInter3);
//        afficheurReseauMousePressed(evtInter4);
//        afficheurReseauMousePressed(evtInter5);
//        
//        constructionTroncon.doClick();
//        afficheurReseauMousePressed(evtInter1);
//        afficheurReseauMousePressed(evtInter2);
//        afficheurReseauMousePressed(evtInter3);
//        afficheurReseauMousePressed(evtInter4);
//        afficheurReseauMousePressed(evtInter5);
//        
//        transport.doClick();
//        
//        afficheurReseauMousePressed(evtInter1);
//        afficheurReseauMousePressed(evtArret2);
//        afficheurReseauMousePressed(evtInter5);
//        
//        ajoutCircuit.doClick();
//        afficheurReseauMousePressed(evtInter1);
//        afficheurReseauMousePressed(evtArret2);
//        afficheurReseauMousePressed(evtTrajet1);
//        
//        allongerCircuit.doClick();
//        afficheurReseauMousePressed(evtInter5);
//        afficheurReseauMousePressed(evtCircuit1);
//        afficheurReseauMousePressed(evtCircuit2);
//        
//        ajoutSource.doClick();
//        afficheurReseauMousePressed(evtSource1);
//
//    this.afficheurReseau.setDimension(true);
    }

    class MyTimerActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            double tmp = m_crono.getTempsDebut();
            boolean finSimulation = false;
            double deltatT = m_crono.getDeltatT();
            if ((m_tempsFinSimulation - m_tempsDebutSimulation) <= m_crono.getTempsDebut()*1000) {
                deltatT = ((m_tempsFinSimulation - m_tempsDebutSimulation) - tmp*1000)/1000;
                finSimulation = true;
                Date itemDate = new Date((long)(m_tempsDebutSimulation + (tmp + deltatT)*1000));
                String itemDateStr = new SimpleDateFormat("HH:mm:ss").format(itemDate);
                 time.setText(itemDateStr);
            }
            else{
                Date itemDate = new Date((long)(m_tempsDebutSimulation + m_crono.getTempsDebut()*1000));
                String itemDateStr = new SimpleDateFormat("HH:mm:ss").format(itemDate);
                 time.setText(itemDateStr);
            }
            // System.out.println(deltatT);
            m_controleur.rafraichirSimulation(new Temps(deltatT));
                    
            // miseAjourSelectionAutobusAjout();
            facteurMultiplicatif.setText("X" + m_crono.getFacteurVitesse());
            if (deltatT != 0) {
                miseAjoutAutobusComboBox();
            }
            m_this.afficheurCommandes.repaint();
            if (finSimulation) {
                playPauseSimulation.setText("Lancer!");
                arreterSimulation();
            }

        }
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
        groupeTransport = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        editerClicDroit = new javax.swing.JMenuItem();
        supprimerClicDroit = new javax.swing.JMenuItem();
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
        annuler = new javax.swing.JButton();
        retablir = new javax.swing.JButton();
        zoomTitre = new javax.swing.JLabel();
        zoom = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        afficheurCommandes = new javax.swing.JLayeredPane();
        defilementAfficheur = new javax.swing.JScrollPane();
        afficheurReseau = new GUI.AfficheurReseau(this);
        jPanel8 = new javax.swing.JPanel();
        coordonnees = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        boutonsRoutier = new javax.swing.JPanel();
        selectionRoutier = new javax.swing.JToggleButton();
        ajoutIntersection = new javax.swing.JToggleButton();
        constructionTroncon = new javax.swing.JToggleButton();
        editerRoutier = new javax.swing.JToggleButton();
        suppressionRoutier = new javax.swing.JButton();
        boutonsTransport = new javax.swing.JPanel();
        selectionTransport = new javax.swing.JToggleButton();
        ajoutSource = new javax.swing.JToggleButton();
        ajoutArret = new javax.swing.JToggleButton();
        ajoutCircuit = new javax.swing.JToggleButton();
        allongerCircuit = new javax.swing.JButton();
        editerTransport = new javax.swing.JToggleButton();
        suppressionTransport = new javax.swing.JButton();
        boutonsSimulation = new javax.swing.JPanel();
        recommancerSimulation = new javax.swing.JButton();
        playPauseSimulation = new javax.swing.JToggleButton();
        arreterSimulation = new javax.swing.JButton();
        avancerSimulation = new javax.swing.JButton();
        ralentirSimulation = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        facteurMultiplicatif = new javax.swing.JLabel();
        boutonsSelectionRoutier = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        comboBoxTroncons = new javax.swing.JComboBox<String>();
        jLabel5 = new javax.swing.JLabel();
        comboBoxIntersections = new javax.swing.JComboBox<String>();
        boutonsSelectionTransport = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        comboBoxArrets = new javax.swing.JComboBox<String>();
        jLabel9 = new javax.swing.JLabel();
        comboBoxSources = new javax.swing.JComboBox<String>();
        jLabel3 = new javax.swing.JLabel();
        comboBoxCircuits = new javax.swing.JComboBox<String>();
        boutonsSelectionSimulation = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        comboBoxAutobus = new javax.swing.JComboBox<String>();
        jLabel11 = new javax.swing.JLabel();
        comboBoxPietons = new javax.swing.JComboBox();
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

        groupeModes.add(selectionTransport);
        groupeModes.add(ajoutSource);
        groupeModes.add(ajoutArret);
        groupeModes.add(ajoutCircuit);
        groupeModes.add(suppressionTransport);

        jPopupMenu1.setName(""); // NOI18N

        editerClicDroit.setText("Éditer...");
        editerClicDroit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editerClicDroitActionPerformed(evt);
            }
        });
        jPopupMenu1.add(editerClicDroit);

        supprimerClicDroit.setText("Supprimer");
        supprimerClicDroit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerClicDroitActionPerformed(evt);
            }
        });
        jPopupMenu1.add(supprimerClicDroit);

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
        transport.setEnabled(false);
        transport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportActionPerformed(evt);
            }
        });
        boutonModes.add(transport);

        besoins.setText("Besoins transport");
        besoins.setEnabled(false);
        besoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                besoinsActionPerformed(evt);
            }
        });
        boutonModes.add(besoins);

        simulation.setText("Simulation");
        simulation.setEnabled(false);
        simulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationActionPerformed(evt);
            }
        });
        boutonModes.add(simulation);

        jPanel1.add(boutonModes, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.GridLayout(2, 2));

        annuler.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        annuler.setText("Annuler");
        annuler.setEnabled(false);
        annuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerActionPerformed(evt);
            }
        });
        jPanel5.add(annuler);

        retablir.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        retablir.setText("Rétablir");
        retablir.setEnabled(false);
        retablir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retablirActionPerformed(evt);
            }
        });
        jPanel5.add(retablir);

        zoomTitre.setText("Zoom :");
        jPanel5.add(zoomTitre);

        zoom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        zoom.setText("100 %");
        zoom.setRequestFocusEnabled(false);
        jPanel5.add(zoom);

        jPanel1.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jPanel2.add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel3.add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.BorderLayout());

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

        defilementAfficheur.setPreferredSize(new java.awt.Dimension(1300, 800));
        defilementAfficheur.setWheelScrollingEnabled(false);

        afficheurReseau.setEnabled(false);
        afficheurReseau.setPreferredSize(new java.awt.Dimension(1600, 900));
        afficheurReseau.setBackground(Color.WHITE);
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
            .addComponent(defilementAfficheur, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1315, Short.MAX_VALUE)
        );
        afficheurCommandesLayout.setVerticalGroup(
            afficheurCommandesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(defilementAfficheur, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
        );
        afficheurCommandes.setLayer(defilementAfficheur, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel9.add(afficheurCommandes, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(1445, 15));
        jPanel8.setLayout(new java.awt.BorderLayout());

        coordonnees.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        coordonnees.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel8.add(coordonnees, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel6.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel10.setPreferredSize(new java.awt.Dimension(130, 4100));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel11.setLayout(new java.awt.BorderLayout());
        jPanel10.add(jPanel11, java.awt.BorderLayout.SOUTH);

        jPanel7.setPreferredSize(new java.awt.Dimension(130, 1000));
        jPanel7.setRequestFocusEnabled(false);
        jPanel7.setVerifyInputWhenFocusTarget(false);

        boutonsRoutier.setAlignmentX(1.0F);
        boutonsRoutier.setAlignmentY(1.0F);
        boutonsRoutier.setOpaque(false);
        boutonsRoutier.setPreferredSize(new java.awt.Dimension(90, 120));
        boutonsRoutier.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        selectionRoutier.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        selectionRoutier.setText("Sélectionner");
        selectionRoutier.setEnabled(false);
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
        constructionTroncon.setEnabled(false);
        constructionTroncon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                constructionTronconActionPerformed(evt);
            }
        });
        boutonsRoutier.add(constructionTroncon);

        editerRoutier.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        editerRoutier.setText("Éditer sélection");
        editerRoutier.setToolTipText("");
        editerRoutier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editerRoutierActionPerformed(evt);
            }
        });
        boutonsRoutier.add(editerRoutier);

        suppressionRoutier.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        suppressionRoutier.setText("Supprimer");
        suppressionRoutier.setEnabled(false);
        suppressionRoutier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressionRoutierActionPerformed(evt);
            }
        });
        boutonsRoutier.add(suppressionRoutier);

        boutonsTransport.setAlignmentX(1.0F);
        boutonsTransport.setAlignmentY(1.0F);
        boutonsTransport.setOpaque(false);
        boutonsTransport.setPreferredSize(new java.awt.Dimension(140, 160));
        boutonsTransport.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        selectionTransport.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        selectionTransport.setText("Sélectionner");
        selectionTransport.setEnabled(false);
        selectionTransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionTransportActionPerformed(evt);
            }
        });
        boutonsTransport.add(selectionTransport);

        ajoutSource.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ajoutSource.setText("Source");
        ajoutSource.setEnabled(false);
        ajoutSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutSourceActionPerformed(evt);
            }
        });
        boutonsTransport.add(ajoutSource);

        ajoutArret.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ajoutArret.setText("Arrêt");
        ajoutArret.setToolTipText("");
        ajoutArret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutArretActionPerformed(evt);
            }
        });
        boutonsTransport.add(ajoutArret);

        ajoutCircuit.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ajoutCircuit.setText("Circuit");
        ajoutCircuit.setEnabled(false);
        ajoutCircuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutCircuitActionPerformed(evt);
            }
        });
        boutonsTransport.add(ajoutCircuit);

        allongerCircuit.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        allongerCircuit.setText("Allonger Circuit");
        allongerCircuit.setToolTipText("");
        allongerCircuit.setEnabled(false);
        allongerCircuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allongerCircuitActionPerformed(evt);
            }
        });
        boutonsTransport.add(allongerCircuit);

        editerTransport.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        editerTransport.setText("Éditer sélection");
        editerTransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editerTransportActionPerformed(evt);
            }
        });
        boutonsTransport.add(editerTransport);

        suppressionTransport.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        suppressionTransport.setText("Supprimer");
        suppressionTransport.setEnabled(false);
        suppressionTransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressionTransportActionPerformed(evt);
            }
        });
        boutonsTransport.add(suppressionTransport);

        boutonsSimulation.setAlignmentX(1.0F);
        boutonsSimulation.setAlignmentY(1.0F);
        boutonsSimulation.setMinimumSize(new java.awt.Dimension(140, 221));
        boutonsSimulation.setOpaque(false);
        boutonsSimulation.setPreferredSize(new java.awt.Dimension(110, 160));
        boutonsSimulation.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        recommancerSimulation.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        recommancerSimulation.setText("Recommencer");
        recommancerSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recommancerSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(recommancerSimulation);

        playPauseSimulation.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        playPauseSimulation.setText("Lancer!");
        playPauseSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playPauseSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(playPauseSimulation);

        arreterSimulation.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        arreterSimulation.setText("Arrêter");
        arreterSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arreterSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(arreterSimulation);

        avancerSimulation.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        avancerSimulation.setText("Avancer X 2");
        avancerSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avancerSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(avancerSimulation);

        ralentirSimulation.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ralentirSimulation.setText("Ralentir / 2");
        ralentirSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ralentirSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(ralentirSimulation);

        jLabel1.setText("Heure :");
        boutonsSimulation.add(jLabel1);
        boutonsSimulation.add(time);

        jLabel2.setText("Facteur multiplicatif :");
        boutonsSimulation.add(jLabel2);
        boutonsSimulation.add(facteurMultiplicatif);

        boutonsSelectionRoutier.setAlignmentX(1.0F);
        boutonsSelectionRoutier.setAlignmentY(1.0F);
        boutonsSelectionRoutier.setOpaque(false);
        boutonsSelectionRoutier.setPreferredSize(new java.awt.Dimension(90, 120));
        boutonsSelectionRoutier.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        jLabel4.setText("Tronçons :");
        boutonsSelectionRoutier.add(jLabel4);

        comboBoxTroncons.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxTroncons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxTronconsActionPerformed(evt);
            }
        });
        boutonsSelectionRoutier.add(comboBoxTroncons);

        jLabel5.setText("Intersections :");
        boutonsSelectionRoutier.add(jLabel5);

        comboBoxIntersections.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxIntersections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxIntersectionsActionPerformed(evt);
            }
        });
        boutonsSelectionRoutier.add(comboBoxIntersections);

        boutonsSelectionTransport.setAlignmentX(1.0F);
        boutonsSelectionTransport.setAlignmentY(1.0F);
        boutonsSelectionTransport.setOpaque(false);
        boutonsSelectionTransport.setPreferredSize(new java.awt.Dimension(90, 120));
        boutonsSelectionTransport.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        jLabel8.setText("Arrêts :");
        boutonsSelectionTransport.add(jLabel8);

        comboBoxArrets.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxArrets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxArretsActionPerformed(evt);
            }
        });
        boutonsSelectionTransport.add(comboBoxArrets);

        jLabel9.setText("Sources :");
        boutonsSelectionTransport.add(jLabel9);

        comboBoxSources.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxSources.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxSourcesActionPerformed(evt);
            }
        });
        boutonsSelectionTransport.add(comboBoxSources);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setText("Sélection Circuit:");
        boutonsSelectionTransport.add(jLabel3);

        comboBoxCircuits.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxCircuits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxCircuitsActionPerformed(evt);
            }
        });
        boutonsSelectionTransport.add(comboBoxCircuits);

        boutonsSelectionSimulation.setAlignmentX(1.0F);
        boutonsSelectionSimulation.setAlignmentY(1.0F);
        boutonsSelectionSimulation.setOpaque(false);
        boutonsSelectionSimulation.setPreferredSize(new java.awt.Dimension(90, 120));
        boutonsSelectionSimulation.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        jLabel10.setText("Autobus :");
        boutonsSelectionSimulation.add(jLabel10);

        comboBoxAutobus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxAutobus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAutobusActionPerformed(evt);
            }
        });
        boutonsSelectionSimulation.add(comboBoxAutobus);

        jLabel11.setText("Pietons");
        boutonsSelectionSimulation.add(jLabel11);

        comboBoxPietons.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        boutonsSelectionSimulation.add(comboBoxPietons);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boutonsSelectionRoutier, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsSelectionTransport, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsSimulation, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(boutonsRoutier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(20, Short.MAX_VALUE)))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(boutonsTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(20, Short.MAX_VALUE)))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsSelectionSimulation, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(681, Short.MAX_VALUE)
                .addComponent(boutonsSelectionRoutier, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                    .addContainerGap(601, Short.MAX_VALUE)
                    .addComponent(boutonsSelectionTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsSimulation, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(481, Short.MAX_VALUE)))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsRoutier, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(641, Short.MAX_VALUE)))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(561, Short.MAX_VALUE)))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                    .addContainerGap(671, Short.MAX_VALUE)
                    .addComponent(boutonsSelectionSimulation, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(22, 22, 22)))
        );

        jPanel10.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel10, java.awt.BorderLayout.EAST);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("SimulatHEURE", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1595, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 873, Short.MAX_VALUE)
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
        boutonsSelectionRoutier.setVisible(true);
        ajoutIntersection.doClick();
    }//GEN-LAST:event_routierActionPerformed

    private void transportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportActionPerformed

        this.setMode(Modes.TRANSPORT);
        boutonsTransport.setVisible(true);
        boutonsSelectionTransport.setVisible(true);
        ajoutArret.doClick();
    }//GEN-LAST:event_transportActionPerformed

    private void besoinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_besoinsActionPerformed
        this.setMode(Modes.BESOINS);
        //boutonsBesoins.setVisible(true);
    }//GEN-LAST:event_besoinsActionPerformed

    private void simulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationActionPerformed
         this.setMode(Modes.SIMULATION);
        routier.setEnabled(false);
        transport.setEnabled(false);
        besoins.setEnabled(false);
        boutonsSelectionSimulation.setVisible(true);
        boutonsSimulation.setVisible(true);
        
    }//GEN-LAST:event_simulationActionPerformed
    private void recommancerSimulation(){
        arreterSimulation();
       playPauseSimulation.doClick();
    }
    private void arreterSimulation(){
        m_timer.stop();
        m_crono.pause();
        m_simulationEstLancer = false;
        m_controleur.arreterSimulation();
    }
    private void alalEditSimulation(){
        EditerSimulation fenetre= new EditerSimulation();{
        fenetre.setMainWindow(m_this);
        fenetre.setResizable(false);
        fenetre.setLocationRelativeTo(null); //pour centrer
       // fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setVisible(true);
        this.setEnabled(false);
}
}
    public void lancerSimulation(){
        m_simulationEstLancer = true;
        boutonsSimulation.setVisible(true);
        m_timer = new Timer(0, new MyTimerActionListener());
        m_crono = new Chronometre();
        m_timer.setDelay(1);
        m_controleur.demarrerSimulation();
        m_crono.start();
        m_timer.start();
    }
    private void afficheurReseauMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMousePressed
        float echelle = afficheurReseau.getEchelle();
        if (SwingUtilities.isLeftMouseButton(evt)) {

            switch (m_mode_courant) {
                case ROUTIER:

                    switch (m_commande_courante) {
                        case SELECTIONNER:
                            if (evt.isControlDown()) {
                                //System.out.println("Pressed");
                                ElementRoutier plusieursEr = m_controleur.selectionnerPlusieursElementRoutier(evt.getX(), evt.getY(), echelle);
                            } else {
                                ElementRoutier er = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle);
                            }

                            break;

                        case INTERSECTION:
                            m_controleur.ajouterIntersection(evt.getX(), evt.getY(), echelle);
                            boolean intersectionAjoutee = true;
                            afficheurReseau.setDimension(intersectionAjoutee);
                            defilementAfficheur.setViewportView(afficheurReseau);
                            miseAjourSelectionIntersectionsAjout();
                            break;

                        case TRONCON:
                            m_controleur.construireTroncon(evt.getX(), evt.getY(), echelle);
                            miseAjourSelectionTronconsAjout();
                            break;

                        default:
                            break;
                    }
                    break;
                case TRANSPORT:
                    switch (m_commande_courante) {

                        case SELECTIONNER:
                            m_controleur.deselectionnerRoutier();
                            ElementTransport et = m_controleur.selectionnerElementTransport(evt.getX(), evt.getY(), echelle);
                            break;

                        case AJOUTERCIRCUIT:
                            if (m_controleur.construireCircuit(evt.getX(), evt.getY(), echelle)) {
                                miseAjourSelectionCircuitsAjout();
                                comboBoxCircuits.setSelectedIndex(comboBoxCircuits.getItemCount() - 1);
                                allongerCircuit.setEnabled(true);
                                allongerCircuit.doClick();
                            }

                            miseAjourSelectionCircuitsAjout();
                            break;

                        case EDITERCIRCUIT:
                            Circuit circ = m_controleur.obtenirCircuitSelectionne();
                            if (circ != null) {
                                m_controleur.editerCircuit(circ, evt.getX(), evt.getY(), echelle);
                            }
                            break;

                        case SOURCE:
                            ElementRoutier elemRoutie = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle);
                            if (elemRoutie != null) {
                                if (elemRoutie.getClass() == Troncon.class) {
                                    m_controleur.ajouterSource(evt.getX(), evt.getY(), echelle);
                                    m_controleur.deselectionnerRoutier();
                                    miseAjourSelectionSourcesAjout();
                                }
                            }
                            break;
                        case ARRET:
                            ElementRoutier elemRoutier = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle);
                            if (elemRoutier != null) {
                                if (elemRoutier.getClass() == Troncon.class || elemRoutier.getClass() == Intersection.class) {
                                    m_controleur.ajouterArret(evt.getX(), evt.getY(), echelle);
                                    m_controleur.deselectionnerRoutier();
                                    miseAjourSelectionArretsAjout();
                                } else if (elemRoutier.getClass() == Intersection.class) {
                                    m_controleur.ajouterArret(evt.getX(), evt.getY(), echelle);
                                    m_controleur.deselectionnerRoutier();
                                    miseAjourSelectionArretsAjout();
                                }
                            }
                            break;

                        default:
                            break;
                    }
                case SIMULATION:

                    break;

                default:
                    break;
            }
        } else if (SwingUtilities.isRightMouseButton(evt)) {
            switch (m_mode_courant) {
                case ROUTIER:
                    m_controleur.deselectionnerTout();
                    ElementRoutier elemRoutier = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle);
                    if (elemRoutier != null) {
                        jPopupMenu1.show(this.afficheurReseau, evt.getX(), evt.getY());
                    }
                    break;

                case TRANSPORT:
                    switch (m_commande_courante) {
                        case AJOUTERCIRCUIT:
                            m_controleur.cancellerCircuit();
                            break;

                        default:
                            break;
                    }
                    m_controleur.deselectionnerTout();
                    ElementTransport elemTransport = m_controleur.selectionnerElementTransport(evt.getX(), evt.getY(), echelle);
                    if (elemTransport != null) {
                        jPopupMenu1.show(this.afficheurReseau, evt.getX(), evt.getY());
                    }
            }
        }
        miseAJourPermissionsBoutons();
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_afficheurReseauMousePressed

    private void miseAjoutAutobusComboBox() {
        comboBoxAutobus.removeAllItems();
        comboBoxAutobus.addItem("Aucun");
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<Autobus> autobuss = circuit.getListeAutobus().listIterator(); autobuss.hasNext();) {
                Autobus autobus = autobuss.next();
                comboBoxAutobus.addItem(autobus.getID());
            }

        }
    }

    public void miseAjourComboBoxTotal() {
        comboBoxCircuits.removeAllItems();
        comboBoxSources.removeAllItems();
        comboBoxArrets.removeAllItems();
        comboBoxIntersections.removeAllItems();
        comboBoxTroncons.removeAllItems();
        comboBoxCircuits.addItem("Aucun");
        comboBoxSources.addItem("Aucun");
        comboBoxArrets.addItem("Aucun");
        comboBoxIntersections.addItem("Aucun");
        comboBoxTroncons.addItem("Aucun");
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            comboBoxCircuits.addItem(circuit.getNom());
        }
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSourceAutobus().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                comboBoxSources.addItem(source.getNom());
            }

        }
        for (ListIterator<Arret> arrets = m_controleur.getTransport().getListArrets().listIterator(); arrets.hasNext();) {
            Arret arret = arrets.next();
            comboBoxArrets.addItem(arret.getNom());
        }
        for (ListIterator<Intersection> intersections = m_controleur.getRoutier().getIntersections().listIterator(); intersections.hasNext();) {
            Intersection intersection = intersections.next();
            comboBoxIntersections.addItem(intersection.getName());
        }
        for (ListIterator<Intersection> intersections = m_controleur.getRoutier().getIntersections().listIterator(); intersections.hasNext();) {
            Intersection intersection = intersections.next();
            for (ListIterator<Troncon> troncons = intersection.getTroncons().listIterator(); troncons.hasNext();) {
                Troncon troncon = troncons.next();
                comboBoxTroncons.addItem(troncon.getNom());
            }
        }
    }

    private void miseAjourSelectionCircuitsAjout() {
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            boolean add = true;
            Circuit circuit = circuits.next();
            String name = circuit.getNom();
            for (int i = 0; i < comboBoxCircuits.getItemCount(); i++) {
                String tmp = (String) comboBoxCircuits.getItemAt(i);
                if (tmp == name) {
                    add = false;
                }
            }
            if (add) {
                comboBoxCircuits.addItem(circuit.getNom());
            }
        }
    }

    private void miseAjourSelectionSourcesAjout() {
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSourceAutobus().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                boolean add = true;

                String name = source.getNom();
                for (int i = 0; i < comboBoxSources.getItemCount(); i++) {
                    String tmp = (String) comboBoxSources.getItemAt(i);
                    if (tmp.equals(name)) {
                        add = false;
                    }
                }
                if (add) {
                    comboBoxSources.addItem(source.getNom());
                }
            }
        }
    }

    private void miseAjourSelectionArretsAjout() {
        for (ListIterator<Arret> arrets = m_controleur.getTransport().getListArrets().listIterator(); arrets.hasNext();) {
            Arret arret = arrets.next();
            boolean add = true;
            String name = arret.getNom();
            for (int i = 0; i < comboBoxArrets.getItemCount(); i++) {
                String tmp = (String) comboBoxArrets.getItemAt(i);
                if (tmp.equals(name)) {
                    add = false;
                }
            }
            if (add) {
                comboBoxArrets.addItem(arret.getNom());
            }
        }
    }

    private void miseAjourSelectionIntersectionsAjout() {
        for (ListIterator<Intersection> intersections = m_controleur.getRoutier().getIntersections().listIterator(); intersections.hasNext();) {
            Intersection intersection = intersections.next();
            boolean add = true;
            String name = intersection.getName();
            for (int i = 0; i < comboBoxIntersections.getItemCount(); i++) {
                String tmp = (String) comboBoxIntersections.getItemAt(i);
                if (tmp == name) {
                    add = false;
                }
            }
            if (add) {
                comboBoxIntersections.addItem(intersection.getName());
            }
        }
    }

    private void miseAjourSelectionAutobusAjout() {
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<Autobus> autobuss = circuit.getListeAutobus().listIterator(); autobuss.hasNext();) {
                Autobus autobus = autobuss.next();
                boolean add = true;
                String name = autobus.getID();
                //System.out.println(name);
                for (int i = 0; i < comboBoxAutobus.getItemCount(); i++) {
                    String tmp = (String) comboBoxAutobus.getItemAt(i);
                    if (tmp == name) {
                        add = false;
                    }
                }
                if (add) {
                    comboBoxAutobus.addItem(autobus.getID());
                }
            }
        }
    }

    private void miseAjourSelectionTronconsAjout() {
        for (ListIterator<Intersection> intersections = m_controleur.getRoutier().getIntersections().listIterator(); intersections.hasNext();) {
            Intersection intersection = intersections.next();
            for (ListIterator<Troncon> troncons = intersection.getTroncons().listIterator(); troncons.hasNext();) {
                Troncon troncon = troncons.next();
                boolean add = true;
                String name = troncon.getNom();
                //System.out.println(name);
                for (int i = 0; i < comboBoxTroncons.getItemCount(); i++) {
                    String tmp = (String) comboBoxTroncons.getItemAt(i);
                    if (tmp == name) {
                        add = false;
                    }
                }
                if (add) {
                    comboBoxTroncons.addItem(troncon.getNom());
                }
            }
        }
    }
    private void afficheurReseauMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_afficheurReseauMouseWheelMoved

        float echelleInitiale = afficheurReseau.getEchelle();
        afficheurReseau.setEchelle(evt.getWheelRotation());
        float rapportEchelles = afficheurReseau.getEchelle() / echelleInitiale;

        int x = defilementAfficheur.getViewport().getViewPosition().x;
        x = (int) (evt.getX() * (rapportEchelles - 1)) + x;

        int y = defilementAfficheur.getViewport().getViewPosition().y;
        y = (int) (evt.getY() * (rapportEchelles - 1)) + y;

        defilementAfficheur.getViewport().setViewPosition(new java.awt.Point(x, y));
        zoom.setText(Integer.toString((int) (afficheurReseau.getEchelle() * 100)) + " %");
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_afficheurReseauMouseWheelMoved

    private void afficheurReseauMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMouseMoved

        float x = evt.getX() / afficheurReseau.getEchelle();
        float y = evt.getY() / afficheurReseau.getEchelle();
        coordonnees.setText(String.format("%.1f", x) + " m  " + String.format("%.1f", y) + " m");
    }//GEN-LAST:event_afficheurReseauMouseMoved

    private void afficheurReseauMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMouseExited
        coordonnees.setText("");
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

        boolean suppressionOK = false;
        switch (m_commande_courante) {
            case SELECTIONNER:
                suppressionOK = m_controleur.supprimerSelectionRoutier();
                if (!suppressionOK){
                    JOptionPane.showMessageDialog(null, "Un élément ne peut pas être supprimé car un élément du réseau de transports en dépend.", "Suppression impossible", JOptionPane.ERROR_MESSAGE);
                }
                break;

            default:
                break;
        }

        //afficheurReseau.setDimension(intersectionSupprimee);
        //defilementAfficheur.setViewportView(afficheurReseau);
        miseAjourComboBoxTotal();
        miseAJourPermissionsBoutons();
        this.afficheurCommandes.repaint();
    }

    private void editage() {
        switch (m_mode_courant) {
            case ROUTIER:
                LinkedList<ElementRoutier> elementsRoutiersSelectionnes = m_controleur.getElementsSelectionnesRoutier();
                if (elementsRoutiersSelectionnes.getFirst() == null) return;
                ElementRoutier elemRoutier = elementsRoutiersSelectionnes.getFirst();

                //ouvrir une fenetre contextuelle qui agit sur elem, dependamment du type d'elem
                if (elemRoutier.getClass() == Intersection.class) {
                    EditerIntersection fenetre = new EditerIntersection();
                    fenetre.setIntersection((Intersection) elemRoutier, this);
                    fenetre.setResizable(false);
                    fenetre.setLocationRelativeTo(null); //pour centrer
                    fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fenetre.setVisible(true);
                } else if (elemRoutier.getClass() == Troncon.class) {
                    EditerTroncon fenetre = new EditerTroncon();
                    fenetre.setTroncon((Troncon) elemRoutier, this);
                    fenetre.setResizable(false);
                    fenetre.setLocationRelativeTo(null); //pour centrer
                    fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fenetre.setVisible(true);
                }
                break;

            case TRANSPORT:
                LinkedList<ElementTransport> elementsTransportSelectionnes = m_controleur.getElementsSelectionnesTransport();
                if (elementsTransportSelectionnes.getFirst() == null) return;
                ElementTransport elemTransport = elementsTransportSelectionnes.getFirst();

                if (elemTransport.getClass() == SourceAutobus.class) {
                    EditerSource fenetre = new EditerSource();
                    fenetre.setSource((SourceAutobus) elemTransport, this);
                    fenetre.setResizable(false);
                    fenetre.setLocationRelativeTo(null); //pour centrer
                    fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fenetre.setVisible(true);
                } else if (elemTransport.getClass() == Arret.class) {
                    EditerArret fenetre = new EditerArret();
                    fenetre.setArret((Arret) elemTransport, this);
                    fenetre.setResizable(false);
                    fenetre.setLocationRelativeTo(null); //pour centrer
                    fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fenetre.setVisible(true);
                } else if (elemTransport.getClass() == Circuit.class) {
                    EditerCircuit fenetre = new EditerCircuit();
                    fenetre.setCircuit((Circuit) elemTransport, this);
                    fenetre.setResizable(false);
                    fenetre.setLocationRelativeTo(null); //pour centrer
                    fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fenetre.setVisible(true);
                }
        }
    }
    private void editerClicDroitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editerClicDroitActionPerformed
        editage();
    }//GEN-LAST:event_editerClicDroitActionPerformed

    private void supprimerClicDroitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerClicDroitActionPerformed
        switch (m_mode_courant) {
            case ROUTIER:
                //LinkedList<ElementRoutier> elementsRoutiersSelectionnes = m_controleur.getElementsSelectionnesRoutier();
                //assert(elementsRoutiersSelectionnes.size() == 1);
                //ElementRoutier elemR = elementsRoutiersSelectionnes.getFirst();

                selectionRoutier.doClick();
                suppressionRoutier.doClick();
                break;

            case TRANSPORT:
                LinkedList<ElementTransport> elementsTransportSelectionnes = m_controleur.getElementsSelectionnesTransport();
                assert (elementsTransportSelectionnes.size() == 1);
                ElementTransport elemT = elementsTransportSelectionnes.getFirst();
                Boolean suppr = m_controleur.supprimerSelectionTransport();
                selectionTransport.doClick();
                suppressionTransport.doClick();

                break;

            case BESOINS:
                break;

            case SIMULATION:
                break;
        }
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_supprimerClicDroitActionPerformed

    private void selectionTransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionTransportActionPerformed
        this.setCommande(Commandes.SELECTIONNER);
    }//GEN-LAST:event_selectionTransportActionPerformed

    private void ajoutArretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutArretActionPerformed

        this.setCommande(Commandes.ARRET);
        m_controleur.deselectionnerRoutier();
        m_controleur.deselectionnerTransport();

        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_ajoutArretActionPerformed

    private void ajoutCircuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutCircuitActionPerformed

        this.setCommande(Commandes.AJOUTERCIRCUIT);
        m_controleur.deselectionnerRoutier();
        m_controleur.deselectionnerTransport();

        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_ajoutCircuitActionPerformed

    private void suppressionTransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppressionTransportActionPerformed
        boolean elementTransportSupprime = false;
        switch (m_commande_courante) {
            case SELECTIONNER:
                elementTransportSupprime = m_controleur.supprimerSelectionTransport();
                if (!elementTransportSupprime) {
                    JOptionPane.showMessageDialog(null, "Un arrêt ne peut pas être supprimé car un circuit en dépend", "Suppression impossible", JOptionPane.ERROR_MESSAGE);
                }
                break;

            default:
                break;
        }

        //afficheurReseau.setDimension(intersectionSupprimee);
        //defilementAfficheur.setViewportView(afficheurReseau);   
        miseAjourComboBoxTotal();
        miseAJourPermissionsBoutons();
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_suppressionTransportActionPerformed

    private void ajoutSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutSourceActionPerformed
        m_controleur.cancellerCircuit();
        this.afficheurCommandes.repaint();
        this.setCommande(Commandes.SOURCE);
    }//GEN-LAST:event_ajoutSourceActionPerformed

    private void afficheurReseauMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_afficheurReseauMouseClicked

    private void annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_annulerActionPerformed

    private void retablirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retablirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_retablirActionPerformed

    private void comboBoxCircuitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxCircuitsActionPerformed
        this.setCommande(Commandes.SELECTIONNER);
        int index = comboBoxCircuits.getSelectedIndex();
        String name = (String) comboBoxCircuits.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            if (circuit.getNom().equals(name)) {
                for (PaireArretTrajet ArretTrajet : circuit.getListeArretTrajet()) {
                    ElementTransport arret = ArretTrajet.getArret();
                    if (!arret.estSelectionne()) {
                        arret.changerStatutSelection();
                    }
                }
                circuit.changerStatutSelection();
                break;
            }
        }
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_comboBoxCircuitsActionPerformed

    private void comboBoxIntersectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxIntersectionsActionPerformed
        this.setCommande(Commandes.SELECTIONNER);
        int index = comboBoxIntersections.getSelectedIndex();
        String name = (String) comboBoxIntersections.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Intersection> intersections = m_controleur.getRoutier().getIntersections().listIterator(); intersections.hasNext();) {
            Intersection intersection = intersections.next();
            if (intersection.getName().equals(name)) {
                intersection.changerStatutSelection();
                break;
            }
        }
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_comboBoxIntersectionsActionPerformed

    private void allongerCircuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allongerCircuitActionPerformed
        if(m_commande_courante != Commandes.EDITERCIRCUIT){
            m_controleur.cancellerCircuit();
            this.afficheurCommandes.repaint();
        }
        this.setCommande(Commandes.EDITERCIRCUIT);
    }//GEN-LAST:event_allongerCircuitActionPerformed

    private void comboBoxTronconsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxTronconsActionPerformed
        this.setCommande(Commandes.SELECTIONNER);
        int index = comboBoxTroncons.getSelectedIndex();
        String name = (String) comboBoxTroncons.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Intersection> intersections = m_controleur.getRoutier().getIntersections().listIterator(); intersections.hasNext();) {
            Intersection intersection = intersections.next();
            for (ListIterator<Troncon> troncons = intersection.getTroncons().listIterator(); troncons.hasNext();) {
                Troncon troncon = troncons.next();
                if (troncon.getNom().equals(name)) {
                    troncon.changerStatutSelection();
                    break;
                }
            }
            this.afficheurCommandes.repaint();
        }
    }//GEN-LAST:event_comboBoxTronconsActionPerformed

    private void playPauseSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playPauseSimulationActionPerformed
        if(!m_simulationEstLancer ){
            alalEditSimulation();
            m_simulationEstLancer = true;
            playPauseSimulation.setText("Pause");
        }
        else if (m_crono.estEnPause()){
            m_crono.start();
            playPauseSimulation.setText("Pause");
        }
        else{
            m_crono.pause();
            playPauseSimulation.setText("Relancer!");
        }
    }//GEN-LAST:event_playPauseSimulationActionPerformed

    private void avancerSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avancerSimulationActionPerformed
        m_crono.avancerX2();
    }//GEN-LAST:event_avancerSimulationActionPerformed

    private void ralentirSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ralentirSimulationActionPerformed
        m_crono.ralentirX2();
    }//GEN-LAST:event_ralentirSimulationActionPerformed

    private void comboBoxAutobusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxAutobusActionPerformed

        /*int index = comboBoxAutobus.getSelectedIndex();
         String name =(String) comboBoxAutobus.getItemAt(index);
         m_controleur.deselectionnerTout();
         for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator() ;circuits.hasNext() ; ){
         Circuit circuit = circuits.next();
         for (ListIterator<Autobus> autobuss = circuit.getListeAutobus().listIterator() ;autobuss.hasNext() ; ){
         Autobus autobus = autobuss.next();
         if (autobus.getID().equals(name)){
         autobusachangerStatutSelection();
         break;
         }
         }
         this.afficheurCommandes.repaint();
         */
    }//GEN-LAST:event_comboBoxAutobusActionPerformed

    private void comboBoxSourcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSourcesActionPerformed
        this.setCommande(Commandes.SELECTIONNER);
        int index = comboBoxSources.getSelectedIndex();
        String name = (String) comboBoxSources.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSourceAutobus().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                if (source.getNom().equals(name)) {
                    source.changerStatutSelection();
                    break;
                }
            }
        }
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_comboBoxSourcesActionPerformed

    private void comboBoxArretsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxArretsActionPerformed
        this.setCommande(Commandes.SELECTIONNER);
        int index = comboBoxArrets.getSelectedIndex();
        String name = (String) comboBoxArrets.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Arret> arrets = m_controleur.getTransport().getListArrets().listIterator(); arrets.hasNext();) {
            Arret arret = arrets.next();
            if (arret.getNom().equals(name)) {
                arret.changerStatutSelection();
                break;
            }
        }
        this.afficheurCommandes.repaint();
    }//GEN-LAST:event_comboBoxArretsActionPerformed

    private void editerRoutierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editerRoutierActionPerformed
        editage();
    }//GEN-LAST:event_editerRoutierActionPerformed

    private void editerTransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editerTransportActionPerformed
        editage();
    }//GEN-LAST:event_editerTransportActionPerformed

    private void recommancerSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recommancerSimulationActionPerformed
        playPauseSimulation.setText("Lancer!");
        recommancerSimulation();
    }//GEN-LAST:event_recommancerSimulationActionPerformed

    private void arreterSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arreterSimulationActionPerformed
       arreterSimulation();
       routier.setEnabled(true);
       transport.setEnabled(true);
       //besoins.setEnabled(true);
    }//GEN-LAST:event_arreterSimulationActionPerformed

    /**
     * @param args the command line arguments
     */
    private void changeLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("javax.swing.plaf.nimbus.NimbusLookAndFeel".equals(info.getClassName())) {
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

    }

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

    public void setMode(Modes p_mode) {
        this.m_mode_courant = p_mode;
        boutonsRoutier.setVisible(false);
        boutonsSelectionRoutier.setVisible(false);
        boutonsTransport.setVisible(false);
        boutonsSelectionTransport.setVisible(false);
        boutonsSimulation.setVisible(false);
        boutonsSelectionSimulation.setVisible(false);
        m_controleur.deselectionnerTout();
        this.afficheurCommandes.repaint();
    }

    public void setCommande(Commandes p_commande) {
        this.m_commande_courante = p_commande;
    }

    public javax.swing.JScrollPane getDefilementAfficheur() {
        return defilementAfficheur;
    }

    public void miseAJourPermissionsBoutons() {
        switch (m_mode_courant) {
            case ROUTIER:

                if (comboBoxIntersections.getItemCount() > 2) {
                    selectionRoutier.setEnabled(true);
                    suppressionRoutier.setEnabled(true);
                    constructionTroncon.setEnabled(true);
                } else if (comboBoxIntersections.getItemCount() > 1) {
                    selectionRoutier.setEnabled(true);
                    suppressionRoutier.setEnabled(true);
                    constructionTroncon.setEnabled(false);
                } else {
                    selectionRoutier.setEnabled(false);
                    suppressionRoutier.setEnabled(false);
                    constructionTroncon.setEnabled(false);
                    ajoutIntersection.doClick();
                }

                if (comboBoxTroncons.getItemCount() > 1) {
                    transport.setEnabled(true);
                } else {
                    transport.setEnabled(false);
                }

                break;

            case TRANSPORT:
                if (comboBoxArrets.getItemCount() > 2) {
                    selectionTransport.setEnabled(true);
                    suppressionTransport.setEnabled(true);
                    ajoutCircuit.setEnabled(true);
                } else if (comboBoxArrets.getItemCount() > 1) {
                    selectionTransport.setEnabled(true);
                    suppressionTransport.setEnabled(true);
                    ajoutCircuit.setEnabled(false);
                } else {
                    selectionTransport.setEnabled(false);
                    suppressionTransport.setEnabled(false);
                    ajoutCircuit.setEnabled(false);
                    ajoutArret.doClick();
                }

                if (comboBoxCircuits.getItemCount() > 1) {
                    allongerCircuit.setEnabled(true);
                    ajoutSource.setEnabled(true);
                } else {
                    allongerCircuit.setEnabled(false);
                    ajoutSource.setEnabled(false);
                }

                if (comboBoxSources.getItemCount() > 1) {
                    simulation.setEnabled(true);
                } else {
                    simulation.setEnabled(false);
                }

                break;

            default:
                break;

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane afficheurCommandes;
    private GUI.AfficheurReseau afficheurReseau;
    private javax.swing.JToggleButton ajoutArret;
    private javax.swing.JToggleButton ajoutCircuit;
    private javax.swing.JToggleButton ajoutIntersection;
    private javax.swing.JToggleButton ajoutSource;
    private javax.swing.JButton allongerCircuit;
    private javax.swing.JButton annuler;
    private javax.swing.JButton arreterSimulation;
    private javax.swing.JButton avancerSimulation;
    private javax.swing.JToggleButton besoins;
    private javax.swing.JPanel boutonModes;
    private javax.swing.JPanel boutonsRoutier;
    private javax.swing.JPanel boutonsSelectionRoutier;
    private javax.swing.JPanel boutonsSelectionSimulation;
    private javax.swing.JPanel boutonsSelectionTransport;
    private javax.swing.JPanel boutonsSimulation;
    private javax.swing.JPanel boutonsTransport;
    private javax.swing.JComboBox<String> comboBoxArrets;
    private javax.swing.JComboBox<String> comboBoxAutobus;
    private javax.swing.JComboBox<String> comboBoxCircuits;
    private javax.swing.JComboBox<String> comboBoxIntersections;
    private javax.swing.JComboBox comboBoxPietons;
    private javax.swing.JComboBox<String> comboBoxSources;
    private javax.swing.JComboBox<String> comboBoxTroncons;
    private javax.swing.JToggleButton constructionTroncon;
    private javax.swing.JLabel coordonnees;
    private javax.swing.JScrollPane defilementAfficheur;
    private javax.swing.JMenuItem editerClicDroit;
    private javax.swing.JToggleButton editerRoutier;
    private javax.swing.JToggleButton editerTransport;
    private javax.swing.JLabel facteurMultiplicatif;
    private javax.swing.JMenu fichier;
    private javax.swing.ButtonGroup groupeModes;
    private javax.swing.ButtonGroup groupeRoutier;
    private javax.swing.ButtonGroup groupeTransport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menu;
    private javax.swing.JToggleButton playPauseSimulation;
    private javax.swing.JMenuItem quitter;
    private javax.swing.JButton ralentirSimulation;
    private javax.swing.JButton recommancerSimulation;
    private javax.swing.JButton retablir;
    private javax.swing.JToggleButton routier;
    private javax.swing.JToggleButton selectionRoutier;
    private javax.swing.JToggleButton selectionTransport;
    private javax.swing.JToggleButton simulation;
    private javax.swing.JButton suppressionRoutier;
    private javax.swing.JButton suppressionTransport;
    private javax.swing.JMenuItem supprimerClicDroit;
    private javax.swing.JLabel time;
    private javax.swing.JToggleButton transport;
    private javax.swing.JLabel zoom;
    private javax.swing.JLabel zoomTitre;
    // End of variables declaration//GEN-END:variables
}
