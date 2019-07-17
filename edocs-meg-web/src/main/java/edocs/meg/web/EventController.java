/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.web;

import edocs.meg.spec.dto.EventTypeTO;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.zbiksoft.edocs.meg.beans.EventLogBeanRemote;

/**
 *
 * @author ZbikKomp
 */
@Path("event")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventController {

    @EJB
    private EventLogBeanRemote eventLogBean;
    
    private static final Logger LOG = Logger.getLogger(EventController.class.getName());
    
    @GET
    @Path("type-list")
    public List<EventTypeTO> getEventTypes() {
        return eventLogBean.getEventTypes();
    }
    
}
