package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Hasher;

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
        if(exists(username))
            throw new MyEntityExistsException("Product Manufacter with username: '" + username + "' already exists");

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

    public void update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        ProductManufacter productManufacter = this.find(username);
        entityManager.lock(productManufacter, LockModeType.OPTIMISTIC);
        productManufacter.setName(name);
        if(password != null){
            productManufacter.setPassword(hasher.hash(password));
        }
        productManufacter.setEmail(email);
        entityManager.merge(productManufacter);
    }

    public void delete(String username) throws MyEntityNotFoundException {
        ProductManufacter productManufacter = this.find(username);

        if (productManufacter.getProductCatalogs() != null) {
            productManufacter.getProductCatalogs().forEach(productCatalog -> productCatalog.setProductManufacter(null));
            productManufacter.getProductCatalogs().forEach(productCatalog -> productCatalog.getProducts().forEach(product -> product.setProductManufacter(null)));
        }

        entityManager.remove(productManufacter);
    }

    //TODO get all
    public List<ProductManufacter> getAll() {
        return entityManager.createNamedQuery("getAllProductManufacters", ProductManufacter.class).getResultList();
    }

    //TODO get product manufacter -> product catalogs
    public ProductManufacter getCatalogs(String username) throws MyEntityNotFoundException {
        ProductManufacter productManufacter = this.find(username);
        Hibernate.initialize(productManufacter.getProductCatalogs());
        return productManufacter;
    }

    //TODO get products manufacter -> products
    public ProductManufacter getProducts(String username) throws MyEntityNotFoundException {
        ProductManufacter productManufacter = this.find(username);
        Hibernate.initialize(productManufacter.getProducts());
        return productManufacter;
    }
}
