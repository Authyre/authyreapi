package de.ottorohenkohl.domain.repository;

import de.ottorohenkohl.domain.model.entity.Permission;
import de.ottorohenkohl.domain.model.entity.Person;
import de.ottorohenkohl.domain.model.entity.Service;
import de.ottorohenkohl.domain.model.enumeration.Access;
import io.vavr.control.Option;

import java.util.List;

public interface PermissionRepository extends PersistableRepository<Permission> {
    
    Option<Permission> readAll(Person person, Service service, Access access);
    
    List<Permission> readAll(Person person);
    
    List<Permission> readAll(Person person, Service service);
    
}
