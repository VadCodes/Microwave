/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import javax.swing.JFileChooser;
import java.io.File; 

/**
 *
 * @author nathaniel
 */
public class Fider {
    private String m_option;
    private MainWindow m_mainwindow;
    private JFileChooser jFileChooser1 = new JFileChooser();
   
    public Fider(){
        
    }
    public void setOption(String p_option){
         m_option = p_option;
    }
    public void setMainWindow(MainWindow mainwindow){
        m_mainwindow = mainwindow;
    }
    public void go(){
        int returnVal;
         if(m_option == "save"){
            returnVal= jFileChooser1.showSaveDialog(m_mainwindow);
         }
         else{
            returnVal = jFileChooser1.showOpenDialog(m_mainwindow);
         }
         
        if (returnVal == JFileChooser.APPROVE_OPTION)  {
            File file = jFileChooser1.getSelectedFile();
            m_mainwindow.m_fileChoosed = file;
            if(m_option == "charger"){
                m_mainwindow.charger();
            }
            else if(m_option == "save"){
                m_mainwindow.save();
            }
            else if (m_option == "gabarit"){
                m_mainwindow.chargerGabarit();
            }
        }
    }
}
