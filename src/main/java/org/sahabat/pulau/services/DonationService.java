package org.sahabat.pulau.services;


import jakarta.transaction.Transactional;
import org.sahabat.pulau.dao.DonationDao;
import org.sahabat.pulau.dto.DonationDto;
import org.sahabat.pulau.dto.DonationSearchParameter;
import org.sahabat.pulau.entity.Donation;
import org.sahabat.pulau.spesification.SpecificationBuilder;
import org.sahabat.pulau.spesification.SpecificationOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class DonationService {

    @Value("${upload.imageReceipt}") // Set this in application.properties
    private String uploadDirectory;

    @Autowired private DonationDao donationDao;

    @Autowired private FileService fileService;

    private static final List<String> allowedFiles = Arrays.asList("image/png", "image/jpg","image/jpeg", "application/pdf");

    private SpecificationBuilder<Donation> DonationBuilder(DonationSearchParameter params) {
        SpecificationBuilder<Donation> builder = new SpecificationBuilder<>();

        if (StringUtils.hasText(params.getDateRange()))
            builder.with("createdAt", SpecificationOperation.LOCAL_DATE_TIME_BETWEEN, params.getDateRange());

        if (StringUtils.hasText(params.getName()))
            builder.with("name", SpecificationOperation.LIKE, params.getName());

        if (StringUtils.hasText(params.getEmail()))
            builder.with("email", SpecificationOperation.LIKE, params.getEmail());


        return builder;
    }

    public Page<Donation> getPage(DonationSearchParameter params, Pageable pageable) {
        return donationDao.findAll(DonationBuilder(params).build(), pageable);
    }

    @Transactional
    public void saveDonation(DonationDto donationDto, MultipartFile file) throws Exception {

        if(!allowedFiles.contains(file.getContentType())){
            throw new Exception("File harus gambar (png,jpg,jpeg) atau pdf");
        }

        String fileName = UUID.randomUUID() + FileService.getFileExtension(file.getOriginalFilename());

        try {

            Donation donation = new Donation();
            donation.setEmail(donationDto.getEmail());
            donation.setMessage(donationDto.getMessage());
            donation.setName(donationDto.getName());
            donation.setBankName(donationDto.getBankName());
            donation.setPhoneNumber(donationDto.getPhoneNumber());
            donation.setBankAccountName(donationDto.getBankAccountName());
            donation.setBankAccountNumber(donationDto.getBankAccountNumber());
            donation.setReceiptImgFilename(file.getOriginalFilename());
            donation.setReceiptImgUrl(fileName);
            donationDao.save(donation);

            fileService.store(uploadDirectory, donation.getId(),
                    fileName,
                    file.getBytes());

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new Exception("Gagal saving donation image", e);
        }



    }

}
