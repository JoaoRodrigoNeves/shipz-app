package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Orde;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

@Stateless
public class OrdeBean {

    @PersistenceContext
    private EntityManager em;

    public void create(long id, String LO) throws MyEntityNotFoundException{
        LogisticOperator logisticOperator = em.find(LogisticOperator.class, LO);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("LogisticOperator with username: " + LO + " doesn't exist");
        }
        Orde orde = new Orde(id , logisticOperator);
        em.persist(orde);
    }
}
