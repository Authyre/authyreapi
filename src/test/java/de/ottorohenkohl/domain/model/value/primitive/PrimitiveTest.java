package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.value.embedded.Error;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public abstract class PrimitiveTest<T extends Primitive<U>, U> {
    
    private final Function<U, Validation<Error, T>> builder;
    
    private final Function<U, T> constructor;
    
    private final U forbiddenValue;
    
    private final U notStoredValue;
    
    private final U permittedValue;
    
    protected PrimitiveTest(Function<U, Validation<Error, T>> builder, Function<U, T> constructor, U forbiddenValue, U notStoredValue, U permittedValue) {
        this.builder = builder;
        this.constructor = constructor;
        this.forbiddenValue = forbiddenValue;
        this.notStoredValue = notStoredValue;
        this.permittedValue = permittedValue;
    }
    
    @Test
    protected void comparePrimitivesCaseEqualInput() {
        var primitive1 = constructor.apply(permittedValue);
        var primitive2 = constructor.apply(permittedValue);
        
        assertEquals(primitive1, primitive2);
    }
    
    @Test
    protected void distinguishPrimitivesCaseDifferentInput() {
        var primitive1 = constructor.apply(notStoredValue);
        var primitive2 = constructor.apply(permittedValue);
        
        assertNotEquals(primitive1, primitive2);
    }
    
    @Test
    protected void keepValueOnUpdateCaseNull() {
        var primitive = builder.apply(permittedValue);
        
        var validation = Primitive.update(builder, primitive::get, null);
        
        assertEquals(builder.apply(permittedValue).get(), validation.get());
    }
    
    @Test
    protected void returnPrimitiveCaseValidInput() {
        var primitive = builder.apply(permittedValue);
        
        assertAll(() -> assertTrue(primitive.isValid()),
                  () -> assertEquals(permittedValue, primitive.get().getValue()));
    }
    
    @Test
    protected void returnErrorCaseInvalidInput() {
        var primitive = builder.apply(forbiddenValue);
        
        assertAll(() -> assertTrue(primitive.isInvalid()),
                  () -> assertEquals(Status.FORMATTING, primitive.getError().getStatus()));
    }
    
    @Test
    protected void returnErrorCaseNullInput() {
        var primitive = builder.apply(null);
        
        assertAll(() -> assertTrue(primitive.isInvalid()),
                  () -> assertEquals(Status.MISSING, primitive.getError().getStatus()));
    }
    
    @Test
    protected void updateValueCaseNotNull() {
        var primitive = builder.apply(permittedValue);
        
        var validation = Primitive.update(builder, primitive::get, notStoredValue);
        
        assertAll(() -> assertNotEquals(builder.apply(permittedValue).get(), validation.get()),
                  () -> assertEquals(builder.apply(notStoredValue).get(), validation.get()));
    }
    
}
