package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class PersonTest extends PersistableTest<Person> {
    
    private static final Name forename = new NameTest().getAbsentInstance();
    
    private static final Name lastname = new NameTest().getAbsentInstance();
    
    private static final Password password = new PasswordTest().getAbsentInstance();
    
    private static final Username username = new UsernameTest().getAbsentInstance();
    
    @Override
    public Person getAbsentInstance() {
        return new Person(forename, lastname, password, username);
    }
    
    @Test
    protected void throwExceptionCasePasswordNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Person(null, username));
    }
    
    @Test
    protected void throwExceptionCaseUsernameNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Person(password, null));
    }
    
}
