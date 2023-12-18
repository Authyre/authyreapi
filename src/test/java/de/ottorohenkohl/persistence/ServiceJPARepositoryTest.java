package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Service;
import de.ottorohenkohl.domain.model.entity.ServiceTest;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import de.ottorohenkohl.domain.model.value.primitive.Name;
import de.ottorohenkohl.domain.model.value.primitive.NameTest;
import de.ottorohenkohl.domain.model.value.primitive.Positive;
import de.ottorohenkohl.domain.repository.ServiceRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ServiceJPARepositoryTest extends PersistableJPARepositoryTest<Service> {
    
    private static final Name title = new NameTest().getStoredInstance();
    
    private final ServiceRepository repository;
    
    @Inject
    protected ServiceJPARepositoryTest(ServiceRepository repository) {
        super(Identifier.build("b6d23146-a12b-404c-b27a-0729d093e7ea").get(),
              Identifier.build("222d033e-7687-464c-972f-fa95b6b65ea2").get(),
              repository,
              Positive.build(2).get(),
              new ServiceTest().getAbsentInstance());
        
        this.repository = repository;
    }
    
    @Test
    protected void returnNoneOnFetchByTitleCaseMissingInDatabase() {
        var service = repository.read(super.absentPersistable.getTitle());
        
        assertFalse(service.isDefined());
    }
    
    @Test
    protected void returnServiceOnFetchByTitleCaseExistingInDatabase() {
        var service = repository.read(title);
        
        assertAll(() -> assertTrue(service.isDefined()), () -> assertEquals(title, service.get().getTitle()));
    }
    
}
