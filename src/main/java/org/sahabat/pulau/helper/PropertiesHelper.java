package org.sahabat.pulau.helper;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PropertiesHelper {

    @Value("${upload.imageReceipt}")
    private String uploadFolder;

    @Value("${export.folder}")
    public String reportFolder;

}
