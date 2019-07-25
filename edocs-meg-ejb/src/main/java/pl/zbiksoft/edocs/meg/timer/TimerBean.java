/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.timer;

import java.util.Date;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import pl.zbiksoft.edocs.meg.beans.simulation.MachineSimulator;
import pl.zbiksoft.edocs.meg.local.beans.SimulationBeanLocal;

/**
 *
 * @author ZbikKomp
 */
@Singleton
public class TimerBean implements TimerBeanLocal {

    @EJB
    private SimulationBeanLocal simulator;
    
    @Resource
    private TimerService service;
    
    @Override
    public Timer createTimer(int owner, Date d, TimerMode mode) {
        return service.createTimer(d, new Pair<>(owner, mode));
    }

    @Override
    public Timer createTimer(int owner, long d, TimerMode mode) {
        return service.createTimer(d, new Pair<>(owner, mode));
    }
    
    @Timeout
    public void handle(Timer t) {
        Pair p = (Pair) t.getInfo();
        int owner = (Integer) p.getKey();
        TimerMode mode = (TimerMode) p.getValue();
        MachineSimulator ms = simulator.getMachines().get(owner);
        if (ms != null) {
            ms.handleTimeout(mode);
        }
    }
    private static final Logger LOG = Logger.getLogger(TimerBean.class.getName());

    public enum TimerMode {
        START_PRODUCTION,
        MANAGE_PRODUCTION,
        CYCLE,
        CYCLE_BREAK
    }
    
}
