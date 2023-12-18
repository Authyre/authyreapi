package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Person;
import de.ottorohenkohl.domain.model.entity.PersonTest;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import de.ottorohenkohl.domain.model.value.primitive.Positive;
import de.ottorohenkohl.domain.model.value.primitive.Username;
import de.ottorohenkohl.domain.model.value.primitive.UsernameTest;
import de.ottorohenkohl.domain.repository.PersonRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PersonJPARepositoryTest extends PersistableJPARepositoryTest<Person> {
    
    public static final Username username = new UsernameTest().getStoredInstance();
    
    private final PersonRepository personRepository;
    
    @Inject
    protected PersonJPARepositoryTest(PersonRepository personRepository) {
        super(Identifier.build("b664fad0-bdcb-40d9-952f-b53989f86331").get(),
              Identifier.build("aac05adf-6a65-4206-87fa-d95b3d97e8a1").get(), personRepository,
              Positive.build(3).get(),
              new PersonTest().getAbsentInstance());
        
        this.personRepository = personRepository;
    }
    
    @Test
    protected void returnNoneOnFetchByUsernameCaseMissingInDatabase() {
        var person = personRepository.read(super.absentPersistable.getUsername());
        
        assertFalse(person.isDefined());
    }
    
    @Test
    protected void returnPersonOnFetchByUsernameCaseExistingInDatabase() {
        var person = personRepository.read(username);
        
        assertAll(() -> assertTrue(person.isDefined()),
                  () -> assertEquals(username, person.get().getUsername()));
    }
    
}
