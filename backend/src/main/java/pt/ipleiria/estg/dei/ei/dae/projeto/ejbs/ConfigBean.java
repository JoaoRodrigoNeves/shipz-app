package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

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
    private TransportPackageBean transportPackageBean;
    @EJB
    private FinalCostumerBean finalCostumerBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private ProductCatalogBean productCatalogBean;

    @EJB
    private SensorBean sensorBean;
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {

        try {
            logisticOperatorBean.create("logisticOperator1", "logisticOperator1", "Logistic Operator 1", "logistic.operator1@mail.pt");
            logisticOperatorBean.create("logisticOperator2", "logisticOperator2", "Logistic Operator 2", "logistic.operator2@mail.pt");
            productManufacterBean.create("productManufacter1", "productManufacter1", "Product Manufacter 1", "product.manufacter1@mail.pt");
            finalCostumerBean.create("finalCostumer1", "finalCostumer1", "Final Costumer 1", "final.costumer1@mail.pt", "Some address, N1");
            finalCostumerBean.create("finalCostumer2", "finalCostumer2", "Final Costumer 2", "final.costumer2@mail.pt", "Some address, N2");

            productCatalogBean.create("Sem catálogo", "Sem catálogo", "Sem catálogo", "Sem catálogo", "productManufacter1");
            productCatalogBean.create("PC1", "tecnologia", "telemovel", "Telemóvel Top", "productManufacter1");
            productCatalogBean.create("PC2", "tecnologia", "telemovel", "Telemóvel Top", "productManufacter1");

            productPackageBean.create(PackageType.SECUNDARY, "cartão");
            productPackageBean.create(PackageType.TERTIARY, "plástico");

            productBean.create(100002);
            productBean.create(100002);
            productBean.create(100002);
            productBean.create(100002);
            productBean.create(100002);
            productBean.create(100002);
            productBean.create(100002);
            productBean.create(100002);
            productBean.create(100002);
            productBean.create(100001);
            productBean.create(100001);
            productBean.create(100001);
            productBean.create(100001);
            productBean.create(100001);
            productBean.create(100001);
            productBean.create(100001);
            productBean.create(100001);


            productBean.addProductToPackage(100000, 100002);
            productBean.addProductToPackage(100001, 100002);
            productBean.addProductToPackage(100002, 100001);

            //transportPackageBean.create("primário", "papel", "leiria", "01/01/2024");
            //clientOrderBean.create("finalCostumer1", List.of(100001L, 100002L));

            sensorBean.create("Temperatura");
            sensorBean.create("Temperatura");
            sensorBean.create("Humidade");
            sensorBean.create("Humidade");
            sensorBean.create("Pressão");
            sensorBean.create("Pressão");

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}