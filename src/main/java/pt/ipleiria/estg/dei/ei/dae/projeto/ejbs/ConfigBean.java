package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.UserType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;

import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    private ProductManufacterBean productManufacterBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() throws MyConstraintViolationException, MyEntityExistsException {
        productManufacterBean.create("rolo009", "123", "Pedro Rolo", "prolo@mail.pt", UserType.PRODUCT_MANUFACTER);
        productManufacterBean.create("diogo", "123", "Diogo", "diogo@mail.pt", UserType.PRODUCT_MANUFACTER);
        productManufacterBean.create("carlos", "123", "Carlos", "carlos@mail.pt", UserType.PRODUCT_MANUFACTER);
        productManufacterBean.create("joao", "123", "Joao", "joao@mail.pt", UserType.PRODUCT_MANUFACTER);
        productManufacterBean.create("gustavo", "123", "Gustavo", "gustavo@mail.pt", UserType.PRODUCT_MANUFACTER);

        productManufacterBean.remove("carlos");
        productManufacterBean.find("rolo009");
        productManufacterBean.update("joao", "123", "João Neves", "j_neves@mail.com");

    }
}