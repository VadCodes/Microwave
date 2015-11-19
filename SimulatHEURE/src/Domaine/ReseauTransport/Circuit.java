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
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Intersection;
import java.util.LinkedList;
import java.util.ListIterator;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Trajet;
import Domaine.ReseauRoutier.Troncon;
import java.awt.geom.Point2D;
import Domaine.Utilitaire.Temps;

public class Circuit extends ElementTransport{
    private String m_nom = "";
    private LinkedList<SourceAutobus> m_listeSources = new LinkedList();
    private LinkedList<Autobus> m_listeAutobus = new LinkedList();
    private LinkedList<PaireArretTrajet> m_listeArretTrajet;
    private ReseauRoutier m_reseauRoutier;
    private Temps tempsDepart;
    private Boolean m_boucle = false;
    private Boolean m_veutBoucler = false;
    
    public Circuit(String nom, LinkedList<PaireArretTrajet> listeArrTraj, ReseauRoutier resRoutier){
        //assert listeArrTraj doit avoir les 2 premiers
        m_nom = nom;
        m_listeArretTrajet = listeArrTraj;
        m_reseauRoutier = resRoutier;
    }
    
    public Circuit(){
        m_listeArretTrajet = new LinkedList();
    }
    
    public LinkedList<SourceAutobus> getListeSourceAutobus(){
        return m_listeSources;
    }
    public void updateSourceAutobus(Temps deltatT){
         ListIterator<SourceAutobus> sourceAutobusItr = m_listeSources.listIterator();
        while (sourceAutobusItr.hasNext()) {
            SourceAutobus src = sourceAutobusItr.next();
            src.miseAJourTempsRestant(deltatT);
            src.genererAutobus();
        }
    }
    public void setNom(String p_nom){
        m_nom = p_nom;
    }
    public String getNom(){
        return m_nom;
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
            calculCirculation(deltatT, autobusItr.next());
        }
    }
    
    public void calculCirculation(Temps deltaT, Autobus bus){
        bus.miseAJourAutobus(deltaT);
    }
    
    public LinkedList<Point2D.Float> getPositionsDesAutobus(){
        
        LinkedList<Point2D.Float> listePositionsAutobus = new LinkedList<Point2D.Float>();
        ListIterator<Autobus> autobusItr = m_listeAutobus.listIterator();
        while (autobusItr.hasNext()) {
            listePositionsAutobus.add(autobusItr.next().getPosition());
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
        bus.assignerTrajet(m_listeArretTrajet);
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
}
