/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.spec.util;

import java.util.Random;

/**
 *
 * @author ZbikKomp
 */
public class Interval {
    
    private final int min;
    
    private final int max;
    
    private final Random random = new Random();
    
    private final TimeUnit timeUnit;
    
    public Interval(int max, TimeUnit tu) {
        timeUnit = tu;
        min = 0;
        this.max = max;
    }

    public Interval(int min, int max, TimeUnit tu) {
        timeUnit = tu;
        this.min = min;
        this.max = max;
    }
    
    public Integer getValue() {
        if(min < max) return random.nextInt(max - min) + min;
        else if(min == max) return min;
        else return null;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Min value: " + min + timeUnit.symbol + "\n"
                + "Max value " + max + timeUnit.symbol + "\n"; //To change body of generated methods, choose Tools | Templates.
    }
    
    public static enum TimeUnit {
        SECOND("s"),
        MINUTE("min"),
        HOUR("h"),
        MILIS("ms");
        
        public String symbol;

        private TimeUnit(String symbol) {
            this.symbol = symbol;
        }
    }
}
