/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.web;

import edocs.meg.spec.simulation.SimulationConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.zbiksoft.edocs.meg.beans.SimulationBeanRemote;

/**
 *
 * @author ZbikKomp
 */
@Path("simulation")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulationController {
    
    @EJB
    private SimulationBeanRemote simulationBean;
    
    @POST
    @Path("config")
    public void updateSimulationConfig(SimulationConfig config) {
        simulationBean.updateConfig(config);
    }
    private static final Logger LOG = Logger.getLogger(SimulationController.class.getName());
    
    @PUT
    @Path("start")
    public void startSimulation(SimulationConfig config) {
        simulationBean.start(config);
    }
    
    @PUT
    @Path("stop")
    public void stopSimulation() {
        simulationBean.stop();
    }
    
}
