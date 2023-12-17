package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.Secret;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class TokenTest {
    
    public static final Secret secret = new Secret();
    
    public static final List<Permission> permissions = new ArrayList<>();
    
    @Test
    protected void throwExceptionCasePermissionsNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Token(null, PersonTest.person, secret, ServiceTest.service));
    }
    
    @Test
    protected void throwExceptionCasePersonNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Token(permissions, null, secret, ServiceTest.service));
    }
    
    @Test
    protected void throwExceptionCaseSecretNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Token(permissions, PersonTest.person, null, ServiceTest.service));
    }
    
    @Test
    protected void throwExceptionCaseServiceNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Token(permissions, PersonTest.person, secret, null));
    }
    
}
