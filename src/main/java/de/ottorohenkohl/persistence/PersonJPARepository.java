package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Person;
import de.ottorohenkohl.domain.model.entity.Person_;
import de.ottorohenkohl.domain.model.value.primitive.Username;
import de.ottorohenkohl.domain.repository.PersonRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jakarta.enterprise.context.Dependent;

@Dependent
public class PersonJPARepository extends PersistableJPARepository<Person> implements PersonRepository {
    
    public PersonJPARepository() {
        super(Person.class);
    }
    
    @Override
    public Option<Person> read(Username username) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Person.class);
        var root = query.from(Person.class);
        
        query.where(builder.equal(root.get(Person_.username), username));
        
        return Try.of(() -> entityManager.createQuery(query).getSingleResult()).toOption();
    }
    
}

