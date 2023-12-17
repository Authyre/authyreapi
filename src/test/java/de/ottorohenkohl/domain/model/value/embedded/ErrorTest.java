package de.ottorohenkohl.domain.model.value.embedded;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ErrorTest {
    
    @Test
    protected void returnErrorWithDefaultValues() {
        var error = new Error();
        
        assertAll(() -> assertEquals(Status.INTERNAL, error.getStatus()),
                  () -> assertEquals(Trace.UNSPECIFIED, error.getTrace()));
    }
    
}
