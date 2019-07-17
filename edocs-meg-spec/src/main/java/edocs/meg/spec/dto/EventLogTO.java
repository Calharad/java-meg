/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.spec.dto;

import java.util.Date;

/**
 *
 * @author ZbikKomp
 */
public class EventLogTO {
    
    private Integer id;
    
    private MachineTO machine;
    
    private EventTypeTO eventType;
    
    private Date databaseTime;

    public EventLogTO() {
    }

    public EventLogTO(Integer id) {
        this.id = id;
    }

    public EventLogTO(MachineTO machine, EventTypeTO eventType, Date databaseTime) {
        this.machine = machine;
        this.eventType = eventType;
        this.databaseTime = databaseTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MachineTO getMachine() {
        return machine;
    }

    public void setMachine(MachineTO machine) {
        this.machine = machine;
    }

    public EventTypeTO getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeTO eventType) {
        this.eventType = eventType;
    }

    public Date getDatabaseTime() {
        return databaseTime;
    }

    public void setDatabaseTime(Date databaseTime) {
        this.databaseTime = databaseTime;
    }

    
    
}
