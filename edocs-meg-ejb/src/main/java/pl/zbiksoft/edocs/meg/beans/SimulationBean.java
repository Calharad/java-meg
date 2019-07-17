/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans;

import edocs.meg.spec.simulation.SimulationConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
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

    //check whether baseTimer has ended production
    private boolean productionFinished = false;

    //if STOP, baseTimer represents time to start production
    //if RUN, production has started
    //if PAUSE, machine has a break
    private MachineState state = MachineState.STOP;

    @PreDestroy
    public void onDestroy() {
        if (state != MachineState.STOP) {
            stop();
        }
    }

    @Override
    public void start(SimulationConfig newConfig) {
        if (newConfig != null) {
            this.config.updateConfig(newConfig);
        }
        if (config.getMachineId() > 0) {
            LocalTime lt = LocalTime.now();
            LOG.log(Level.INFO, "Actual Time: {0}", lt.toString());
            LOG.log(Level.INFO, "Start time: {0}", config.getStartTime().toString());
            LOG.log(Level.INFO, "Stop time: {0}", config.getStopTime().toString());
            if (config.getStartTime().isBefore(lt) && config.getStopTime().isAfter(lt)) {
                LOG.log(Level.INFO, "Starting . . .");
                startSimulation();
            } else {
                LOG.log(Level.INFO, "Setting timer . . .");
                setStartTimer();

            }
        }
    }
    private static final Logger LOG = Logger.getLogger(SimulationBean.class.getName());

    @Override
    public void updateConfig(SimulationConfig config) {
        LOG.log(Level.INFO, "Restarting simulation . . .");
        stop();
        start(config);
    }

    @Override
    public void stop() {
        if (baseTimer != null) {
            baseTimer.cancel();
        }
        if (cycleTimer != null) {
            cycleTimer.cancel();
        }
        productionTime = 0;
        if (state == MachineState.RUN) {
            eventLogBean.saveEvent(config.getMachineId(), MACHINE_CYCLE_STOP);
            eventLogBean.saveEvent(config.getMachineId(), EVENT_PRODUCTION_STOP);
        }
        eventLogBean.saveEvent(config.getMachineId(), STOP_RECORDER);
        SimulationBaseConfig.restartConfig(config);
        state = MachineState.STOP;

    }

    private void startSimulation() {
        state = MachineState.STOP;
        handleState();
        LOG.log(Level.INFO, "Simulation started at {0}", LocalDateTime.now().toString());

    }

    @Timeout
    public void handleTimeout(Timer t) {
        switch ((TimerMode) t.getInfo()) {
            case START_PRODUCTION:
                startSimulation();
                break;
            case MANAGE_PRODUCTION:
                handleState();
                break;
            case CYCLE:
                finishCycle();
                break;
        }
    }

    private void finishCycle() {
        eventLogBean.saveEvent(config.getMachineId(), MACHINE_PIECE_PRODUCED);
        eventLogBean.saveEvent(config.getMachineId(), MACHINE_CYCLE_STOP);
        if (productionFinished) {
            handleState();
        } else {
            startCycle();
        }
    }

    private void startCycle() {
        cycleTimer = service.createTimer(config.getCycleInterval().getValue(), TimerMode.CYCLE);
        eventLogBean.saveEvent(config.getMachineId(), MACHINE_CYCLE_START);
    }

    private void handleState() {
        switch (state) {
            case STOP:
                state = MachineState.RUN;
                eventLogBean.saveEvent(config.getMachineId(), START_RECORDER);
                productionTime = config.getInterval().getValue() * 1000;
                baseTimer = service.createTimer(productionTime, TimerMode.MANAGE_PRODUCTION);
                eventLogBean.saveEvent(config.getMachineId(), EVENT_PRODUCTION_START);
                startCycle();
                break;
            case PAUSE:
                if (LocalTime.now().isAfter(config.getStopTime())) {
                    LOG.log(Level.INFO, "Simulation stooped at {0}", LocalDateTime.now().toString());
                    state = MachineState.STOP;
                    eventLogBean.saveEvent(config.getMachineId(), STOP_RECORDER);
                    setStartTimer();
                } else {
                    state = MachineState.RUN;
                    productionTime = config.getInterval().getValue() * 1000;
                    baseTimer = service.createTimer(productionTime, TimerMode.MANAGE_PRODUCTION);
                    eventLogBean.saveEvent(config.getMachineId(), EVENT_PRODUCTION_START);
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
                    eventLogBean.saveEvent(config.getMachineId(), EVENT_PRODUCTION_STOP);
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
        LOG.log(Level.INFO, "Date: {0}", date.toString());
        baseTimer = service.createTimer(date, TimerMode.START_PRODUCTION);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private enum TimerMode {
        START_PRODUCTION,
        MANAGE_PRODUCTION,
        CYCLE
    }

}
