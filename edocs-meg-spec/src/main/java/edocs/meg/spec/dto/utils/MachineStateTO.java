/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.spec.dto.utils;

/**
 *
 * @author ZbikKomp
 */
public class MachineStateTO implements Comparable<MachineStateTO> {
    
    private String state;
    
    private int machineId;
    
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public MachineStateTO() {
    }

    @Override
    public int compareTo(MachineStateTO t) {
        return Integer.compare(machineId, t.machineId);
    }
    
}
