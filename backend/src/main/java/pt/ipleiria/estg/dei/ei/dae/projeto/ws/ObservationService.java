package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.LogisticOperatorDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ObservationDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

@Path("/observations")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ObservationService {
    @EJB
    private ObservationBean observationBean;

    @POST
    @Path("/")
    public Response create(ObservationDTO observationDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        observationBean.create(
                observationDTO.getSensorCode(),
                observationDTO.getValue()
        );

        return Response.status(Response.Status.CREATED).build();
    }
}
