package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.Name;
import de.ottorohenkohl.domain.model.value.primitive.NameTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class ServiceTest {
    
    public static final Name title = Name.build(NameTest.permittedValue).get();
    
    public static final Service service = new Service(title);
    
    @Test
    public void throwExceptionCaseTitleNullInput() {
        var exception = NullPointerException.class;
        
        assertThrows(exception, () -> new Service(null));
    }
    
}
