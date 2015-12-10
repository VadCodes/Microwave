package Domaine.Utilitaire;

import Domaine.Reseau;

import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauTransport.ReseauTransport;
import Domaine.BesoinsTransport.*;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author Vinny
 */
public class Historique implements java.io.Serializable {

    private LinkedList<Reseau> m_reseauxTransports = new LinkedList<>();
    private ListIterator<Reseau> m_curseurAvantTransportCourant = m_reseauxTransports.listIterator();
    private Reseau m_reseauTransportCourant;

    private Boolean m_peutAnnuler = false;
    private Boolean m_peutRetablir = false;

    public Historique() {
        m_curseurAvantTransportCourant.add(new ReseauTransport());
        m_curseurAvantTransportCourant.add(new ReseauTransport());  // Pour conserver un réseau vide
        m_reseauTransportCourant = m_curseurAvantTransportCourant.previous();  // on replace le curseur avant le reseau courant
    }

    public ReseauRoutier getRoutierCourant() {
        return ((ReseauTransport)m_reseauTransportCourant).getRoutier();
    }
    
    public ReseauTransport getTransportCourant() {
        return (ReseauTransport)m_reseauTransportCourant;
    }

    public ListIterator<Reseau> getCurseur() {
        return m_curseurAvantTransportCourant;
    }
    
    public void setCurseur(Integer p_indexCurseur) {
        m_curseurAvantTransportCourant = m_reseauxTransports.listIterator(p_indexCurseur);
    }
    
    public void clearCurseur() {
        m_curseurAvantTransportCourant = null;
    }
    
    public void initCurseur() {
        Integer p_indexCurseur = m_reseauxTransports.size() - 1;
        m_curseurAvantTransportCourant = m_reseauxTransports.listIterator(p_indexCurseur);
        m_reseauTransportCourant = m_curseurAvantTransportCourant.next();
        m_curseurAvantTransportCourant.previous();  // on replace le curseur avant le reseau courant
    }

    public Boolean peutAnnuler() {
        return m_peutAnnuler;
    }

    public Boolean peutRetablir() {
        return m_peutRetablir;
    }

    public void modifier() {
        
        modifierSelon(m_reseauTransportCourant, m_curseurAvantTransportCourant);
    }

    private void modifierSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {
        
        //if (p_reseau.getClass() == ReseauTransport.class)
            p_curseur.add(new ReseauTransport((ReseauTransport)m_reseauTransportCourant));
            viderApresReseauCourant();
    }
    
    public void viderApresReseauCourant() {
        m_curseurAvantTransportCourant.next();

        //p_iterateur.forEachRemaining(ListIterator.remove());
        while (m_curseurAvantTransportCourant.hasNext()) {
            m_curseurAvantTransportCourant.next();
            m_curseurAvantTransportCourant.remove();
        }

        m_curseurAvantTransportCourant.previous();  // on replace le curseur avant le reseau courant
        
        m_peutAnnuler = true;
        m_peutRetablir = false;
    }

    public void annuler() {
        
        annulerSelon(m_reseauTransportCourant, m_curseurAvantTransportCourant);
        m_peutRetablir = true;
    }

    private void annulerSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {
        
        p_curseur.previous();
        p_curseur.remove();
        
        //if (p_reseau.getClass() == ReseauTransport.class)
            m_reseauTransportCourant = p_curseur.previous();        
        
        m_peutAnnuler = p_curseur.hasPrevious();

        //if (p_reseau.getClass() == ReseauTransport.class)
            p_curseur.add(new ReseauTransport((ReseauTransport)m_reseauTransportCourant));
    }

    public void retablir() {
        
        retablirSelon(m_reseauTransportCourant, m_curseurAvantTransportCourant);
        m_peutAnnuler = true;
    }
    
    private void retablirSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {
        
        p_curseur.next();
        p_curseur.remove();
        
        //if (p_reseau.getClass() == ReseauTransport.class)
            m_reseauTransportCourant = p_curseur.next();
        
        m_peutRetablir = p_curseur.hasNext();
        p_curseur.previous();  // on replace le curseur en avant du courant
        
        //if (p_reseau.getClass() == ReseauTransport.class)
            p_curseur.add(new ReseauTransport((ReseauTransport)m_reseauTransportCourant));
    }

}
