/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domaine.Utilitaire;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 *
 * @author ns222
 */
public class Simulatheure_log {
    private String m_log = "";
    private String m_retablir = "";
    public Simulatheure_log(){}
    public void ajouterAction(String p_action){
            
        p_action = p_action.concat("\n");
        m_log = p_action.concat(m_log);
        
    }     
    public String getLastAction(){
        if(m_log.equals("")){
            return null;
        }
        else{
        BufferedReader reader = new BufferedReader(new StringReader(m_log));
        String action = null;
        try{
            action = reader.readLine();
            if(!action.isEmpty()){
                m_log = m_log.substring(m_log.indexOf("\n") + 1);
            }
        }
        catch (Exception e){
            System.out.println("errorLog");
        }
        return action;
        }
    }
}
