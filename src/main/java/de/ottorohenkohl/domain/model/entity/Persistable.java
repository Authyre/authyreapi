package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.enumeration.Tag;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class Persistable implements Serializable {
    
    @EmbeddedId
    protected Identifier identifier = new Identifier();
    
    @Enumerated
    @Setter
    protected Tag tag = Tag.STANDARD;
    
}
