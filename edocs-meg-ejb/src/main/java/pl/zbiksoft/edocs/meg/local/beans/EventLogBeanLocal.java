/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.local.beans;

import javax.ejb.Local;
import pl.zbiksoft.edocs.meg.entities.EventType;

/**
 *
 * @author ZbikKomp
 */
@Local
public interface EventLogBeanLocal {

    EventType getEventTypeById(int id);
    
    void saveEvent(Integer machineId, Integer eventTypeId);

    
}
