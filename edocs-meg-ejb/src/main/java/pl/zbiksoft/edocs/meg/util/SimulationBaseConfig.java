/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.util;

import edocs.meg.spec.simulation.SimulationConfig;
import edocs.meg.spec.util.Interval;
import java.time.LocalTime;

/**
 *
 * @author ZbikKomp
 */
public class SimulationBaseConfig {
    
    public static SimulationBaseConfig restartConfig(SimulationBaseConfig config) {
        config.cycleInterval = new Interval(1500, 2500, Interval.TimeUnit.MILIS);
        config.interval = new Interval(3600, 3600, Interval.TimeUnit.SECOND);
        config.machineId = -1;
        config.machineUsage = 0.5F;
        config.startTime = LocalTime.of(8, 0);
        config.stopTime = LocalTime.of(16, 0);
        config.cycleBreak = 0;
        return config;
    }
    
    private LocalTime startTime = LocalTime.of(8, 0);

    private LocalTime stopTime = LocalTime.of(16, 0);

    private int machineId = -1;

    private Interval cycleInterval = new Interval(1500, 2500, Interval.TimeUnit.MILIS);

    private Interval interval = new Interval(3600, 3600, Interval.TimeUnit.SECOND);
    
    private int cycleBreak = 0;

    private float machineUsage = 0.5F;
    
    public void updateConfig(SimulationConfig config) {
        setStartTime(config.getStartTime());
        setStopTime(config.getStopTime());
        setCycleInterval(new Interval(config.getCycleTime() - config.getCycleInterval(), 
                config.getCycleTime() + config.getCycleInterval(), Interval.TimeUnit.MILIS));
        setMachineId(config.getMachine());
        if(config.getMinInterval() != null)
            setInterval(new Interval(config.getMinInterval(), config.getMaxInterval(), Interval.TimeUnit.SECOND));
        else setInterval(new Interval(config.getMaxInterval(), Interval.TimeUnit.SECOND));
        setMachineUsage(config.getMachineUsage());
        setCycleBreak(config.getCycleBreak());
    }

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        startTime = LocalTime.parse(time);
    }

    public int getCycleBreak() {
        return cycleBreak;
    }

    public void setCycleBreak(int cycleBreak) {
        this.cycleBreak = cycleBreak;
    }

    public LocalTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(String time) {
        stopTime = LocalTime.parse(time);
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public Interval getCycleInterval() {
        return cycleInterval;
    }

    public void setCycleInterval(Interval cycleInterval) {
        this.cycleInterval = cycleInterval;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public float getMachineUsage() {
        return machineUsage;
    }

    public void setMachineUsage(float usage) {
        if (usage >= 0.0 && usage <= 1.0) {
            this.machineUsage = usage;
        }
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "Simulation config:\n"
                + "\t\tStart Time: " + startTime.toString() + "\n"
                + "\t\tEnd Time: " + stopTime.toString() + "\n"
                + "\t\tAssigned machine id: " + machineId + "\n"
                + "\t\tProduction interval: \n" + interval.toString() + "\n"
                + "\t\tCycle interval: \n" + cycleInterval.toString() + "\n"
                + "\t\tUsage: " + machineUsage + "\n"; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
