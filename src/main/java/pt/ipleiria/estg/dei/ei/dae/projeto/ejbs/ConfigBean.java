package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    private FinalCostumerBean finalCostumerBean;
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");
    @PostConstruct
    public void populateDB() throws MyEntityNotFoundException, MyConstraintViolationException {
        finalCostumerBean.create("gustavom", "123", "Gustavo", "gustavom@hotmail.pt", "Rua Tenente Aragão");
        finalCostumerBean.delete("gustavom");
        finalCostumerBean.find("gustavom");
        finalCostumerBean.update("gustavom", "123", "Pedro", "dioguinho@mail.pt", "Rua da ribafria");
    }
}