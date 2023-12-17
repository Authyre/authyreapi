package de.ottorohenkohl.domain.transfer.response;

import de.ottorohenkohl.domain.model.value.embedded.Error;
import lombok.Getter;

import java.util.List;

@Getter
public class Pagination<T> extends Answer {
    
    private final Long numberItems;
    
    private final Long numberTotal;
    
    private final List<T> items;
    
    public Pagination(Error error) {
        this.error = error;
        this.items = null;
        this.numberItems = null;
        this.numberTotal = null;
    }
    
    public Pagination(Long numberTotal, List<T> items) {
        this.error = null;
        this.items = items;
        this.numberItems = (long) items.size();
        this.numberTotal = numberTotal;
    }
    
}
