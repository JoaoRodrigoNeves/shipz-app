package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

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
    private FinalCostumerBean finalCostumerBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private ProductCatalogBean productCatalogBean;
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {

        try {
            // USERS
            logisticOperatorBean.create("logisticOperator1", "logisticOperator1", "Logistic Operator 1", "logistic.operator1@mail.pt");
            logisticOperatorBean.create("logisticOperator2", "logisticOperator2", "Logistic Operator 2", "logistic.operator2@mail.pt");
            productManufacterBean.create("productManufacter1", "productManufacter1", "Product Manufacter 1", "product.manufacter1@mail.pt");
            productManufacterBean.create("productManufacter2", "productManufacter2", "Product Manufacter 2", "product.manufacter2@mail.pt");
            finalCostumerBean.create("finalCostumer1", "finalCostumer1", "Final Costumer 1", "final.costumer1@mail.pt", "Some address, N1");
            finalCostumerBean.create("finalCostumer2", "finalCostumer2", "Final Costumer 2", "final.costumer2@mail.pt", "Some address, N2");

            productCatalogBean.create(1, "Sem catálogo", "productManufacter1");
            productCatalogBean.create(2, "PC1", "productManufacter1");
            productCatalogBean.create(3, "PC2", "productManufacter1");
            productCatalogBean.create(4, "PC3", "productManufacter2");

            productPackageBean.create(1, "primário", "tinteiro", "em preparação", new Date());
            productPackageBean.create(2, "secundário", "tinteiro", "em preparação", new Date());
            productPackageBean.create(3, "terciário", "tinteiro", "em preparação", new Date());


            /*
            productBean.addProductToPackage(1, 3);
            productBean.addProductToPackage(2, 3);
            productBean.addProductToPackage(3, 2);

            productCatalogBean.addProductToProductCatalog(1, 1);
            productCatalogBean.addProductToProductCatalog(1, 2);
            productCatalogBean.removeProductToProductCatalog(1, 1);

            clientOrderBean.create(123, "logisticOperator1");
            clientOrderBean.create(124, "logisticOperator1");
            clientOrderBean.addProduct(123, 1);
            clientOrderBean.addProduct(123, 2);
            clientOrderBean.addProduct(123, 3);
            */

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}