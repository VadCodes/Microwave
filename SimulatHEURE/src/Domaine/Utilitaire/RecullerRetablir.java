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
public class RecullerRetablir {
    private String m_annuler = "";
    private String m_retablir = "";
    public RecullerRetablir(){}
    public void ajouterAction(String p_action){
            
        p_action = p_action.concat("\n");
        m_annuler = p_action.concat(m_annuler);
        
    }     
    public String getLastAction(){
        if(m_annuler.equals("")){
            return null;
        }
        else{
        BufferedReader reader = new BufferedReader(new StringReader(m_annuler));
        String action = null;
        try{
            action = reader.readLine();
            m_retablir = m_retablir.concat(action.concat("\n"));
            if(!action.isEmpty()){
                m_annuler = m_annuler.substring(m_annuler.indexOf("\n") + 1);
            }
        }
        catch (Exception e){
            System.out.println("errorLog");
        }
        return action;
        }
    }
}

