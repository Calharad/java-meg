/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans.simulation;

import edocs.meg.spec.simulation.SimulationConfig;
import edocs.meg.spec.util.Interval;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import pl.zbiksoft.edocs.meg.events.EventSender;
import pl.zbiksoft.edocs.meg.util.MachineState;
import pl.zbiksoft.edocs.meg.util.SimulationBaseConfig;
import pl.zbiksoft.edocs.meg.util.StopWatch;

/**
 *
 * @author ZbikKomp
 */
public class MachineSimulator {

    private final SimulationBaseConfig config = new SimulationBaseConfig();

    private final StopWatch watch = new StopWatch();

    //check whether baseTimer has ended production
    private boolean productionFinished = false;

    //if STOP, baseTimer represents time to start production
    //if RUN, production has started
    //if PAUSE, machine has a break
    private MachineState state = MachineState.STOP;

    private long productionTime = 0;

    private final TimerService service;

    private final EventSender sender;

    private Timer baseTimer;

    private Timer cycleTimer;

    public MachineSimulator(TimerService service, EventSender sender) {
        this.service = service;
        this.sender = sender;
    }

    public MachineSimulator(TimerService service, EventSender sender, SimulationConfig cfg) {
        this.service = service;
        this.sender = sender;
        this.config.updateConfig(cfg);
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

    public int getMachineId() {
        return config.getMachineId();
    }

    public void updateConfig(SimulationConfig cfg) {
        if (state != MachineState.STOP) {
            LOG.log(Level.INFO, "Restarting simulation . . .");
            stop();
            start();
        } else {
            config.updateConfig(cfg);
        }
    }
    
    public void restartConfig() {
        if (state != MachineState.STOP) {
            LOG.log(Level.INFO, "Restarting simulation . . .");
            stop();
            SimulationBaseConfig.restartConfig(config);
            start();
        } else {
            SimulationBaseConfig.restartConfig(config);
        }
    }

    public void start() {
        if (state == MachineState.STOP) {
            LOG.log(Level.INFO, config.toString());
            if (config.getMachineId() > 0) {
                LocalTime lt = LocalTime.now();
                LOG.log(Level.INFO, "Actual Time: {0}", lt.toString());
                LOG.log(Level.INFO, "Start time: {0}", config.getStartTime().toString());
                LOG.log(Level.INFO, "Stop time: {0}", config.getStopTime().toString());
                if (config.getStartTime().isBefore(lt) && config.getStopTime().isAfter(lt)) {
                    LOG.log(Level.INFO, "Starting machine . . .");
                    startSimulation();
                } else {
                    LOG.log(Level.INFO, "Setting begin timer . . .");
                    setStartTimer();

                }
            }
        } else {
            LOG.log(Level.WARNING, "Simulation already started");
        }
    }

    public void stop() {
        if (state != MachineState.STOP) {
            LOG.log(Level.INFO, "Stopping simulation . . .");
            if(baseTimer != null && baseTimer.getTimeRemaining() > 0) {
                baseTimer.cancel();
            }
            
            if(cycleTimer != null && cycleTimer.getTimeRemaining() > 0) {
                cycleTimer.cancel();
            }
            
            LOG.log(Level.INFO, "Timers stopped");
            productionTime = 0;
            if (state == MachineState.RUN) {
                sender.addEvent(MACHINE_CYCLE_STOP, config.getMachineId());
                LOG.log(Level.INFO, "Cycle stopped");
                sender.addEvent(EVENT_PRODUCTION_STOP, config.getMachineId());
                LOG.log(Level.INFO, "Production stopped");
            }
            sender.addAndSendEvent(STOP_RECORDER, config.getMachineId());
            state = MachineState.STOP;
            LOG.log(Level.INFO, "Simulation stopped at {0}", LocalDateTime.now().toString());
        } else {
            LOG.log(Level.WARNING, "Simulation already stopped");
        }
    }

    private void startSimulation() {
        state = MachineState.STOP;
        handleStateChange();
        LOG.log(Level.INFO, "Simulation started at {0}", LocalDateTime.now().toString());

    }

    @Timeout
    public void handleTimeout(Timer t) {
        switch ((TimerMode) t.getInfo()) {
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

    private void finishCycle() {
        sender.addEvent(MACHINE_PIECE_PRODUCED, config.getMachineId());
        sender.addAndSendEvent(MACHINE_CYCLE_STOP, config.getMachineId());
        if (productionFinished) {
            handleStateChange();
        } else if (config.getCycleBreak() > 0) {
            cycleTimer = service.createTimer(config.getCycleBreak(), TimerMode.CYCLE_BREAK);
        } else {
            startCycle();
        }
    }

    private void startCycle() {
        cycleTimer = service.createTimer(config.getCycleInterval().getValue(), TimerMode.CYCLE);
        sender.addAndSendEvent(MACHINE_CYCLE_START, config.getMachineId());
    }

    private void handleStateChange() {
        switch (state) {
            case STOP:
                state = MachineState.RUN;
                sender.addEvent(START_RECORDER, config.getMachineId());
                productionTime = config.getInterval().getValue() * 1000;
                baseTimer = service.createTimer(productionTime, TimerMode.MANAGE_PRODUCTION);
                sender.addAndSendEvent(EVENT_PRODUCTION_START, config.getMachineId());
                startCycle();
                break;
            case PAUSE:
                if (LocalTime.now().isAfter(config.getStopTime())) {
                    LOG.log(Level.INFO, "Simulation stooped at {0}", LocalDateTime.now().toString());
                    state = MachineState.STOP;
                    sender.addAndSendEvent(STOP_RECORDER, config.getMachineId());
                    setStartTimer();
                } else {
                    state = MachineState.RUN;
                    productionTime = config.getInterval().getValue() * 1000;
                    baseTimer = service.createTimer(productionTime, TimerMode.MANAGE_PRODUCTION);
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
                            .createTimer((long) ((productionTime + watch.getElapsedTime() - (productionTime + watch.getElapsedTime()) * usage) / usage),
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
        LOG.log(Level.INFO, "Scheduled start time: {0}", date.toString());
        baseTimer = service.createTimer(date, TimerMode.START_PRODUCTION);
    }
    private enum TimerMode {
        START_PRODUCTION,
        MANAGE_PRODUCTION,
        CYCLE,
        CYCLE_BREAK
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
