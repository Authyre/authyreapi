package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.Secret;
import de.ottorohenkohl.domain.model.value.primitive.SecretTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class TokenTest extends PersistableTest<Token> {
    
    private static final List<Permission> permissions = new ArrayList<>();
    
    private static final Person person = new PersonTest().getAbsentInstance();
    
    private static final Secret secret = new SecretTest().getAbsentInstance();
    
    private static final Service service = new ServiceTest().getAbsentInstance();
    
    @Override
    public Token getAbsentInstance() {
        return new Token(permissions, person, secret, service);
    }
    
    @Test
    protected void throwExceptionCasePermissionsNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Token(null, person, secret, service));
    }
    
    @Test
    protected void throwExceptionCasePersonNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Token(permissions, null, secret, service));
    }
    
    @Test
    protected void throwExceptionCaseSecretNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Token(permissions, person, null, service));
    }
    
    @Test
    protected void throwExceptionCaseServiceNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Token(permissions, person, secret, null));
    }
    
}
