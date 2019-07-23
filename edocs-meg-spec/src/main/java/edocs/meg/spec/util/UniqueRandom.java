/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.spec.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ZbikKomp
 */
public class UniqueRandom {
    
    private final Random random = new Random();
    
    private final List<Integer> values;

    public UniqueRandom(List<Integer> values) {
        this.values = values;
    }

    public UniqueRandom(Integer... values) {
        this.values = Arrays.asList(values);
    }
    
    public int nextInt() {
        int index = random.nextInt(values.size());
        return values.remove(index);    
    }
    
}
