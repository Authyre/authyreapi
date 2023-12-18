package de.ottorohenkohl.domain.repository;

import de.ottorohenkohl.domain.model.entity.Service;
import de.ottorohenkohl.domain.model.value.primitive.Name;
import io.vavr.control.Option;

public interface ServiceRepository extends PersistableRepository<Service> {
    
    Option<Service> read(Name title);
    
}
