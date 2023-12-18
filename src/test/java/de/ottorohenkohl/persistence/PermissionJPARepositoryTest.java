package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Permission;
import de.ottorohenkohl.domain.model.entity.PermissionTest;
import de.ottorohenkohl.domain.model.enumeration.Access;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import de.ottorohenkohl.domain.model.value.primitive.NameTest;
import de.ottorohenkohl.domain.model.value.primitive.Positive;
import de.ottorohenkohl.domain.model.value.primitive.UsernameTest;
import de.ottorohenkohl.domain.repository.PermissionRepository;
import de.ottorohenkohl.domain.repository.PersonRepository;
import de.ottorohenkohl.domain.repository.ServiceRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PermissionJPARepositoryTest extends PersistableJPARepositoryTest<Permission> {
    
    private final PermissionRepository permissionRepository;
    
    private final PersonRepository personRepository;
    
    private final ServiceRepository serviceRepository;
    
    @Inject
    protected PermissionJPARepositoryTest(PermissionRepository permissionRepository, PersonRepository personRepository, ServiceRepository serviceRepository) {
        super(Identifier.build("6a76d531-981a-4f26-85b7-088bcc08767d").get(),
              Identifier.build("75003075-802b-4630-b8aa-7effe38a190c").get(),
              permissionRepository,
              Positive.build(3).get(),
              new PermissionTest().getAbsentInstance());
        
        this.permissionRepository = permissionRepository;
        this.personRepository = personRepository;
        this.serviceRepository = serviceRepository;
    }
    
    @Test
    protected void returnListOfPermissionOnFetchByPerson() {
        var person = personRepository.read(new UsernameTest().getStoredInstance()).get();
        
        var permissions = permissionRepository.readAll(person);
        
        assertAll(() -> assertFalse(permissions.isEmpty()), () -> assertEquals(2, permissions.size()));
    }
    
    @Test
    protected void returnListOfPermissionOnFetchByPersonAndService() {
        var person = personRepository.read(new UsernameTest().getStoredInstance()).get();
        var service = serviceRepository.read(new NameTest().getStoredInstance()).get();
        
        var permissions = permissionRepository.readAll(person, service);
        
        assertAll(() -> assertFalse(permissions.isEmpty()), () -> assertEquals(1, permissions.size()));
    }
    
    @Test
    protected void returnPermissionOnFetchByPersonAndServiceAndAccessCaseExisting() {
        var access = Access.VIEW;
        var person = personRepository.read(new UsernameTest().getStoredInstance()).get();
        var service = serviceRepository.read(new NameTest().getStoredInstance()).get();
        
        var permission = permissionRepository.readAll(person, service, access);
        
        assertAll(() -> assertTrue(permission.isDefined()),
                  () -> assertEquals(Access.VIEW, permission.get().getAccess()),
                  () -> assertEquals(person.getIdentifier(), permission.get().getPerson().getIdentifier()),
                  () -> assertEquals(service.getIdentifier(), permission.get().getService().getIdentifier()));
    }
    
    @Test
    protected void returnNoneOnFetchByPersonAndServiceAndAccessCaseMissing() {
        var access = Access.INTERACT;
        var person = personRepository.read(new UsernameTest().getStoredInstance()).get();
        var service = serviceRepository.read(new NameTest().getStoredInstance()).get();
        
        var permission = permissionRepository.readAll(person, service, access);
        
        assertFalse(permission.isDefined());
    }
    
}
