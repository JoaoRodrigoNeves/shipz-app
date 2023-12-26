package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Hasher;

import java.util.Date;
import java.util.List;

@Stateless
public class ProductManufacterBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public boolean exists(String username){
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.username) FROM ProductManufacter p WHERE p.username = :username",
                String.class
        );
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;

    }

    public void create(String username, String password, String name, String email) throws MyEntityExistsException, MyConstraintViolationException {
        ProductManufacter studentCheck = entityManager.find(ProductManufacter.class, username);
        if(studentCheck != null)
            throw new MyEntityExistsException("O username " + username + " já existe.");

        try {
            var productManufacter = new ProductManufacter(username, hasher.hash(password), name, email);
            entityManager.persist(productManufacter);
            entityManager.flush();

        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public ProductManufacter find(String username) throws MyEntityNotFoundException {
        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, username);
        if (productManufacter == null)
            throw new MyEntityNotFoundException("Product Manufacter with usernme: '" + username + "' not found");
        return productManufacter;
    }

    public void update(String username, String password, String name, String email) {

        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, username);

        if (productManufacter == null) {
            System.err.println("ERROR_PRODUCTS_MANUFACTERS_NOT_FOUND: " + username);
            return;
        }

        entityManager.lock(productManufacter, LockModeType.OPTIMISTIC);

        productManufacter.setName(name);
        productManufacter.setPassword(hasher.hash(password));
        productManufacter.setEmail(email);

        entityManager.merge(productManufacter);
    }

    public boolean remove(String username) {

        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, username);

        if (productManufacter == null) {
            System.err.println("ERROR_PRODUCTS_MANUFACTERS_NOT_FOUND: " + username);
            return false;
        }
        entityManager.remove(productManufacter);

        ProductManufacter productManufacterFind = entityManager.find(ProductManufacter.class, username);
        return productManufacterFind != null;
    }

    //TODO get all
    public List<ProductManufacter> getAll() {
        return entityManager.createNamedQuery("getAllProductManufacters", ProductManufacter.class).getResultList();
    }

    //TODO get product manufacter - product catalogs
    public ProductManufacter getManufacterCatalogs(String username) throws MyEntityNotFoundException {
        ProductManufacter productManufacter = this.find(username);
        Hibernate.initialize(productManufacter.getProductsCatalog());
        return productManufacter;
    }
}
