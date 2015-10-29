/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BesoinsTransport;

/**
 *
 * @author vadimcote
 */
import java.util.List;
import java.util.ArrayList;
public class Itineraire {
    private final List<PaireParcours> list;
    
    public Itineraire(){
        this.list = new ArrayList<PaireParcours>();
    }
}
  