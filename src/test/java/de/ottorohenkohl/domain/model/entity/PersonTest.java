package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class PersonTest {
    
    public static final Password password = Password.build(PasswordTest.permittedValue).get();
    
    public static final Username username = Username.build(UsernameTest.permittedValue).get();
    
    public static final Person person = new Person(password, username);
    
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
