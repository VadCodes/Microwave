package Domaine.ReseauTransport;

import Domaine.Reseau;

import Domaine.ReseauRoutier.Emplacement;
import Domaine.ReseauRoutier.Intersection;
import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauRoutier.Troncon;

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
    public final ReseauTransportFactory m_factory = new ReseauTransportFactory();
    
    private LinkedList<Arret> m_listeArrets = new LinkedList<>();
    private LinkedList<Circuit> m_listeCircuits = new LinkedList<>();
    
    private int m_compteurArrets;
    private int m_compteurCircuits;
    private int m_compteurSources;
    
    private PileSelectionTransport m_pileSelection = new PileSelectionTransport();
    
    private ReseauRoutier m_reseauRoutier;
    
    public ReseauTransport(){
        m_compteurArrets = 1;
        m_compteurCircuits = 1;
        m_compteurSources = 1;
        
        m_reseauRoutier = new ReseauRoutier();
    }
    
    public ReseauTransport(ReseauTransport p_reseauSource){
        this.m_reseauRoutier = new ReseauRoutier(p_reseauSource.m_reseauRoutier);
        
        int indexInter;  
        int indexTroncon;
        for (Arret arretSource : p_reseauSource.getListeArrets())
        {
            if (arretSource.getEmplacement().estSurTroncon())
            {
                indexInter = p_reseauSource.m_reseauRoutier.getIntersections().indexOf(arretSource.getEmplacement().getTroncon().getOrigine());
                indexTroncon = p_reseauSource.m_reseauRoutier.getIntersections().get(indexInter).getTroncons().indexOf(arretSource.getEmplacement().getTroncon());
                
                Intersection interCopiee = this.m_reseauRoutier.getIntersections().get(indexInter);
                this.m_listeArrets.add(new Arret(new Emplacement(true, arretSource.getEmplacement().getPourcentageParcouru(), 
                        interCopiee.getTroncons().get(indexTroncon), interCopiee)));  // UNE TITE LIGNE LOUIS ?
            }
            else
            {
                indexInter = p_reseauSource.m_reseauRoutier.getIntersections().indexOf(arretSource.getEmplacement().getIntersection());
                
                this.m_listeArrets.add(new Arret(new Emplacement(false, 0, null, this.m_reseauRoutier.getIntersections().get(indexInter))));
            }
            
            this.m_listeArrets.getLast().setNom(arretSource.getNom());        
        }
        
        int indexArretInitiale;
        int indexArretFinale;        
        for (Circuit circuitSource : p_reseauSource.m_listeCircuits)
        {
            LinkedList<PaireArretTrajet> pairesCopiees = new LinkedList<>();
            for (ListIterator<PaireArretTrajet> itPaireSource = circuitSource.getListeArretTrajet().listIterator() ; itPaireSource.hasNext() ; )
            {
                PaireArretTrajet paireSource = itPaireSource.next();
                
                indexArretInitiale = p_reseauSource.m_listeArrets.indexOf(paireSource.getArret());
                
                if (itPaireSource.hasNext())
                {
                    Arret arretInitialeCopiee = this.m_listeArrets.get(indexArretInitiale);
                    indexArretFinale = p_reseauSource.m_listeArrets.indexOf(itPaireSource.next().getArret());
                    itPaireSource.previous();
                    
                    LinkedList<Troncon> tronconsTrajetCopie = new LinkedList<>();
                    for (Troncon tronconSource : paireSource.getTrajet().getListeTroncons())
                    {
                        indexInter = p_reseauSource.m_reseauRoutier.getIntersections().indexOf(tronconSource.getOrigine());
                        indexTroncon = p_reseauSource.m_reseauRoutier.getIntersections().get(indexInter).getTroncons().indexOf(tronconSource);
                        
                        tronconsTrajetCopie.add(this.m_reseauRoutier.getIntersections().get(indexInter).getTroncons().get(indexTroncon));
                    }
                    
                    pairesCopiees.add(new PaireArretTrajet(arretInitialeCopiee, new Trajet(arretInitialeCopiee.getEmplacement(), 
                            this.m_listeArrets.get(indexArretFinale).getEmplacement(), tronconsTrajetCopie)));                
                }
                else
                {
                    pairesCopiees.add(new PaireArretTrajet(this.m_listeArrets.get(indexArretInitiale), null));
                }
            }
            
            this.m_listeCircuits.add(new Circuit(pairesCopiees));
            this.m_listeCircuits.getLast().setNom(circuitSource.getNom());
            this.m_listeCircuits.getLast().setPeutBoucler(circuitSource.peutBoucler());
            this.m_listeCircuits.getLast().setVeutBoucler(circuitSource.veutBoucler());
            this.m_listeCircuits.getLast().setCouleur(circuitSource.getCouleur());
            
            for (SourceAutobus sourceSource : circuitSource.getListeSources())
            {
                if (sourceSource.getEmplacement().estSurTroncon())
                {
                    indexInter = p_reseauSource.m_reseauRoutier.getIntersections().indexOf(sourceSource.getEmplacement().getTroncon().getOrigine());
                    indexTroncon = p_reseauSource.m_reseauRoutier.getIntersections().get(indexInter).getTroncons().indexOf(sourceSource.getEmplacement().getTroncon());

                    Intersection interCopiee = this.m_reseauRoutier.getIntersections().get(indexInter);
                    this.m_listeCircuits.getLast().getListeSources().add(new SourceAutobus(new Emplacement(true, sourceSource.getEmplacement().getPourcentageParcouru(), 
                            interCopiee.getTroncons().get(indexTroncon), interCopiee), this.m_listeCircuits.getLast()));  // UNE TITE LIGNE LOUIS ?
                }
                else
                {
                    indexInter = p_reseauSource.m_reseauRoutier.getIntersections().indexOf(sourceSource.getEmplacement().getIntersection());

                    this.m_listeCircuits.getLast().getListeSources().add(new SourceAutobus(new Emplacement(false, 0, 
                            null, this.m_reseauRoutier.getIntersections().get(indexInter)),  this.m_listeCircuits.getLast()));
                }
                
                this.m_listeCircuits.getLast().getListeSources().getLast().setNom(sourceSource.getNom());
                this.m_listeCircuits.getLast().getListeSources().getLast().setDistribution(sourceSource.getDistribution());
                this.m_listeCircuits.getLast().getListeSources().getLast().setTempsAttenteInitial(sourceSource.getTempsAttenteInitial());
                this.m_listeCircuits.getLast().getListeSources().getLast().setNbMaxAutobus(sourceSource.getNbMaxAutobus());
                this.m_listeCircuits.getLast().getListeSources().getLast().setCapaciteAutobus(sourceSource.getCapaciteAutobus());
            }
        }
        
        this.m_compteurArrets = p_reseauSource.m_compteurArrets;
        this.m_compteurCircuits = p_reseauSource.m_compteurCircuits;
        this.m_compteurSources = p_reseauSource.m_compteurSources;
    }
    
    public ReseauRoutier getRoutier()
    {
        return m_reseauRoutier;
    }
    
    public LinkedList<Circuit> getListeCircuits(){
        return m_listeCircuits;
    }
    public LinkedList<Arret> getListeArrets (){
        return m_listeArrets;
    }
    public void ajouterArret(Arret p_arret){
        p_arret.setNom("AR" + Integer.toString(m_compteurArrets));
        m_compteurArrets++;
        m_listeArrets.add(p_arret);
    }
    public void setListeCircuits(LinkedList<Circuit> listeCircuits){
        m_listeCircuits = listeCircuits;
    }
    public void ajouterCircuit(Circuit circ){
        circ.setNom("C"+ Integer.toString(m_compteurCircuits));
        m_compteurCircuits++;
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
   
   public SourceAutobus ajoutSource(Emplacement p_emplacement, Circuit p_circuit){
       SourceAutobus src = new SourceAutobus(p_emplacement, p_circuit);
       src.setNom("S" + Integer.toString(m_compteurSources));
       m_compteurSources++;
       p_circuit.ajouterSource(src);
       return src;
   }
   
   public Arret creerArret(Emplacement emplacement){
       return new Arret(emplacement);
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
        return emplacementsSontConnectables(arr1.getEmplacement(), arr2.getEmplacement());
    }
    
    public Boolean emplacementsSontConnectables(Emplacement empl1, Emplacement empl2){
        
        //cas de base
        if (empl1.estSurTroncon() && empl2.estSurTroncon()){
            if(empl1.getTroncon()==empl2.getTroncon()){
                if(empl1.getPourcentageParcouru() < empl2.getPourcentageParcouru()){
                    return true;
                }
            }
            if(empl1.getTroncon().getDestination()==empl2.getTroncon().getOrigine()){
                return true;
            }
        }
        else if(empl1.estSurTroncon()){
            if(empl1.getTroncon().getDestination()==empl2.getIntersection()){
                return true;
            }
        }
        else if(empl2.estSurTroncon()){
            if(empl1.getIntersection()==empl2.getTroncon().getOrigine()){
                return true;
            }
        }
        
        Intersection inter1;
        Intersection inter2;
        if (empl1.estSurTroncon()){
            inter1 = empl1.getTroncon().getDestination();
        }
        else{
            inter1 = empl1.getIntersection();
        }
        if (empl2.estSurTroncon()){
            inter2 = empl2.getTroncon().getOrigine();
        }
        else{
            inter2 = empl2.getIntersection();
        }
        
        LinkedList<Intersection> intersectionsVerifiees = new LinkedList<>();
        return intersectionsConnectees(inter1, inter2, intersectionsVerifiees);
    }
    
    public Boolean intersectionsConnectees(Intersection inter1, Intersection inter2, LinkedList<Intersection> intersectionsVerifiees){
        Boolean est_connecte = false;
        for (Intersection intr : inter1.getEnfants()){
            if (intr.equals(inter2)){
                return true;
            }
            else{
                if (!intersectionsVerifiees.contains(intr)){
                    intersectionsVerifiees.add(intr);
                    est_connecte =  intersectionsConnectees(intr, inter2, intersectionsVerifiees);
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
