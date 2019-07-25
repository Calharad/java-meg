/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans.simulation;

import edocs.meg.spec.dto.utils.MachineStateTO;
import edocs.meg.spec.simulation.SimulationConfig;
import edocs.meg.spec.util.UniqueRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.zbiksoft.edocs.meg.beans.SimulationBeanRemote;
import pl.zbiksoft.edocs.meg.events.EventSender;
import pl.zbiksoft.edocs.meg.local.beans.EventLogBeanLocal;
import pl.zbiksoft.edocs.meg.local.beans.MachineBeanLocal;
import pl.zbiksoft.edocs.meg.local.beans.SimulationBeanLocal;
import pl.zbiksoft.edocs.meg.timer.TimerBeanLocal;
import pl.zbiksoft.edocs.meg.util.ApplicationConnector;
import pl.zbiksoft.edocs.meg.util.ConfigRandom;
import pl.zbiksoft.edocs.meg.util.MachineState;
import pl.zbiksoft.edocs.meg.util.SimulationBaseConfig;

/**
 *
 * @author ZbikKomp
 */
@Singleton
@Stateless
public class SimulationBean implements SimulationBeanRemote, SimulationBeanLocal {

    @Inject
    private TimerBeanLocal service;

    @EJB
    private EventLogBeanLocal eventLogBean;

    @EJB
    private MachineBeanLocal machineBean;

    private EventSender sender;

    private final Map<Integer, MachineSimulator> machineList = new HashMap<>();

    @PostConstruct
    public void init() {
        sender = new EventSender(eventLogBean);
    }

    @PreDestroy
    public void onDestroy() {
        machineList.values().forEach(m -> {
            if (m.getState() != MachineState.STOP) {
                m.stop();
            }
        });
    }

    @Override
    public void start(SimulationConfig newConfig) {
        MachineSimulator ms = machineList.get(newConfig.getMachine());
        if (ms != null) {
            ms.updateConfig(newConfig);
            ms.start();
        } else {
            ms = new MachineSimulator(service, sender, newConfig);
            ms.start();
            machineList.put(ms.getMachineId(), ms);
        }
    }

    private static final Logger LOG = Logger.getLogger(SimulationBean.class.getName());

    @Override
    public void updateConfig(SimulationConfig config) {
        MachineSimulator m = machineList.get(config.getMachine());
        if (m != null) {
            m.updateConfig(config);
        }
    }

    @Override
    public void stop(int machineId) {
        MachineSimulator ms = machineList.get(machineId);
        if (ms != null) {
            ms.stop();
            //machineList.remove(machineId);
        }
    }

    @Override
    public SimulationConfig getConfig(int machineId) {
        MachineSimulator m = machineList.get(machineId);
        if (m != null) {
            return m.getConfigTO();
        } else {
            return null;
        }
    }

    @Override
    public void restartConfig(int machineId) {
        MachineSimulator m = machineList.get(machineId);
        if (m != null) {
            m.restartConfig();
        }
    }

    @Override
    public void stopSimulationMachines() {
        machineList.values().forEach(m -> m.stop());
    }

    @Override
    public String getState(int machineId) {
        MachineSimulator m = machineList.get(machineId);
        if (m != null) {
            return m.getState().toString().toLowerCase();
        } else {
            return MachineState.STOP.toString().toLowerCase();
        }
    }

    @Override
    public SimulationConfig getDefaultConfig() {
        return new SimulationBaseConfig().toSimulationConfig();
    }

    @Override
    public void stopAllMachines() {
        stopSimulationMachines();
        machineList.clear();
        EventSender eventSender = new EventSender(eventLogBean);
        machineBean.getMachines().forEach(m -> {
            eventSender.addEvent(MachineSimulator.MACHINE_CYCLE_STOP, m.getId());
            eventSender.addEvent(MachineSimulator.EVENT_PRODUCTION_STOP, m.getId());
            eventSender.addEvent(MachineSimulator.STOP_RECORDER, m.getId());
        });
        eventSender.sendEvents();
    }

    @Override
    public void startRandomSimulation(int count, SimulationConfig config) {
        //stopAllMachines();
        stopSimulationMachines();
        UniqueRandom random = new UniqueRandom(machineBean.getMachineIds());
        if (config == null) {
            ConfigRandom cfgRandom = new ConfigRandom();
            for (int i = 0; i < count; i++) {
                SimulationBaseConfig cfg = cfgRandom.getRandomConfig();
                cfg.setMachineId(random.nextInt());
                MachineSimulator ms = new MachineSimulator(service, sender, cfg);
                ms.start();
                machineList.put(cfg.getMachineId(), ms);
            }
        } else {
            for (int i = 0; i < count; i++) {
                SimulationBaseConfig cfg = new SimulationBaseConfig(config);
                cfg.setMachineId(random.nextInt());
                MachineSimulator ms = new MachineSimulator(service, sender, cfg);
                ms.start();
                machineList.put(cfg.getMachineId(), ms);
            }
        }
    }

    @Override
    public Map<Integer, MachineSimulator> getMachines() {
        return machineList;
    }

    @Override
    public List<MachineStateTO> getRegisteredMachines() {
        List<MachineStateTO> result = new ArrayList<>();
        machineList.values().forEach(m -> {
            MachineStateTO ms = new MachineStateTO();
            ms.setState(m.getState().toString());
            ms.setMachineId(m.getMachineId());
            if(ApplicationConnector.isMonitoringAvailable()) {
                ms.setUrl(m.getUrl());
            }
            result.add(ms);
        });
        return result;
    }


    
    

}
