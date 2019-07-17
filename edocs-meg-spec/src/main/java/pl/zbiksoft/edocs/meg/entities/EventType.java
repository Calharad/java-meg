/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Marcin
 */
@Entity
@Table(name = "events_type", /*catalog = "zps_monitoring",*/ schema = "efi_wsp")
public class EventType
        implements  Serializable {

//<editor-fold defaultstate="collapsed" desc="Constants">
    public static final int EVENT_NO_DATA = 50;
    
    public static final int EVENT_HOLDER_PRESENT = 108;
    public static final int EVENT_HOLDER_ABSENT = 109;
    public static final int EVENT_GOOD_PIECES_INSERTION = 110;
    
    public static final int EVENT_OPERATOR_LOGON = 100;
    public static final int EVENT_OPERATOR_LOGOUT = 101;
    
    public static final int EVENT_PRODUCTION_START = 2000;
    public static final int EVENT_PRODUCTION_STOP = 2001;
    
    public static final int EVENT_PRODUCTION_COUNTER_RESET = 107;
    
    public static final int EVENT_ENGINE_START = 1000;
    public static final int EVENT_ENGINE_STOP = 1001;
    public static final int EVENT_ACTIVE_WORKSPACE = 1010;
    public static final int EVENT_PIECE_PRODUCED = 1020;
    public static final int EVENT_BAD_PIECE = 1030;
    
    public static final int EVENT_MACHINE_CYCLE_START = 1021;
    public static final int EVENT_MACHINE_CYCLE_STOP = 1022;
    public static final int EVENT_MACHINE_CYCLE_CANCEL = 1023;
    public static final int EVENT_MACHINE_CYCLE_ORDER_END = 1024;
    public static final int MACHINE_PIECE_PRODUCED_IN_ADDITIONAL_STATE = 1040;
    
    public static final int EVENT_CONTROLLER_ON = 1;
    public static final int EVENT_CONTROLLER_OFF_DEMAND = 8;
    public static final int EVENT_CONTROLLER_OFF = 51;
    
    public static final int EVENT_DATA_ACQUISITION_START = 21;
    public static final int EVENT_DATA_ACQUISITION_STOP = 22;
    
    public static final int EVENT_MACHINE_COMM_LOST = 11;
    public static final int EVENT_MACHINE_COMM_ESTABLISHED = 12;
    
    public static final int EVENT_BUFFER_OVERFLOW = 15;
    
    public static final int EVENT_ORDER_GET = 102;
    public static final int EVENT_ORDER_FINISH = 103;
    public static final int EVENT_ORDER_BREAK = 104;
    public static final int EVENT_ORDER_CANCEL = 105;
    public static final int EVENT_OPERATION_GET = 111;
    public static final int EVENT_ORDER_PARAMETERS_INPUT = 112;
    public static final int EVENT_DETAIL_GET = 113;
    
    public static final int EVENT_WORKSPACE_CONFIG = 301;
    public static final int EVENT_MACHINE_PIECE_STATUS_INPUT = 302;
    public static final int EVENT_MACHINE_QCOPERATOR_PRESENT = 303;
    
    public static final int EVENT_FAILURE_CLOSED = 500;
    
    // setup mode events (przezbrojenie)
    public static final int EVENT_SETUP_START = 600;
    public static final int EVENT_SETUP_STOP = 601;
    
    public static final int EVENT_QC_RECALL = 602;
    public static final int EVENT_SAMPLE_TAKEN = 603;
    public static final int EVENT_SAMPLE_APPROVED = 604;
    public static final int EVENT_SAMPLE_REJECTED = 605;
    public static final int EVENT_QC_INTERCEPT = 606;
    public static final int EVENT_TECH_RECALL = 607;
    public static final int EVENT_TECH_INTERCEPT = 608;
    public static final int EVENT_QC_RECALL_FINISHED = 609;
    public static final int EVENT_TECH_RECALL_FINISHED = 610;
    public static final int EVENT_CUSTOM_STATE_START = 620;
    public static final int EVENT_CUSTOM_STATE_STOP = 621;
    
    public static final int EVENT_OPERATOR_MACHINE_ON = 3007;
    public static final int EVENT_OPERATOR_MACHINE_OFF = 3008;
    
    public static final int EVENT_SPINDLE_TEST_START = 700;
    public static final int EVENT_SPINDLE_TEST_FINISH = 701;
    public static final int EVENT_GEOMETRY_TEST_START = 702;
    public static final int EVENT_GEOMETRY_TEST_FINISH = 703;
    
    public static final int EVENT_PROGRAM_CHANGED = 3011;
    
    public static final int ALARM_LOW_1 = 6000;
    public static final int ALARM_HIGH_1 = 6001;
    
    public static final int ALARM_LOW_2 = 6002;
    public static final int ALARM_HIGH_2 = 6003;
    
    public static final int EVENT_SUPERVISED_PROCESS_START = 7000;
    public static final int EVENT_SUPERVISED_PROCESS_STOP = 7001;
    public static final int EVENT_SUPERVISED_PROCESS_START_FAILED = 7002;
    
    public static final int COLLISION_LOW_2 = 7002;
    public static final int COLLISION_HIGH_2 = 7003;
    
    public static final int EVENT_STOP_REASON_BRAK_OBSLUGI = 5007;
    public static final int EVENT_STOP_REASON_URUCHOMIENIE = 5018;
    public static final int STOP_REASON_BRAK_PRZYPISANEGO_ZLECENIA = 5145;
    public static final int STOP_REASON_BRAK_NOMINALNEJ_WYDAJNOSCI = 5146;
    public static final int STOP_REASON_MICROSTOPPAGE = 5025;
    
    
    // Stop reason not entered
    public static final int EVENT_GENERIC_STOP_REASON = 5019;
    
    public static final int EVENT_STOP_REASON_MACHINE_DISABLED = 5030;
    
    public static final int EVENT_STOP_REASON_ADDITIONAL_STATE = 5148;
    
    public static final String holderPresentEvent = "SEQ_HOLDER_PRESENT";
    public static final String holderAbsentEvent = "SEQ_HOLDER_ABSENT";
    public static final String goodPiecesEnterEvent = "SEQ_GOOD_PIECES_INPUT";
    
    public static final String logEvent = "SEQ_LOGOWANIE";
    public static final String wylogEvent = "SEQ_WYLOGOWANIE";
    
    public static final String controllerPowerOnEvent = "CONTROLLER_POWER_ON";
    public static final String controllerPowerOffEvent = "CONTROLLER_SYSTEM_SHUTDOWN";
    
    public static final String przejZmEvent = "SEQ_PRZEJECIE_ZMIANY";
    public static final String pobZlecEvent = "SEQ_POBRANIE_ZLECENIA";
    public static final String zakZlecEvent = "SEQ_ZAKONCZENIE_ZLECENIA";
    public static final String przerwZlecEvent = "SEQ_PRZERWANIE_ZLECENIA";
    public static final String anulZlecEvent = "SEQ_ANULOWANIE_ZLECENIA";
    
    public static final String rozpProdEvent = "EVENT_PRODUCTION_START";
    public static final String zakProdEvent = "EVENT_PRODUCTION_STOP";
    public static final String wyborPaletyEvent = "MACHINE_WORKSPACE_SELECTION";
    public static final String wyborOperacjiEvent = "SEQ_OPERATION_ID";
    public static final String wyborDetaluEvent = "SEQ_DETAIL_ID";
    //public static final String genericStopReasonEvent = "STOP_REASON_NIEZNANY";
    public static final String genericStopReasonEvent = "STOP_REASON_BRAK_POWODU";
    public static final String stopReasonEventPrefix = "STOP_REASON_";
    public static final String stopReasonServiceEventPrefix = "STOP_REASON_SERVICE";
    public static final String stopReasonBrakObsady = "STOP_REASON_BRAK_OBSADY";
    public static final String stopReasonBrakZlecenia = "STOP_REASON_BRAK_ZLECENIA";
    public static final String stopReasonRegulacja = "STOP_REASON_REGULACJA";
    public static final String stopReasonUruchomienie = "STOP_REASON_URUCHOMIENIE";
    public static final String stopReasonServiceWylog = "STOP_REASON_SERVICE_WYLOGOWANIE";
    public static final String stopReasonServiceZalog = "STOP_REASON_SERVICE_ZALOG";
    public static final String stopReasonSetting = "STOP_REASON_SETTING";
    public static final String stopReasonOffline = "STOP_REASON_OFFLINE";
    public static final String stopReasonAdditionalState = "STOP_REASON_ADDITIONAL_STATE";
    
    public static final String machineManualMode = "OPERATOR_SET_MANUAL_MODE";
    public static final String machineAutoMode = "OPERATOR_SET_AUTOMATIC_MODE";
    public static final String machineEngineStart = "MACHINE_MAIN_ENGINE_START";
    public static final String machineEngineStop = "MACHINE_MAIN_ENGINE_STOP";
    
    public static final String stopReasonsCategoryName = "POSTOJ";
    
    public static final String machinePaletteConfigSelect = "MACHINE_PALLETE_CONFIG_SELECT";
    
    public static final String machineStartSetupMode = "SETUP_START";
    public static final String machineEndSetupMode = "SETUP_STOP";
    
    public static final String qualityControlRecall = "QC_RECALL";
    public static final String qualityControlIntercept = "QC_INTERCEPT";
    public static final String qualityControlSampleTaken = "SAMPLE_TAKEN";
    public static final String qualityControlApprovedSample = "SAMPLE_APPROVED";
    public static final String qualityControlRejectedSample = "SAMPLE_REJECTED";
    public static final String technologistRecall = "TECH_RECALL";
    public static final String technologistIntercept = "TECH_INTERCEPT";
    public static final String qcRecallFinished = "QC_RECALL_FINISHED";
    public static final String techRecallFinished = "TECH_RECALL_FINISHED";
    
    private static final HashMap<Integer,String> eventAcronymsMap;
    static
    {
        eventAcronymsMap = new HashMap<>();
        eventAcronymsMap.put(EVENT_HOLDER_ABSENT, holderAbsentEvent);
        eventAcronymsMap.put(EVENT_HOLDER_PRESENT, holderPresentEvent);
        eventAcronymsMap.put(EVENT_PRODUCTION_START, rozpProdEvent);
        eventAcronymsMap.put(EVENT_PRODUCTION_STOP, zakProdEvent);
        eventAcronymsMap.put(EVENT_ENGINE_START, machineEngineStart);
        eventAcronymsMap.put(EVENT_ENGINE_STOP, machineEngineStop);
        eventAcronymsMap.put(EVENT_CONTROLLER_ON, controllerPowerOnEvent);
        eventAcronymsMap.put(EVENT_CONTROLLER_OFF, controllerPowerOffEvent);
    }
    
    public static String findAcronym(Integer eventTypeId)
    {
        if (eventAcronymsMap.containsKey(eventTypeId))
            return eventAcronymsMap.get(eventTypeId);
        else
            return "UNKNOWN";
    }
//</editor-fold>
    
    private static final long serialVersionUID = 23135428581L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", precision = 8, scale = 8)
    private Integer id;
    
    @Column(name = "priority")
    private Integer priority;
    
    @Column(name = "acronim", length = 120)
    private String acronim;
    
    @Column(name = "short_description", length = 200)
    private String shortDescription;

    @Column(name = "event_color", length = 8)
    private String eventColor;
    
    @Column(name = "is_stop_reason")
    private Boolean isStopReason;
    
    @Column(name = "active")
    private Boolean active;
    
    @Column(name = "inputtable_by_operator")
    private Boolean isInputtableByOperator;
    
    @Column(name = "mail_notification_enabled")
    private Boolean isMailNotificationEnabled;
    
    @Column(name = "mail_notification_address")
    private String mailNotificationAddress;
    
    @Column(name = "stop_interval_begin_from_event_date")
    private Boolean isStopIntervalBeginFromEventDate;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventTypeId", fetch = FetchType.LAZY)
    private Set<EventsLog> eventsLogs;
    
    public EventType() {
    }

    public EventType(Integer id, String acronim, String shortDescription) {
        super();
        this.id = id;
        this.acronim = acronim;
        this.shortDescription = shortDescription;
    }

    public EventType(Integer id, String acronim, String shortDescription, String color) {
        super();
        this.id = id;
        this.acronim = acronim;
        this.shortDescription = shortDescription;
        this.eventColor = color;
    }

    public EventType(Integer id) {
        super();
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

//    public EventType(EventsTypePK eventsTypePK) {
//        this.eventsTypePK = eventsTypePK;
//    }
//
//    public EventType(long id, long machineId) {
//        this.eventsTypePK = new EventsTypePK(id, machineId);
//    }
//
//    @Override
//    public EventsTypePK getEventsTypePK() {
//        return eventsTypePK;
//    }
//
//    public void setEventsTypePK(EventsTypePK eventsTypePK) {
//        this.eventsTypePK = eventsTypePK;
//    }
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getAcronim() {
        return acronim;
    }

    public void setAcronim(String acronim) {
        this.acronim = acronim;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setEventColor(String eventColor) {
        this.eventColor = eventColor;
    }

    public String getEventColor() {
        return eventColor;
    }

    public Boolean getIsStopReason() {
        return isStopReason;
    }

    public void setIsStopReason(Boolean isStopReason) {
        this.isStopReason = isStopReason;
    }

    public Boolean getIsInputtableByOperator() {
        return isInputtableByOperator;
    }

    public void setIsInputtableByOperator(Boolean isInputtableByOperator) {
        this.isInputtableByOperator = isInputtableByOperator;
    }

    public Boolean getIsStopIntervalBeginFromEventDate() {
        return isStopIntervalBeginFromEventDate;
    }

    public void setIsStopIntervalBeginFromEventDate(Boolean isStopIntervalBeginFromEventDate) {
        this.isStopIntervalBeginFromEventDate = isStopIntervalBeginFromEventDate;
    }

    /**
     * 
     * @return
     */
    public Collection<EventsLog> getEventsLogCollection() {
        return new ArrayList<>(eventsLogs);
//        if (Hibernate.isInitialized(eventsLogs)) {
//            return new ArrayList<EventsLog>(eventsLogs);
//        }
//        return null;
    }

    public void setEventsLogCollection(Collection<EventsLog> eventsTypeSet) {
        this.eventsLogs = new TreeSet<>(eventsTypeSet);
    }

    public Boolean getIsMailNotificationEnabled() {
        return isMailNotificationEnabled;
    }

    public void setIsMailNotificationEnabled(Boolean isMailNotificationEnabled) {
        this.isMailNotificationEnabled = isMailNotificationEnabled;
    }

    public String getMailNotificationAddress() {
        return mailNotificationAddress;
    }

    public void setMailNotificationAddress(String mailNotificationAddress) {
        this.mailNotificationAddress = mailNotificationAddress;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<EventsLog> getEventsLogs() {
        return eventsLogs;
    }

    public void setEventsLogs(Set<EventsLog> eventsLogs) {
        this.eventsLogs = eventsLogs;
    }

    public static EventType generateUnknownEventType() {
        return new EventType(0, "UNKNOWN", "UNKNOWN");
    }

    public static EventType generateGenericStopReason() {
        return new EventType(0, genericStopReasonEvent, genericStopReasonEvent);
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
        if (!(object instanceof EventType)) {
            return false;
        }
        EventType other = (EventType) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "edocs.base.dao.postgresql8362.mappings.wsp.EventsType[id=" + id + "]";
    }
}
