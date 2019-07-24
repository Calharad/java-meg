/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.events;

import edocs.meg.spec.dto.controller.ControllerEventParameterTO;
import edocs.meg.spec.dto.controller.ControllerEventTO;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import pl.zbiksoft.edocs.meg.local.beans.EventLogBeanLocal;

/**
 *
 * @author ZbikKomp
 */
public class EventSender implements Serializable {

    public EventSender(EventLogBeanLocal eventLogBean) {
        this.eventLogBean = eventLogBean;
        df.setTimeZone(TimeZone.getDefault());
    }
        
    private final EventLogBeanLocal eventLogBean;
    
    private final List<ControllerEventTO> events = new ArrayList<>();

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public void addEvent(int eventType, int machineId, ControllerEventParameterTO... additionalParameters) {
        ControllerEventTO event = new ControllerEventTO();
        if (additionalParameters != null) {
            event.setAdditionalParameters(Arrays.asList(additionalParameters));
        }
        event.setEventTypeId(eventType);
        event.setMachineId(machineId);
        Date date = new Date(System.currentTimeMillis());
        event.setPlcTime(df.format(date));
        events.add(event);
    }

    public void sendEvents() {
        Collections.reverse(events);
        eventLogBean.saveEventByController(events);
        events.clear();
    }

    public void addAndSendEvent(int eventType, int machineId, ControllerEventParameterTO... additionalParameters) {
        addEvent(eventType, machineId, additionalParameters);
        sendEvents();
    }
    
}
