/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans;

import edocs.meg.spec.dto.MachineTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ZbikKomp
 */
@Remote
public interface MachineBeanRemote {

    List<MachineTO> getMachines();
    
}
