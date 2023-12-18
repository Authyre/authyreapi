package de.ottorohenkohl.domain.model.value.primitive;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class PositiveTest extends PrimitiveTest<Positive, Integer> {
    
    public PositiveTest() {
        super(Positive::build, Positive::new, 123, -11, 3);
    }
    
    @Test
    protected void returnErrorOnBuildWithNegativeValue() {
        var positive = Positive.build(-1);
        
        assertAll(() -> assertTrue(positive.isInvalid()));
    }
    
    @Test
    protected void returnErrorOnBuildWithZeroValue() {
        var positive = Positive.build(0);
        
        assertAll(() -> assertTrue(positive.isInvalid()));
    }
    
}
