package de.ottorohenkohl.view.resource;

import de.ottorohenkohl.domain.service.ServiceService;
import de.ottorohenkohl.domain.transfer.object.service.CreateService;
import de.ottorohenkohl.domain.transfer.object.service.UpdateService;
import de.ottorohenkohl.view.handler.Mapper;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("service")
@Produces({"application/json", "application/yaml"})
@Consumes({"application/json", "application/yaml"})
public class ServiceResource {
    
    private final ServiceService serviceService;
    
    @Inject
    protected ServiceResource(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
    
    @GET
    @Path("{title}")
    public Response getService(@PathParam("title") String title) {
        return Mapper.ok(serviceService.read(title));
    }
    
    @GET
    public Response getServices(@QueryParam("pages") Integer pages) {
        return Mapper.ok(pages == null ? serviceService.readAll() : serviceService.readAll(pages));
    }
    
    @DELETE
    @Path("{title}")
    @Transactional
    public Response deleteService(@PathParam("title") String title) {
        return Mapper.accept(serviceService.delete(title));
    }
    
    @PATCH
    @Path("{title}")
    public Response patchService(@PathParam("title") String title, UpdateService updateService) {
        return Mapper.accept(serviceService.update(title, updateService));
    }
    
    @POST
    public Response postService(CreateService createService) {
        return Mapper.create(serviceService.create(createService));
    }
    
}
