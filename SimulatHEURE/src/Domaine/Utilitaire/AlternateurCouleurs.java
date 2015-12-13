/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Utilitaire;

import java.awt.Color;

/**
 *
 * @author louis
 */
public class AlternateurCouleurs { //SINGLETON ALRIGHT
   private static AlternateurCouleurs m_this = new AlternateurCouleurs( );
   private final static Integer[] m_couleurs = new Integer[] { 
        0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xFF00FF, 0x00FFFF,
        0xC00000, 0x00C000, 0x0000C0, 0xC0C000, 0xC000C0, 0x00C0C0,
        0xA00000, 0x00A000, 0x0000A0, 0xA0A000, 0xA000A0, 0x00A0A0,
        0xE00000, 0x00E000, 0x0000E0, 0xE0E000, 0xE000E0, 0x00E0E0,
        0x990000, 0x009900, 0x009900, 0x909000, 0x900090, 0x009090, 
    };
    private static Integer m_couleurIndex = (int)(Math.random() * ((m_couleurs.length) + 1));

   private AlternateurCouleurs(){ }

   public static AlternateurCouleurs getInstance() {
      return m_this;
   }
   
   public static Integer getCouleur(){
       Integer couleur = m_couleurs[m_couleurIndex];
       incrementerCompteur();
       return couleur;
   }
   
   public static Color getCouleurCircuit(){
       Integer couleur = getCouleur();
       return new Color(couleur & 0x808080);
   }
   
   public static Color getCouleurItineraire(){
       Integer couleur = getCouleur();
       return new Color(couleur);
   }
   
   private static void incrementerCompteur(){
       m_couleurIndex+=1;
//m_couleurIndex=m_couleurIndex+1+(int)(Math.random() * (2));
       if(m_couleurIndex>=m_couleurs.length){
           m_couleurIndex = m_couleurIndex%m_couleurs.length;
       }
   }
}
