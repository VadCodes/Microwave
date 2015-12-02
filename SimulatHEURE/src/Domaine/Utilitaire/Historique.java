package Domaine.Utilitaire;

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
    
    private LinkedList<ReseauRoutier> m_reseauxRoutiers = new LinkedList<>();
    private ListIterator<ReseauRoutier> m_curseurAvantRoutierCourant = m_reseauxRoutiers.listIterator();
    private ReseauRoutier m_reseauRoutierCourant;
    
    private Boolean m_peutAnnuler = false;
    private Boolean m_peutRetablir = false;
    
    public Historique()
    {
        m_curseurAvantRoutierCourant.add(new ReseauRoutier());
        m_reseauRoutierCourant = m_curseurAvantRoutierCourant.previous();
    }

    public ReseauRoutier getRoutierCourant()
    {
        return m_reseauRoutierCourant;
    }    
    
    public Boolean peutAnnuler()
    {
        return m_peutAnnuler;
    }
    
    public Boolean peutRetablir()
    {
        return m_peutRetablir;
    }

    public void modifier()
    {
        m_curseurAvantRoutierCourant.add(new ReseauRoutier(m_reseauRoutierCourant));
        
        m_curseurAvantRoutierCourant.next();
        
        //m_pointeurReseauRoutierCourant.forEachRemaining(ListIterator.remove());
        while (m_curseurAvantRoutierCourant.hasNext())
        {
            m_curseurAvantRoutierCourant.next();
            m_curseurAvantRoutierCourant.remove();
        }
        
        m_curseurAvantRoutierCourant.previous();  // on replace le curseur en avant du courant
        
        m_peutAnnuler = true;
        m_peutRetablir = false;
    }
    
    public void annuler()
    {
        m_reseauRoutierCourant = m_curseurAvantRoutierCourant.previous();
        m_peutAnnuler = m_curseurAvantRoutierCourant.hasPrevious();
        
        m_peutRetablir = true;
    }
    
    public void retablir()
    {
        m_curseurAvantRoutierCourant.next();
        m_reseauRoutierCourant = m_curseurAvantRoutierCourant.next();
        m_peutRetablir = m_curseurAvantRoutierCourant.hasNext();
        m_curseurAvantRoutierCourant.previous();  // on replace le curseur en avant du courant
        
        m_peutAnnuler = true;
    }
        
    }