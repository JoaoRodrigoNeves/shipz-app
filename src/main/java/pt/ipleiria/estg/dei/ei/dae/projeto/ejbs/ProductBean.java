package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

@Stateless
public class ProductBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long code) {
        Query query = entityManager.createQuery("SELECT COUNT (p.code) FROM Product p WHERE p.code = :code", Long.class);
        query.setParameter("code", code);
        return (long) query.getSingleResult() > 0L;
    }

    //TODO CRUD operations for Product entity
    public void create(long code, String name, long packageCode) throws MyEntityExistsException {
        if (exists(code))
            throw new MyEntityExistsException("Product with code: " + code + " already exists");

        Package pack = entityManager.find(Package.class, packageCode);

        if (pack == null)
            throw new MyEntityExistsException("Package with code: " + packageCode + " already exists");
        try {
            Product product = new Product(code, name, pack);
            pack.addProduct(product);
            entityManager.persist(product);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            System.out.println(e);
        }
    }

    public Product find(long code) throws MyEntityNotFoundException {
        Product product = entityManager.find(Product.class, code);
        if (product == null)
            throw new MyEntityNotFoundException("Product with code: " + code + " not found");

        return product;
    }

    public void update(long code, String name, long packCode) throws MyEntityNotFoundException {
        Product product = this.find(code);
        product.setName(name);

        if (product.getPackage().getCode() != packCode) {
            Package pack = entityManager.find(Package.class, packCode);
            if (pack == null)
                throw new MyEntityNotFoundException("Package with code: " + packCode + " not found");
            product.setPackage(pack);
        }

        entityManager.merge(product);
    }

    public void delete(long code) throws MyEntityNotFoundException {
        Product product = this.find(code);
        if (product == null)
            throw new MyEntityNotFoundException("Product with code: " + code + " not found");

        entityManager.remove(product);
        product.getPackage().remoteProduct(product);
    }
}