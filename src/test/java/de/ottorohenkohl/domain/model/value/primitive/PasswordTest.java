package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Trace;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PasswordTest extends PrimitiveTest<Password, String> {
    
    public PasswordTest() {
        super(Password::build, Password::new, "0th1rValidPa$$word", "AnForbiddenPassword", "S0m€ValidPa$$word");
    }
    
    public String getStoredOriginal() {
        return "S0m€ValidPa$$word";
    }
    
    @Override
    protected void returnPrimitiveCaseValidInput() {
        var password = Password.build(super.storedValue);
        
        assertAll(() -> assertTrue(password.isValid()),
                  () -> assertEquals(DigestUtils.sha256Hex(super.storedValue), password.get().getValue()));
    }
    
    @Test
    protected void returnErrorCaseEmptyInput() {
        var password = Password.build("");
        
        assertAll(() -> assertTrue(password.isInvalid()),
                  () -> assertEquals(Trace.PASSWORD, password.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorCaseMissingSpecialCharacterInput() {
        var password = Password.build("SomeWeakPassword");
        
        assertAll(() -> assertTrue(password.isInvalid()),
                  () -> assertEquals(Trace.PASSWORD, password.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorCaseTooLongInput() {
        var password = Password.build("ThisIsAWayTooLongPasswordAndThereforeNotAllowed");
        
        assertAll(() -> assertTrue(password.isInvalid()),
                  () -> assertEquals(Trace.PASSWORD, password.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorCaseTooShortInput() {
        var password = Password.build("Pass");
        
        assertAll(() -> assertTrue(password.isInvalid()),
                  () -> assertEquals(Trace.PASSWORD, password.getError().getTrace()));
    }
    
}
