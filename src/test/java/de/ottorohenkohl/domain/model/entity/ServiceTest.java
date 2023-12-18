package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.Name;
import de.ottorohenkohl.domain.model.value.primitive.NameTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class ServiceTest extends PersistableTest<Service> {
    
    private static final Name description = new NameTest().getAbsentInstance();
    
    private static final Name title = new NameTest().getAbsentInstance();
    
    @Override
    public Service getAbsentInstance() {
        return new Service(description, title);
    }
    
    @Test
    public void throwExceptionCaseTitleNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Service(null));
    }
    
}
