/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Utilitaire;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Nathaniel
 */
public class Temps {
    private double m_temps;
    public Temps(double p_temps)
    {
        m_temps = p_temps;
    }
    
    public double getTemps()
    {
        return m_temps;
    }
    public String getTempsFormat(){
        
        Date itemDate = new Date((long)((m_temps)));
        String itemDateStr = new SimpleDateFormat("HH:mm:ss").format(itemDate);
        System.out.println(itemDateStr);
        return itemDateStr;
    }
}