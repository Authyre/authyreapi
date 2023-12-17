package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.embedded.Error;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Name extends Primitive<String> {
    
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z ]{1,20}$");
    
    protected Name(String value) {
        super(value);
    }
    
    public static Validation<Error, Name> build(String value) {
        return Option.of(value)
                     .toValidation(new Error(Status.MISSING, Trace.NAME))
                     .map(Name::matches)
                     .flatMap(t -> t)
                     .map(Name::new);
    }
    
    private static Validation<Error, String> matches(String value) {
        return Try.of(() -> pattern.matcher(value))
                  .filter(Matcher::matches)
                  .toValidation(new Error(Status.FORMATTING, Trace.NAME))
                  .map(t -> value);
    }
    
}
