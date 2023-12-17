package de.ottorohenkohl.domain.model.value.primitive;

import io.vavr.control.Option;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public abstract class PrimitiveConverter<T extends Primitive<U>, U> implements AttributeConverter<T, U> {
    
    private final Function<U, T> function;
    
    @Override
    public U convertToDatabaseColumn(T primitive) {
        return primitive != null ? primitive.getValue() : null;
    }
    
    @Override
    public T convertToEntityAttribute(U value) {
        return function.apply(value);
    }
    
}
