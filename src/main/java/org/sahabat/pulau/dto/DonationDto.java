package org.sahabat.pulau.dto;


import jakarta.persistence.Transient;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DonationDto {

    private String name;
    private String phoneNumber;
    private String email;
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountName;
    private String message;


    private String receiptImgUrl;
    private String receiptImgFilename;

    @Transient
    private MultipartFile file;

}
