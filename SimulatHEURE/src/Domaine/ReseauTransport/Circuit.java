/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;


/**
 *
 * @author louis
 */
import Domaine.ReseauRoutier.Intersection;
import Domaine.Utilitaire.Temps;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Trajet;
import Domaine.ReseauRoutier.Troncon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.Font;
import java.awt.font.GlyphVector;
import java.awt.font.FontRenderContext;
import java.util.AbstractMap;
import java.util.LinkedHashMap;

import java.util.LinkedList;
import java.util.ListIterator;

public class Circuit extends ElementTransport{
    private String m_nom = "";
    private GlyphVector m_representationNom;
    private LinkedList<SourceAutobus> m_listeSources = new LinkedList<>();
    private LinkedList<Autobus> m_listeAutobus = new LinkedList<>();
    private LinkedList<PaireArretTrajet> m_listeArretTrajet;
    private ReseauRoutier m_reseauRoutier;
    private Temps tempsDepart;
    private Boolean m_boucle = false;
    private Boolean m_veutBoucler = false;
    
    public Circuit(LinkedList<PaireArretTrajet> listeArrTraj, ReseauRoutier resRoutier){
        //assert listeArrTraj doit avoir les 2 premiers
        m_listeArretTrajet = listeArrTraj;
        m_reseauRoutier = resRoutier;
    }
    
//    public Circuit(){
//        m_listeArretTrajet = new LinkedList<>();
//    }
    
    public LinkedList<Autobus> getListeAutobus(){
        return m_listeAutobus;
    }
    public LinkedList<SourceAutobus> getListeSourceAutobus(){
        return m_listeSources;
    }
    public void updateSourceAutobus(Temps deltatT){
         ListIterator<SourceAutobus> sourceAutobusItr = m_listeSources.listIterator();
        while (sourceAutobusItr.hasNext()) {
            SourceAutobus src = sourceAutobusItr.next();
            src.miseAJourTempsRestant(deltatT);
            src.genererAutobus(deltatT);
        }
    }
    public void setNom(String p_nom){
        m_nom = p_nom;
        Font a = new Font(null, Font.BOLD, 12);
        m_representationNom = a.createGlyphVector(new FontRenderContext(null, false, false), p_nom);
        double ajX = m_representationNom.getVisualBounds().getCenterX();
        double ajY = m_representationNom.getVisualBounds().getCenterY();
        
        for (int i = 0 ; i< p_nom.length() ; i++)
        {
            Point2D position = m_representationNom.getGlyphPosition(i);
            position.setLocation(position.getX() - ajX, position.getY() - ajY);
            m_representationNom.setGlyphPosition(i, position);
        }        
    }
    public String getNom(){
        return m_nom;
    }
    public GlyphVector getRepresentationNom(){
        return m_representationNom;
    }
    public void ajouterSource(SourceAutobus source){
        m_listeSources.add(source);
    }
    
    public void ajouterAutobus(Autobus autobus){
        m_listeAutobus.add(autobus);
    }
    

    public void ajouterPaire(Arret arr, Trajet traj){
        assert(m_listeArretTrajet.getLast().getArret().getEmplacement().equals(traj.getEmplacementInitial()));
        assert(arr.getEmplacement().equals(traj.getEmplacementFinal()));
        
        m_listeArretTrajet.add(new PaireArretTrajet(arr, traj));
    }
    
    public void calculCirculationGlobal(Temps deltatT){
        
        //on vide toutes les files d'arrets
        ListIterator<PaireArretTrajet> arretTrajetItr = m_listeArretTrajet.listIterator();
        while (arretTrajetItr.hasNext()) {
            arretTrajetItr.next().getArret().viderFile();
        }
        
        //pour chaque autobus on calcule la circulation
        ListIterator<Autobus> autobusItr = m_listeAutobus.listIterator();
        while (autobusItr.hasNext()) {
            Autobus aut = autobusItr.next();
            if (aut.asTerminer()){
                autobusItr.remove();
            }
            else{
                calculCirculation(deltatT, aut);
            }
        }
    }
    
    public void calculCirculation(Temps deltaT, Autobus bus){
        bus.miseAJourAutobus(deltaT);
    }
    
    public LinkedList<Point2D.Float> getPositionsDesAutobus(Float p_echelle){
        
        LinkedList<Point2D.Float> listePositionsAutobus = new LinkedList<>();
        ListIterator<Autobus> autobusItr = m_listeAutobus.listIterator();
        while (autobusItr.hasNext()) {
            listePositionsAutobus.add(autobusItr.next().getPosition(p_echelle));
        }
        
        return listePositionsAutobus;
    }
    
    public LinkedList<PaireArretTrajet> getListeArretTrajet(){
        return m_listeArretTrajet;
    }
    
    public void incrementerIterateurAutobus(Autobus bus){
        bus.incrementerIterateur();
    }
    
    public void assignerTrajetAutobus(Autobus bus){
        bus.assignerTrajet(m_listeArretTrajet, (m_boucle && m_veutBoucler));
    }
    
    public void initCircuit(){
        ListIterator<SourceAutobus> sourceAutobusItr = m_listeSources.listIterator();
        while (sourceAutobusItr.hasNext()) {
            sourceAutobusItr.next().initSourceAutobus();
        }
    }
    
    public void setBoucle(Boolean boucle){
        m_boucle = boucle;
    }
    public Boolean getBoucle(){
        return m_boucle;
    }
    public void setVeutBoucler(Boolean veutBoucler){
        m_veutBoucler = veutBoucler;
    }
    public Boolean getVeutBoucler(){
        return m_veutBoucler;
    }
    
    public LinkedList<Troncon> dijkstra(Intersection debut, Intersection fin){
        
        //declarations des structures
        LinkedList<Intersection> noeuds = m_reseauRoutier.getIntersections(); 
        LinkedList<Intersection> pasEncoreVu = new LinkedList<>();
        LinkedHashMap<Intersection, Float> parcouru = new LinkedHashMap<>();
        LinkedHashMap<Intersection, Intersection> precedent = new LinkedHashMap<>();
        
        //initialisation
        for (Intersection intrsct : noeuds){
            if(intrsct.equals(debut)){
                parcouru.put(intrsct, 0.0f);
            }
            else{
                parcouru.put(intrsct, Float.MAX_VALUE);
            }
            
            precedent.put(intrsct, null);
            
            //shallow copy de chaque element de noeuds
            pasEncoreVu.add(intrsct);
        }
        
        Intersection n1; 
        Float n1_parcouru;
        Float n2_parcouru;
        Float min;
        while(!pasEncoreVu.isEmpty()){
            //trouver min de pasEncoreVu
            n1_parcouru = Float.MAX_VALUE;
            n1 = pasEncoreVu.getFirst();
            for (Intersection intr : pasEncoreVu){
                min = parcouru.get(intr);
                if (min < n1_parcouru){
                    n1_parcouru = min;
                    n1 = intr;
                }
            }
            
            pasEncoreVu.remove(n1);
            
            for(Intersection n2 : n1.getEnfants()){
                Troncon arc = m_reseauRoutier.getTronconParIntersections(n1, n2);
                Float distance_n1_n2 = (float) arc.getDistribution().getTempsPlusFrequent().getTemps();
                n2_parcouru = parcouru.get(n2);
                if (n2_parcouru > n1_parcouru + distance_n1_n2) { //min = parcouru.get(n1)
                    n2_parcouru = n1_parcouru + distance_n1_n2;
                    parcouru.put(n2, n2_parcouru);
                    precedent.replace(n2, n1);
                }
            }
        }
        
        LinkedList<Troncon> chemin = new LinkedList<>();
        Intersection n = fin;
        Intersection n_dest;
        while (n != debut){
            n_dest = n;
            n = precedent.get(n);
            chemin.addFirst(m_reseauRoutier.getTronconParIntersections(n, n_dest));
        }        
        
        return chemin;
    }
}
