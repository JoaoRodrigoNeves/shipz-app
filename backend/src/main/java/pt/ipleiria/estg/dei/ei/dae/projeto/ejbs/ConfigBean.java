package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;

import java.util.List;
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
            productManufacterBean.create("productManufacter1", "productManufacter1", "Product Manufacter 1", "product.manufacter1@mail.pt");

            logisticOperatorBean.create("logisticOperator1", "logisticOperator1", "Logistic Operator 1", "logistic.operator1@mail.pt");
            logisticOperatorBean.create("logisticOperator2", "logisticOperator2", "Logistic Operator 2", "logistic.operator2@mail.pt");

            finalCostumerBean.create("finalCostumer1", "finalCostumer1", "Final Costumer 1", "final.costumer1@mail.pt", "Some address, N1");
            finalCostumerBean.create("finalCostumer2", "finalCostumer2", "Final Costumer 2", "final.costumer2@mail.pt", "Some address, N2");

            transportPackageCatalogBean.create("Embalagem S", "Cartão", 50);
            transportPackageCatalogBean.create("Embalagem M", "Cartão", 100);
            transportPackageCatalogBean.create("Embalagem L", "Cartão", 150);

            productCatalogBean.create("IPhone 15", "Tecnologia", "Telemovel", "Telemóvel IPhone 15 - Apple", "productManufacter1", 4, 3, 15, "Cartão", "Cartão", "Palete", List.of("Pressão", "Dano", "Gps"));
            productCatalogBean.create("Computador Portátil", "Tecnologia", "Computadores Portáteis", "Computador Portátil MSI", "productManufacter1", 5, 2, 100, "Cartão", "Cartão", "Palete", List.of("Pressão", "Dano", "Gps"));
            productCatalogBean.create("Iogurte", "Alimentação", "Laticinios", "Iogurte Magro", "productManufacter1", 4, 3, 20, "Plástico", "Cartão", "Plástico", List.of("Temperatura"));
            productCatalogBean.create("Amendoins Torrados", "Alimentação", "Frutos Secos", "Amendoins Torrados", "productManufacter1", 12, 3, 12, "Plástico", "Cartão", "Cartão", List.of("Temperatura", "Humidade"));
            productCatalogBean.create("Pão de Forma", "Alimentação", "Pão", "Pão de Forma", "productManufacter1", 8, 5, 30, "Plástico", "Cartão", "Cartão", List.of("Temperatura", "Humidade"));


            //Criar produtos para cada catálogo e associar os respetivos sensores
            for (int j = 100000; j < 100005; j++) {
                for (int i = 0; i < 15; i++) {
                    Product product = productBean.create(j);
                    ProductCatalog productCatalog = productCatalogBean.find(j);
                    productPackageBean.addProductToAllPackages(product);

                    ProductPackage productPackage = product.getProductPackages().stream().
                            filter(productPackage1 -> productPackage1.getType() == PackageType.PRIMARY).findFirst().orElseThrow();

                    if (productCatalog.isDamageSensor()) {
                        Sensor sensor = sensorBean.create(SensorType.DAMAGE.getSensorType(), false);
                        sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
                    }
                    if (productCatalog.isGpsSensor()) {
                        Sensor sensor = sensorBean.create(SensorType.GPS.getSensorType(), false);
                        sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
                    }
                    if (productCatalog.isPressureSensor()) {
                        Sensor sensor = sensorBean.create(SensorType.PRESSURE.getSensorType(), false);
                        sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
                    }
                    if (productCatalog.isTemperatureSensor()) {
                        Sensor sensor = sensorBean.create(SensorType.TEMPERATURE.getSensorType(), false);
                        sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
                    }
                    if (productCatalog.isHumiditySensor()) {
                        Sensor sensor = sensorBean.create(SensorType.HUMIDITY.getSensorType(), false);
                        sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
                    }
                }
            }


            sensorBean.create(SensorType.TEMPERATURE.getSensorType(), false);
            sensorBean.create(SensorType.HUMIDITY.getSensorType(), false);
            sensorBean.create(SensorType.PRESSURE.getSensorType(), false);
            sensorBean.create(SensorType.GPS.getSensorType(), false);
            sensorBean.create(SensorType.DAMAGE.getSensorType(), false);

            orderBean.create(
                    "finalCostumer1",
                    "logisticOperator1",
                    List.of(
                            new ProductOrderDTO(100000, 2, "IPhone 15", "Tecnologia", "Telemovel", "Telemóvel IPhone 15 - Apple", "Product Manufacter 1", "productManufacter1", 4, 3, "Cartão", "Cartão", "Palete", 15),
                            new ProductOrderDTO(100001, 1, "Computador Portátil", "Tecnologia", "Computadores Portáteis", "Computador Portátil MSI", "Product Manufacter 1", "productManufacter1", 5, 2, "Cartão", "Cartão", "Palete", 100)
                    ),
                    transportPackageCatalogBean.getAll()
            );

            orderBean.create(
                    "finalCostumer1",
                    "logisticOperator1",
                    List.of(
                            new ProductOrderDTO(100002, 4, "Iogurte", "Alimentação", "Laticinios", "Iogurte Magro", "Product Manufacter 1", "productManufacter1", 4, 3, "Plástico", "Cartão", "Plástico", 20),
                            new ProductOrderDTO(100003, 2, "Amendoins Torrados", "Alimentação", "Frutos Secos", "Amendoins Torrados", "Product Manufacter 1", "productManufacter1", 12, 3, "Plástico", "Cartão", "Cartão", 12),
                            new ProductOrderDTO(100004, 2, "Pão de Forma", "Alimentação", "Pão", "Pão de Forma", "Product Manufacter 1", "productManufacter1", 8, 5, "Plástico", "Cartão", "Cartão", 30)
                    ),
                    transportPackageCatalogBean.getAll()
            );

            orderBean.create(
                    "finalCostumer2",
                    "logisticOperator2",
                    List.of(
                            new ProductOrderDTO(100000, 1, "IPhone 15", "Tecnologia", "Telemovel", "Telemóvel IPhone 15 - Apple", "Product Manufacter 1", "productManufacter1", 4, 3, "Cartão", "Cartão", "Palete", 15),
                            new ProductOrderDTO(100001, 2, "Computador Portátil", "Tecnologia", "Computadores Portáteis", "Computador Portátil MSI", "Product Manufacter 1", "productManufacter1", 5, 2, "Cartão", "Cartão", "Palete", 100)
                    ),
                    transportPackageCatalogBean.getAll()
            );

            orderBean.create(
                    "finalCostumer2",
                    "logisticOperator2",
                    List.of(
                            new ProductOrderDTO(100002, 4, "Iogurte", "Alimentação", "Laticinios", "Iogurte Magro", "Product Manufacter 1", "productManufacter1", 4, 3, "Plástico", "Cartão", "Plástico", 20),
                            new ProductOrderDTO(100003, 2, "Amendoins Torrados", "Alimentação", "Frutos Secos", "Amendoins Torrados", "Product Manufacter 1", "productManufacter1", 12, 3, "Plástico", "Cartão", "Cartão", 12),
                            new ProductOrderDTO(100004, 2, "Pão de Forma", "Alimentação", "Pão", "Pão de Forma", "Product Manufacter 1", "productManufacter1", 8, 5, "Plástico", "Cartão", "Cartão", 30)
                    ),
                    transportPackageCatalogBean.getAll()
            );

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}