/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.util;

import edocs.meg.spec.util.Interval;
import java.util.Random;

/**
 *
 * @author ZbikKomp
 */
public class ConfigRandom {
    
    private final Random random = new Random();
    
    private int between(int min, int max) {
        return random.nextInt(max - min) + min;
    }
    
    public SimulationBaseConfig getRandomConfig() {
        SimulationBaseConfig cfg = new SimulationBaseConfig();
        
        cfg.setStartTime("00:01");
        cfg.setStopTime("23:59");
        
        cfg.setMachineUsage((random.nextInt(17) + 2)/20.0F);
        
        cfg.setCycleBreak(between(500, 5000));
        
        cfg.setCycleInterval(new Interval(between(1000, 4000), between(5000, 10000), Interval.TimeUnit.MILIS));
        
        cfg.setInterval(new Interval(between(20, 200), between(200, 300), Interval.TimeUnit.SECOND));
        
        return cfg;
    }
    
}
