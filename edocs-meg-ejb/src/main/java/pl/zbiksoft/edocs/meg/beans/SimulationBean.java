/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans;

import edocs.meg.spec.dto.controller.ControllerEventParameterTO;
import edocs.meg.spec.dto.controller.ControllerEventTO;
import edocs.meg.spec.simulation.SimulationConfig;
import edocs.meg.spec.util.Interval;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import pl.zbiksoft.edocs.meg.events.EventSender;
import pl.zbiksoft.edocs.meg.util.SimulationBaseConfig;
import pl.zbiksoft.edocs.meg.local.beans.EventLogBeanLocal;
import pl.zbiksoft.edocs.meg.util.MachineState;
import pl.zbiksoft.edocs.meg.util.StopWatch;

/**
 *
 * @author ZbikKomp
 */
@Singleton
@Stateless
public class SimulationBean implements SimulationBeanRemote {

    private final int EVENT_PRODUCTION_START = 2000;
    private final int EVENT_PRODUCTION_STOP = 2001;

    private final int MACHINE_PIECE_PRODUCED = 1020;

    private final int START_RECORDER = 21;
    private final int STOP_RECORDER = 22;

    private final int MACHINE_CYCLE_START = 1021;
    private final int MACHINE_CYCLE_STOP = 1022;

    @Resource
    private TimerService service;

    @EJB
    private EventLogBeanLocal eventLogBean;

    private long productionTime = 0;

    private final SimulationBaseConfig config = new SimulationBaseConfig();

    private Timer cycleTimer;

    private Timer baseTimer;

    private final StopWatch watch = new StopWatch();
    
    private EventSender sender;

    //check whether baseTimer has ended production
    private boolean productionFinished = false;

    //if STOP, baseTimer represents time to start production
    //if RUN, production has started
    //if PAUSE, machine has a break
    private MachineState state = MachineState.STOP;
    
    @PostConstruct
    public void init() {
         sender = new EventSender(config, eventLogBean);
    }
    
    
    @PreDestroy
    public void onDestroy() {
        if (state != MachineState.STOP) {
            stop();
        }
    }

    @Override
    public void start(SimulationConfig newConfig) {
        if (state == MachineState.STOP) {
            LOG.log(Level.INFO, "Starting simulation . . .");
            if (newConfig != null) {
                this.config.updateConfig(newConfig);
                LOG.log(Level.INFO, "Config updated");
            }
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
    private static final Logger LOG = Logger.getLogger(SimulationBean.class.getName());

    @Override
    public void updateConfig(SimulationConfig config) {
        if (state != MachineState.STOP) {
            LOG.log(Level.INFO, "Restarting simulation . . .");
            stop();
            start(config);
        } else {
            this.config.updateConfig(config);
        }
        LOG.log(Level.INFO, "Config updated successfully");
    }

    @Override
    public void stop() {
        if (state != MachineState.STOP) {
            LOG.log(Level.INFO, "Stopping simulation . . .");
            service.getTimers().forEach(t -> t.cancel());
            LOG.log(Level.INFO, "Timers stopped");
            productionTime = 0;
            if (state == MachineState.RUN) {
                sender.addEvent(MACHINE_CYCLE_STOP, null);
                LOG.log(Level.INFO, "Cycle stopped");
                //eventLogBean.saveEvent(config.getMachineId(), EVENT_PRODUCTION_STOP);
                sender.addEvent(EVENT_PRODUCTION_STOP, null);
                LOG.log(Level.INFO, "Production stopped");
            }
            //eventLogBean.saveEvent(config.getMachineId(), STOP_RECORDER);
            sender.addAndSendEvent(STOP_RECORDER, null);
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
        LOG.log(Level.INFO, t.getInfo().toString());
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
//        eventLogBean.saveEvent(config.getMachineId(), MACHINE_PIECE_PRODUCED);
//        eventLogBean.saveEvent(config.getMachineId(), MACHINE_CYCLE_STOP);
        sender.addEvent(MACHINE_PIECE_PRODUCED, null);
        sender.addAndSendEvent(MACHINE_CYCLE_STOP, null);
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
//        eventLogBean.saveEvent(config.getMachineId(), MACHINE_CYCLE_START);
        sender.addAndSendEvent(MACHINE_CYCLE_START, null);
    }

    private void handleStateChange() {
        switch (state) {
            case STOP:
                state = MachineState.RUN;
//                eventLogBean.saveEvent(config.getMachineId(), START_RECORDER);
                sender.addEvent(START_RECORDER, null);
                productionTime = config.getInterval().getValue() * 1000;
                baseTimer = service.createTimer(productionTime, TimerMode.MANAGE_PRODUCTION);
//                eventLogBean.saveEvent(config.getMachineId(), EVENT_PRODUCTION_START);
                sender.addAndSendEvent(EVENT_PRODUCTION_START, null);
                startCycle();
                break;
            case PAUSE:
                if (LocalTime.now().isAfter(config.getStopTime())) {
                    LOG.log(Level.INFO, "Simulation stooped at {0}", LocalDateTime.now().toString());
                    state = MachineState.STOP;
//                    eventLogBean.saveEvent(config.getMachineId(), STOP_RECORDER);
                    sender.addAndSendEvent(STOP_RECORDER, null);
                    setStartTimer();
                } else {
                    state = MachineState.RUN;
                    productionTime = config.getInterval().getValue() * 1000;
                    baseTimer = service.createTimer(productionTime, TimerMode.MANAGE_PRODUCTION);
//                    eventLogBean.saveEvent(config.getMachineId(), EVENT_PRODUCTION_START);
                    sender.addAndSendEvent(EVENT_PRODUCTION_START, null);
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
//                    eventLogBean.saveEvent(config.getMachineId(), EVENT_PRODUCTION_STOP);
                    sender.addAndSendEvent(EVENT_PRODUCTION_STOP, null);
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private enum TimerMode {
        START_PRODUCTION,
        MANAGE_PRODUCTION,
        CYCLE,
        CYCLE_BREAK
    }

    @Override
    public SimulationConfig getConfig() {
        SimulationConfig result = new SimulationConfig();
        result.setCycleBreak(config.getCycleBreak());
        Interval cycle = config.getCycleInterval();
        Interval interval = config.getInterval();
        result.setCycleTime((cycle.getMax() + cycle.getMin()) / 2);
        result.setCycleInterval(result.getCycleTime() - cycle.getMin());
        result.setMachine(config.getMachineId());
        result.setMachineUsage(config.getMachineUsage());
        result.setMaxInterval(interval.getMax());
        result.setMinInterval(interval.getMin());
        result.setStartTime(config.getStartTime().toString());
        result.setStopTime(config.getStopTime().toString());
        return result;
    }

    @Override
    public void restartConfig() {
        if (state != MachineState.STOP) {
            LOG.log(Level.INFO, "Restarting simulation . . .");
            stop();
            SimulationBaseConfig.restartConfig(config);
            start(null);
        } else {
            SimulationBaseConfig.restartConfig(config);
        }
    }

    

}
