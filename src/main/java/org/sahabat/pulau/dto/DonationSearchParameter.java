package org.sahabat.pulau.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DonationSearchParameter extends BaseSearchParameter{

    private String name;
    private String email;

}
