/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Marcin
 */
@Entity
@Table(name = "events_log", schema = "efi_prd")
@NamedNativeQueries({
    @NamedNativeQuery(name = "getMachineQualityControlStat", 
        query = "SELECT * FROM efi_prd.get_machine_quality_control_stat(?,?,?,?,?) ORDER BY machine_id, plc_time", resultClass = EventsLog.class)
})
public class EventsLog implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "plc_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date plcTime;
    
    @JoinColumn(name = "machine_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Machine machine;
    
    @Column(name = "machine_id")
    private Integer machineId;
    
    @JoinColumn(name = "event_type_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EventType eventTypeId;
    
    @Column(name = "event_number", nullable = false)
    private Integer eventNumber = 0;

    @Column(name = "unit_quantity", nullable = false)
    private Long unitQuantity = 0L;
    
    @Column(name = "group_id")
    private Integer groupId;

    
    public EventsLog() {
    }

    public EventsLog(EventsLog e) {
        this();
        this.eventNumber = e.eventNumber;
        this.eventTypeId = e.eventTypeId;
        this.groupId = e.groupId;
        this.id = e.id;
        this.machine = e.machine;
        this.plcTime = e.plcTime;
        this.unitQuantity = e.unitQuantity;
    }

    public EventsLog(Long id) {
        this();
        this.id = id;
    }

    public EventsLog(Long id, Machine machine, Integer groupId, EventType eventTypeId, Date plcTime, Long unitQuantity) {
        this();
        this.id = id;
        this.plcTime = plcTime;
        this.groupId = groupId;
        this.machine = machine;
        this.machineId = machine.getId();
        this.eventTypeId = eventTypeId;
        this.unitQuantity = unitQuantity;
    }
    
    public Integer getId() {
        return id.intValue();
    }

    public void setId(Integer id) {
        this.id = new Long(id);
    }

    public Calendar getPlcTime() {
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(plcTime);
        return tmp;
    }

    public void setPlcTime(Calendar plcTime) {
        this.plcTime = plcTime.getTime();
    }
    
    public Date getPlcDate(){
        return plcTime;
    }

    public void setPlcTime(Date plcTime) {
        this.plcTime = plcTime;
    }

    public void setUnitQuantity(Long unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public Long getUnitQuantity() {
        return unitQuantity;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
        this.machineId = machine.getId();
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
        if (machine == null || !machine.getId().equals(machineId))
            machine = new Machine(machineId);
    }

    public EventType getEventType() {
        return eventTypeId;
    }

    public void setEventType(EventType eventTypeId) {
        this.eventTypeId = (EventType) eventTypeId;
    }

    public Integer getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(Integer eventNumber) {
        this.eventNumber = eventNumber;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Boolean isStopReason() {
        return (getEventType().getAcronim().startsWith(EventType.stopReasonEventPrefix));
    }

    public Boolean isServiceStopReason() {
        return (getEventType().getAcronim().startsWith(EventType.stopReasonServiceEventPrefix));
    }

    public boolean compareRodzaj(String rodzajToCompare) {
        return (getEventType().getAcronim().compareTo(rodzajToCompare) == 0);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventsLog)) {
            return false;
        }
        EventsLog other = (EventsLog) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "edocs.statistics.ejb.dao.postgresql8362.mappings.prd.EventsLog[id=" + id + "] " + (eventTypeId.getAcronim() != null ? eventTypeId.getAcronim() : "UNKNOWN") + " " + plcTime;
    }
}
