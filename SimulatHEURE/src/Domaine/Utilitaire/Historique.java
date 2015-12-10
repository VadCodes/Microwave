package Domaine.Utilitaire;

import Domaine.Reseau;

import Domaine.ReseauRoutier.ReseauRoutier;
import Domaine.ReseauTransport.ReseauTransport;
import Domaine.BesoinsTransport.ReseauBesoins;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author Vinny
 */
public class Historique implements java.io.Serializable {

    private LinkedList<Reseau> m_reseauxBesoins = new LinkedList<>();
    private ListIterator<Reseau> m_curseurAvantBesoinCourant = m_reseauxBesoins.listIterator();
    private Reseau m_reseauBesoinsCourant;

    private Boolean m_peutAnnuler = false;
    private Boolean m_peutRetablir = false;

    public Historique() {
        m_curseurAvantBesoinCourant.add(new ReseauBesoins());
        m_curseurAvantBesoinCourant.add(new ReseauBesoins());  // Pour conserver un réseau vide
        m_reseauBesoinsCourant = m_curseurAvantBesoinCourant.previous();  // on replace le curseur avant le reseau courant
    }

    public ReseauRoutier getRoutierCourant() {
        return ((ReseauBesoins) m_reseauBesoinsCourant).getTransport().getRoutier();
    }

    public ReseauTransport getTransportCourant() {
        return ((ReseauBesoins) m_reseauBesoinsCourant).getTransport();
    }

    public ReseauBesoins getBesoinsCourant() {
        return (ReseauBesoins) m_reseauBesoinsCourant;
    }

    public ListIterator<Reseau> getCurseur() {
        return m_curseurAvantBesoinCourant;
    }

    public void setCurseur(Integer p_indexCurseur) {
        m_curseurAvantBesoinCourant = m_reseauxBesoins.listIterator(p_indexCurseur);
    }

    public void clearCurseur() {
        m_curseurAvantBesoinCourant = null;
    }

    public void initCurseur() {
        Integer p_indexCurseur = m_reseauxBesoins.size() - 1;
        m_curseurAvantBesoinCourant = m_reseauxBesoins.listIterator(p_indexCurseur);
        m_reseauBesoinsCourant = m_curseurAvantBesoinCourant.next();
        m_curseurAvantBesoinCourant.previous();  // on replace le curseur avant le reseau courant
    }

    public Boolean peutAnnuler() {
        return m_peutAnnuler;
    }

    public Boolean peutRetablir() {
        return m_peutRetablir;
    }

    public void modifier() {

        modifierSelon(m_reseauBesoinsCourant, m_curseurAvantBesoinCourant);
    }

    private void modifierSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {

        p_curseur.add(new ReseauBesoins((ReseauBesoins) m_reseauBesoinsCourant));
        viderApresReseauCourant();
    }

    public void viderApresReseauCourant() {

        m_curseurAvantBesoinCourant.next();

        //p_iterateur.forEachRemaining(ListIterator.remove());
        while (m_curseurAvantBesoinCourant.hasNext()) {
            m_curseurAvantBesoinCourant.next();
            m_curseurAvantBesoinCourant.remove();
        }

        m_curseurAvantBesoinCourant.previous();  // on replace le curseur avant le reseau courant

        m_peutAnnuler = true;
        m_peutRetablir = false;
    }

    public void annuler() {

        annulerSelon(m_reseauBesoinsCourant, m_curseurAvantBesoinCourant);
        m_peutRetablir = true;
    }

    private void annulerSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {

        p_curseur.previous();
        p_curseur.remove();

        m_reseauBesoinsCourant = p_curseur.previous();

        m_peutAnnuler = p_curseur.hasPrevious();

        p_curseur.add(new ReseauBesoins((ReseauBesoins) m_reseauBesoinsCourant));
    }

    public void retablir() {

        retablirSelon(m_reseauBesoinsCourant, m_curseurAvantBesoinCourant);
        m_peutAnnuler = true;
    }

    private void retablirSelon(Reseau p_reseau, ListIterator<Reseau> p_curseur) {

        p_curseur.next();
        p_curseur.remove();

        m_reseauBesoinsCourant = p_curseur.next();

        m_peutRetablir = p_curseur.hasNext();
        p_curseur.previous();  // on replace le curseur en avant du courant

        p_curseur.add(new ReseauBesoins((ReseauBesoins) m_reseauBesoinsCourant));
    }

}
