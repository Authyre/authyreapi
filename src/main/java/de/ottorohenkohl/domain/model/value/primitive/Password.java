package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.embedded.Error;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password extends Primitive<String> {
    
    private static final Pattern pattern = Pattern.compile("^(?=.*?[A-Z\\d])(?=.*?[a-z])(?=.*?[#?!€@$%^&*-]).{5,20}$");
    
    protected Password(String value) {
        super(value);
    }
    
    public static Validation<Error, Password> build(String value) {
        return Option.of(value)
                     .toValidation(new Error(Status.MISSING, Trace.PASSWORD))
                     .map(Password::matches)
                     .flatMap(t -> t)
                     .map(DigestUtils::sha256Hex)
                     .map(Password::new);
    }
    
    private static Validation<Error, String> matches(String value) {
        return Try.of(() -> pattern.matcher(value))
                  .filter(Matcher::matches)
                  .toValidation(new Error(Status.FORMATTING, Trace.PASSWORD))
                  .map(t -> value);
    }
    
}
