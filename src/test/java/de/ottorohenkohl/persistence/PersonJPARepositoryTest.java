package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Person;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import de.ottorohenkohl.domain.model.value.primitive.*;
import de.ottorohenkohl.domain.repository.PersonRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PersonJPARepositoryTest extends PersistableJPARepositoryTest<Person> {
    
    public static final Identifier notStoredIdentifier = Identifier.build("b664fad0-bdcb-40d9-952f-b53989f86331").get();
    
    public static final Identifier permittedIdentifier = Identifier.build("aac05adf-6a65-4206-87fa-d95b3d97e8a1").get();
    
    public static final Positive amount = Positive.build(3).get();
    
    public static final Password permittedPassword = Password.build(PasswordTest.permittedValue).get();
    
    public static final Username notStoredUsername = Username.build(UsernameTest.notStoredValue).get();
    
    public static final Username permittedUsername = Username.build(UsernameTest.permittedValue).get();
    
    private final PersonRepository repository;
    
    @Inject
    protected PersonJPARepositoryTest(PersonRepository repository) {
        super(notStoredIdentifier,
              permittedIdentifier,
              repository,
              amount,
              new Person(permittedPassword, notStoredUsername));
        
        this.repository = repository;
    }
    
    @Test
    protected void returnNoneOnFetchByUsernameCaseMissingInDatabase() {
        var person = repository.read(notStoredUsername);
        
        assertFalse(person.isDefined());
    }
    
    @Test
    protected void returnPersonOnFetchByUsernameCaseExistingInDatabase() {
        var person = repository.read(permittedUsername);
        
        assertAll(() -> assertTrue(person.isDefined()),
                  () -> assertEquals(permittedUsername, person.get().getUsername()));
    }
    
}
