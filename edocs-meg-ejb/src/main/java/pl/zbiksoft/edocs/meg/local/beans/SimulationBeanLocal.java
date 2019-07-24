/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.local.beans;

import java.util.Map;
import javax.ejb.Local;
import pl.zbiksoft.edocs.meg.beans.simulation.MachineSimulator;

/**
 *
 * @author ZbikKomp
 */
@Local
public interface SimulationBeanLocal {
    
    Map<Integer, MachineSimulator> getMachines();
}
