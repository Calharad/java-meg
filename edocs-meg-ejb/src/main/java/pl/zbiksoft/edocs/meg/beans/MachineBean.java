/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans;

import edocs.meg.spec.dto.MachineTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import pl.zbiksoft.edocs.meg.local.beans.MachineBeanLocal;
import javax.ejb.Stateless;
import pl.zbiksoft.edocs.meg.dao.MachineDao;
import pl.zbiksoft.edocs.meg.entities.Machine;

/**
 *
 * @author ZbikKomp
 */
@Stateless
public class MachineBean implements MachineBeanRemote, MachineBeanLocal {

    @EJB
    private MachineDao machineDao;
    
    @Override
    public List<MachineTO> getMachines() {
        List<MachineTO> result = new ArrayList<>();
        machineDao.getMachines().forEach(m -> {
            MachineTO mTO = new MachineTO();
            mTO.setId(m.getId());
            mTO.setLongComment(m.getLongComment());
            mTO.setShortDescription(m.getShortDescription());
            mTO.setObjectName(m.getObjectName());
            mTO.setIdDescription(m.getIdDescription());
            result.add(mTO);
        });
        return result;
    }
    
    

    @Override
    public Machine getMachineById(int id) {
        return machineDao.getMachineById(id);
    }

    @Override
    public long getMachineCount() {
        return machineDao.getMachineCount();
    }

    @Override
    public List<Integer> getMachineIds() {
        return machineDao.getMachineIds();
    }
}
