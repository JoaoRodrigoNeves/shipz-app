package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ClientOrderBean {

    @PersistenceContext
    private EntityManager em;

    public void create(long id, String LO) throws MyEntityNotFoundException{
        LogisticOperator logisticOperator = em.find(LogisticOperator.class, LO);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("LogisticOperator with username: " + LO + " doesn't exist");
        }

        ClientOrder orde = new ClientOrder(id , logisticOperator);
        em.persist(orde);
    }

    public List<ClientOrder> getAll(){
        return em.createNamedQuery("getAllOrders", ClientOrder.class).getResultList();
    }
}
