package de.ottorohenkohl.domain.model.value.embedded;

import de.ottorohenkohl.domain.model.enumeration.Status;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class IdentifierTest {
    
    public static final String forbiddenValue = "016811a5-e0d0-472d-9f6d-3d223f1a0f3g";
    
    public static final String permittedValue = "0cef9a1e-a391-43c1-952c-69ef3ed0ad8d";
    
    @Test
    protected void returnIdentifierWithRandomizedValue() {
        var identifier1 = new Identifier();
        var identifier2 = new Identifier();
        
        assertAll(() -> assertFalse(identifier1.getUuid().isBlank()),
                  () -> assertFalse(identifier2.getUuid().isBlank()),
                  () -> assertNotEquals(identifier1, identifier2));
    }
    
    @Test
    protected void returnIdentifierCaseValidInput() {
        var identifier = Identifier.build(permittedValue);
        
        assertAll(() -> assertTrue(identifier.isValid()),
                  () -> assertEquals(permittedValue, identifier.get().getUuid()));
    }
    
    @Test
    protected void returnErrorCaseInvalidInput() {
        var identifier = Identifier.build(forbiddenValue);
        
        assertAll(() -> assertTrue(identifier.isInvalid()),
                  () -> assertEquals(Status.FORMATTING, identifier.getError().getStatus()));
    }
    
}
