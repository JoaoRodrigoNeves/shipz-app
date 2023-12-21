package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.time.LocalDateTime;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    private LogisticOperatorBean logisticOperatorBean;
    @PostConstruct
    public void populateDB() {
        try {
            logisticOperatorBean.create("logisticOperator1", "logisticOperator1", "logisticOperator1", "logisticOperator1@mail.pt",
                    12.0, 12.0, 20, 30, 1, 0, true, true);
            logisticOperatorBean.create("logisticOperator2", "logisticOperator2", "logisticOperator2", "logisticOperator2@mail.pt",
                    12.0, 12.0, 20, 30, 1, 0, true, true);
            logisticOperatorBean.update("logisticOperator1", "logisticOperator123", "logisticOperator123", "logisticOperator123@mail.pt",
                    10, 10, 25, 50, 2, 0, false, false);
            logisticOperatorBean.delete("logisticOperator2");
        }catch (Exception e){
            System.out.println("Error creating");
        }


    }
}