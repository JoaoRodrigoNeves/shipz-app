package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import com.opencsv.CSVReader;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

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


    //TODO add sensor to package
    @POST
    @Path("upload-csv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadCSV(InputStream fileInputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> lines = csvReader.readAll();
            int observationsCreated = 0;
            for (String[] line : lines) {
                String[] sensorObservation = Arrays.stream(line).map(String::trim).toArray(String[]::new)[0].split(";");
                if(sensorObservation.length == 2){
                    observationBean.create(Long.parseLong(sensorObservation[0]), sensorObservation[1]);
                    observationsCreated++;
                }
            }

            return Response.ok(lines.size() == 1 ? "Foi adicionada " + observationsCreated + " observação" : "Foram adicionadas " + observationsCreated + " observações" ).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro durante o processamento do arquivo CSV.").build();
        }
    }
}
