package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    private LogisticOperatorBean logisticOperatorBean;
    @EJB
    private OrderBean orderBean;
    @EJB
    private ProductManufacterBean productManufacterBean;
    @EJB
    private ProductPackageBean productPackageBean;
    @EJB
    private TransportPackageBean transportPackageBean;
    @EJB
    private TransportPackageCatalogBean transportPackageCatalogBean;
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

            productCatalogBean.create("PC1", "tecnologia", "telemovel", "Telemóvel Top", "productManufacter1", null, null, 10, "Plástico", null, null, null);
            productCatalogBean.create("PC2", "tecnologia", "telemovel", "Telemóvel Top", "productManufacter1", null, null, 15, "Plástico", null, null, null);
            productCatalogBean.create("TESTE", "TESTE", "TESTE", "TESTE", "productManufacter1", 2, 1, 50, "TESTE", "TESTE", "TESTE", null);

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

            transportPackageCatalogBean.create("Embalagem S", "Cartão", 50);
            transportPackageCatalogBean.create("Embalagem M", "Cartão", 100);
            transportPackageCatalogBean.create("Embalagem L", "Cartão", 150);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}