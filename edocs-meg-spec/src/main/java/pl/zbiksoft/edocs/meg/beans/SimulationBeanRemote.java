/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans;

import edocs.meg.spec.simulation.SimulationConfig;
import javax.ejb.Remote;


/**
 *
 * @author ZbikKomp
 */
@Remote
public interface SimulationBeanRemote {

    void start(SimulationConfig config);    

    void stop(int machineId);

    void updateConfig(SimulationConfig config);

    SimulationConfig getConfig(int machineId);

    void restartConfig(int machineId);

    void stopSimulationMachines();

    String getState(int machineId);

    SimulationConfig getDefaultConfig();

    void stopAllMachines();
}
