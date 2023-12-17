package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.embedded.Error;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Positive extends Primitive<Integer> {
    
    protected Positive(Integer value) {
        super(value);
    }
    
    public static Validation<Error, Positive> build(Integer value) {
        return Option.of(value)
                     .toValidation(new Error(Status.MISSING, Trace.POSITIVE))
                     .filter(t -> t > 0)
                     .toValidation(new Error(Status.FORMATTING, Trace.POSITIVE))
                     .flatMap(t -> t)
                     .map(Positive::new);
    }
    
}
