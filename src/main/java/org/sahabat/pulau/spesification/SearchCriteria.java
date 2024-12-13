package org.sahabat.pulau.spesification;

import lombok.Data;

@Data
public class SearchCriteria {

    private String key;
    private SpecificationOperation operation;
    private Object value;

    public SearchCriteria(String key, SpecificationOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
