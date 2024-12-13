package org.sahabat.pulau.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
public class Donation extends BaseEntity{


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

    public String getType(){
        if(!StringUtils.hasText(this.receiptImgUrl)) return "empty";
        return this.receiptImgUrl.toLowerCase().contains("pdf") ? "PDF" : "IMAGE";
    }

}
