package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.enumeration.Access;
import de.ottorohenkohl.domain.model.enumeration.Scope;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class PermissionTest {
    
    public static final Access ACCESS = Access.VIEW;
    
    public static final Scope scope = Scope.SELF;
    
    public static final Permission permission = new Permission(ACCESS, PersonTest.person, scope, ServiceTest.service);
    
    @Test
    protected void throwExceptionCaseKeywordNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Permission(null, PersonTest.person, scope, ServiceTest.service));
    }
    
    @Test
    protected void throwExceptionCasePersonNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Permission(ACCESS, null, scope, ServiceTest.service));
    }
    
    @Test
    protected void throwExceptionCaseScopeNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Permission(ACCESS, PersonTest.person, null, ServiceTest.service));
    }
    
    @Test
    protected void throwExceptionCaseServiceNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Permission(ACCESS, PersonTest.person, scope, null));
    }
    
}
