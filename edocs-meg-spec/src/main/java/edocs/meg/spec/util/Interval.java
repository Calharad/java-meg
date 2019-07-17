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
    
    public Interval(int max) {
        min = 0;
        this.max = max;
    }

    public Interval(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    public Integer getValue() {
        if(min < max) return random.nextInt(max - min) + min;
        else if(min == max) return min;
        else return null;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Min value: " + min + "\n"
                + "Max value " + max + "\n"; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
