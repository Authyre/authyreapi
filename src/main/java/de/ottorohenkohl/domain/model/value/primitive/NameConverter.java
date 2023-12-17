package de.ottorohenkohl.domain.model.value.primitive;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NameConverter extends PrimitiveConverter<Name, String> {
    
    public NameConverter() {
        super(Name::new);
    }
    
}
