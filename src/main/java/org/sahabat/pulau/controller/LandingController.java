package org.sahabat.pulau.controller;

import org.sahabat.pulau.dto.DonationDto;
import org.sahabat.pulau.entity.Donation;
import org.sahabat.pulau.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class LandingController {

    @Autowired private DonationService donationService;

    @GetMapping
    public String landing(Model model) {

        model.addAttribute("donation", new DonationDto());

        return "index";
    }

    @PostMapping("/")
    public String addDonation(DonationDto donation, MultipartFile file, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        try {
            donationService.saveDonation(donation, file);
            return "thank";
        }catch (Exception e){
            modelMap.addAttribute("errorMessage",e.getMessage());
            System.out.println("Error: " + e.getMessage());
            return "error";
        }

    }

}
