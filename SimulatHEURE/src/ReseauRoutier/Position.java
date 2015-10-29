/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReseauRoutier;

/**
 *
 * @author Nathaniel
 */
public class Position {
        private double m_x;
        private double m_y;
        public Position(double x, double y){
            m_x = x;
            m_y = y;
        }
        public double getPositionX(){
            return m_x;
    }
         public double getPositionY(){
            return m_y;
    }
         public double distanceEntrePositions(Position position){
             
         }
}
