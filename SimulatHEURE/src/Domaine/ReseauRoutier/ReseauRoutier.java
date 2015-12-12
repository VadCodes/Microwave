package Domaine.ReseauRoutier;

import Domaine.Reseau;

import Domaine.Utilitaire.PaireFloats;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.LinkedHashMap;

import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutier extends Reseau{
    public final ReseauRoutierFactory m_factory = new ReseauRoutierFactory();
    
    private LinkedList<Intersection> m_listeIntersections = new LinkedList<>();
    
    private int m_compteurIntersection;
    private int m_compteurTroncon;
    
    private PileSelectionRoutier m_pileSelection = new PileSelectionRoutier();
    private ElementRoutier m_elementCurseur = null;
    
    public final static double VITESSE_PIETON = 4 ;
    
    public ReseauRoutier(){
        m_compteurIntersection = 1;
        m_compteurTroncon = 1;    
    }
    
    public ReseauRoutier(ReseauRoutier p_reseauSource){
        
        for (Intersection interSource : p_reseauSource.m_listeIntersections)
        {
            this.m_listeIntersections.add(this.m_factory.intersection(interSource.getPosition()));
            this.m_listeIntersections.getLast().setNom(interSource.getName());
        }
        
        ListIterator<Intersection> itInterCopiee = this.m_listeIntersections.listIterator();
        int indexDestination;
        for (Intersection interSource : p_reseauSource.m_listeIntersections)
        {
            Intersection interCopiee = itInterCopiee.next();
            for (Troncon tronconSource : interSource.getTroncons())
            {
                indexDestination = p_reseauSource.m_listeIntersections.indexOf(tronconSource.getDestination());
                interCopiee.ajouterTroncon(this.m_factory.troncon(interCopiee, this.m_listeIntersections.get(indexDestination)));            
                
                interCopiee.getTroncons().getLast().setNom(tronconSource.getNom());
                interCopiee.getTroncons().getLast().setDistribution(tronconSource.getDistribution());
                interCopiee.getTroncons().getLast().copierDoubleSens(tronconSource.estDoubleSens());                
            }
        }
        
        this.m_compteurTroncon = p_reseauSource.m_compteurTroncon;
        this.m_compteurIntersection = p_reseauSource.m_compteurIntersection;
    }
    
    public Emplacement nouvelEmplacementHomologue(ReseauRoutier p_reseauSource, Emplacement p_empSource)
    {
        if (p_empSource.estSurTroncon())
        {
            int indexInter = p_reseauSource.m_listeIntersections.indexOf(p_empSource.getTroncon().getOrigine());
            int indexTroncon = p_reseauSource.m_listeIntersections.get(indexInter).getTroncons().indexOf(p_empSource.getTroncon());

            Intersection interCopiee = this.m_listeIntersections.get(indexInter);
            return new Emplacement(true, p_empSource.getPourcentageParcouru(), interCopiee.getTroncons().get(indexTroncon), interCopiee);
        }
        else
        {
            int indexInter = p_reseauSource.m_listeIntersections.indexOf(p_empSource.getIntersection());
            return new Emplacement(false, 0, null, this.m_listeIntersections.get(indexInter));
        }
    }
    
    public LinkedList<Troncon> tronconsHomologues(ReseauRoutier p_reseauSource, LinkedList<Troncon> tronconsSources)
    {
        LinkedList<Troncon> tronconsHomologues = new LinkedList<>();
        for (Troncon tronconSource : tronconsSources)
        {
            int indexInter = p_reseauSource.m_listeIntersections.indexOf(tronconSource.getOrigine());
            int indexTroncon = p_reseauSource.m_listeIntersections.get(indexInter).getTroncons().indexOf(tronconSource);

            tronconsHomologues.add(this.m_listeIntersections.get(indexInter).getTroncons().get(indexTroncon));
        }
        
        return tronconsHomologues;
    }
    
    public LinkedList<Intersection> getIntersections()
    {
        return m_listeIntersections;
    }    
    
    public void ajouterIntersection(float p_x, float p_y)
    {
        Intersection inter = m_factory.intersection(new Point2D.Float(p_x, p_y));
        inter.setNom("I" + Integer.toString(m_compteurIntersection));
        m_compteurIntersection++;
        m_listeIntersections.add(inter);
    }
    
    public void setNomTroncon(Troncon p_troncon, String p_nom){
        p_troncon.setNom(p_nom);
    }
    
    public Boolean selectionnerIntersectionVinny(Float p_x, Float p_y, Float p_diametre){
        // À utiliser pour contruire les tronçons
        Intersection inter = obtenirIntersection(p_x, p_y, p_diametre);
        if (m_pileSelection.contient(inter))
            m_pileSelection.getListe().remove(inter);
        else
            m_pileSelection.ajouter(inter);
        
        return (inter != null);
    }
    
    public Intersection obtenirIntersection(Float p_x, Float p_y, Float p_diametre)
    {
        Ellipse2D.Float zoneSelection = new Ellipse2D.Float(p_x, p_y, p_diametre, p_diametre);

        for (Intersection inter : m_listeIntersections){
            if (zoneSelection.contains(inter.getPosition())){
                return inter;
            }
        }
        return null;
    }
    
    public Troncon obtenirTroncon(Float p_x, Float p_y, Float p_largeur, Float p_echelle)
    {
        Rectangle2D.Float zoneApproximative = new Rectangle2D.Float(p_x, p_y, p_largeur, p_largeur);
        
        for (Intersection intersection: m_listeIntersections)
        {
            Point2D.Float p1 = intersection.getPosition();
            
            for (Troncon troncon: intersection.getTroncons())
            {   
                Point2D.Float p2 = troncon.getDestination().getPosition();
                
                PaireFloats pAj = troncon.ajusterSiDoubleSens(p1, p2, p_echelle);
                Float ajX = pAj.getFloat1();
                Float ajY = pAj.getFloat2();
                Line2D.Float segment = new Line2D.Float(new Point2D.Float(p1.x + ajX, p1.y + ajY), new Point2D.Float(p2.x + ajX, p2.y + ajY));
                
                if (segment.intersects(zoneApproximative))
                {
                    return troncon;
                }
            }
        }
        return null;
    }
    
    public void desuggererTout()
    {
        for (Intersection intersection: m_listeIntersections)
        {
            intersection.setEstSuggere(false);
            
            for (Troncon troncon: intersection.getTroncons())
            {   
                troncon.setEstSuggere(false);
            }
        }
    }
    
    public void deselectionnerTout()
    {
        desuggererTout();
        m_pileSelection.vider();
    }
    
    public LinkedList<ElementRoutier> getElementsSelectionnes(){
        return m_pileSelection.getListe();
    }
    
    public void ajouterTroncon(Intersection p_origine, Intersection p_destination)
    {        
        for(Troncon trc : p_origine.getTroncons()){
            if (trc.getDestination()==p_destination){
                throw new IllegalArgumentException("Un même tronçon est déjà présent présent.", new Throwable("Ajout impossible")); 
            }
        }
        Troncon tr = m_factory.troncon(p_origine, p_destination);
        tr.setNom( "T" +Integer.toString(m_compteurTroncon));
        m_compteurTroncon++;
        p_origine.ajouterTroncon(tr);
    }
    
    public void supprimerSelection()
    {        
//        for (ListIterator<ElementRoutier> erIt = m_pileSelection.getListe().listIterator(); erIt.hasNext() ; ){
//            ElementRoutier er = erIt.next();
//            if (er.getClass() == Intersection.class){
//                Intersection inter = (Intersection) er;
//                inter.getTroncons().clear();
//            }
//            erIt.remove();
//        }
        
        for (ListIterator<Intersection> intersectionIt = m_listeIntersections.listIterator() ; intersectionIt.hasNext() ; )
        {
            Intersection intersection = intersectionIt.next();
            
            if (m_pileSelection.contient(intersection))
            {
                intersection.getTroncons().clear();
                intersectionIt.remove();
            }
            else
            {
                for (ListIterator<Troncon> troncon = intersection.getTroncons().listIterator() ; troncon.hasNext() ; )
                {
                    if (m_pileSelection.contient(troncon.next()) || m_pileSelection.contient(troncon.previous().getDestination()))
                    {
                        troncon.remove();
                    }
                    else
                    {
                        troncon.next();
                    }
                }
            }
        }
    }
    
    public void initReseauRoutier(){
        for (Intersection intersection : m_listeIntersections)
        {
            for (Troncon troncon : intersection.getTroncons())
            {
                troncon.initTroncon();
            }
        }
    }
    
    public Troncon getTronconParIntersections(Intersection orig, Intersection dest){
        if (m_listeIntersections.contains(orig)){
            for (Troncon trc : orig.getTroncons()){
                if (trc.getDestination().equals(dest)){
                    return trc;
                }
            }
        }
        return null;
    }
    
    public LinkedList<Troncon> dijkstra(Emplacement emplacementInitial, Emplacement emplacementFinal){
        
        LinkedList<Troncon> proximite = miniDijkstra(emplacementInitial, emplacementFinal);
        if (!proximite.isEmpty()){
            return proximite;
        }
        
        //preparation pour passer de emplacement a intersection
        Intersection debut;
        Intersection fin;
        Troncon trc_debut = null;
        Troncon trc_fin = null;
        
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
        LinkedList<Intersection> noeuds = getIntersections(); 
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
                Troncon arc = getTronconParIntersections(n1, n2);
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
            chemin.addFirst(getTronconParIntersections(n, n_dest));
        }        
        
        if (trc_debut != null){
            chemin.addFirst(trc_debut);
        }
        if (trc_fin != null){
            chemin.addLast(trc_fin);
        }
        
        return chemin;
    }
    
    public LinkedList<Troncon> miniDijkstra(Emplacement emplInit, Emplacement emplFin){
        LinkedList<Troncon> proximite = new LinkedList<>();
        if(emplInit.estSurTroncon()){
            if(emplFin.estSurTroncon()){
                if(emplInit.getTroncon()==emplFin.getTroncon() 
                        && emplInit.getPourcentageParcouru()< emplFin.getPourcentageParcouru()){
                    proximite.add(emplInit.getTroncon());
                }
                else if(emplInit.getTroncon().getDestination() == emplFin.getTroncon().getOrigine()){
                    proximite.add(emplInit.getTroncon());
                    proximite.add(emplFin.getTroncon());
                }
            }
            else{
                if(emplInit.getTroncon().getDestination()==emplFin.getIntersection()){
                    proximite.add(emplInit.getTroncon());
                }
            }
        }
        else{
            if(emplFin.estSurTroncon()){
                if(emplFin.getTroncon().getOrigine()==emplInit.getIntersection()){
                    proximite.add(emplFin.getTroncon());
                }
            }
        }
        
        return proximite;
    }
    
    public static LinkedList<Intersection> obtenirInterContigues(LinkedList<Troncon> tronconsContigues)
    {
        LinkedList<Intersection> interContigues = new LinkedList<>();
        interContigues.add(tronconsContigues.getFirst().getOrigine());
        for (Troncon troncon : tronconsContigues)
        {
            interContigues.add(troncon.getDestination());  // Obtient certaines intersections en double si plusieurs fois le même troncon
        } 
        return interContigues;
    }
    
    public PileSelectionRoutier getPileSelection(){
        return m_pileSelection;
    }
    
    public ElementRoutier getElementCurseur(){
        return m_elementCurseur;
    }
    public void setElementCurseur(ElementRoutier er){
        m_elementCurseur = er;
    }
}
