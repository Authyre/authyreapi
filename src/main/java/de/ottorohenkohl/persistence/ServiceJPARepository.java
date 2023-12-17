package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Service;
import de.ottorohenkohl.domain.model.entity.Service_;
import de.ottorohenkohl.domain.model.value.primitive.Name;
import de.ottorohenkohl.domain.repository.ServiceRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jakarta.enterprise.context.Dependent;

@Dependent
public class ServiceJPARepository extends PersistableJPARepository<Service> implements ServiceRepository {
    
    public ServiceJPARepository() {
        super(Service.class);
    }
    
    @Override
    public Option<Service> read(Name title) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Service.class);
        var root = query.from(Service.class);
        
        query.where(builder.equal(root.get(Service_.title), title));
        
        return Try.of(() -> entityManager.createQuery(query).getSingleResult()).toOption();
    }
    
}

