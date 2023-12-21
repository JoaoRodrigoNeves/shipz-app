package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    private LogisticOperatorBean logisticOperatorBean;

    @EJB
    private OrdeBean ordeBean;
    @PostConstruct
    public void populateDB() {
        try {

            logisticOperatorBean.create("logisticOperator1", "logisticOperator1", "logisticOperator1", "logisticOperator1@mail.pt");
            logisticOperatorBean.create("logisticOperator2", "logisticOperator2", "logisticOperator2", "logisticOperator2@mail.pt");
            logisticOperatorBean.update("logisticOperator1", "logisticOperator123", "logisticOperator123", "logisticOperator123@mail.pt");
            ordeBean.create(1, "logisticOperator1");
            ordeBean.create(2, "logisticOperator1");
            ordeBean.create(3, "logisticOperator1");
            logisticOperatorBean.delete("logisticOperator2");
            logisticOperatorBean.find("logisticOperator1");
        }catch (Exception e){
            System.out.println("Error creating");
        }


    }
}