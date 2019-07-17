/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import pl.zbiksoft.edocs.meg.entities.EventType;

/**
 *
 * @author ZbikKomp
 */
@Stateless
public class EventsLogDao extends Dao {

    private static final List<String> EVENT_TYPES = new ArrayList<>();

    public static final String START_RECORDER = "START_RECORDER";
    
    public static final String STOP_RECORDER = "STOP_RECORDER";
    
    public static final String EVENT_PRODUCTION_START = "EVENT_PRODUCTION_START";
    
    public static final String EVENT_PRODUCTION_STOP = "EVENT_PRODUCTION_STOP";

    public static final String MACHINE_CYCLE_START = "MACHINE_CYCLE_START";
    
    public static final String MACHINE_CYCLE_STOP = "MACHINE_CYCLE_STOP";
    
    public static final String MACHINE_PIECE_PRODUCTED = "MACHINE_PIECE_PRODUCED";
    
    static {
        EVENT_TYPES.add(STOP_RECORDER);
        EVENT_TYPES.add(START_RECORDER);
        EVENT_TYPES.add(EVENT_PRODUCTION_START);
        EVENT_TYPES.add(EVENT_PRODUCTION_STOP);
        EVENT_TYPES.add(MACHINE_CYCLE_START);
        EVENT_TYPES.add(MACHINE_PIECE_PRODUCTED);
        EVENT_TYPES.add(MACHINE_CYCLE_STOP);
    }
    
    public EventType getEventTypeById(int id) {
        return em.find(EventType.class, id);
    }
    
    public List<EventType> getEventTypes() {
        return em.createQuery("select et from EventType et where et.acronim in :list")
                .setParameter("list", EVENT_TYPES)
                .getResultList();
    }
    
    
}
