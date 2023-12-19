package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Startup
@Singleton
public class ConfigBean {
    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");
    }
}