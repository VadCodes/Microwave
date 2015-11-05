/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;
import ReseauRoutier.ReseauRoutier;
import ReseauTransport.ReseauTransport;
import BesoinsTransport.BesoinTransport;

import java.util.LinkedList;


/**
 *
 * @author vinny
 */
public class Simulatheure {
    private ReseauRoutier m_reseauRoutier = new ReseauRoutier();
    private ReseauTransport m_reseauTransport = new ReseauTransport();
    private LinkedList<BesoinTransport> m_listBesoins = new LinkedList();
    
    public Simulatheure() { }

}
