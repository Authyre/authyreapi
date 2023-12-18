package de.ottorohenkohl.domain.model.value.primitive;

import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.function.Function;

@AllArgsConstructor
public abstract class PrimitiveConverter<T extends Primitive<U>, U> implements AttributeConverter<T, U> {
    
    @NonNull
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
