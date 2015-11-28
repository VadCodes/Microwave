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
import Domaine.BesoinsTransport.Individu;
import Domaine.BesoinsTransport.PairePietonBus;
import Domaine.BesoinsTransport.TempsArriverPietons;
import Domaine.ReseauRoutier.Emplacement;
import Domaine.Utilitaire.Temps;
import java.util.LinkedList;
import java.util.ListIterator;

public class Arret extends ElementTransport{
    private Emplacement m_emplacement;
    private String m_nom = "";
    private int m_nombreIndividu = 0;
    private LinkedList<TempsArriveeAutobus> m_fileAutobus = new LinkedList<>();
    private LinkedList<TempsArriverPietons> m_filePietons = new LinkedList<>();
    private LinkedList<PairePietonBus> m_pietonEnAttenteDeSortir = new LinkedList<>();
    public final static float RAYON = 10;
    
    public Arret(Emplacement emplacement, String nom){
        m_emplacement = emplacement;
        m_nom = nom;
    }
    
    public Arret(){
        
    }
    public void addPietonAttenteDeSortirBus(PairePietonBus p_paire){
        m_pietonEnAttenteDeSortir.add(p_paire);
    }
    public void viderFile(){
        m_fileAutobus.clear();
    }
    public void ajouterPieton(Temps tempsArrivee, Individu individu){
        m_filePietons.add(new TempsArriverPietons(tempsArrivee, individu));
    }
    public void ajouterAutobus(Temps tempsArrivee, Autobus autobus)
    {
        m_fileAutobus.offer(new TempsArriveeAutobus(tempsArrivee, autobus));
    }
    
    public int nbAutobus()
    {
        return m_fileAutobus.size();
    }
    
    public String getNom(){
        return m_nom;
    }
    
    public void setNom(String nom){
        m_nom = nom;
    }
    
    public Emplacement getEmplacement(){
        return m_emplacement;
    }
    
    public void setEmplacement(Emplacement empl){
        m_emplacement = empl;
    }
    
    public void incrementerNbreIndividu(){
        m_nombreIndividu++;
    }
    
    public int getNbreIndividu(){
        return m_nombreIndividu;
    }
    public void miseAJourArret(){
          for (ListIterator<TempsArriverPietons> tmppietons = m_filePietons.listIterator(); tmppietons.hasNext();) {
            TempsArriverPietons tmppieton = tmppietons.next();
            Circuit circuitPieton = tmppieton.getPieton().getProchaineCircuit();
            for (ListIterator<TempsArriveeAutobus> tmpbuss = m_fileAutobus.listIterator(); tmpbuss.hasNext();) {
                TempsArriveeAutobus tmpbus = tmpbuss.next();
                Circuit circuitBus = tmpbus.getAutobus().getCircuit();
                if(circuitPieton.equals(circuitBus)){
                    if(tmppieton.getTempsArrivee().getTemps() < tmpbus.getTempsArrivee().getTemps()){
                        if(tmpbus.getAutobus().getCapaciteMax() > tmpbus.getAutobus().getnbPassager()){
                             tmppieton.getPieton().setIndividuEstDansBus(true,tmpbus.getAutobus());
                              tmpbus.getAutobus().setPlusUnIndividu();
                              tmppietons.remove();
                        }
                       
                    }
                }
            }
        }
        sortieAutobus();
    }
    private void sortieAutobus(){
        for (ListIterator<PairePietonBus> tmppietons = m_pietonEnAttenteDeSortir.listIterator(); tmppietons.hasNext();) {
            PairePietonBus tmppieton = tmppietons.next();
            for (ListIterator<TempsArriveeAutobus> tmpbuss = m_fileAutobus.listIterator(); tmpbuss.hasNext();) {
                TempsArriveeAutobus tmpbus = tmpbuss.next();
                if(tmpbus.getAutobus().equals(tmppieton.getBus())){
                    tmppieton.getPieton().setIndividuEstDansBus(false, null);
                    tmppieton.getPieton().setTempsApparition(tmpbus.getTempsArrivee());
                    tmpbus.getAutobus().setmoinsUnIndividu();
                }
            }
        }
    }
    
}
