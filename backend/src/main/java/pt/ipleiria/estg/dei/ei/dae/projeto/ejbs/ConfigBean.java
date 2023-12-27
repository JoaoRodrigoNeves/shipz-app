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

            productCatalogBean.create("Sem catálogo", "Sem catálogo", "Sem catálogo", "Sem catálogo", "productManufacter1");
            productCatalogBean.create("PC1", "tecnologia", "telemovel", "Telemóvel Top", "productManufacter1");
            productCatalogBean.create("PC2", "tecnologia", "telemovel", "Telemóvel Top", "productManufacter1");
            productCatalogBean.create("PC3", "tecnologia", "telemovel", "Telemóvel Top", "productManufacter2");

            productPackageBean.create("primário", "tinteiro", "em preparação", new Date());
            productPackageBean.create("secundário", "tinteiro", "em preparação", new Date());
            productPackageBean.create("terciário", "tinteiro", "em preparação", new Date());

            productBean.create(1);
            productBean.create(1);
            productBean.create(2);
            productBean.create(3);

            productCatalogBean.addProduct(100000, 100000);
            productCatalogBean.addProduct(100000, 100001);

            productBean.addProductToPackage(100000, 100002);
            productBean.addProductToPackage(100001, 100002);
            productBean.addProductToPackage(100002, 100001);

            clientOrderBean.create(123, "logisticOperator1");
            clientOrderBean.create(124, "logisticOperator1");
            clientOrderBean.addProduct(123, 100000);
            clientOrderBean.addProduct(123, 100001);
            clientOrderBean.addProduct(123, 100002);


        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}