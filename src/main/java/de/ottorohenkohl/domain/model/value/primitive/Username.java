package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.embedded.Error;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Username extends Primitive<String> {
    
    private static final Pattern pattern = Pattern.compile("^[a-z.0-9_-]{5,20}$");
    
    protected Username(String value) {
        super(value);
    }
    
    private static Validation<Error, String> matches(String value) {
        return Try.of(() -> pattern.matcher(value))
                  .filter(Matcher::matches)
                  .toValidation(new Error(Status.FORMATTING, Trace.USERNAME))
                  .map(t -> value);
    }
    
    public static Validation<Error, Username> build(String value) {
        return Option.of(value)
                     .toValidation(new Error(Status.MISSING, Trace.USERNAME))
                     .map(Username::matches)
                     .flatMap(t -> t)
                     .map(Username::new);
    }
    
}
