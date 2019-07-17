/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.dao;

import edocs.meg.spec.dto.MachineTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import pl.zbiksoft.edocs.meg.entities.Machine;

/**
 *
 * @author ZbikKomp
 */
@Stateless
@LocalBean
public class MachineDao extends Dao {

    public List<Machine> getMachines() {
        return em.createQuery("select m from Machine m").getResultList();
    }
    
    public Machine getMachineById(int id) {
        return em.find(Machine.class, id);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
