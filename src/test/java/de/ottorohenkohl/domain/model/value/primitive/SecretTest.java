package de.ottorohenkohl.domain.model.value.primitive;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SecretTest extends PrimitiveTest<Secret, String> {
    
    public static final String forbiddenValue = null;
    
    public static final String notStoredValue = "nbasb89f97qb379ev78F%/DS/$%D§";
    
    public static final String permittedValue = "&/ASFDG%)F956865fds6a8%F$(4d8";
    
    protected SecretTest() {
        super(Secret::build, Secret::new, forbiddenValue, notStoredValue, permittedValue);
    }
    
    @Override
    protected void returnPrimitiveCaseValidInput() {
        var access = Secret.build(permittedValue);
        
        assertAll(() -> assertTrue(access.isValid()),
                  () -> assertEquals(DigestUtils.sha256Hex(permittedValue), access.get().getValue()));
    }
    
    @Override
    protected void returnErrorCaseInvalidInput() {
        super.returnErrorCaseNullInput();
    }
    
    @Test
    protected void returnSecretWithRandomizedValue() {
        var access1 = new Secret();
        var access2 = new Secret();
        
        assertAll(() -> assertFalse(access2.getValue().isBlank()),
                  () -> assertFalse(access1.getValue().isBlank()),
                  () -> assertNotEquals(access1, access2));
    }
    
}
