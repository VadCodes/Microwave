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
public class Historique {

    private LinkedList<Reseau> m_reseauxRoutiers = new LinkedList<>();
    private ListIterator<Reseau> m_curseurAvantRoutierCourant = m_reseauxRoutiers.listIterator();
    private Reseau m_reseauRoutierCourant;

    private Boolean m_peutAnnuler = false;
    private Boolean m_peutRetablir = false;

    public Historique() {
        m_curseurAvantRoutierCourant.add(new ReseauRoutier());
        m_curseurAvantRoutierCourant.add(new ReseauRoutier());  // Pour conserver un réseau vide
        m_reseauRoutierCourant = m_curseurAvantRoutierCourant.previous();  // on replace le curseur avant le reseau courant
    }

    public ReseauRoutier getRoutierCourant() {
        return (ReseauRoutier)m_reseauRoutierCourant;
    }

    public Boolean peutAnnuler() {
        return m_peutAnnuler;
    }

    public Boolean peutRetablir() {
        return m_peutRetablir;
    }

    public void modifier() {
        
        modifierSelon(m_reseauRoutierCourant, m_curseurAvantRoutierCourant);
        m_peutAnnuler = true;
        m_peutRetablir = false;
    }

    private void modifierSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {
        
        if (p_reseau.getClass() == ReseauRoutier.class)
            p_curseur.add(new ReseauRoutier((ReseauRoutier)m_reseauRoutierCourant));

        p_curseur.next();

        //p_iterateur.forEachRemaining(ListIterator.remove());
        while (p_curseur.hasNext()) {
            p_curseur.next();
            p_curseur.remove();
        }

        p_curseur.previous();  // on replace le curseur avant le reseau courant
    }

    public void annuler() {
        
        annulerSelon(m_reseauRoutierCourant, m_curseurAvantRoutierCourant);
        m_peutRetablir = true;
    }

    private void annulerSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {
        
        p_curseur.previous();
        p_curseur.remove();
        
        if (p_reseau.getClass() == ReseauRoutier.class)
            m_reseauRoutierCourant = p_curseur.previous();        
        
        m_peutAnnuler = p_curseur.hasPrevious();

        if (p_reseau.getClass() == ReseauRoutier.class)
            p_curseur.add(new ReseauRoutier((ReseauRoutier)m_reseauRoutierCourant));
    }

    public void retablir() {
        
        retablirSelon(m_reseauRoutierCourant, m_curseurAvantRoutierCourant);
        m_peutAnnuler = true;
    }
    
    private void retablirSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {
        
        p_curseur.next();
        p_curseur.remove();
        
        if (p_reseau.getClass() == ReseauRoutier.class)
            m_reseauRoutierCourant = p_curseur.next();
        
        m_peutRetablir = p_curseur.hasNext();
        p_curseur.previous();  // on replace le curseur en avant du courant
        
        if (p_reseau.getClass() == ReseauRoutier.class)
            p_curseur.add(new ReseauRoutier((ReseauRoutier)m_reseauRoutierCourant));
    }

}
