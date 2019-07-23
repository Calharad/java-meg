/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.web;

import edocs.meg.spec.simulation.SimulationConfig;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    public void updateSimulationConfig(SimulationConfig[] configs) {
        Arrays.asList(configs).forEach(config -> simulationBean.updateConfig(config));
    }
    private static final Logger LOG = Logger.getLogger(SimulationController.class.getName());

    @PUT
    @Path("start")
    public void startSimulation(SimulationConfig[] configs) {
        Arrays.asList(configs).forEach(c -> simulationBean.start(c));
    }

    @PUT
    @Path("stop/{id}")
    public void stopSimulation(@PathParam("id") Integer id) {
        simulationBean.stop(id);
    }

    @PUT
    @Path("stop/active")
    public void stopAllSimulations() {
        simulationBean.stopSimulationMachines();
    }

    @PUT
    @Path("stop")
    public void stopSimulations(Integer[] ids) {
        Arrays.asList(ids).forEach(id -> simulationBean.stop(id));
    }

    @PUT
    @Path("stop/all")
    public void stopAll() {
        simulationBean.stopAllMachines();
    }

    @GET
    @Path("config/{id}")
    public SimulationConfig getConfig(@PathParam("id") Integer id) {
        return simulationBean.getConfig(id);
    }

    @GET
    @Path("config/default")
    public SimulationConfig getDefaultConfig() {
        return simulationBean.getDefaultConfig();
    }

    @GET
    @Path("state/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getState(@PathParam("id") Integer id) {
        return simulationBean.getState(id);
    }

    @PUT
    @Path("config/{id}/restart")
    public void restartConfig(@PathParam("id") Integer id) {
        simulationBean.restartConfig(id);
    }
    
    @PUT
    @Path("config/restart")
    public void restartConfigs(Integer[] ids) {
        Arrays.asList(ids).forEach(id -> simulationBean.restartConfig(id));
    }

    @PUT
    @Path("random")
    public void startRandomSimulation(@QueryParam("auto") Boolean auto, @QueryParam("count") Integer count, SimulationConfig config) {
        
    }
}
