package de.ottorohenkohl.domain.model.value.embedded;

import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class Error implements Serializable {
    
    @Enumerated
    @NonNull
    private final Status status;
    
    @Enumerated
    @NonNull
    private final Trace trace;
    
    public Error() {
        this.status = Status.INTERNAL;
        this.trace = Trace.UNSPECIFIED;
    }
    
}
