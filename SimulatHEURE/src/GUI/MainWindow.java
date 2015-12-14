 package GUI;

import Domaine.BesoinsTransport.ElementBesoins;
import Domaine.BesoinsTransport.Individu;
import Domaine.BesoinsTransport.Itineraire;
import Domaine.Simulatheure;
import Domaine.Simulatheure.Mode;
import Domaine.Simulatheure.Commande;
import Domaine.Statistiques.StatistiqueBesoin;
import Domaine.Statistiques.StatistiquesGeneral;
 
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import Domaine.Utilitaire.*;
import Domaine.ReseauRoutier.*;
import Domaine.ReseauTransport.*;
import java.awt.Color;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import javax.swing.JFrame;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Vinny
 */
public class MainWindow extends javax.swing.JFrame {
    
    public Simulatheure m_controleur;
    
    public Mode m_modeCourant;
    public Commande m_commandeCourante;
    
    private Timer m_timer;
    private Chronometre m_crono;
    private MainWindow m_this = this; //l33t
    public double m_tempsDebutSimulation;
    public double m_precision = -1;
    public double m_tempsFinSimulation;
    private boolean m_simulationEstLancer = false;
    private LinkedList<StatistiquesGeneral> m_statistiques = new LinkedList<>();
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;
    boolean m_skipAffichage = false;
    public File m_fileChoosed;
    

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        changeLookAndFeel();
        m_controleur = new Simulatheure();
        initComponents();
        routier.doClick();
        initialiserPanels();
        updateIconBoutons();
        this.afficheurReseau.setDimension(false);
    }

    private void updateIconBoutons(){
        //boutons general
        Icon iconLoad = new ImageIcon("src/Icons/load.png");
        Icon iconSave= new ImageIcon("src/Icons/save.png");
        Icon iconUndo = new ImageIcon("src/Icons/undo.png");
        Icon iconRedo = new ImageIcon("src/Icons/redo.png");
        Icon iconZoomIn = new ImageIcon("src/Icons/zoomIn.png");
        Icon iconZoomOut = new ImageIcon("src/Icons/zoomOut.png");
        loadButton.setIcon(iconLoad);
        saveButton.setIcon(iconSave);
        undoButton.setIcon(iconUndo);
        redoButton.setIcon(iconRedo);
        zoomInButton.setIcon(iconZoomIn);
        zoomOutButton.setIcon(iconZoomOut);
        
        
        //boutons simulation
        Icon iconPlay = new ImageIcon("src/Icons/play.png");
        Icon iconStop = new ImageIcon("src/Icons/stop.png");
        Icon iconRecommancer = new ImageIcon("src/Icons/reload.png");
        Icon iconRalentir = new ImageIcon("src/Icons/rewind.png");
        Icon iconAvancer = new ImageIcon("src/Icons/forward.png");
        playPauseSimulation.setIcon(iconPlay);
        arreterSimulation.setIcon(iconStop);
        recommancerSimulation.setIcon(iconRecommancer);
        ralentirSimulation.setIcon(iconRalentir);
        avancerSimulation.setIcon(iconAvancer);
        
        //Panel Color
        Color panelColor =  new Color(186,190,198);
        jPanel13.setBackground(panelColor);
        jPanel15.setBackground(panelColor);
    }

    void save() {
        this.m_controleur.getHistorique().viderApresReseauCourant();
        int indexCurseur = this.m_controleur.getHistorique().getCurseur().nextIndex();
        this.m_controleur.getHistorique().clearCurseur();
        this.m_controleur.setGabarit(null);
        try
        {
            FileOutputStream fileOut = new FileOutputStream (m_fileChoosed.getAbsolutePath());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(m_controleur);
            out.close();
            fileOut.close();
            System.out.println("Ça marche !");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        this.m_controleur.getHistorique().setCurseur(indexCurseur);    
    }                                           

    class MyTimerActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            double tmp = m_crono.getTempsDebut();
            boolean finSimulation = false;
            double deltatT = m_crono.getDeltatT();
            if(m_skipAffichage){
                m_skipAffichage = false;
                deltatT = (m_tempsFinSimulation - m_tempsDebutSimulation)/1000;
                finSimulation = true;
                Date itemDate = new Date((long)(m_tempsDebutSimulation + (deltatT)*1000));
                String itemDateStr = new SimpleDateFormat("HH:mm:ss").format(itemDate);
                 time.setText(itemDateStr);
            }
            else if ((m_tempsFinSimulation - m_tempsDebutSimulation) <= m_crono.getTempsDebut()*1000) {
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
            double tmpD = deltatT;
            int nbExe = 1;
            while(tmpD > 10){
                nbExe++;
                tmpD = deltatT/nbExe;
            }
            for(int i =0; i < nbExe;i++){
                m_controleur.rafraichirSimulation(new Temps(tmpD));
            }
            facteurMultiplicatif.setText("X" + m_crono.getFacteurVitesse());
            if (deltatT != 0) {
                miseAjoutAutobusPietonsComboBox();
            }
            m_this.afficheurReseau.repaint();
            if (finSimulation) {
                
                Icon warnIcon = new ImageIcon("src/Icons/play.png");
                playPauseSimulation.setIcon(warnIcon);
                arreterSimulation.doClick();
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
        jButton2 = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        loadButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        zoomInButton = new javax.swing.JButton();
        zoomOutButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        panneauModes = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        boutonModes = new javax.swing.JPanel();
        routier = new javax.swing.JToggleButton();
        transport = new javax.swing.JToggleButton();
        besoins = new javax.swing.JToggleButton();
        simulation = new javax.swing.JToggleButton();
        jPanel5 = new javax.swing.JPanel();
        zoomTitre = new javax.swing.JLabel();
        zoom = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        panneauCommandes = new javax.swing.JPanel();
        boutonsRoutier = new javax.swing.JPanel();
        selectionRoutier = new javax.swing.JToggleButton();
        suppressionRoutier = new javax.swing.JButton();
        ajoutIntersection = new javax.swing.JToggleButton();
        constructionTroncon = new javax.swing.JToggleButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        boutonsTransport = new javax.swing.JPanel();
        selectionTransport = new javax.swing.JToggleButton();
        suppressionTransport = new javax.swing.JButton();
        ajoutArret = new javax.swing.JToggleButton();
        ajoutCircuit = new javax.swing.JToggleButton();
        checkBoxDijkstra = new javax.swing.JCheckBox();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        boutonsBesoins = new javax.swing.JPanel();
        selectionBesoins = new javax.swing.JToggleButton();
        suppressionBesoins = new javax.swing.JButton();
        ajoutBesoin = new javax.swing.JToggleButton();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        boutonsSimulation = new javax.swing.JPanel();
        playPauseSimulation = new javax.swing.JToggleButton();
        arreterSimulation = new javax.swing.JButton();
        recommancerSimulation = new javax.swing.JButton();
        ralentirSimulation = new javax.swing.JButton();
        avancerSimulation = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        facteurMultiplicatif = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        defilementAfficheur = new javax.swing.JScrollPane();
        afficheurReseau = new GUI.AfficheurReseau(this);
        jPanel8 = new javax.swing.JPanel();
        coordonnees = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        boutonsSelectionTransport = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        comboBoxArrets = new javax.swing.JComboBox<String>();
        jLabel9 = new javax.swing.JLabel();
        comboBoxSources = new javax.swing.JComboBox<String>();
        jLabel3 = new javax.swing.JLabel();
        comboBoxCircuits = new javax.swing.JComboBox<String>();
        boutonsSelectionBesoins = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        comboBoxBesoins = new javax.swing.JComboBox<String>();
        boutonsSelectionSimulation = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        comboBoxAutobus = new javax.swing.JComboBox<String>();
        jLabel11 = new javax.swing.JLabel();
        comboBoxPietons = new javax.swing.JComboBox<String>();
        boutonsSelectionRoutier = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        comboBoxTroncons = new javax.swing.JComboBox<String>();
        jLabel5 = new javax.swing.JLabel();
        comboBoxIntersections = new javax.swing.JComboBox<String>();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        panelArret1 = new GUI.PanelArret();
        panelCircuit1 = new GUI.PanelCircuit();
        panelIntersection1 = new GUI.PanelIntersection();
        panelSourceAutobus1 = new GUI.PanelSourceAutobus();
        panelTroncon1 = new GUI.PanelTroncon();
        panelItineraire1 = new GUI.PanelItineraire();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        comboBoxStat = new javax.swing.JComboBox<String>();
        jLabel6 = new javax.swing.JLabel();
        menu = new javax.swing.JMenuBar();
        fichier = new javax.swing.JMenu();
        annuler = new javax.swing.JMenuItem();
        retablir = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        sauvegarder = new javax.swing.JMenuItem();
        charger = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        quitter = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        chargerGabarit = new javax.swing.JMenuItem();
        toggleGabarit = new javax.swing.JCheckBoxMenuItem();

        groupeModes.add(routier);
        groupeModes.add(transport);
        groupeModes.add(besoins);
        groupeModes.add(simulation);

        groupeModes.add(selectionRoutier);
        groupeModes.add(ajoutIntersection);
        groupeModes.add(constructionTroncon);

        groupeModes.add(selectionTransport);
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

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 600));
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        mainPanel.setMinimumSize(new java.awt.Dimension(1200, 600));
        mainPanel.setPreferredSize(new java.awt.Dimension(1600, 900));
        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(1600, 35));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel15.setPreferredSize(new java.awt.Dimension(1600, 20));

        jPanel13.setPreferredSize(new java.awt.Dimension(1600, 35));
        jPanel13.setLayout(new java.awt.GridLayout(1, 0));

        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });
        jPanel13.add(loadButton);

        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel13.add(saveButton);

        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });
        jPanel13.add(undoButton);

        redoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoButtonActionPerformed(evt);
            }
        });
        jPanel13.add(redoButton);

        zoomInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInButtonActionPerformed(evt);
            }
        });
        jPanel13.add(zoomInButton);

        zoomOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutButtonActionPerformed(evt);
            }
        });
        jPanel13.add(zoomOutButton);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1368, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel15, java.awt.BorderLayout.CENTER);

        mainPanel.add(jPanel11, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        panneauModes.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(150, 400));
        jPanel1.setLayout(new java.awt.BorderLayout());

        boutonModes.setPreferredSize(new java.awt.Dimension(150, 400));
        boutonModes.setRequestFocusEnabled(false);
        boutonModes.setLayout(new java.awt.GridLayout(4, 1));

        routier.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        routier.setText("Réseau routier");
        routier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routierActionPerformed(evt);
            }
        });
        boutonModes.add(routier);

        transport.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        transport.setText("Réseau transport");
        transport.setEnabled(false);
        transport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportActionPerformed(evt);
            }
        });
        boutonModes.add(transport);

        besoins.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        besoins.setText("Besoins transport");
        besoins.setEnabled(false);
        besoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                besoinsActionPerformed(evt);
            }
        });
        boutonModes.add(besoins);

        simulation.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        simulation.setText("Simulation");
        simulation.setEnabled(false);
        simulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationActionPerformed(evt);
            }
        });
        boutonModes.add(simulation);

        jPanel1.add(boutonModes, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.GridLayout(1, 2));

        zoomTitre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        zoomTitre.setText("Zoom : ");
        jPanel5.add(zoomTitre);

        zoom.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        zoom.setText("100 %");
        zoom.setRequestFocusEnabled(false);
        jPanel5.add(zoom);

        jPanel1.add(jPanel5, java.awt.BorderLayout.SOUTH);

        panneauModes.add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel3.add(panneauModes, java.awt.BorderLayout.WEST);

        jPanel6.setLayout(new java.awt.BorderLayout());

        panneauCommandes.setMinimumSize(new java.awt.Dimension(1200, 35));
        panneauCommandes.setPreferredSize(new java.awt.Dimension(1200, 33));
        panneauCommandes.setRequestFocusEnabled(false);
        panneauCommandes.setVerifyInputWhenFocusTarget(false);
        panneauCommandes.setLayout(new java.awt.CardLayout());

        boutonsRoutier.setMinimumSize(new java.awt.Dimension(1200, 35));
        boutonsRoutier.setPreferredSize(new java.awt.Dimension(1200, 35));
        boutonsRoutier.setLayout(new java.awt.GridLayout(1, 0));

        selectionRoutier.setText("Sélectionner");
        selectionRoutier.setEnabled(false);
        selectionRoutier.setMaximumSize(new java.awt.Dimension(32767, 32767));
        selectionRoutier.setMinimumSize(new java.awt.Dimension(100, 35));
        selectionRoutier.setPreferredSize(new java.awt.Dimension(100, 35));
        selectionRoutier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionRoutierActionPerformed(evt);
            }
        });
        boutonsRoutier.add(selectionRoutier);

        suppressionRoutier.setText("Supprimer");
        suppressionRoutier.setMaximumSize(new java.awt.Dimension(32767, 32767));
        suppressionRoutier.setMinimumSize(new java.awt.Dimension(100, 35));
        suppressionRoutier.setPreferredSize(new java.awt.Dimension(100, 35));
        suppressionRoutier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressionRoutierActionPerformed(evt);
            }
        });
        boutonsRoutier.add(suppressionRoutier);

        ajoutIntersection.setText("Intersection");
        ajoutIntersection.setMaximumSize(new java.awt.Dimension(32767, 32767));
        ajoutIntersection.setMinimumSize(new java.awt.Dimension(100, 35));
        ajoutIntersection.setPreferredSize(new java.awt.Dimension(100, 35));
        ajoutIntersection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutIntersectionActionPerformed(evt);
            }
        });
        boutonsRoutier.add(ajoutIntersection);

        constructionTroncon.setText("Tronçon");
        constructionTroncon.setMaximumSize(new java.awt.Dimension(32767, 32767));
        constructionTroncon.setMinimumSize(new java.awt.Dimension(100, 35));
        constructionTroncon.setPreferredSize(new java.awt.Dimension(100, 35));
        constructionTroncon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                constructionTronconActionPerformed(evt);
            }
        });
        boutonsRoutier.add(constructionTroncon);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsRoutier.add(jPanel25);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsRoutier.add(jPanel26);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsRoutier.add(jPanel27);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsRoutier.add(jPanel28);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsRoutier.add(jPanel31);

        panneauCommandes.add(boutonsRoutier, "card2");

        boutonsTransport.setAlignmentX(1.0F);
        boutonsTransport.setAlignmentY(1.0F);
        boutonsTransport.setMinimumSize(new java.awt.Dimension(1200, 35));
        boutonsTransport.setPreferredSize(new java.awt.Dimension(1200, 35));
        boutonsTransport.setLayout(new java.awt.GridLayout(1, 0));

        selectionTransport.setText("Sélectionner");
        selectionTransport.setEnabled(false);
        selectionTransport.setPreferredSize(new java.awt.Dimension(100, 35));
        selectionTransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionTransportActionPerformed(evt);
            }
        });
        boutonsTransport.add(selectionTransport);

        suppressionTransport.setText("Supprimer");
        suppressionTransport.setEnabled(false);
        suppressionTransport.setPreferredSize(new java.awt.Dimension(100, 35));
        suppressionTransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressionTransportActionPerformed(evt);
            }
        });
        boutonsTransport.add(suppressionTransport);

        ajoutArret.setText("Arrêt");
        ajoutArret.setToolTipText("");
        ajoutArret.setPreferredSize(new java.awt.Dimension(100, 35));
        ajoutArret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutArretActionPerformed(evt);
            }
        });
        boutonsTransport.add(ajoutArret);

        ajoutCircuit.setText("Circuit");
        ajoutCircuit.setPreferredSize(new java.awt.Dimension(100, 35));
        ajoutCircuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutCircuitActionPerformed(evt);
            }
        });
        boutonsTransport.add(ajoutCircuit);

        checkBoxDijkstra.setSelected(true);
        checkBoxDijkstra.setText("Optimiser");
        checkBoxDijkstra.setPreferredSize(new java.awt.Dimension(100, 35));
        checkBoxDijkstra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxDijkstraActionPerformed(evt);
            }
        });
        boutonsTransport.add(checkBoxDijkstra);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsTransport.add(jPanel21);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsTransport.add(jPanel22);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsTransport.add(jPanel23);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsTransport.add(jPanel30);

        panneauCommandes.add(boutonsTransport, "card3");

        boutonsBesoins.setAlignmentX(1.0F);
        boutonsBesoins.setAlignmentY(1.0F);
        boutonsBesoins.setMinimumSize(new java.awt.Dimension(1200, 35));
        boutonsBesoins.setPreferredSize(new java.awt.Dimension(1200, 35));
        boutonsBesoins.setLayout(new java.awt.GridLayout(1, 0));

        selectionBesoins.setText("Sélectionner");
        selectionBesoins.setEnabled(false);
        selectionBesoins.setPreferredSize(new java.awt.Dimension(100, 35));
        selectionBesoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionBesoinsActionPerformed(evt);
            }
        });
        boutonsBesoins.add(selectionBesoins);

        suppressionBesoins.setText("Supprimer");
        suppressionBesoins.setPreferredSize(new java.awt.Dimension(100, 35));
        suppressionBesoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressionBesoinsActionPerformed(evt);
            }
        });
        boutonsBesoins.add(suppressionBesoins);

        ajoutBesoin.setText("Itinéraire");
        ajoutBesoin.setToolTipText("");
        ajoutBesoin.setPreferredSize(new java.awt.Dimension(100, 35));
        ajoutBesoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutBesoinActionPerformed(evt);
            }
        });
        boutonsBesoins.add(ajoutBesoin);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsBesoins.add(jPanel17);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsBesoins.add(jPanel18);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsBesoins.add(jPanel19);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsBesoins.add(jPanel20);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsBesoins.add(jPanel24);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsBesoins.add(jPanel29);

        panneauCommandes.add(boutonsBesoins, "card3");

        boutonsSimulation.setMinimumSize(new java.awt.Dimension(1200, 35));
        boutonsSimulation.setPreferredSize(new java.awt.Dimension(1200, 35));
        boutonsSimulation.setLayout(new java.awt.GridLayout(1, 0));

        playPauseSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playPauseSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(playPauseSimulation);

        arreterSimulation.setEnabled(false);
        arreterSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arreterSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(arreterSimulation);

        recommancerSimulation.setEnabled(false);
        recommancerSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recommancerSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(recommancerSimulation);

        ralentirSimulation.setEnabled(false);
        ralentirSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ralentirSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(ralentirSimulation);

        avancerSimulation.setEnabled(false);
        avancerSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avancerSimulationActionPerformed(evt);
            }
        });
        boutonsSimulation.add(avancerSimulation);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Heure : ");
        boutonsSimulation.add(jLabel1);
        boutonsSimulation.add(time);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Vitesse : ");
        boutonsSimulation.add(jLabel2);
        boutonsSimulation.add(facteurMultiplicatif);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 126, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        boutonsSimulation.add(jPanel16);

        panneauCommandes.add(boutonsSimulation, "card4");

        jPanel6.add(panneauCommandes, java.awt.BorderLayout.NORTH);

        jPanel9.setLayout(new java.awt.BorderLayout());

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
            public void mousePressed(java.awt.event.MouseEvent evt) {
                afficheurReseauMousePressed(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                afficheurReseauMouseExited(evt);
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

        jPanel9.add(defilementAfficheur, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(1445, 15));
        jPanel8.setLayout(new java.awt.BorderLayout());

        coordonnees.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        coordonnees.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel8.add(coordonnees, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel6.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel12.setPreferredSize(new java.awt.Dimension(180, 400));
        jPanel12.setRequestFocusEnabled(false);
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel14.setPreferredSize(new java.awt.Dimension(150, 250));

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

        jLabel3.setText("Circuits :");
        jLabel3.setToolTipText("");
        boutonsSelectionTransport.add(jLabel3);

        comboBoxCircuits.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxCircuits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxCircuitsActionPerformed(evt);
            }
        });
        boutonsSelectionTransport.add(comboBoxCircuits);

        boutonsSelectionBesoins.setAlignmentX(1.0F);
        boutonsSelectionBesoins.setAlignmentY(1.0F);
        boutonsSelectionBesoins.setOpaque(false);
        boutonsSelectionBesoins.setPreferredSize(new java.awt.Dimension(90, 120));
        boutonsSelectionBesoins.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        jLabel7.setText("Itinéraires :");
        jLabel7.setToolTipText("");
        boutonsSelectionBesoins.add(jLabel7);

        comboBoxBesoins.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxBesoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxBesoinsActionPerformed(evt);
            }
        });
        boutonsSelectionBesoins.add(comboBoxBesoins);

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

        jLabel11.setText("Piétons :");
        boutonsSelectionSimulation.add(jLabel11);

        comboBoxPietons.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        comboBoxPietons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxPietonsActionPerformed(evt);
            }
        });
        boutonsSelectionSimulation.add(comboBoxPietons);

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

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boutonsSelectionTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsSelectionSimulation, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsSelectionRoutier, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonsSelectionBesoins, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(boutonsSelectionTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                    .addContainerGap(92, Short.MAX_VALUE)
                    .addComponent(boutonsSelectionSimulation, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                    .addContainerGap(92, Short.MAX_VALUE)
                    .addComponent(boutonsSelectionRoutier, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addGap(132, 132, 132)
                    .addComponent(boutonsSelectionBesoins, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(52, Short.MAX_VALUE)))
        );

        jPanel12.add(jPanel14, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 624));
        jPanel2.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel7, "card7");

        panelArret1.setBorder(javax.swing.BorderFactory.createTitledBorder("Arrêt"));
        jPanel2.add(panelArret1, "card2");

        panelCircuit1.setBorder(javax.swing.BorderFactory.createTitledBorder("Circuit"));
        jPanel2.add(panelCircuit1, "card3");

        panelIntersection1.setBorder(javax.swing.BorderFactory.createTitledBorder("Intersection"));
        jPanel2.add(panelIntersection1, "card4");

        panelSourceAutobus1.setBorder(javax.swing.BorderFactory.createTitledBorder("Source d'autobus"));
        jPanel2.add(panelSourceAutobus1, "card5");

        panelTroncon1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tronçon"));
        jPanel2.add(panelTroncon1, "card6");

        panelItineraire1.setBorder(javax.swing.BorderFactory.createTitledBorder("Itinéraire"));
        jPanel2.add(panelItineraire1, "card8");

        jPanel12.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel12, java.awt.BorderLayout.EAST);

        jTabbedPane1.addTab("SimulatHEURE", jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Itinéraire", "Temps minimum", "Temps moyen", "Temps maximum"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setEnabled(false);
        jScrollPane1.setViewportView(jTable1);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel10.setPreferredSize(new java.awt.Dimension(50, 50));

        comboBoxStat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxStatActionPerformed(evt);
            }
        });

        jLabel6.setText("Simulation :");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel6)
                .addGap(78, 78, 78)
                .addComponent(comboBoxStat, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1225, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxStat, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        jPanel4.add(jPanel10, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Statistiques", jPanel4);

        mainPanel.add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext().setAccessibleName("SimulatHeure");

        getContentPane().add(mainPanel);

        fichier.setText("Fichier");

        annuler.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        annuler.setText("Annuler");
        annuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerActionPerformed(evt);
            }
        });
        fichier.add(annuler);

        retablir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        retablir.setText("Rétablir");
        retablir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retablirActionPerformed(evt);
            }
        });
        fichier.add(retablir);
        fichier.add(jSeparator1);

        sauvegarder.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        sauvegarder.setText("Sauvegarder");
        sauvegarder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sauvegarderActionPerformed(evt);
            }
        });
        fichier.add(sauvegarder);

        charger.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        charger.setText("Charger");
        charger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargerActionPerformed(evt);
            }
        });
        fichier.add(charger);
        fichier.add(jSeparator2);

        jMenuItem1.setText("Crédits");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fichier.add(jMenuItem1);

        quitter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitter.setText("Quitter");
        quitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitterActionPerformed(evt);
            }
        });
        fichier.add(quitter);

        menu.add(fichier);

        jMenu1.setText("Gabarit");

        chargerGabarit.setText("Charger un gabarit");
        chargerGabarit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargerGabaritActionPerformed(evt);
            }
        });
        jMenu1.add(chargerGabarit);

        toggleGabarit.setSelected(true);
        toggleGabarit.setText("Afficher le gabarit");
        toggleGabarit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleGabaritActionPerformed(evt);
            }
        });
        jMenu1.add(toggleGabarit);

        menu.add(jMenu1);

        setJMenuBar(menu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void quitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitterActionPerformed
        
        System.exit(0);
    }//GEN-LAST:event_quitterActionPerformed

    private void routierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routierActionPerformed

        this.setMode(Mode.ROUTIER);
        boutonsRoutier.setVisible(true);
        boutonsSelectionRoutier.setVisible(true);
        constructionTroncon.doClick();
    }//GEN-LAST:event_routierActionPerformed

    private void transportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportActionPerformed

        this.setMode(Mode.TRANSPORT);
        boutonsTransport.setVisible(true);
        boutonsSelectionTransport.setVisible(true);
        ajoutCircuit.doClick();
    }//GEN-LAST:event_transportActionPerformed

    private void besoinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_besoinsActionPerformed
        
        this.setMode(Mode.BESOINS);
        boutonsBesoins.setVisible(true);
        boutonsSelectionBesoins.setVisible(true);
        ajoutBesoin.doClick();
    }//GEN-LAST:event_besoinsActionPerformed

    private void simulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationActionPerformed
        
        this.setMode(Mode.SIMULATION);
        boutonsSelectionSimulation.setVisible(true);
        boutonsSimulation.setVisible(true);   
    }//GEN-LAST:event_simulationActionPerformed

    private void agrandirAfficheur()
    {
        boolean intersectionAjoutee = true;
        afficheurReseau.setDimension(intersectionAjoutee);
        defilementAfficheur.setViewportView(afficheurReseau);
    }
    
    private void afficheurReseauMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMousePressed
        float echelle = afficheurReseau.getEchelle();
        if (SwingUtilities.isLeftMouseButton(evt)) {

            switch (m_modeCourant) {
                case ROUTIER:

                    switch (m_commandeCourante) {
                        case SELECTIONNER:
                            ElementRoutier elemRoutier = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle, evt.isControlDown());
                            
                            break;

                        case INTERSECTION:
                            m_controleur.ajouterIntersection(evt.getX(), evt.getY(), echelle);
                            agrandirAfficheur();
                            miseAjourSelectionIntersectionsAjout();
                            
                            
                            break;

                        case TRONCON:
                            try
                            {
                                m_controleur.construireTroncon(evt.getX(), evt.getY(), echelle);
                                miseAjourSelectionTronconsAjout();
                            }
                            catch (IllegalArgumentException e)
                            {
                                JOptionPane.showMessageDialog(null, e.getMessage(), e.getCause().getMessage(), JOptionPane.WARNING_MESSAGE);
                            }
                            
                            miseAjourSelectionIntersectionsAjout();
                            agrandirAfficheur();

                            break;

                        default:
                            break;
                    }
                    break;
                case TRANSPORT:
                    switch (m_commandeCourante) {

                        case SELECTIONNER:
                            m_controleur.deselectionnerRoutier();
                            ElementTransport et = m_controleur.selectionnerElementTransport(evt.getX(), evt.getY(), echelle, evt.isControlDown());
                            
                            break;

                        case CIRCUIT:
                            Boolean circuitConstruit = false;
                            try {
                                circuitConstruit = m_controleur.construireCircuit(evt.getX(), evt.getY(), echelle);
                            }
                            catch(IllegalArgumentException e) {
                                JOptionPane.showMessageDialog(null, e.getMessage(), e.getCause().getMessage(), JOptionPane.WARNING_MESSAGE);
                            }
                            if (circuitConstruit)
                            {
                                miseAjourSelectionCircuitsAjout();
                                panelCircuit1.afficheInfo((Circuit)m_controleur.getTransport().getPileSelection().getDessus());
                            }
                            
                            miseAjourSelectionArretsAjout();
                            break;

                        case SOURCEAUTOBUS:
                            ElementTransport elemSelectionne = m_controleur.getTransport().getPileSelection().getDessus();
                            
                            if (elemSelectionne == null || elemSelectionne.getClass() != Circuit.class) break;
                            
                            ElementTransport elementTransport = m_controleur.obtenirElementTransport(evt.getX(), evt.getY(), echelle);
                            if (elementTransport!= null && elementTransport.getClass()!=SourceAutobus.class) {
                                m_controleur.ajouterSource(evt.getX(), evt.getY(), echelle);
                                m_controleur.deselectionnerRoutier();
                                miseAjourSelectionSourcesAjout();
                                m_controleur.getTransport().getPileSelection().ajouter(elemSelectionne);
                                
                              }
                            break;
                        case ARRET:
                            if (m_controleur.ajouterArret(evt.getX(), evt.getY(), echelle))
                                miseAjourSelectionArretsAjout();
                            
                            break;

                        default:
                            break;
                    }
                    break;
                case BESOINS:
                    switch (m_commandeCourante) {

                        case SELECTIONNER:
                            m_controleur.deselectionnerRoutier();
                            ElementBesoins eb = m_controleur.selectionnerElementBesoins(evt.getX(), evt.getY(), echelle, evt.isControlDown());

                            break;

                        case BESOIN:
                            Boolean besoinConstruit = false;
                            try {
                                besoinConstruit = m_controleur.construireItineraire(evt.getX(), evt.getY(), echelle);
                            }
                            catch(IllegalArgumentException e) {
                                JOptionPane.showMessageDialog(null, e.getMessage(), e.getCause().getMessage(), JOptionPane.WARNING_MESSAGE);
                            }

                            miseAjourSelectionBeoinssAjout();
                            miseAjourSelectionArretsAjout();
                            break;
                        default:
                            break;
                    }
                    break;
                    
                case SIMULATION:

                    return;

                default:
                    break;
            }
        } else if (SwingUtilities.isRightMouseButton(evt)) {
            switch (m_modeCourant) {
                case ROUTIER:
                    m_controleur.deselectionnerTout();
                    ElementRoutier elemRoutier = m_controleur.selectionnerElementRoutier(evt.getX(), evt.getY(), echelle, false);
                    
                    if (elemRoutier != null) {
                        jPopupMenu1.show(this.afficheurReseau, evt.getX(), evt.getY());
                    }
                    break;

                case TRANSPORT:
                    m_controleur.deselectionnerTout();
                    ElementTransport elemTransport = m_controleur.selectionnerElementTransport(evt.getX(), evt.getY(), echelle, false);
                    if (elemTransport != null) {
                        jPopupMenu1.show(this.afficheurReseau, evt.getX(), evt.getY());
                    }
                    break;
                
                case BESOINS:
                    m_controleur.deselectionnerTout();
                    ElementBesoins elemBesoins = m_controleur.selectionnerElementBesoins(evt.getX(), evt.getY(), echelle, evt.isControlDown());
                    if (elemBesoins != null) {
                        jPopupMenu1.show(this.afficheurReseau, evt.getX(), evt.getY());
                    }
                    break;
                case SIMULATION:
                    return;
            }
        }
        miseAJourPanels();
        miseAJourPermissionsBoutons();
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_afficheurReseauMousePressed
    private void miseAjourStatistiqueApresArret(){
          m_controleur.miseAjoutStatistiqueApresArret();
          /*StatistiquesGeneral st = new StatistiquesGeneral(m_controleur.getStatistique());
          st.miseAjourApresFin();
          m_statistiques.add(st);
          */
          m_statistiques = m_controleur.getListStatistique();
    }
    private void miseAjoutComboBoxStat(){
        miseAjourStatistiqueApresArret();
        comboBoxStat.removeAllItems();
        for (int i = 0 ; i < m_statistiques.size(); i++){
            comboBoxStat.addItem(Integer.toString(i+1));
        }
    }
    
    private void miseAjourTableauStatistique(int p_stat){
        int conteur = 0;
        int x = 0;
        for (StatistiquesGeneral stat : m_statistiques){
            conteur++;
                if (conteur == p_stat) {
                    ListIterator<StatistiqueBesoin> statis = stat.getListeStatistiqueBesoin().listIterator();
                    String header[] = new String[]{"Nom de l'itineraire", "Temps minimum", "Temps moyen", 
                        "Temps maximum", "Temps moyen d'attente", "Nombre de piétons"}; 
                    DefaultTableModel model = new DefaultTableModel(header,stat.getListeStatistiqueBesoin().size());
                    while(statis.hasNext()){
                        StatistiqueBesoin besoin = statis.next();  
                        jTable1.setModel(model);
                        String moyenne = Double.toString(besoin.getMoyenne());
                        String min = Double.toString(besoin.getminTempsDeplacement());
                        String max = Double.toString(besoin.getmaxTempsDeplacement());
                        String attenteArret = Double.toString(besoin.getMoyenneAttente());
                        String echantillon = Integer.toString(besoin.getNbIteration());
                        
                        
                        jTable1.setValueAt(besoin.getNameItineraire(), x, 0);
                        jTable1.setValueAt(min.concat("  min(s)"), x, 1);
                        jTable1.setValueAt(moyenne.concat("  min(s)"), x, 2);
                        jTable1.setValueAt(max.concat("  min(s)"), x, 3);
                        jTable1.setValueAt(attenteArret.concat("  min(s)"), x, 4);
                        jTable1.setValueAt(echantillon.concat("  piétons"), x, 5);
                         x++;
                    }
                }
            }   
    }
    
    private void miseAjoutAutobusPietonsComboBox() {
        comboBoxAutobus.removeAllItems();
        comboBoxAutobus.addItem("Aucun");
        comboBoxPietons.removeAllItems();
        comboBoxPietons.addItem("Aucun");
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<Autobus> autobuss = circuit.getListeAutobus().listIterator(); autobuss.hasNext();) {
                Autobus autobus = autobuss.next();
                comboBoxAutobus.addItem(autobus.getID());
            }

        }
        for (ListIterator<Itineraire> itineraires = m_controleur.getBesoins().getListItineraire().listIterator(); itineraires.hasNext();) {
            Itineraire itineraire = itineraires.next();
            for (ListIterator<Individu> pietons = itineraire.getListIndividu().listIterator(); pietons.hasNext();) {
                Individu pieton = pietons.next();
                comboBoxPietons.addItem(pieton.getNom());
            }

        }
    }

    public void miseAjourComboBoxTotal() {
        Commande cmdTemp = this.m_commandeCourante;  // wtf la commande change..
        
        comboBoxCircuits.removeAllItems();
        comboBoxSources.removeAllItems();
        comboBoxArrets.removeAllItems();
        comboBoxIntersections.removeAllItems();
        comboBoxTroncons.removeAllItems();
        comboBoxBesoins.removeAllItems();
        comboBoxCircuits.addItem("Aucun");
        comboBoxSources.addItem("Aucun");
        comboBoxArrets.addItem("Aucun");
        comboBoxIntersections.addItem("Aucun");
        comboBoxTroncons.addItem("Aucun");
        comboBoxBesoins.addItem("Aucun");
        
        
        m_commandeCourante = cmdTemp;  // wtf la commande change..
        
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            comboBoxCircuits.addItem(circuit.getNom());
        }
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSources().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                comboBoxSources.addItem(source.getNom());
            }

        }
        for (ListIterator<Arret> arrets = m_controleur.getTransport().getListeArrets().listIterator(); arrets.hasNext();) {
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
        for (ListIterator<Itineraire> itineraires = m_controleur.getBesoins().getListItineraire().listIterator(); itineraires.hasNext();) {
            Itineraire itineraire = itineraires.next();
            comboBoxBesoins.addItem(itineraire.getNom());
        }
    }

    private void miseAjourSelectionBeoinssAjout() {
       for (ListIterator<Itineraire> itineraires = m_controleur.getBesoins().getListItineraire().listIterator(); itineraires.hasNext();) {
            Itineraire itineraire = itineraires.next();
            boolean add = true;
            String name = itineraire.getNom();
            for (int i = 0; i < comboBoxBesoins.getItemCount(); i++) {
                String tmp = comboBoxBesoins.getItemAt(i);
                if (tmp == name) {
                    add = false;
                }
            }
            if (add) {
                comboBoxBesoins.addItem(itineraire.getNom());
            }
        }
    }
    
    private void miseAjourSelectionCircuitsAjout() {
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            boolean add = true;
            Circuit circuit = circuits.next();
            String name = circuit.getNom();
            for (int i = 0; i < comboBoxCircuits.getItemCount(); i++) {
                String tmp = comboBoxCircuits.getItemAt(i);
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
            for (ListIterator<SourceAutobus> sources = circuit.getListeSources().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                boolean add = true;

                String name = source.getNom();
                for (int i = 0; i < comboBoxSources.getItemCount(); i++) {
                    String tmp = comboBoxSources.getItemAt(i);
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
        for (ListIterator<Arret> arrets = m_controleur.getTransport().getListeArrets().listIterator(); arrets.hasNext();) {
            Arret arret = arrets.next();
            boolean add = true;
            String name = arret.getNom();
            for (int i = 0; i < comboBoxArrets.getItemCount(); i++) {
                String tmp = comboBoxArrets.getItemAt(i);
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
                String tmp = comboBoxIntersections.getItemAt(i);
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
                for (int i = 0; i < comboBoxAutobus.getItemCount(); i++) {
                    String tmp = comboBoxAutobus.getItemAt(i);
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
                for (int i = 0; i < comboBoxTroncons.getItemCount(); i++) {
                    String tmp = comboBoxTroncons.getItemAt(i);
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
        zoom(evt.getWheelRotation(),evt.getX(),evt.getY());
    }//GEN-LAST:event_afficheurReseauMouseWheelMoved

    private void zoom(int intensite, int evtx, int evty){
        float echelleInitiale = afficheurReseau.getEchelle();
        afficheurReseau.setEchelle(intensite);
        float rapportEchelles = afficheurReseau.getEchelle() / echelleInitiale;

        int x = defilementAfficheur.getViewport().getViewPosition().x;
        x = (int) (evtx * (rapportEchelles - 1)) + x;

        int y = defilementAfficheur.getViewport().getViewPosition().y;
        y = (int) (evty * (rapportEchelles - 1)) + y;

        defilementAfficheur.getViewport().setViewPosition(new java.awt.Point(x, y));
        zoom.setText(Integer.toString((int) (afficheurReseau.getEchelle() * 100)) + " %");
        
        this.afficheurReseau.repaint();
    }
    
    private void afficheurReseauMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMouseMoved

        float x = evt.getX() / afficheurReseau.getEchelle();
        float y = evt.getY() / afficheurReseau.getEchelle();
        coordonnees.setText(String.format("%.3f", Intersection.ECHELLE*x/1000) + " km  " + String.format("%.3f", Intersection.ECHELLE*y/1000) + " km");
        
        this.curseurSurElement(evt);
        
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_afficheurReseauMouseMoved

    private void curseurSurElement(java.awt.event.MouseEvent evt){
        float echelle = afficheurReseau.getEchelle();
        switch(this.m_modeCourant){
            case ROUTIER:
                m_controleur.setElementCurseurRoutier(evt, echelle);
                break;
                
            case TRANSPORT:
                m_controleur.setElementCurseurTransport(evt, echelle);
                break;
                    
            case BESOINS:
                m_controleur.setElementCurseurBesoins(evt, echelle);
                break;
                        
            case SIMULATION:
                return;
        }
    }
    
    private void afficheurReseauMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afficheurReseauMouseExited
        coordonnees.setText("");
        
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_afficheurReseauMouseExited

    private void selectionRoutierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionRoutierActionPerformed
        m_controleur.deselectionnerRoutier();
        this.setCommande(Commande.SELECTIONNER);
    }//GEN-LAST:event_selectionRoutierActionPerformed

    private void ajoutIntersectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutIntersectionActionPerformed

        this.setCommande(Commande.INTERSECTION);
        m_controleur.deselectionnerRoutier();

        this.afficheurReseau.repaint();
    }//GEN-LAST:event_ajoutIntersectionActionPerformed

    private void constructionTronconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_constructionTronconActionPerformed

        this.setCommande(Commande.TRONCON);
        m_controleur.deselectionnerRoutier();

        this.afficheurReseau.repaint();
    }//GEN-LAST:event_constructionTronconActionPerformed

    private void suppressionRoutierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppressionRoutierActionPerformed
        boolean suppressionOK = false;
        suppressionOK = m_controleur.supprimerSelectionRoutier();
        if (!suppressionOK){
            JOptionPane.showMessageDialog(null, "L'élément ne peut pas être supprimé car un élément d'un réseau supérieur en dépend.", "Suppression impossible", JOptionPane.ERROR_MESSAGE);
        }

        miseAjourComboBoxTotal();
        miseAJourPermissionsBoutons();
        miseAJourPanels();
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_suppressionRoutierActionPerformed
    
    private void editerElement() {
        switch (m_modeCourant) {
            case ROUTIER:
                LinkedList<ElementRoutier> elementsRoutiersSelectionnes = m_controleur.getElementsSelectionnesRoutier();
                if (elementsRoutiersSelectionnes == null || elementsRoutiersSelectionnes.size() == 0) return;
                ElementRoutier elemRoutier = elementsRoutiersSelectionnes.getLast();

                //ouvrir une fenetre contextuelle qui agit sur elem, dependamment du type d'elem
                if (elemRoutier.getClass() == Intersection.class) {
                    EditerIntersection fenetre = new EditerIntersection();
                    fenetre.setIntersection((Intersection) elemRoutier, this);
                    fenetre.setResizable(false);
                    fenetre.setLocationRelativeTo(null); //pour centrer
                    fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fenetre.setVisible(true);
                } else if (elemRoutier.getClass() == Troncon.class) {
                    double ancienTempsMoyen = ((Troncon)elemRoutier).getDistribution().getTempsMoyen().getTemps();
                    EditerTroncon fenetre = new EditerTroncon();
                    fenetre.getTroncon((Troncon) elemRoutier, this);
                    fenetre.setResizable(false);
                    fenetre.setLocationRelativeTo(null); //pour centrer
                    fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fenetre.setVisible(true);
                }
                break;

            case TRANSPORT:
                LinkedList<ElementTransport> elementsTransportSelectionnes = m_controleur.getElementsSelectionnesTransport();
                if (elementsTransportSelectionnes == null || elementsTransportSelectionnes.size() == 0) return;
                ElementTransport elemTransport = elementsTransportSelectionnes.getLast();

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
            case BESOINS:
                LinkedList<ElementBesoins> elementsBesoinsSelectionnes = m_controleur.getElementsSelectionnesBesoins();
                if (elementsBesoinsSelectionnes == null || elementsBesoinsSelectionnes.size() == 0) return;
                ElementBesoins elemBesoins = elementsBesoinsSelectionnes.getLast();

                //ouvrir une fenetre contextuelle qui agit sur elem, dependamment du type d'elem
                if (elemBesoins.getClass() == Itineraire.class) {
                    EditerItineraire fenetre = new EditerItineraire();
                    fenetre.setItineraire((Itineraire) elemBesoins, this);
                    fenetre.setResizable(false);
                    fenetre.setLocationRelativeTo(null); //pour centrer
                    fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fenetre.setVisible(true);
                }
                break;
        }
    }
    private void editerClicDroitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editerClicDroitActionPerformed
        editerElement();
    }//GEN-LAST:event_editerClicDroitActionPerformed

    public void supprimerElementPanel(){
        suppression();
    }
    
    private void supprimerClicDroitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerClicDroitActionPerformed
        suppression();
    }//GEN-LAST:event_supprimerClicDroitActionPerformed

    private void suppression(){
        switch (m_modeCourant) {
            case ROUTIER:
                suppressionRoutier.doClick();
                break;

            case TRANSPORT:
                suppressionTransport.doClick();
                break;

            case BESOINS:
                suppressionBesoins.doClick();
                break;

            case SIMULATION:
                break;
        }
        this.afficheurReseau.repaint();
    }
    
    private void initialiserPanels(){
        panelIntersection1.setMainWindow(m_this);
        panelTroncon1.setMainWindow(m_this);
        panelArret1.setMainWindow(m_this);
        panelCircuit1.setMainWindow(m_this);
        panelSourceAutobus1.setMainWindow(m_this);
        panelItineraire1.setMainWindow(m_this);
        disparaitrePanels();
    }
    
    public void miseAJourPanels(){
        disparaitrePanels();
        switch(m_modeCourant){
            case ROUTIER:
                ElementRoutier er = m_controleur.getRoutier().getPileSelection().getDessus();
                if(er!=null)
                    afficherPanelRoutier(er);
                break;
            
            case TRANSPORT:
                ElementTransport et = m_controleur.getTransport().getPileSelection().getDessus();
                if(et!=null)
                    afficherPanelTransport(et);
                break;
            
            case BESOINS:
                ElementBesoins eb = m_controleur.getBesoins().getPileSelection().getDessus();
                if(eb!=null)
                    afficherPanelBesoins(eb);
                break;
                
            default:
                break;
        }
    }
    
    private void disparaitrePanels(){
        jPanel7.setVisible(false);
        panelTroncon1.setVisible(false);
        panelIntersection1.setVisible(false);
        panelArret1.setVisible(false);
        panelCircuit1.setVisible(false);
        panelSourceAutobus1.setVisible(false);
        panelItineraire1.setVisible(false);
    }
    
    private void afficherPanelRoutier(ElementRoutier elemRoutier){
        
        if (elemRoutier != null){
            disparaitrePanels();
            if (elemRoutier.getClass() == Troncon.class) {
                panelTroncon1.setVisible(true);
                panelTroncon1.afficheInfo((Troncon) elemRoutier);
            }
            else if (elemRoutier.getClass() == Intersection.class) {
                panelIntersection1.setVisible(true);
                panelIntersection1.afficheInfo((Intersection) elemRoutier);
            }
        }
    }
    
    private void afficherPanelTransport(ElementTransport elemTransport){
        
        if (elemTransport != null){
            disparaitrePanels();
            if (elemTransport.getClass() == Arret.class) {
                panelArret1.setVisible(true);
                panelArret1.afficheInfo((Arret) elemTransport);
            }
            else if (elemTransport.getClass() == SourceAutobus.class) {
                panelSourceAutobus1.setVisible(true);
                SourceAutobus src = (SourceAutobus) elemTransport;
                Circuit circ = m_controleur.obtenirCircuitDeSource(src);
                panelSourceAutobus1.afficheInfo(src, circ);
            }
            else if (elemTransport.getClass() == Circuit.class){
                panelCircuit1.setVisible(true);
                panelCircuit1.afficheInfo((Circuit) elemTransport);
            }
        }
    }
    
    private void afficherPanelBesoins(ElementBesoins elemBesoins){
        if (elemBesoins != null){
            disparaitrePanels();
            if (elemBesoins.getClass() == Itineraire.class) {
                panelItineraire1.setVisible(true);
                panelItineraire1.afficheInfo((Itineraire) elemBesoins);
            }
        }
    }
    
    private void selectionTransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionTransportActionPerformed
        this.setCommande(Commande.SELECTIONNER);
    }//GEN-LAST:event_selectionTransportActionPerformed

    private void ajoutArretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutArretActionPerformed

        this.setCommande(Commande.ARRET);
        m_controleur.deselectionnerTout();

        this.afficheurReseau.repaint();
    }//GEN-LAST:event_ajoutArretActionPerformed

    private void ajoutCircuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutCircuitActionPerformed

        this.setCommande(Commande.CIRCUIT);
        //m_controleur.deselectionnerTout();

        this.afficheurReseau.repaint();
    }//GEN-LAST:event_ajoutCircuitActionPerformed

    private void suppressionTransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppressionTransportActionPerformed
        boolean elementTransportSupprime = true;

        try{
            elementTransportSupprime = m_controleur.supprimerSelectionTransport();
        }
        catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), e.getCause().getMessage(), JOptionPane.WARNING_MESSAGE);
        }
        if(!elementTransportSupprime){
            JOptionPane.showMessageDialog(null, "Un arrêt ne peut pas être supprimé car un circuit en dépend", "Suppression impossible", JOptionPane.WARNING_MESSAGE);
        }
        
        miseAjourComboBoxTotal();
        miseAJourPermissionsBoutons();
        miseAJourPanels();
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_suppressionTransportActionPerformed

        
    public void ajoutSource(){
        m_controleur.deselectionnerRoutier();
        this.afficheurReseau.repaint();
        this.setCommande(Commande.SOURCEAUTOBUS);
    }    
    private void alalEditSimulation() {
        EditerSimulation fenetre= new EditerSimulation();{
        fenetre.setMainWindow(m_this);
        fenetre.setResizable(false);
        fenetre.setLocationRelativeTo(null); //pour centrer
       // fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setVisible(true);
        this.setEnabled(false);
        }
    }
    
    public void lancerSimulation() {
        m_simulationEstLancer = true;
        boutonsSimulation.setVisible(true);
        m_timer = new Timer(0, new MyTimerActionListener());
        m_crono = new Chronometre();
        m_timer.setDelay(1);
        m_controleur.demarrerSimulation();
        m_crono.start();
        m_timer.start();
    }
    
    private void playPauseSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playPauseSimulationActionPerformed
        routier.setEnabled(false);
        transport.setEnabled(false);
        besoins.setEnabled(false);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
        
        recommancerSimulation.setEnabled(true);
        arreterSimulation.setEnabled(true);
        avancerSimulation.setEnabled(true);
        ralentirSimulation.setEnabled(true);
        
        if(!m_simulationEstLancer ){
            alalEditSimulation();
            m_simulationEstLancer = true;
            Icon iconPause = new ImageIcon("src/Icons/pause.png");
            playPauseSimulation.setIcon(iconPause);
        }
        else if (m_crono.estEnPause()){
            m_crono.start();
            Icon iconPause = new ImageIcon("src/Icons/pause.png");
            playPauseSimulation.setIcon(iconPause);
        }
        else{
            m_crono.pause();
            Icon iconPlay = new ImageIcon("src/Icons/play.png"
                    + "");
            playPauseSimulation.setIcon(iconPlay);
        }
    }//GEN-LAST:event_playPauseSimulationActionPerformed

    private void avancerSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avancerSimulationActionPerformed
        m_crono.avancerX2();
    }//GEN-LAST:event_avancerSimulationActionPerformed

    private void ralentirSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ralentirSimulationActionPerformed
        m_crono.ralentirX2();
    }//GEN-LAST:event_ralentirSimulationActionPerformed

    private void comboBoxAutobusActionPerformed(java.awt.event.ActionEvent evt) {                                                

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
         this.afficheurReseau.repaint();
         */
    }                                               

    private void recommancerSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recommancerSimulationActionPerformed
        Icon warnIcon = new ImageIcon("src/Icons/play.png");
        playPauseSimulation.setIcon(warnIcon);
        arreterSimulation.doClick();
        playPauseSimulation.doClick();
    }//GEN-LAST:event_recommancerSimulationActionPerformed

    private void arreterSimulation(){
        m_timer.stop();
        m_crono.pause();
        m_simulationEstLancer = false;
        m_controleur.arreterSimulation();
        miseAjoutComboBoxStat();
        m_precision = -1;
    }
    
    private void arreterSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arreterSimulationActionPerformed
        arreterSimulation();
        Icon warnIcon = new ImageIcon("src/Icons/play.png");
        playPauseSimulation.setIcon(warnIcon);
        playPauseSimulation.setSelected(false);
        
        routier.setEnabled(true);
        transport.setEnabled(true);
        besoins.setEnabled(true);
        undoButton.setEnabled(m_controleur.getHistorique().peutAnnuler());
        redoButton.setEnabled(m_controleur.getHistorique().peutRetablir());
        
        recommancerSimulation.setEnabled(false);
        arreterSimulation.setEnabled(false);
        avancerSimulation.setEnabled(false);
        ralentirSimulation.setEnabled(false);
        
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_arreterSimulationActionPerformed

    private void checkBoxDijkstraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxDijkstraActionPerformed
        m_controleur.changerStatutDijkstra();
    }//GEN-LAST:event_checkBoxDijkstraActionPerformed

    private void comboBoxIntersectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxIntersectionsActionPerformed
        this.setCommande(Commande.SELECTIONNER);
        int index = comboBoxIntersections.getSelectedIndex();
        String name = comboBoxIntersections.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Intersection> intersections = m_controleur.getRoutier().getIntersections().listIterator(); intersections.hasNext();) {
            Intersection intersection = intersections.next();
            if (intersection.getName().equals(name)) {
                m_controleur.getRoutier().getPileSelection().ajouter(intersection);
                afficherPanelRoutier(intersection);
                break;
            }
        }
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_comboBoxIntersectionsActionPerformed

    private void comboBoxTronconsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxTronconsActionPerformed
        this.setCommande(Commande.SELECTIONNER);
        int index = comboBoxTroncons.getSelectedIndex();
        String name = comboBoxTroncons.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Intersection> intersections = m_controleur.getRoutier().getIntersections().listIterator(); intersections.hasNext();) {
            Intersection intersection = intersections.next();
            for (ListIterator<Troncon> troncons = intersection.getTroncons().listIterator(); troncons.hasNext();) {
                Troncon troncon = troncons.next();
                if (troncon.getNom().equals(name)) {
                    m_controleur.getRoutier().getPileSelection().ajouter(troncon);
                    afficherPanelRoutier(troncon);
                    break;
                }
            }
            this.afficheurReseau.repaint();
        }
    }//GEN-LAST:event_comboBoxTronconsActionPerformed

    private void comboBoxCircuitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxCircuitsActionPerformed
        this.setCommande(Commande.SELECTIONNER);
        int index = comboBoxCircuits.getSelectedIndex();
        String name = comboBoxCircuits.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            if (circuit.getNom().equals(name)) {
                for (PaireArretTrajet ArretTrajet : circuit.getListeArretTrajet()) {
                    ElementTransport arret = ArretTrajet.getArret();
                    m_controleur.getTransport().getPileSelection().ajouter(arret);
                }
                m_controleur.getTransport().getPileSelection().ajouter(circuit);
                afficherPanelTransport(circuit);
                break;
            }
        }
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_comboBoxCircuitsActionPerformed

    private void comboBoxSourcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSourcesActionPerformed
        this.setCommande(Commande.SELECTIONNER);
        int index = comboBoxSources.getSelectedIndex();
        String name = comboBoxSources.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Circuit> circuits = m_controleur.getTransport().getListeCircuits().listIterator(); circuits.hasNext();) {
            Circuit circuit = circuits.next();
            for (ListIterator<SourceAutobus> sources = circuit.getListeSources().listIterator(); sources.hasNext();) {
                SourceAutobus source = sources.next();
                if (source.getNom().equals(name)) {
                    m_controleur.getTransport().getPileSelection().ajouter(source);
                    afficherPanelTransport(source);
                    break;
                }
            }
        }
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_comboBoxSourcesActionPerformed

    private void comboBoxArretsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxArretsActionPerformed
        this.setCommande(Commande.SELECTIONNER);
        int index = comboBoxArrets.getSelectedIndex();
        String name = comboBoxArrets.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Arret> arrets = m_controleur.getTransport().getListeArrets().listIterator(); arrets.hasNext();) {
            Arret arret = arrets.next();
            if (arret.getNom().equals(name)) {
                m_controleur.getTransport().getPileSelection().ajouter(arret);
                afficherPanelTransport(arret);
                break;
            }
        }
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_comboBoxArretsActionPerformed

    private void comboBoxPietonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxPietonsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxPietonsActionPerformed

    private void selectionBesoinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionBesoinsActionPerformed
        this.setCommande(Commande.SELECTIONNER);
    }//GEN-LAST:event_selectionBesoinsActionPerformed

    private void ajoutBesoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutBesoinActionPerformed
        this.setCommande(Commande.BESOIN);
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_ajoutBesoinActionPerformed

    private void suppressionBesoinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppressionBesoinsActionPerformed
        boolean suppressionOK = false;
        suppressionOK = m_controleur.supprimerSelectionBesoins();
        
        miseAjourComboBoxTotal();
        miseAJourPermissionsBoutons();
        miseAJourPanels();
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_suppressionBesoinsActionPerformed

    private void comboBoxBesoinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxBesoinsActionPerformed
        this.setCommande(Commande.SELECTIONNER);
        int index = comboBoxBesoins.getSelectedIndex();
        String name = comboBoxBesoins.getItemAt(index);
        m_controleur.deselectionnerTout();
        for (ListIterator<Itineraire> itineraires = m_controleur.getBesoins().getListItineraire().listIterator(); itineraires.hasNext();) {
            Itineraire itineraire = itineraires.next();
            if (itineraire.getNom().equals(name)) {
                m_controleur.getBesoins().getPileSelection().ajouter(itineraire);
                afficherPanelBesoins(itineraire);
                break;
            }
        }
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_comboBoxBesoinsActionPerformed

   

    private void comboBoxStatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxStatActionPerformed
        int index = comboBoxStat.getSelectedIndex();
        String name = comboBoxStat.getItemAt(index);
        if (name != null){
            miseAjourTableauStatistique(Integer.parseInt(name));
        }
    }//GEN-LAST:event_comboBoxStatActionPerformed
    
    private void sauvegarderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sauvegarderActionPerformed
        Fider fider = new Fider();
       fider.setMainWindow(m_this);
       fider.setOption("save");
       fider.go();
        /*PathSelector fenetre = new PathSelector();
        fenetre.setOption("save");
        fenetre.setMainWindow(this);
        fenetre.setResizable(false);
        fenetre.setLocationRelativeTo(null); //pour centrer
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setVisible(true);
        */
        /*
    this.m_controleur.getHistorique().viderApresReseauCourant();
    int indexCurseur = this.m_controleur.getHistorique().getCurseur().nextIndex();
    this.m_controleur.getHistorique().clearCurseur();
    try
    {
        FileOutputStream fileOut = new FileOutputStream ("controleur.mw");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(m_controleur);
        out.close();
        fileOut.close();
        System.out.println("Ça marche !");
    }
    catch (IOException e)
    {
        e.printStackTrace();
        return;
    }
    this.m_controleur.getHistorique().setCurseur(indexCurseur); 
        */
    }//GEN-LAST:event_sauvegarderActionPerformed

    public void charger(){
        try
    {
        FileInputStream fileIn = new FileInputStream(m_fileChoosed.getAbsolutePath());
        ObjectInputStream in = new ObjectInputStream(fileIn);
        m_controleur = (Simulatheure) in.readObject();
        in.close();
        fileIn.close();
        System.out.println("Ça marche !");
    }
    catch (IOException e)
    {
        e.printStackTrace();
        return;
    }
    catch (ClassNotFoundException e)
    {
        e.printStackTrace();
        return;
    }
    this.m_controleur.initControleur();
    
    miseAJourPanels();
    miseAjourComboBoxTotal();
    miseAJourPermissionsBoutons();
    this.afficheurReseau.repaint();
    
    checkBoxDijkstra.setSelected(m_controleur.getStatutDijkstra());
    toggleGabarit.setSelected(m_controleur.getStatutAfficherGabarit());
    }
    
    private void chargerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargerActionPerformed
       Fider fider = new Fider();
       fider.setMainWindow(m_this);
       fider.setOption("charger");
       fider.go();
        /*PathSelector fenetre = new PathSelector();
        fenetre.setOption("charger");
        fenetre.setMainWindow(this);
        fenetre.setResizable(false);
        fenetre.setLocationRelativeTo(null); //pour centrer
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setVisible(true);
        */
    /*
    try
    {
        FileInputStream fileIn = new FileInputStream ("controleur.mw");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        m_controleur = (Simulatheure) in.readObject();
        in.close();
        fileIn.close();
        System.out.println("Ça marche !");
    }
    catch (IOException e)
    {
        e.printStackTrace();
        return;
    }
    catch (ClassNotFoundException e)
    {
        e.printStackTrace();
        return;
    }
    this.m_controleur.initControleur();
    
    miseAJourPanels();
    miseAjourComboBoxTotal();
    miseAJourPermissionsBoutons();
    this.afficheurReseau.repaint();
    */
    }//GEN-LAST:event_chargerActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Credits fenetre = new Credits();
        fenetre.setResizable(false);
        fenetre.setLocationRelativeTo(null); //pour centrer
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        Fider fider = new Fider();
        fider.setMainWindow(m_this);
        fider.setOption("charger");
        fider.go();
        /*PathSelector fenetre = new PathSelector();
        fenetre.setOption("charger");
        fenetre.setMainWindow(this);
        fenetre.setResizable(false);
        fenetre.setLocationRelativeTo(null); //pour centrer
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setVisible(true);
        */
    }//GEN-LAST:event_loadButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        Fider fider = new Fider();
        fider.setMainWindow(m_this);
        fider.setOption("save");
        fider.go();
        /*PathSelector fenetre = new PathSelector();
        fenetre.setOption("save");
        fenetre.setMainWindow(this);
        fenetre.setResizable(false);
        fenetre.setLocationRelativeTo(null); //pour centrer
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setVisible(true);
        */
    }//GEN-LAST:event_saveButtonActionPerformed

    private void redoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoButtonActionPerformed
        m_controleur.retablir();
        miseAJourPanels();
        miseAjourComboBoxTotal();
        miseAJourPermissionsBoutons();
        
        this.afficheurReseau.repaint();
    }//GEN-LAST:event_redoButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        m_controleur.annuler();
        miseAJourPanels();
        miseAjourComboBoxTotal();
        miseAJourPermissionsBoutons();
        
        this.afficheurReseau.repaint(); 
    }//GEN-LAST:event_undoButtonActionPerformed

    private void annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerActionPerformed
        undoButton.doClick();
    }//GEN-LAST:event_annulerActionPerformed

    private void retablirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retablirActionPerformed
        redoButton.doClick();
    }//GEN-LAST:event_retablirActionPerformed

    private void zoomInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInButtonActionPerformed
        zoom(-5,100,100);
    }//GEN-LAST:event_zoomInButtonActionPerformed

    private void zoomOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutButtonActionPerformed
        zoom(5,100,100);
    }//GEN-LAST:event_zoomOutButtonActionPerformed
    
    public void chargerGabarit()
    {
        this.m_controleur.setCheminGabarit(m_fileChoosed.getAbsolutePath());
    }
    private void chargerGabaritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargerGabaritActionPerformed
        Fider fider = new Fider();
        fider.setMainWindow(m_this);
        fider.setOption("gabarit");
        fider.go();
    }//GEN-LAST:event_chargerGabaritActionPerformed

    private void toggleGabaritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleGabaritActionPerformed
        this.m_controleur.changerStatutAfficherGabarit();
        this.afficheurReseau.repaint(); 
    }//GEN-LAST:event_toggleGabaritActionPerformed
    
    /**
     * @param args the command line arguments
     */
    private void changeLookAndFeel() {
        /*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("javax.swing.plaf.nimbus.NimbusLookAndFeel".equals(info.getClassName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        */
        try {
            // select Look and Feel
              javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
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

    public void setMode(Mode p_mode) {
        this.m_modeCourant = p_mode;
        boutonsRoutier.setVisible(false);
        boutonsSelectionRoutier.setVisible(false);
        boutonsTransport.setVisible(false);
        boutonsBesoins.setVisible(false);
        boutonsSelectionTransport.setVisible(false);
        boutonsSimulation.setVisible(false);
        boutonsSelectionSimulation.setVisible(false);
        boutonsSelectionBesoins.setVisible(false);
        m_controleur.deselectionnerTout();
        disparaitrePanels();
        this.afficheurReseau.repaint();
    }

    public void setCommande(Commande p_commande) {
        this.m_commandeCourante = p_commande;
    }

    public javax.swing.JScrollPane getDefilementAfficheur() {
        return defilementAfficheur;
    }

    public void miseAJourPermissionsBoutons() {
        undoButton.setEnabled(m_controleur.getHistorique().peutAnnuler());
        redoButton.setEnabled(m_controleur.getHistorique().peutRetablir());
        
        if (m_controleur.getRoutier().getIntersections().size() > 1) {
            selectionRoutier.setEnabled(true);
            suppressionRoutier.setEnabled(true);
        } else if (m_controleur.getRoutier().getIntersections().size() > 0) {
            selectionRoutier.setEnabled(true);
            suppressionRoutier.setEnabled(true);
        } else {
            selectionRoutier.setEnabled(false);
            suppressionRoutier.setEnabled(false);
            if (m_modeCourant == Mode.ROUTIER)
                constructionTroncon.doClick();
        }

        if (comboBoxTroncons.getItemCount() > 1) {
            transport.setEnabled(true);
        } else {
            transport.setEnabled(false);
            if (m_modeCourant != Mode.ROUTIER)
                routier.doClick();
        }

        if (m_controleur.getTransport().getListeArrets().size() > 0) {
            selectionTransport.setEnabled(true);
            suppressionTransport.setEnabled(true);
        } else {
            selectionTransport.setEnabled(false);
            suppressionTransport.setEnabled(false);
            if (m_modeCourant == Mode.TRANSPORT)
                ajoutCircuit.doClick();
        }
        
        if (m_controleur.getTransport().getListeCircuits().size() > 0)
            besoins.setEnabled(true);
        else {
            besoins.setEnabled(false);  
            if (m_modeCourant == Mode.BESOINS)
                transport.doClick();
        }
        if (comboBoxSources.getItemCount() > 1) {
            simulation.setEnabled(true);
        } else {
            simulation.setEnabled(false);
            if (m_modeCourant == Mode.SIMULATION)
                transport.doClick();
        }
        
        if (m_controleur.getBesoins().getListItineraire().size() > 0) {
            selectionBesoins.setEnabled(true);
            suppressionBesoins.setEnabled(true);
        } else {
            selectionBesoins.setEnabled(false);
            suppressionBesoins.setEnabled(false);
            if (m_modeCourant == Mode.BESOINS)
                ajoutBesoin.doClick();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.AfficheurReseau afficheurReseau;
    private javax.swing.JToggleButton ajoutArret;
    private javax.swing.JToggleButton ajoutBesoin;
    private javax.swing.JToggleButton ajoutCircuit;
    private javax.swing.JToggleButton ajoutIntersection;
    private javax.swing.JMenuItem annuler;
    private javax.swing.JButton arreterSimulation;
    private javax.swing.JButton avancerSimulation;
    private javax.swing.JToggleButton besoins;
    private javax.swing.JPanel boutonModes;
    private javax.swing.JPanel boutonsBesoins;
    private javax.swing.JPanel boutonsRoutier;
    private javax.swing.JPanel boutonsSelectionBesoins;
    private javax.swing.JPanel boutonsSelectionRoutier;
    private javax.swing.JPanel boutonsSelectionSimulation;
    private javax.swing.JPanel boutonsSelectionTransport;
    private javax.swing.JPanel boutonsSimulation;
    private javax.swing.JPanel boutonsTransport;
    private javax.swing.JMenuItem charger;
    private javax.swing.JMenuItem chargerGabarit;
    private javax.swing.JCheckBox checkBoxDijkstra;
    private javax.swing.JComboBox<String> comboBoxArrets;
    private javax.swing.JComboBox<String> comboBoxAutobus;
    private javax.swing.JComboBox<String> comboBoxBesoins;
    private javax.swing.JComboBox<String> comboBoxCircuits;
    private javax.swing.JComboBox<String> comboBoxIntersections;
    private javax.swing.JComboBox<String> comboBoxPietons;
    private javax.swing.JComboBox<String> comboBoxSources;
    private javax.swing.JComboBox<String> comboBoxStat;
    private javax.swing.JComboBox<String> comboBoxTroncons;
    private javax.swing.JToggleButton constructionTroncon;
    private javax.swing.JLabel coordonnees;
    private javax.swing.JScrollPane defilementAfficheur;
    private javax.swing.JMenuItem editerClicDroit;
    private javax.swing.JLabel facteurMultiplicatif;
    private javax.swing.JMenu fichier;
    private javax.swing.ButtonGroup groupeModes;
    private javax.swing.ButtonGroup groupeRoutier;
    private javax.swing.ButtonGroup groupeTransport;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton loadButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menu;
    private GUI.PanelArret panelArret1;
    private GUI.PanelCircuit panelCircuit1;
    private GUI.PanelIntersection panelIntersection1;
    private GUI.PanelItineraire panelItineraire1;
    private GUI.PanelSourceAutobus panelSourceAutobus1;
    private GUI.PanelTroncon panelTroncon1;
    private javax.swing.JPanel panneauCommandes;
    private javax.swing.JPanel panneauModes;
    private javax.swing.JToggleButton playPauseSimulation;
    private javax.swing.JMenuItem quitter;
    private javax.swing.JButton ralentirSimulation;
    private javax.swing.JButton recommancerSimulation;
    private javax.swing.JButton redoButton;
    private javax.swing.JMenuItem retablir;
    private javax.swing.JToggleButton routier;
    private javax.swing.JMenuItem sauvegarder;
    private javax.swing.JButton saveButton;
    private javax.swing.JToggleButton selectionBesoins;
    private javax.swing.JToggleButton selectionRoutier;
    private javax.swing.JToggleButton selectionTransport;
    private javax.swing.JToggleButton simulation;
    private javax.swing.JButton suppressionBesoins;
    private javax.swing.JButton suppressionRoutier;
    private javax.swing.JButton suppressionTransport;
    private javax.swing.JMenuItem supprimerClicDroit;
    private javax.swing.JLabel time;
    private javax.swing.JCheckBoxMenuItem toggleGabarit;
    private javax.swing.JToggleButton transport;
    private javax.swing.JButton undoButton;
    private javax.swing.JLabel zoom;
    private javax.swing.JButton zoomInButton;
    private javax.swing.JButton zoomOutButton;
    private javax.swing.JLabel zoomTitre;
    // End of variables declaration//GEN-END:variables
}
