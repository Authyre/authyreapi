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
    
    protected final U absentValue;
    
    protected final U brokenValue;
    
    protected final U storedValue;
    
    protected PrimitiveTest(Function<U, Validation<Error, T>> builder, Function<U, T> constructor, U absentValue, U brokenValue, U storedValue) {
        this.builder = builder;
        this.constructor = constructor;
        this.absentValue = absentValue;
        this.brokenValue = brokenValue;
        this.storedValue = storedValue;
    }
    
    public T getAbsentInstance() {
        return builder.apply(absentValue).get();
    }
    
    public T getStoredInstance() {
        return builder.apply(storedValue).get();
    }
    
    @Test
    protected void comparePrimitivesCaseEqualInput() {
        var primitive1 = constructor.apply(storedValue);
        var primitive2 = constructor.apply(storedValue);
        
        assertEquals(primitive1, primitive2);
    }
    
    @Test
    protected void distinguishPrimitivesCaseDifferentInput() {
        var primitive1 = constructor.apply(absentValue);
        var primitive2 = constructor.apply(storedValue);
        
        assertNotEquals(primitive1, primitive2);
    }
    
    @Test
    protected void keepValueOnUpdateCaseNull() {
        var primitive = builder.apply(storedValue);
        
        var validation = Primitive.update(builder, primitive::get, null);
        
        assertEquals(builder.apply(storedValue).get(), validation.get());
    }
    
    @Test
    protected void returnPrimitiveCaseValidInput() {
        var primitive = builder.apply(storedValue);
        
        assertAll(() -> assertTrue(primitive.isValid()), () -> assertEquals(storedValue, primitive.get().getValue()));
    }
    
    @Test
    protected void returnErrorCaseInvalidInput() {
        var primitive = builder.apply(brokenValue);
        
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
        var primitive = builder.apply(storedValue);
        
        var validation = Primitive.update(builder, primitive::get, absentValue);
        
        assertAll(() -> assertNotEquals(builder.apply(storedValue).get(), validation.get()),
                  () -> assertEquals(builder.apply(absentValue).get(), validation.get()));
    }
    
}
