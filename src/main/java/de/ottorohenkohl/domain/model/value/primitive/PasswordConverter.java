package de.ottorohenkohl.domain.model.value.primitive;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PasswordConverter extends PrimitiveConverter<Password, String> {
    
    public PasswordConverter() {
        super(Password::new);
    }
    
}
