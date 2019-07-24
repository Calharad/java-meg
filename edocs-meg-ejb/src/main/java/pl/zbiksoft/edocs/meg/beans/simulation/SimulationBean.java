/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans.simulation;

import edocs.meg.spec.simulation.SimulationConfig;
import edocs.meg.spec.util.UniqueRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TimerService;
import pl.zbiksoft.edocs.meg.beans.SimulationBeanRemote;
import pl.zbiksoft.edocs.meg.events.EventSender;
import pl.zbiksoft.edocs.meg.local.beans.EventLogBeanLocal;
import pl.zbiksoft.edocs.meg.local.beans.MachineBeanLocal;
import pl.zbiksoft.edocs.meg.util.ConfigRandom;
import pl.zbiksoft.edocs.meg.util.MachineState;
import pl.zbiksoft.edocs.meg.util.SimulationBaseConfig;

/**
 *
 * @author ZbikKomp
 */
@Singleton
@Stateless
public class SimulationBean implements SimulationBeanRemote {

    @Resource
    private TimerService service;

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

        if (machineList.containsKey(newConfig.getMachine())) {
            updateConfig(newConfig);
        } else {
            MachineSimulator m = new MachineSimulator(service, sender);
            m.updateConfig(newConfig);
            m.start();
            machineList.put(m.getMachineId(), m);
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
        MachineSimulator ms = machineList.remove(machineId);
        if (ms != null) {
            ms.stop();
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
        machineBean.getMachines().forEach(m -> eventSender.addEvent(MachineSimulator.STOP_RECORDER, m.getId()));
        eventSender.sendEvents();
    }

    @Override
    public void startRandomSimulation(int count, SimulationConfig config) {
        stopAllMachines();
        UniqueRandom random = new UniqueRandom(machineBean.getMachineIds());
        if (config == null) {
            ConfigRandom cfgRandom = new ConfigRandom();
            for (int i = 0; i < count; i++) {
                SimulationBaseConfig cfg = cfgRandom.getRandomConfig();
                cfg.setMachineId(random.nextInt());
                machineList.put(cfg.getMachineId(), new MachineSimulator(service, sender, cfg));
            }
        } else {
            for (int i = 0; i < count; i++) {
                SimulationBaseConfig cfg = new SimulationBaseConfig(config);
                cfg.setMachineId(random.nextInt());
                machineList.put(cfg.getMachineId(), new MachineSimulator(service, sender, cfg));
            }
            machineList.values().forEach(m -> m.start());
        }
    }

}
