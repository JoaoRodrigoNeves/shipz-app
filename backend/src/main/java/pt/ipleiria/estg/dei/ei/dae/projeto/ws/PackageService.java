package pt.ipleiria.estg.dei.ei.dae.projeto.ws;


import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.PackageDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.PackageBean;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("packages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PackageService {

    @EJB
    private PackageBean packageBean;

    private PackageDTO packageToDTO(Package pack) {
        return new PackageDTO(
                pack.getCode(),
                pack.getType(),
                pack.getMaterial(),
                pack.getStatus(),
                pack.getManufacturingDate()
        );
    }

    private List<PackageDTO> packageToDTOs(List<Package> packages) {
        return packages.stream().map(this::packageToDTO).collect(Collectors.toList());
    }


    //TODO create a new package
    @POST
    @Path("/") // means: the relative url path is “/api/package/”
    public Response createNewPackage(PackageDTO packageDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        packageBean.create(
                packageDTO.getCode(),
                packageDTO.getType(),
                packageDTO.getMaterial(),
                packageDTO.getStatus(),
                packageDTO.getManufacturingDate()
        );
        Package newPackage = packageBean.find(packageDTO.getCode());
        return Response.status(Response.Status.CREATED).entity(packageToDTO(newPackage)).build();
    }
}
