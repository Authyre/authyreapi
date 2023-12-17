package de.ottorohenkohl.domain.transfer.response;

import de.ottorohenkohl.domain.model.value.embedded.Error;
import lombok.Getter;

@Getter
public class Fetched<T> extends Answer {
    
    private final T item;
    
    public Fetched(Error error) {
        this.error = error;
        this.item = null;
    }
    
    public Fetched(T item) {
        this.error = null;
        this.item = item;
    }
    
}
