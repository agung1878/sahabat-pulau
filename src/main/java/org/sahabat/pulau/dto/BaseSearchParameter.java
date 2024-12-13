package org.sahabat.pulau.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseSearchParameter {

    private String q;
    private String v;

    private String date;
    private String dateRange;
    private String dateRange2;

    private String sort;
    private Boolean active;
    private Boolean selectAll;

    private List<String> exclusions = new ArrayList<>();
}
