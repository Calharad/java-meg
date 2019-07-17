/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Marcin
 */
@Entity
@Table(name = "machines", schema = "efi_prd")
public class Machine implements Serializable {

    public static final Integer CONTROLLER_MACHINE_ID = 1;
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;
    
    @Column(name = "id_description")
    private String idOpisowy;
    
    @Column(name = "short_description")
    private String opis;
    
    @Column(name = "long_comment")
    private String longComment;
    
    @Column(name = "object_name")
    private String objectName;
    
    @Column(name = "inventory_number")
    private String inventoryNumber;
    
    @Column(name = "system_id")
    private String systemId;
    
    @Column(name = "start_work_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date czasRozpPracy;
    
    @Column(name = "performance_for_min")
    private Double performanceForMinute;
    
    @Column(name = "stop_reason_entry_timeout_sec")
    private Long stopReasonEntryTimeout;
    
    @Column(name = "stop_reason_after_prod")
    private Boolean stopReasonAfterProd;
    
    @Column(name = "max_orders_number")
    private Integer maxOrdersNumber;
    
    @Column(name = "default_process_id")
    private Long defaultProcessId;
    
    @Column(name = "archived")
    private Boolean archived;
    
    @Column(name = "is_monitored")
    private boolean isMonitored;

    @Column(name = "msg_user_id")
    private Long msgUserId;
    
    @Column(name = "has_cycles_counter")
    private boolean hasCycleCounter;
    
    @Column(name = "parent_machine_id")
    private Long parentMachineId;
    
    @Column(name = "microstoppages_max_stop_time_sec")
    private int microstoppagesMaxStopTimeSec;
    
    @Column(name = "manual_workstation")
    private boolean manualWorkstation;
    
    public Machine() {
    }

    public Machine(Integer id) {
        this.id = id;
    }

    public Machine(Integer id, String idOpisowy, String opis,
            Integer iloscZmian, Integer czasZmiany, Date czasRozpPracy) {
        this.id = id;
        this.idOpisowy = idOpisowy;
        this.opis = opis;
    }

    public Machine(Integer id, String idOpisowy) {
        this.id = id;
        this.idOpisowy = idOpisowy;
    }

    public Machine(Integer id, String idOpisowy, String objectName) {
        this.id = id;
        this.idOpisowy = idOpisowy;
        this.objectName = objectName;
    }
    
    
    
    public Integer getId() {
        if (id == null) {
            id = 0;
        }
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }

    /**
     * np. "T-01"
     *
     * @return
     */
    public String getName() {
        return idOpisowy;
    }

    /**
     * np. "T-01"
     *
     * @param idOpisowy
     */
    public void setName(String idOpisowy) {
        this.idOpisowy = idOpisowy;
    }

    /**
     * np. "YH 1564 -1"
     *
     * @return
     */
    public String getShortDescription() {
        return opis;
    }

    /**
     * np. "YH 1564 -1"
     *
     * @param opis
     */
    public void setShortDescription(String opis) {
        this.opis = opis;
    }

    public String getFullName() {
        return String.format("%s %s", idOpisowy, objectName);
    }
    
    /**
     * Pobiera jaki jest typ maszyny
     *
     * @return
     */
    public String getLongComment() {
        return longComment;
    }

    public void setLongComment(String komentarzDlugi) {
        this.longComment = komentarzDlugi;
    }
    
    public Date getCzasRozpPracy() {
        return czasRozpPracy;
    }
//

    public void setCzasRozpPracy(Date czasRozpPracy) {
        this.czasRozpPracy = czasRozpPracy;
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
        if (!(object instanceof Machine)) {
            return false;
        }
        Machine other = (Machine) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    @Override
    public String toString() {
        return getName();
    }

    public String getIdDescription() {
        return idOpisowy;
    }

    public void setIdDescription(String idDescription) {
        idOpisowy = idDescription;
    }

    public void setShortComment(String ShortComment) {
    }

    public void setLongDescription(String LongDescription) {
    }

    public Double getPerformanceForMinute() {
        return performanceForMinute;
    }

    public void setPerformanceForMinute(Double performanceForMinute) {
        this.performanceForMinute = performanceForMinute;
    }

    public Long getStopReasonEntryTimeout() {
        return stopReasonEntryTimeout;
    }

    public void setStopReasonEntryTimeout(Long stopReasonEntryTimeout) {
        this.stopReasonEntryTimeout = stopReasonEntryTimeout;
    }

    public void setWorking(Integer working) {
    }

    public Boolean getStopReasonAfterProd() {
        return stopReasonAfterProd;
    }

    public void setStopReasonAfterProd(Boolean stopReasonAfterProd) {
        this.stopReasonAfterProd = stopReasonAfterProd;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public Integer getMaxOrdersNumber() {
        return maxOrdersNumber;
    }

    public void setMaxOrdersNumber(Integer maxOrdersNumber) {
        this.maxOrdersNumber = maxOrdersNumber;
    }

    public Long getDefaultProcessId() {
        return defaultProcessId;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public boolean isMonitored() {
        return isMonitored;
    }

    public void setIsMonitored(boolean isMonitored) {
        this.isMonitored = isMonitored;
    }

    public Long getMsgUserId() {
        return msgUserId;
    }

    public void setMsgUserId(Long msgUserId) {
        this.msgUserId = msgUserId;
    }

    public boolean isHasCycleCounter() {
        return hasCycleCounter;
    }

    public void setHasCycleCounter(boolean hasCycleCounter) {
        this.hasCycleCounter = hasCycleCounter;
    }

    public Long getParentMachineId() {
        return parentMachineId;
    }

    public void setParentMachineId(Long parentMachineId) {
        this.parentMachineId = parentMachineId;
    }

    public int getMicrostoppagesMaxStopTimeSec() {
        return microstoppagesMaxStopTimeSec;
    }

    public void setMicrostoppagesMaxStopTimeSec(int microstoppagesMaxStopTimeSec) {
        this.microstoppagesMaxStopTimeSec = microstoppagesMaxStopTimeSec;
    }

    public boolean isManualWorkstation() {
        return manualWorkstation;
    }

    public void setManualWorkstation(boolean manualWorkstation) {
        this.manualWorkstation = manualWorkstation;
    }
}
