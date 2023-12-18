package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.enumeration.Tag;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class Persistable implements Serializable {
    
    @EmbeddedId
    private Identifier identifier = new Identifier();
    
    @Enumerated
    @Setter
    private Tag tag = Tag.STANDARD;
    
}
