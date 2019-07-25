/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans.simulation;

import edocs.meg.spec.simulation.SimulationConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Timer;
import pl.zbiksoft.edocs.meg.events.EventSender;
import pl.zbiksoft.edocs.meg.util.MachineState;
import pl.zbiksoft.edocs.meg.util.SimulationBaseConfig;
import edocs.meg.spec.util.StopWatch;
import java.io.Serializable;
import pl.zbiksoft.edocs.meg.timer.TimerBean.TimerMode;
import pl.zbiksoft.edocs.meg.timer.TimerBeanLocal;

/**
 *
 * @author ZbikKomp
 */
public class MachineSimulator implements Serializable {

    private SimulationBaseConfig config = new SimulationBaseConfig();

    private final StopWatch watch = new StopWatch();

    //check whether baseTimer has ended production
    private boolean productionFinished = false;

    //if STOP, baseTimer represents time to start production
    //if RUN, production has started
    //if PAUSE, machine has a break
    private MachineState state = MachineState.STOP;

    private long productionTime = 0;

    private final TimerBeanLocal service;

    private final EventSender sender;

    private String url;

    private Timer baseTimer;

    private Timer cycleTimer;

    private MachineSimulator(TimerBeanLocal service, EventSender sender) {
        this.service = service;
        this.sender = sender;
    }

    public MachineSimulator(TimerBeanLocal service, EventSender sender, SimulationConfig cfg) {
        this(service, sender);
        this.config.updateConfig(cfg);
        //url = ApplicationConnector.LOCALHOST
        url = "http://192.168.30.3:38080/"
                + "edocs-monitoring/secure/monitoring/machineDashboard.jsf?id=" + getMachineId();

    }

    public MachineSimulator(TimerBeanLocal service, EventSender sender, SimulationBaseConfig cfg) {
        this(service, sender);
        this.config = cfg;
        //url = ApplicationConnector.LOCALHOST
        url = "http://192.168.30.3:38080/"
                + "edocs-monitoring/secure/monitoring/machineDashboard.jsf?id=" + getMachineId();

    }

    private static final Logger LOG = Logger.getLogger(MachineSimulator.class.getName());

    public MachineState getState() {
        return state;
    }

    public SimulationBaseConfig getConfig() {
        return config;
    }

    public SimulationConfig getConfigTO() {
        return SimulationBaseConfig.toSimulationConfig(config);
    }

    public final int getMachineId() {
        return config.getMachineId();
    }

    public String getUrl() {
        return url;
    }

    public void updateConfig(SimulationConfig cfg) {
        if (state != MachineState.STOP) {
            stop();
            start();
        } else {
            config.updateConfig(cfg);
        }
    }

    public void restartConfig() {
        if (state != MachineState.STOP) {
            stop();
            SimulationBaseConfig.restartConfig(config);
            start();
        } else {
            SimulationBaseConfig.restartConfig(config);
        }
    }

    public void start() {
        if (state == MachineState.STOP) {
            if (config.getMachineId() > 0) {
                LocalTime lt = LocalTime.now();
                if (config.getStartTime().isBefore(lt) && config.getStopTime().isAfter(lt)) {
                    startSimulation();
                } else {
                    setStartTimer();
                }
            }
        } else {
            LOG.log(Level.WARNING, "Machine with id {0} already started", config.getMachineId());
        }
    }

    public void stop() {
        if (state != MachineState.STOP) {
            state = MachineState.STOP;
            productionTime = 0;
            sender.addEvent(MACHINE_CYCLE_STOP, config.getMachineId());
            sender.addAndSendEvent(EVENT_PRODUCTION_STOP, config.getMachineId());
            sender.addAndSendEvent(STOP_RECORDER, config.getMachineId());

            LOG.log(Level.INFO, "Machine with id {0} has been stopped", config.getMachineId());
        }
    }

    private void startSimulation() {
        state = MachineState.STOP;
        handleStateChange();
        LOG.log(Level.INFO, "Machine with id {0} has been started", config.getMachineId());

    }

    public void handleTimeout(TimerMode mode) {
        if (state != MachineState.STOP) {
            switch (mode) {
                case START_PRODUCTION:
                    startSimulation();
                    break;
                case MANAGE_PRODUCTION:
                    handleStateChange();
                    break;
                case CYCLE:
                    finishCycle();
                    break;
                case CYCLE_BREAK:
                    startCycle();
                    break;
            }
        }
    }

    private void finishCycle() {
        sender.addEvent(MACHINE_PIECE_PRODUCED, config.getMachineId());
        sender.addAndSendEvent(MACHINE_CYCLE_STOP, config.getMachineId());
        if (productionFinished) {
            handleStateChange();
        } else if (config.getCycleBreak() > 0) {
            cycleTimer = service.createTimer(getMachineId(), config.getCycleBreak(), TimerMode.CYCLE_BREAK);
        } else {
            startCycle();
        }
    }

    private void startCycle() {
        cycleTimer = service.createTimer(getMachineId(), config.getCycleInterval().getValue(), TimerMode.CYCLE);
        sender.addAndSendEvent(MACHINE_CYCLE_START, config.getMachineId());
    }

    private void handleStateChange() {
        switch (state) {
            case STOP:
                state = MachineState.RUN;
                sender.addEvent(START_RECORDER, config.getMachineId());
                productionTime = config.getInterval().getValue() * 1000;
                baseTimer = service.createTimer(getMachineId(), productionTime, TimerMode.MANAGE_PRODUCTION);
                sender.addAndSendEvent(EVENT_PRODUCTION_START, config.getMachineId());
                startCycle();
                break;
            case PAUSE:
                if (LocalTime.now().isAfter(config.getStopTime())) {
                    state = MachineState.STOP;
                    sender.addAndSendEvent(STOP_RECORDER, config.getMachineId());
                    setStartTimer();
                } else {
                    state = MachineState.RUN;
                    productionTime = config.getInterval().getValue() * 1000;
                    baseTimer = service.createTimer(getMachineId(), productionTime, TimerMode.MANAGE_PRODUCTION);
                    sender.addAndSendEvent(EVENT_PRODUCTION_START, config.getMachineId());
                    startCycle();
                }
                break;
            case RUN:
                if (!productionFinished) {
                    productionFinished = true;
                    watch.start();
                } else {
                    watch.stop();
                    productionFinished = false;
                    state = MachineState.PAUSE;
                    float usage = config.getMachineUsage();
                    baseTimer = service
                            .createTimer(getMachineId(), (long) ((productionTime + watch.getElapsedTime() - (productionTime + watch.getElapsedTime()) * usage) / usage),
                                    TimerMode.MANAGE_PRODUCTION);
                    sender.addAndSendEvent(EVENT_PRODUCTION_STOP, config.getMachineId());
                }
                break;
        }
    }

    private void setStartTimer() {
        LocalTime lt = LocalTime.now();
        Date date;
        if (config.getStartTime().isAfter(lt)) {
            date = new Date(LocalDateTime.of(LocalDate.now(), config.getStartTime())
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond() * 1000);
        } else {
            date = new Date(LocalDateTime.of(LocalDate.now().plusDays(1), config.getStartTime())
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond() * 1000);
        }
        baseTimer = service.createTimer(getMachineId(), date, TimerMode.START_PRODUCTION);
    }

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final int EVENT_PRODUCTION_START = 2000;
    static final int EVENT_PRODUCTION_STOP = 2001;

    private static final int MACHINE_PIECE_PRODUCED = 1020;

    private static final int START_RECORDER = 21;
    static final int STOP_RECORDER = 22;

    private static final int MACHINE_CYCLE_START = 1021;
    final static int MACHINE_CYCLE_STOP = 1022;
    //</editor-fold>

}
