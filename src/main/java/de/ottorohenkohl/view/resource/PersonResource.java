package de.ottorohenkohl.view.resource;

import de.ottorohenkohl.domain.service.PersonService;
import de.ottorohenkohl.domain.transfer.object.person.CreatePerson;
import de.ottorohenkohl.domain.transfer.object.person.UpdatePerson;
import de.ottorohenkohl.view.handler.Mapper;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("person")
@Produces({"application/json", "application/yaml"})
@Consumes({"application/json", "application/yaml"})
public class PersonResource {
    
    private final PersonService personService;
    
    @Inject
    protected PersonResource(PersonService personService) {
        this.personService = personService;
    }
    
    @GET
    @Path("{username}")
    public Response getPerson(@PathParam("username") String username) {
        return Mapper.ok(personService.read(username));
    }
    
    @GET
    public Response getPersons(@QueryParam("pages") Integer pages) {
        return Mapper.ok(pages == null ? personService.readAll() : personService.readAll(pages));
    }
    
    @DELETE
    @Path("{username}")
    @Transactional
    public Response deletePerson(@PathParam("username") String username) {
        return Mapper.accept(personService.delete(username));
    }
    
    @PATCH
    @Path("{username}")
    public Response patchPerson(@PathParam("username") String username, UpdatePerson updatePerson) {
        return Mapper.accept(personService.update(username, updatePerson));
    }
    
    @POST
    public Response postPerson(CreatePerson createPerson) {
        return Mapper.create(personService.create(createPerson));
    }
    
}
