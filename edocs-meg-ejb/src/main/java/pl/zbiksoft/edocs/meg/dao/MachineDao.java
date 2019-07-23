/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.dao;

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
    
    public Long getMachineCount() {
        return em.createQuery("SELECT count(m.archived) from Machine m", Long.class).getSingleResult();
    }
    
    public Machine getMachineById(int id) {
        return em.find(Machine.class, id);
    }
    
    public List<Integer> getMachineIds() {
        return em.createQuery("select m.id from Machine m").getResultList();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
