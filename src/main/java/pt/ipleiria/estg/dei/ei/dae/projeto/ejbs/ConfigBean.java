package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.Date;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private ProductBean productBean;

    @EJB
    private PackageBean packageBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
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