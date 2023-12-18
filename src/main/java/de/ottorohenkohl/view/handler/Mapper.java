package de.ottorohenkohl.view.handler;

import de.ottorohenkohl.domain.transfer.response.Answer;
import jakarta.ws.rs.core.Response;

public class Mapper {
    
    private static Response error(Answer answer) {
        var response = switch (answer.getError().getStatus()) {
            case DUPLICATE -> Response.status(Response.Status.CONFLICT);
            case FORMATTING -> Response.status(Response.Status.BAD_REQUEST);
            case INTERNAL -> Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            case MISSING -> Response.status(Response.Status.NOT_FOUND);
        };
        
        return response.entity(answer).build();
    }
    
    public static Response accept(Answer answer) {
        return answer.hasError() ? error(answer) : Response.status(Response.Status.ACCEPTED).entity(answer).build();
    }
    
    public static Response create(Answer answer) {
        return answer.hasError() ? error(answer) : Response.status(Response.Status.CREATED).entity(answer).build();
    }
    
    public static Response ok(Answer answer) {
        return answer.hasError() ? error(answer) : Response.status(Response.Status.OK).entity(answer).build();
    }
    
}
