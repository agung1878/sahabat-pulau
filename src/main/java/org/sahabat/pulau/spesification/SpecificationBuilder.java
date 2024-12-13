package org.sahabat.pulau.spesification;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Getter
public class SpecificationBuilder<T> {

    private final List<SearchCriteria> params;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SpecificationBuilder() {
        this.params = new ArrayList<>();
    }
    public SpecificationBuilder(SpecificationBuilder<T> builder) {
        this.params = builder.getParams();
    }

    public SpecificationBuilder<T> with(String key, SpecificationOperation operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));

        return this;
    }

    public SpecificationBuilder<T> without(String key) {
        int target = -1;
        for (int i = 0; i < params.size(); i++) {
            if(params.get(i).getKey().equalsIgnoreCase(key)){
                target = i;
            }
        }
        params.remove(target);

        return this;
    }

    public Specification<T> build() {
        for (SearchCriteria param : params) {
            logger.debug("params :  [{}], [{}], [{}]", param.getKey(),param.getOperation(), param.getValue());
        }
        if (params.isEmpty()) {
            return null;
        }

        List<Specification<T>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new CustomSpecification<>(param));
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = where(result).and(specs.get(i));
        }
        return result;
    }
}
