/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans;

import edocs.meg.spec.dto.EventTypeTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import pl.zbiksoft.edocs.meg.local.beans.EventLogBeanLocal;
import javax.ejb.Stateless;
import pl.zbiksoft.edocs.meg.dao.EventsLogDao;
import pl.zbiksoft.edocs.meg.entities.EventType;
import pl.zbiksoft.edocs.meg.entities.EventsLog;
import pl.zbiksoft.edocs.meg.entities.Machine;
import pl.zbiksoft.edocs.meg.local.beans.MachineBeanLocal;

/**
 *
 * @author ZbikKomp
 */
@Stateless
public class EventLogBean implements EventLogBeanRemote, EventLogBeanLocal {

    @EJB
    private EventsLogDao eventsLogDao;
    
    @EJB
    private MachineBeanLocal machineBean;
   
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void saveEvent(Integer machineId, Integer eventTypeId) {
        Machine machine = machineBean.getMachineById(machineId);
        EventType type = getEventTypeById(eventTypeId);
        if(machine != null && type != null) {
            EventsLog eventLog = new EventsLog();
            eventLog.setMachine(machine);
            eventLog.setEventType(type);
            eventLog.setPlcTime(Calendar.getInstance());
            eventsLogDao.save(eventLog);
        }
    }
    
    @Override
    public EventType getEventTypeById(int id) {
        return eventsLogDao.getEventTypeById(id);
    }

    @Override
    public List<EventTypeTO> getEventTypes() {
        List<EventTypeTO> result = new ArrayList<>();
        
        eventsLogDao.getEventTypes().forEach(et -> {
            EventTypeTO etTO = new EventTypeTO();
            etTO.setId(et.getId());
            etTO.setAcronim(et.getAcronim());
            etTO.setShortDescription(et.getShortDescription());
            result.add(etTO);
        });
        
        return result;
    }
}
