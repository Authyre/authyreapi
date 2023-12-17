package de.ottorohenkohl.domain.model.value.primitive;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.embedded.Error;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.util.Arrays;

public class Secret extends Primitive<String> {
    
    public Secret() {
        super(generate());
    }
    
    protected Secret(String value) {
        super(value);
    }
    
    private static String generate() {
        var generator = new SecureRandom();
        var access = new byte[124];
        
        generator.nextBytes(access);
        
        return Arrays.toString(access);
    }
    
    public static Validation<Error, Secret> build(String value) {
        return Option.of(value)
                     .toValidation(new Error(Status.MISSING, Trace.PASSWORD))
                     .map(DigestUtils::sha256Hex)
                     .map(Secret::new);
    }
    
}
