package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Service;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import de.ottorohenkohl.domain.model.value.primitive.*;
import de.ottorohenkohl.domain.repository.ServiceRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ServiceJPARepositoryTest extends PersistableJPARepositoryTest<Service> {
    
    public static final Identifier notStoredIdentifier = Identifier.build("b6d23146-a12b-404c-b27a-0729d093e7ea").get();
    
    public static final Identifier permittedIdentifier = Identifier.build("222d033e-7687-464c-972f-fa95b6b65ea2").get();
    
    public static final Name permittedTitle = Name.build("Hello").get();
    
    public static final Name notStoredTitle = Name.build("World").get();
    
    public static final Positive amount = Positive.build(2).get();
    
    private final ServiceRepository repository;
    
    @Inject
    protected ServiceJPARepositoryTest(ServiceRepository repository) {
        super(notStoredIdentifier, permittedIdentifier, repository, amount, new Service(notStoredTitle));
        
        this.repository = repository;
    }
    
    @Test
    protected void returnNoneOnFetchByTitleCaseMissingInDatabase() {
        var service = repository.read(notStoredTitle);
        
        assertFalse(service.isDefined());
    }
    
    @Test
    protected void returnServiceOnFetchByTitleCaseExistingInDatabase() {
        var service = repository.read(permittedTitle);
        
        assertAll(() -> assertTrue(service.isDefined()),
                  () -> assertEquals(permittedTitle, service.get().getTitle()));
    }
    
}
