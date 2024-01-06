package pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.NoStockException;

import java.util.logging.Logger;

public class NoStockExceptionMapper implements ExceptionMapper<NoStockException> {
    private static final Logger logger =
            Logger.getLogger(NoStockException.class.getCanonicalName());

    @Override
    public Response toResponse(NoStockException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.NOT_ACCEPTABLE)
                .entity(errorMsg)
                .build();
    }
}