package de.ottorohenkohl.domain.model.value.embedded;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.primitive.Primitive;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class Identifier implements Serializable {
    
    @NonNull
    private final String uuid;
    
    public Identifier() {
        this.uuid = build(UUID.randomUUID().toString()).get().getUuid();
    }
    
    public static Validation<Error, Identifier> build(String value) {
        return Option.of(value)
                     .toValidation(new Error(Status.MISSING, Trace.IDENTIFIER))
                     .map(Identifier::uuid)
                     .flatMap(t -> t)
                     .map(Identifier::new);
    }
    
    private static Validation<Error, String> uuid(String value) {
        return Try.of(() -> UUID.fromString(value))
                  .toValidation(new Error(Status.FORMATTING, Trace.IDENTIFIER))
                  .map(UUID::toString);
    }
    
}
