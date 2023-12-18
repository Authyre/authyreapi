package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Trace;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UsernameTest extends PrimitiveTest<Username, String> {
    
    public UsernameTest() {
        super(Username::build, Username::new, "dora.mosonyi", "not/|lowed", "otto.rohenkohl");
    }
    
    @Test
    protected void returnErrorCaseEmptyInput() {
        var username = Username.build("");
        
        assertAll(() -> assertTrue(username.isInvalid()),
                  () -> assertEquals(Trace.USERNAME, username.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorCaseTooLongInput() {
        var username = Username.build("ThisIsAWayTooLongUsernameAndThereforeNotAllowed");
        
        assertAll(() -> assertTrue(username.isInvalid()),
                  () -> assertEquals(Trace.USERNAME, username.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorCaseTooShortInput() {
        var username = Username.build("User");
        
        assertAll(() -> assertTrue(username.isInvalid()),
                  () -> assertEquals(Trace.USERNAME, username.getError().getTrace()));
    }
    
}
