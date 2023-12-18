package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.enumeration.Access;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class PermissionTest extends PersistableTest<Permission> {
    
    private static final Access access = Access.VIEW;
    
    private static final Person person = new PersonTest().getAbsentInstance();
    
    private static final Service service = new ServiceTest().getAbsentInstance();
    
    @Override
    public Permission getAbsentInstance() {
        return new Permission(access, person, service);
    }
    
    @Test
    protected void throwExceptionCaseKeywordNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Permission(null, person, service));
    }
    
    @Test
    protected void throwExceptionCasePersonNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Permission(access, null, service));
    }
    
    @Test
    protected void throwExceptionCaseServiceNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Permission(access, person, null));
    }
    
}
