/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.local.beans;

import edocs.meg.spec.dto.MachineTO;
import java.util.List;
import javax.ejb.Local;
import pl.zbiksoft.edocs.meg.entities.Machine;

/**
 *
 * @author ZbikKomp
 */
@Local
public interface MachineBeanLocal {

    Machine getMachineById(int id);
    
    List<MachineTO> getMachines();

    List<Integer> getMachineIds();
    
}
