/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.spec.simulation;

/**
 *
 * @author ZbikKomp
 */
public class SimulationConfig {
    
    private String startTime;
    
    private String stopTime;
    
    private Integer cycleTime;
    
    private Integer cycleInterval;
    
    private Integer cycleBreak;
    
    private Float machineUsage;
    
    private Integer minInterval;
    
    private Integer maxInterval;
    
    private Integer machine;

    public Integer getCycleBreak() {
        return cycleBreak;
    }

    public void setCycleBreak(Integer cycleBreak) {
        this.cycleBreak = cycleBreak;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(Integer cycleTime) {
        this.cycleTime = cycleTime;
    }

    public Integer getCycleInterval() {
        return cycleInterval;
    }

    public void setCycleInterval(Integer cycleInterval) {
        this.cycleInterval = cycleInterval;
    }

    public Float getMachineUsage() {
        return machineUsage;
    }

    public void setMachineUsage(Float machineUsage) {
        this.machineUsage = machineUsage;
    }

    public Integer getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(Integer minInterval) {
        this.minInterval = minInterval;
    }

    public Integer getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(Integer maxInterval) {
        this.maxInterval = maxInterval;
    }

    public Integer getMachine() {
        return machine;
    }

    public void setMachine(Integer machine) {
        this.machine = machine;
    }

    
    
}
