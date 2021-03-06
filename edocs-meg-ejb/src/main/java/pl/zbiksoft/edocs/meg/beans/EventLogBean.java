/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans;

import edocs.meg.spec.dto.EventTypeTO;
import edocs.meg.spec.dto.controller.ControllerEventTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import pl.zbiksoft.edocs.meg.local.beans.EventLogBeanLocal;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import pl.zbiksoft.edocs.meg.dao.EventsLogDao;
import pl.zbiksoft.edocs.meg.entities.EventType;
import pl.zbiksoft.edocs.meg.entities.EventsLog;
import pl.zbiksoft.edocs.meg.entities.Machine;
import pl.zbiksoft.edocs.meg.local.beans.MachineBeanLocal;
import pl.zbiksoft.edocs.meg.util.ApplicationConnector;

/**
 *
 * @author ZbikKomp
 */
@Stateless
public class EventLogBean implements EventLogBeanRemote, EventLogBeanLocal {

    public static final String SAVE_EVENTS_ENDPOINT = "edocs-controller-ws/api/controller/save-events";

    private static final String DEFAULT_LINK = "http://192.168.30.3:38080/";
    
    private static String link = null;

    @EJB
    private EventsLogDao eventsLogDao;

    @EJB
    private MachineBeanLocal machineBean;
    private static final Logger LOG = Logger.getLogger(EventLogBean.class.getName());
    
    @PostConstruct
    public void init() {
        link = ApplicationConnector.isControllerAvailable()
                ? ApplicationConnector.LOCALHOST : DEFAULT_LINK;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void saveEvent(Integer machineId, Integer eventTypeId) {
        Machine machine = machineBean.getMachineById(machineId);
        EventType type = getEventTypeById(eventTypeId);
        if (machine != null && type != null) {
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

    @Override
    public void saveEventByController(List<ControllerEventTO> events) {
        ControllerEventTO[] array = events.toArray(new ControllerEventTO[events.size()]);
        Client client = ClientBuilder.newClient();
        WebTarget myTarget = client.target(link + SAVE_EVENTS_ENDPOINT);
        myTarget.request(MediaType.TEXT_PLAIN).post(Entity.json(array));
    }
}
