/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.ReseauTransport;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Troncon;
import Domaine.Utilitaire.Distribution;
import java.util.LinkedList;
import java.util.ListIterator;
import Domaine.Utilitaire.Temps;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Path2D;
import java.util.LinkedHashMap;

/**
 *
 * @author louis
 */
public class ReseauTransport {
    public  ReseauTransportFactory m_factory = new ReseauTransportFactory();
    private LinkedList<Circuit> m_listeCircuits = new LinkedList<>();
    private LinkedList<Arret> m_listeArrets = new LinkedList<>();
    private int m_conteurArrets = 1;
    private int m_conteurCircuits = 1;
    private int m_conteurSources = 1;
    private ReseauRoutier m_reseauRoutier;
    
    public ReseauTransport(ReseauRoutier rr){
        m_reseauRoutier = rr;
    }
    
    public LinkedList<Circuit> getListeCircuits(){
        return m_listeCircuits;
    }
    public LinkedList<Arret> getListeArrets (){
        return m_listeArrets;
    }
    public void ajouterArret(Arret p_arret){
        p_arret.setNom("AR" + Integer.toString(m_conteurArrets));
        m_conteurArrets++;
        m_listeArrets.add(p_arret);
    }
    public void setListeCircuits(LinkedList<Circuit> listeCircuits){
        m_listeCircuits = listeCircuits;
    }
    public void ajouterCircuit(Circuit circ){
        circ.setNom("C"+ Integer.toString(m_conteurCircuits));
        m_conteurCircuits++;
        m_listeCircuits.add(circ);
    }
            
    public void initReseauTransport(){
        ListIterator<Circuit> circuitItr = m_listeCircuits.listIterator();
        while (circuitItr.hasNext()) {
            circuitItr.next().initCircuit();
        }
    };
    public void calculEtatReseauTransport(Temps deltaT){
        ListIterator<Circuit> circuitItr = m_listeCircuits.listIterator();
        while (circuitItr.hasNext()) {
            Circuit  crc = circuitItr.next();
            crc.updateSourceAutobus(deltaT);
            crc.calculCirculationGlobal(deltaT);
        }
    }
    
    public Arret selectionnerArret(Float p_x, Float p_y, Float p_diametre, Float p_echelle){
       
        Arret arret = obtenirArret(p_x, p_y, p_diametre, p_echelle);
        if (arret != null)
        {
            arret.changerStatutSelection();
        }
        return arret;
    }
    
    
   public Arret obtenirArret(Float p_x, Float p_y, Float p_diametre, Float p_echelle){
       
       Ellipse2D.Float zoneSelection = new Ellipse2D.Float(p_x, p_y, p_diametre, p_diametre);

        for (Arret arret : m_listeArrets){
            Point2D.Float p = arret.getEmplacement().calculPosition(p_echelle);
            
            if (zoneSelection.contains(p))
            {
                return arret;
            }
        }
        return null;
    }
   
    public SourceAutobus selectionnerSourceAutobus(Float p_x, Float p_y, Float p_largeur, Float p_echelle){
       
        Path2D.Float zoneSelection = new Path2D.Float();
        
        zoneSelection.moveTo(p_x, p_y - p_largeur / 2);
        zoneSelection.lineTo(p_x + p_largeur / 2, p_y);
        zoneSelection.lineTo(p_x, p_y + p_largeur / 2);
        zoneSelection.lineTo(p_x - p_largeur / 2, p_y);
        zoneSelection.closePath();

        for (Circuit circ : m_listeCircuits){
            for (SourceAutobus src : circ.getListeSourceAutobus()){
                Emplacement em = src.getEmplacement();
                Point2D.Float p = em.calculPosition(p_echelle);
                
                if (zoneSelection.contains(p))
                {
                    src.changerStatutSelection();
                    return src;
                }
            }
        }
       
        return null;
    }
   
   public SourceAutobus ajoutSource(Emplacement p_emplacement, Circuit p_circuit, String p_nomSource, Distribution p_distribution,  Temps p_tempsAttenteinitial){
       SourceAutobus src = new SourceAutobus(p_emplacement, p_circuit,p_nomSource,p_distribution,p_tempsAttenteinitial);
       src.setNom("S" + Integer.toString(m_conteurSources));
       m_conteurSources++;
       p_circuit.ajouterSource(src);
       return src;
   }
   
   public Arret creerArret(Emplacement emplacement, String nom){
       return new Arret(emplacement, nom);
   }
   
   public void deselectionnerTout(){
       for(Arret arr : m_listeArrets){
           if (arr.estSelectionne())
            {
                arr.changerStatutSelection();
            }
       }
       for(Circuit circ : m_listeCircuits){
           if (circ.estSelectionne()){
                circ.changerStatutSelection();
           }
       }
       for(Circuit circ : m_listeCircuits){
                for(SourceAutobus sa : circ.getListeSourceAutobus()){
                    if(sa.estSelectionne()){
                        sa.changerStatutSelection();
                    }
                }
           }
   }
   
    public LinkedList<ElementTransport> getElementsSelectionnes(){
        
        LinkedList<ElementTransport> listeRetour = new LinkedList<>();
                
        for (Circuit circ: m_listeCircuits)
        {
            if (circ.estSelectionne())
            {
                listeRetour.add(circ);
            }
            
            for (SourceAutobus src: circ.getListeSourceAutobus())
            {   
                if (src.estSelectionne())
                {
                    listeRetour.add(src);
                }
            }
        }
        for (Arret arr : m_listeArrets){
            if (arr.estSelectionne()){
                listeRetour.add(arr);
            }
        }
        return listeRetour;
    }
    
    public Boolean supprimerSelection()
    {
        
        for (ListIterator<Circuit> circ = m_listeCircuits.listIterator() ; circ.hasNext(); )
        {
            Circuit circuit = circ.next();
            if (circuit.estSelectionne())
            {
                circuit.getListeSourceAutobus().clear();
                circ.remove();
            }
            else
            {
                for (ListIterator<SourceAutobus> src = circuit.getListeSourceAutobus().listIterator() ; src.hasNext() ; )
                {
                    if (src.next().estSelectionne())
                    {
                        src.remove();
                    }
                }
            }
        }
        
        Boolean supprArretOK = true;
        Boolean supprTotalOK = true;
        for (ListIterator<Arret> arrIt = m_listeArrets.listIterator() ; arrIt.hasNext() ; )
        {
            Arret arr = arrIt.next();
            if (arr.estSelectionne())
            {
                for (Circuit circ : m_listeCircuits) {
                    for (PaireArretTrajet pat : circ.getListeArretTrajet()){
                        if (pat.getArret() == arr){
                            supprArretOK = false;
                            supprTotalOK = false;
                        }
                    }
                }
                if (supprArretOK){
                    arrIt.remove();
                }
                supprArretOK = true;
            }
        }
        return supprTotalOK;
    }
    
    public Boolean arretsSontConnectables(Arret arr1, Arret arr2){
        Intersection inter1;
        Intersection inter2;
        if (arr1.getEmplacement().estSurTroncon()){
            inter1 = arr1.getEmplacement().getTroncon().getDestination();
        }
        else{
            inter1 = arr1.getEmplacement().getIntersection();
        }
        if (arr2.getEmplacement().estSurTroncon()){
            inter2 = arr2.getEmplacement().getTroncon().getOrigine();
        }
        else{
            inter2 = arr2.getEmplacement().getIntersection();
        }
        
        LinkedList<Intersection> intersectionsVerifiees = new LinkedList<>();
        return arretsConnectesRec(inter1, inter2, intersectionsVerifiees);
    }
    
    public Boolean arretsConnectesRec(Intersection inter1, Intersection inter2, LinkedList<Intersection> intersectionsVerifiees){
        Boolean est_connecte = false;
        for (Intersection intr : inter1.getEnfants()){
            if (intr.equals(inter2)){
                return true;
            }
            else{
                if (!intersectionsVerifiees.contains(intr)){
                    intersectionsVerifiees.add(intr);
                    est_connecte =  arretsConnectesRec(intr, inter2, intersectionsVerifiees);
                    if (est_connecte)
                        return est_connecte;
                }
            }
        }
        return est_connecte;
    }
    
    public LinkedList<Troncon> dijkstra(Emplacement emplacementInitial, Emplacement emplacementFinal){
        
        //preparation pour passer de arret a intersection
        Intersection debut;
        Intersection fin;
        Troncon trc_debut = null;
        Troncon trc_fin = null;
        LinkedList<Troncon> dijk = new LinkedList<>();
        
        if (emplacementInitial.estSurTroncon()) {
            debut = emplacementInitial.getTroncon().getDestination();
            trc_debut = emplacementInitial.getTroncon();
        }
        else{
            debut = emplacementInitial.getIntersection();
        }
        
        if (emplacementFinal.estSurTroncon()) {
            fin = emplacementFinal.getTroncon().getOrigine();
            trc_fin = emplacementFinal.getTroncon();
        }
        else{
            fin = emplacementFinal.getIntersection();
        }
                  
            
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
                Float distance_n1_n2 = (float) arc.getDistribution().getTempsMoyen().getTemps();
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
        
        if (trc_debut != null){
            chemin.addFirst(trc_debut);
        }
        if (trc_fin != null){
            chemin.addLast(trc_fin);
        }
        
        return chemin;
    }
    
    public LinkedList<Trajet> obtenirTrajetsAffectes(Troncon p_tronconModifie)
    {
        LinkedList<Trajet> trajetsAffectes = new LinkedList<>();
        for (Circuit circuit : m_listeCircuits)
        {
            for (PaireArretTrajet paire : circuit.getListeArretTrajet())
            {
                if (paire.getTrajet() != null)
                {
                    for (Troncon troncon : paire.getTrajet().getListeTroncons())
                    {
                        if (troncon == p_tronconModifie)
                            trajetsAffectes.add(paire.getTrajet());
                    }
                }
            }
        }
        return trajetsAffectes;
    }
    
    public void optimiserCircuitsAffectes(LinkedList<Trajet> trajetsAffectes)
    {
        for (Trajet trajet : trajetsAffectes)
        {
            trajet.setListeTroncons(dijkstra(trajet.getEmplacementInitial(), trajet.getEmplacementFinal()));
        }
    }
}
