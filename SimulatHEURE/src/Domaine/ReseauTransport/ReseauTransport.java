package Domaine.ReseauTransport;

import Domaine.Reseau;

import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Troncon;

import Domaine.Utilitaire.Distribution;
import Domaine.Utilitaire.Temps;

import java.util.LinkedList;
import java.util.ListIterator;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Path2D;


/**
 *
 * @author louis
 */
public class ReseauTransport extends Reseau{
    public  ReseauTransportFactory m_factory = new ReseauTransportFactory();
    private LinkedList<Circuit> m_listeCircuits = new LinkedList<>();
    private LinkedList<Arret> m_listeArrets = new LinkedList<>();
    private int m_conteurArrets = 1;
    private int m_conteurCircuits = 1;
    private int m_conteurSources = 1;
    private PileSelectionTransport m_pileSelection = new PileSelectionTransport();
    
    public ReseauTransport(){}
    
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
    
    public Boolean selectionnerArret(Float p_x, Float p_y, Float p_diametre, Float p_echelle){
       
        Arret arret = obtenirArret(p_x, p_y, p_diametre, p_echelle);
        m_pileSelection.ajouter(arret);
        return (arret!=null);
    }
    
    public Arret selectionnerArretVinny(Float p_x, Float p_y, Float p_diametre, Float p_echelle){
       
        Arret arret = obtenirArret(p_x, p_y, p_diametre, p_echelle);
        if (m_pileSelection.contient(arret))
            m_pileSelection.enlever(arret);
        else
            m_pileSelection.ajouter(arret);
        
        return arret;
    }
    
    public Boolean selectionnerCircuit(Float xReel, Float yReel, Float p_echelle, Troncon trc){
        if (trc!=null){
            Float pourcentageClic = trc.getPourcentageClic(xReel, yReel, p_echelle);
            Emplacement empl_deb = null;
            Emplacement empl_fin = null;
            Boolean circuitSelectionne = false;
            for (Circuit circ : m_listeCircuits){
                for (PaireArretTrajet pat : circ.getListeArretTrajet()){
                    if (pat.getTrajet() != null){
                        for (Troncon trc2 : pat.getTrajet().getListeTroncons()){
                            if (trc2.equals(trc)){
                                if (pat.getTrajet().getEmplacementInitial().estSurTroncon())
                                    empl_deb = pat.getTrajet().getEmplacementInitial();
                                if (pat.getTrajet().getEmplacementFinal().estSurTroncon())
                                    empl_fin = pat.getTrajet().getEmplacementFinal();
                                
                                if (empl_deb != null && empl_fin != null){
                                    if (empl_deb.getTroncon().equals(trc) && empl_fin.getTroncon().equals(trc)){
                                        if (empl_deb.getPourcentageParcouru() < empl_fin.getPourcentageParcouru()){
                                            //si entre
                                            if (pourcentageClic > empl_deb.getPourcentageParcouru() && 
                                                    pourcentageClic < empl_fin.getPourcentageParcouru()){
                                                circuitSelectionne = true;
                                            }
                                        }
                                        else{
                                            //si avant ou apres
                                            if (pourcentageClic > empl_deb.getPourcentageParcouru() ||
                                                    pourcentageClic < empl_fin.getPourcentageParcouru()){
                                                circuitSelectionne = true;
                                            }
                                        }
                                    }
                                    else if(empl_deb.getTroncon().equals(trc)){
                                        // si apres
                                        if (pourcentageClic > empl_deb.getPourcentageParcouru()){
                                            circuitSelectionne = true;
                                        }
                                    }
                                    else if(empl_fin.getTroncon().equals(trc)){
                                        // si avant
                                        if (pourcentageClic < empl_fin.getPourcentageParcouru()){
                                            circuitSelectionne = true;
                                        }
                                    }
                                    else{
                                        circuitSelectionne = true;
                                    }
                                }
                                else if(empl_deb != null){
                                    if (empl_deb.getTroncon().equals(trc)){
                                        if (pourcentageClic > empl_deb.getPourcentageParcouru()){
                                            circuitSelectionne = true;
                                        }
                                    }
                                    else{
                                        circuitSelectionne = true;
                                    }
                                }
                                else if(empl_fin != null){
                                    if(empl_fin.getTroncon().equals(trc)){
                                        if (pourcentageClic < empl_fin.getPourcentageParcouru()){
                                            circuitSelectionne = true;
                                        }
                                    }
                                    else{
                                        circuitSelectionne = true;
                                    }
                                }
                                else{
                                    circuitSelectionne = true;
                                }

                                if (circuitSelectionne){
                                    m_pileSelection.ajouter(circ);
                                    return true;
                                }

                                empl_deb = null;
                                empl_fin = null;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
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
   
    public Boolean selectionnerSourceAutobus(Float p_x, Float p_y, Float p_largeur, Float p_echelle){
       
        Path2D.Float zoneSelection = new Path2D.Float();
        
        zoneSelection.moveTo(p_x, p_y - p_largeur / 2);
        zoneSelection.lineTo(p_x + p_largeur / 2, p_y);
        zoneSelection.lineTo(p_x, p_y + p_largeur / 2);
        zoneSelection.lineTo(p_x - p_largeur / 2, p_y);
        zoneSelection.closePath();

        for (Circuit circ : m_listeCircuits){
            for (SourceAutobus src : circ.getListeSources()){
                Emplacement em = src.getEmplacement();
                Point2D.Float p = em.calculPosition(p_echelle);
                
                if (zoneSelection.contains(p))
                {
                    m_pileSelection.ajouter(src);
                    return true;
                }
            }
        }
       
        return false;
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
       m_pileSelection.vider();
   }
   
    public LinkedList<ElementTransport> getElementsSelectionnes(){
        return m_pileSelection.getListe();
    }
    
    public Boolean supprimerSelection()
    {
        
        for (ListIterator<Circuit> circ = m_listeCircuits.listIterator() ; circ.hasNext(); )
        {
            Circuit circuit = circ.next();
            if (m_pileSelection.contient(circuit))
            {
                circuit.getListeSources().clear();
                circ.remove();
            }
            else
            {
                for (ListIterator<SourceAutobus> src = circuit.getListeSources().listIterator() ; src.hasNext() ; )
                {
                    if (m_pileSelection.contient(src.next()))
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
            if (m_pileSelection.contient(arr))
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
    
    public LinkedList<Circuit> obtenirCircuitsAffectes(Troncon p_tronconModifie)
    {
        LinkedList<Circuit> circuitsAffectes = new LinkedList<>();
        for (Circuit circuit : m_listeCircuits)
        {
            for (PaireArretTrajet paire : circuit.getListeArretTrajet())
            {
                if (paire.getTrajet() != null)
                {
                    if (paire.getTrajet().getListeTroncons().contains(p_tronconModifie))
                        circuitsAffectes.add(circuit);
                }
            }
        }
        return circuitsAffectes;
    }
    
    public LinkedList<Trajet> obtenirTrajetsAffectes(LinkedList<Circuit> circuitsAffectes, Troncon tronconModifie)
    {
        LinkedList<Trajet> trajetsAffectes = new LinkedList<>();
        for (Circuit circuit : circuitsAffectes)
        {
            for (PaireArretTrajet paire : circuit.getListeArretTrajet())
            {
                if (paire.getTrajet() != null)
                {
                    if (paire.getTrajet().getListeTroncons().contains(tronconModifie))
                        trajetsAffectes.add(paire.getTrajet());
                }
            }
        }
        return trajetsAffectes;
    }
    
    public void supprimerSourcesOrphelines(LinkedList<Circuit> circuitsAffectes)
    {
        for (Circuit circuit : circuitsAffectes)
        {
            LinkedList<Troncon> tronconsCircuit = circuit.obtenirTroncons();
            LinkedList<Intersection> interCircuit = ReseauRoutier.obtenirInterContigues(tronconsCircuit);
            for (ListIterator<SourceAutobus> itSource = circuit.getListeSources().listIterator() ; itSource.hasNext() ; )
            {
                SourceAutobus source = itSource.next();
                if (source.getEmplacement().estSurTroncon())
                {
                    if (!tronconsCircuit.contains(source.getEmplacement().getTroncon()))
                        itSource.remove();
                }
                else
                {
                    if (!interCircuit.contains(source.getEmplacement().getIntersection()))
                        itSource.remove();
                }
            }
        }
    }
    
    public PileSelectionTransport getPileSelection(){
        return m_pileSelection;
    }
}
