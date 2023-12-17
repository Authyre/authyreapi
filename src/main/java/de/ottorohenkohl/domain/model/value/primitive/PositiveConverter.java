package de.ottorohenkohl.domain.model.value.primitive;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PositiveConverter extends PrimitiveConverter<Positive, Integer> {
    
    public PositiveConverter() {
        super(Positive::new);
    }
    
}
