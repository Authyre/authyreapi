package de.ottorohenkohl.domain.model.value.embedded;

import de.ottorohenkohl.domain.model.enumeration.Status;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class IdentifierTest {
    
    public static final String brokenValue = "016811a5-e0d0-472d-9f6d-3d223f1a0f3g";
    
    public static final String storedValue = "aac05adf-6a65-4206-87fa-d95b3d97e8a1";
    
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
        var identifier = Identifier.build(storedValue);
        
        assertAll(() -> assertTrue(identifier.isValid()), () -> assertEquals(storedValue, identifier.get().getUuid()));
    }
    
    @Test
    protected void returnErrorCaseInvalidInput() {
        var identifier = Identifier.build(brokenValue);
        
        assertAll(() -> assertTrue(identifier.isInvalid()),
                  () -> assertEquals(Status.FORMATTING, identifier.getError().getStatus()));
    }
    
}
