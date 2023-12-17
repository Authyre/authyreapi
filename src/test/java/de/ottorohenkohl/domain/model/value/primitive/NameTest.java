package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Trace;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class NameTest extends PrimitiveTest<Name, String> {
    
    public static final String forbiddenValue = "Sp€ci/|l Characters";
    
    public static final String notStoredValue = "Some Name";
    
    public static final String permittedValue = "Another Name";
    
    protected NameTest() {
        super(Name::build, Name::new, forbiddenValue, notStoredValue, permittedValue);
    }
    
    @Test
    protected void returnErrorCaseEmptyInput() {
        var name = Name.build("");
        
        assertAll(() -> assertTrue(name.isInvalid()), () -> assertEquals(Trace.NAME, name.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorCaseTooLongInput() {
        var name = Name.build("ThisIsAWayTooLongNameAndThereforeNotAllowed");
        
        assertAll(() -> assertTrue(name.isInvalid()), () -> assertEquals(Trace.NAME, name.getError().getTrace()));
    }
    
}
