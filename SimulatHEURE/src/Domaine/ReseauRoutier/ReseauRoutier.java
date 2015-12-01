package Domaine.ReseauRoutier;

import Domaine.Utilitaire.PaireFloats;
import java.util.LinkedList;
import java.util.ListIterator;

import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Nathaniel
 */
public class ReseauRoutier {
    private LinkedList<Intersection> m_listeIntersections = new LinkedList<>();
    public final ReseauRoutierFactory m_factory = new ReseauRoutierFactory();
    private int m_compteurTroncon = 1;
    private int m_compteurIntersection = 1;
    private PileSelectionRoutier m_pileSelection = new PileSelectionRoutier();
    
    public final static double VITESSE_PIETON = 4;
    
    public ReseauRoutier(){}
    
    public LinkedList<Intersection> getIntersections()
    {
        return m_listeIntersections;
    }
    
    public void copier(ReseauRoutier p_reseauRoutier){
        p_reseauRoutier.m_compteurIntersection = this.m_compteurIntersection;
        p_reseauRoutier.m_compteurTroncon = this.m_compteurTroncon;
        p_reseauRoutier.m_listeIntersections  = new LinkedList<>();
        for (ListIterator<Intersection> intersections = this.m_listeIntersections.listIterator() ;intersections.hasNext() ; ){
           // Intersection inter = p_reseauRoutier.intersections.next();
        }
               // Intersection intersection = p_reseauRoutier.copyIntersection(intersections.next());
    }
    public void ajouterIntersection(float p_x, float p_y)
    {
        Intersection inter = m_factory.intersection(new Point2D.Float(p_x, p_y));
        inter.setNom("I" + Integer.toString(m_compteurIntersection));
        m_compteurIntersection++;
        m_listeIntersections.add(inter);
    }
    
    public void setNameTroncon(Troncon p_troncon, String p_nom){
        p_troncon.setNom(p_nom);
    }
    
    public Boolean selectionnerIntersectionVinny(Float p_x, Float p_y, Float p_diametre){
        // À utiliser pour contruire les tronçons
        Intersection inter = obtenirIntersection(p_x, p_y, p_diametre);
        if (m_pileSelection.contient(inter))
            m_pileSelection.enlever(inter);
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
    
//    public Troncon selectionnerTroncon(Float p_x, Float p_y, Float p_largeur, Float p_echelle){
//        Troncon trc = obtenirTroncon(p_x, p_y, p_largeur, p_echelle); //sans selection
//        if (trc != null){
//            trc.changerStatutSelection();
//        }
//        return trc;
//    }
    
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
    
    public void deselectionnerTout()
    {
        desuggererTout();
        m_pileSelection.vider();
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
        Troncon tr = m_factory.creerTroncon(p_origine, p_destination);
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
    
}
