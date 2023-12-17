package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.value.embedded.Error;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public abstract class Primitive<T> implements Serializable {
    
    protected final T value;
    
    public static <T extends Primitive<U>, U> Validation<Error, T> update(Function<U, Validation<Error, T>> builder, Supplier<T> supplier, U change) {
        if (change == null) {
            return Validation.valid(supplier.get());
        }
        
        return builder.apply(change);
    }
    
}
