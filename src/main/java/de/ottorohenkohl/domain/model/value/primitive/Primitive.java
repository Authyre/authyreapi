package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.value.embedded.Error;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public abstract class Primitive<T> implements Serializable {
    
    @NonNull
    private final T value;
    
    public static <T extends Primitive<U>, U> Validation<Error, T> update(Function<U, Validation<Error, T>> builder, Supplier<T> supplier, U change) {
        if (change == null) {
            return Validation.valid(supplier.get());
        }
        
        return builder.apply(change);
    }
    
}