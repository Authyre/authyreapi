package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.enumeration.Access;
import de.ottorohenkohl.domain.model.enumeration.Scope;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
public class Permission extends Persistable {
    
    @Enumerated
    @NonNull
    private Access access;
    
    @ManyToOne
    @NonNull
    private Person person;
    
    @Enumerated
    @NonNull
    private Scope scope;
    
    @ManyToOne
    @NonNull
    private Service service;
    
}
