package pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.NoStockException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.NoVolumeException;

import java.util.logging.Logger;

public class NoVolumeExceptionMapper implements ExceptionMapper<NoVolumeException> {
    private static final Logger logger =
            Logger.getLogger(NoVolumeException.class.getCanonicalName());

    @Override
    public Response toResponse(NoVolumeException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.NOT_ACCEPTABLE)
                .entity(errorMsg)
                .build();
    }
}