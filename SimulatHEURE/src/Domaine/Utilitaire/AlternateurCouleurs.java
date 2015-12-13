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
   private final static String[] m_couleurs = new String[] { 
        "FF0000", "00FF00", "0000FF", "FFFF00", "FF00FF", "00FFFF",
        "800000", "008000", "000080", "808000", "800080", "008080",
        "C00000", "00C000", "0000C0", "C0C000", "C000C0", "00C0C0",
        "600000", "006000", "000060", "606000", "600060", "006060", 
        "A00000", "00A000", "0000A0", "A0A000", "A000A0", "00A0A0",
        "E00000", "00E000", "0000E0", "E0E000", "E000E0", "00E0E0",
        "990000", "009900", "009900", "909000", "900090", "009090", 
    };
    private static Integer m_couleurIndex = (int)(Math.random() * ((m_couleurs.length) + 1));

   private AlternateurCouleurs(){ }

   public static AlternateurCouleurs getInstance() {
      return m_this;
   }
   
   public static Color getCouleur(){
       String couleur = m_couleurs[m_couleurIndex];
       incrementerCompteur();
       return new Color(Integer.parseInt(couleur, 16));
   }
   
   private static void incrementerCompteur(){
       m_couleurIndex=m_couleurIndex+1+(int)(Math.random() * (4));
       if(m_couleurIndex>=m_couleurs.length){
           m_couleurIndex = m_couleurIndex%m_couleurs.length;
       }
   }
}
