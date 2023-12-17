package de.ottorohenkohl.domain.model.value.embedded;

import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.enumeration.Status;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class Error implements Serializable {
    
    @Enumerated
    @NonNull
    public final Status status;
    
    @Enumerated
    @NonNull
    public final Trace trace;
    
    public Error() {
        this.status = Status.INTERNAL;
        this.trace = Trace.UNSPECIFIED;
    }
    
}
