package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.UserType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;

import java.util.logging.Logger;
import java.util.Date;
@Startup
@Singleton
public class ConfigBean {
    @EJB
    private ProductManufacterBean productManufacterBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");


    @EJB
    private ProductBean productBean;

    @EJB
    private PackageBean packageBean;

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
        
        try {
            packageBean.create(1, "primário", "tinteiro", "em preparação", new Date());
            packageBean.create(2, "secundário", "tinteiro", "em preparação", new Date());
            packageBean.create(3, "terciário", "tinteiro", "em preparação", new Date());
            productBean.create(1, "tinteiro azul", 1);
            productBean.create(2, "tinteiro vermelho", 2);
            productBean.create(3, "tinteiro verde", 3);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}