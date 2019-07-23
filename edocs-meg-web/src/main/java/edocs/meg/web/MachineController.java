/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.web;

import edocs.meg.spec.dto.MachineTO;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.zbiksoft.edocs.meg.beans.EventLogBeanRemote;
import pl.zbiksoft.edocs.meg.beans.MachineBeanRemote;

/**
 *
 * @author ZbikKomp
 */
@Path("machine")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MachineController {

    private static final Logger LOG = Logger.getLogger(MachineController.class.getName());
    
    @EJB
    private MachineBeanRemote machineBean;
    
    @EJB
    private EventLogBeanRemote eventLogBean;
    
    @GET
    @Path("list")
    public List<MachineTO> getMachines() {
        return machineBean.getMachines();
    }
    
    @GET
    @Path("list/count")
    public String getMachineCount() {
        return String.valueOf(machineBean.getMachineCount());
    }
    
    @POST 
    @Path("{id}/event/new/{eventId}")
    public void saveEvent(@PathParam("id") Integer machineId, @PathParam("eventId") Integer eventId) {
            eventLogBean.saveEvent(machineId, eventId);
    }
    
    
}
