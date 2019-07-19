/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.events;

import edocs.meg.spec.dto.controller.ControllerEventParameterTO;
import edocs.meg.spec.dto.controller.ControllerEventTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import pl.zbiksoft.edocs.meg.local.beans.EventLogBeanLocal;
import pl.zbiksoft.edocs.meg.util.SimulationBaseConfig;

/**
 *
 * @author ZbikKomp
 */
public class EventSender {

    public EventSender(SimulationBaseConfig config, EventLogBeanLocal eventLogBean) {
        this.config = config;
        this.eventLogBean = eventLogBean;
        df.setTimeZone(TimeZone.getDefault());
    }
    
    private final SimulationBaseConfig config;
    
    private final EventLogBeanLocal eventLogBean;
    
    private final List<ControllerEventTO> events = new ArrayList<>();

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public void addEvent(int eventType, ControllerEventParameterTO... additionalParameters) {
        ControllerEventTO event = new ControllerEventTO();
        if (additionalParameters != null) {
            event.getAdditionalParameters().addAll(Arrays.asList(additionalParameters));
        }
        event.setEventTypeId(eventType);
        event.setMachineId(config.getMachineId());
        Date date = new Date(System.currentTimeMillis());
        event.setPlcTime(df.format(date));
        events.add(event);
    }

    public void sendEvents() {
        Collections.reverse(events);
        eventLogBean.saveEventByController(events);
        events.clear();
    }

    public void addAndSendEvent(int eventType, ControllerEventParameterTO... additionalParameters) {
        addEvent(eventType, additionalParameters);
        sendEvents();
    }
    
}
