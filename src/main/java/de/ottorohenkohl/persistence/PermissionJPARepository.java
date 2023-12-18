package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Permission;
import de.ottorohenkohl.domain.model.entity.Permission_;
import de.ottorohenkohl.domain.model.entity.Person;
import de.ottorohenkohl.domain.model.entity.Service;
import de.ottorohenkohl.domain.model.enumeration.Access;
import de.ottorohenkohl.domain.repository.PermissionRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jakarta.enterprise.context.Dependent;

import java.util.List;

@Dependent
public class PermissionJPARepository extends PersistableJPARepository<Permission> implements PermissionRepository {
    
    public PermissionJPARepository() {
        super(Permission.class);
    }
    
    @Override
    public Option<Permission> readAll(Person person, Service service, Access access) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Permission.class);
        var root = query.from(Permission.class);
        
        query.where(builder.and(builder.equal(root.get(Permission_.person), person),
                                builder.equal(root.get(Permission_.service), service),
                                builder.equal(root.get(Permission_.access), access)));
        
        return Try.of(() -> entityManager.createQuery(query).getSingleResult()).toOption();
    }
    
    @Override
    public List<Permission> readAll(Person person) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Permission.class);
        var root = query.from(Permission.class);
        
        query.where(builder.equal(root.get(Permission_.person), person));
        
        return entityManager.createQuery(query).getResultList();
    }
    
    @Override
    public List<Permission> readAll(Person person, Service service) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Permission.class);
        var root = query.from(Permission.class);
        
        query.where(builder.and(builder.equal(root.get(Permission_.person), person),
                                builder.equal(root.get(Permission_.service), service)));
        
        return entityManager.createQuery(query).getResultList();
    }
    
}

