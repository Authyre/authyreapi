package de.ottorohenkohl.domain.repository;

import de.ottorohenkohl.domain.model.entity.Person;
import de.ottorohenkohl.domain.model.value.primitive.Username;
import io.vavr.control.Option;

public interface PersonRepository extends PersistableRepository<Person> {
    
    Option<Person> read(Username username);
    
}
