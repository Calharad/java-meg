/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.beans;

import edocs.meg.spec.simulation.SimulationConfig;
import javafx.util.Pair;
import javax.ejb.Remote;


/**
 *
 * @author ZbikKomp
 */
@Remote
public interface SimulationBeanRemote {

    void start(SimulationConfig config);    

    void stop();

    void updateConfig(SimulationConfig config);

    SimulationConfig getConfig();

    void restartConfig();
}
