package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.ws.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Date;
@Startup
@Singleton
public class ConfigBean {
    @EJB
    private LogisticOperatorBean logisticOperatorBean;
    @EJB
    private ClientOrderBean clientOrderBean;
    @EJB
    private ProductManufacterBean productManufacterBean;
    @EJB
    private ProductPackageBean productPackageBean;
    @EJB
    private ProductCatalogBean poductCatalogBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private ProductCatalogBean productCatalogBean;
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() throws MyConstraintViolationException, MyEntityExistsException {
        try {
            productManufacterBean.create("rolo009", "123", "Pedro Rolo", "prolo@mail.pt");
            productManufacterBean.create("diogo", "123", "Diogo", "diogo@mail.pt");
            productManufacterBean.create("carlos", "123", "Carlos", "carlos@mail.pt");
            productManufacterBean.create("joao", "123", "Joao", "joao@mail.pt");
            productManufacterBean.create("gustavo", "123", "Gustavo", "gustavo@mail.pt");

            productManufacterBean.remove("carlos");
            productManufacterBean.find("rolo009");
            productManufacterBean.update("joao", "123", "João Neves", "j_neves@mail.com");

            productCatalogBean.create(1, "PC1", "rolo009");
            productCatalogBean.create(2, "PC2", "diogo");
            productCatalogBean.create(3, "PC3", "joao");

            productPackageBean.create(1, "primário", "tinteiro", "em preparação", new Date());
            productPackageBean.create(2, "secundário", "tinteiro", "em preparação", new Date());
            productPackageBean.create(3, "terciário", "tinteiro", "em preparação", new Date());

            productBean.create(1, "tinteiro azul", 1);
            productBean.create(2, "tinteiro vermelho", 2);
            productBean.create(3, "tinteiro verde", 3);

            productBean.addProductToPackage(1, 3);
            productBean.addProductToPackage(2, 3);
            productBean.addProductToPackage(3, 2);

            productCatalogBean.addProductToProductCatalog(1,1);
            productCatalogBean.addProductToProductCatalog(1,2);
            productCatalogBean.removeProductToProductCatalog(1,1);
            
            logisticOperatorBean.create("logisticOperator1", "logisticOperator1", "logisticOperator1", "logisticOperator1@mail.pt");
            logisticOperatorBean.create("logisticOperator2", "logisticOperator2", "logisticOperator2", "logisticOperator2@mail.pt");
            logisticOperatorBean.update("logisticOperator1", "logisticOperator123", "logisticOperator123", "logisticOperator123@mail.pt");
            logisticOperatorBean.delete("logisticOperator2");
            logisticOperatorBean.find("logisticOperator1");
            clientOrderBean.create(123,"logisticOperator1");
            clientOrderBean.create(124,"logisticOperator1");
            clientOrderBean.enrollProduct(123,1);
            clientOrderBean.enrollProduct(123,2);
            clientOrderBean.enrollProduct(123,3);





        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}